package com.shoppingcart.admin.category;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.poi.poifs.property.Parent;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shoppingcart.admin.entity.Category;
import com.shoppingcart.admin.entity.User;


@Service
@Transactional
public class CategoryService {
	
	@Autowired
	private CategoryRepository cateRepo;
	
	public static final int ROOT_CATEGORY_PER_PAGE = 4;
	
	public List<Category> listAll(){
		return (List<Category>) cateRepo.findAll(Sort.by("name").ascending());
	}
	
	public Category save (Category cate) {
		return cateRepo.save(cate);
	}
	
	public Category get(Integer Id) throws CategoryNotFoundException{
		try {
			return this.cateRepo.findById(Id).get();
		}
		catch (NoSuchElementException ex) {
			throw new CategoryNotFoundException("Could not find any category with ID "+ Id);
		}
	}
	
	public void delete(Integer Id) throws CategoryNotFoundException{
		Long countById = cateRepo.countById(Id);
		if(countById == null|| countById==0) {
			throw new CategoryNotFoundException("Could not find any category with ID "+ Id);
		}
		this.cateRepo.deleteById(Id);
	}
	
	public void updateCategoryEnabledStatus(Integer id, boolean enabled) {
		cateRepo.updateEnabledStatus(enabled, id);
	}
	
	public boolean isNameUnique (Integer id, String name) {
		Category cateByName = cateRepo.getCategoryByName(name);
		
		if(cateByName == null) return true;
		
		boolean isCreatingNew = (id == null);
		
		if(isCreatingNew) {
			if (cateByName != null) return false;			
		}
		else {
			if (cateByName.getId() != id) {
				return false;
			}
		}
		return true;
	}
	
	public List<Category> listByPage(CategoryPageInfo pageInfo, int pageNum,  String sortDir,
			String keyword){
		Sort sort = Sort.by("name");
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum -1, ROOT_CATEGORY_PER_PAGE, sort);
		
		Page<Category> pageCategories = null;
		
		if (keyword != null && !keyword.isEmpty()) {
			pageCategories = cateRepo.search(keyword, pageable);
		} else {
			pageCategories = cateRepo.findRootCategories(pageable);
		}
		
		List<Category> rootCategories = pageCategories.getContent();
		
		pageInfo.setTotalElements(pageCategories.getTotalElements());
		pageInfo.setTotalPages(pageCategories.getTotalPages());
		
		if (keyword != null && !keyword.isEmpty()) {
			List<Category> searchResult = pageCategories.getContent();
			
			for (Category category : searchResult) {
				category.setHasChildren(category.getChildren().size() > 0);
			}
			return searchResult;
		}
		else {
			return listHierachicalCategories(rootCategories);
		}
	}
	
	private List<Category> listHierachicalCategories(List<Category> rootCategories) {
		List<Category> hierarchiCategories = new ArrayList<>();
		
		for (Category rootCategory : rootCategories) {
			hierarchiCategories.add(Category.copyFull(rootCategory));
			
			Set<Category> children = rootCategory.getChildren();
			
			for (Category subCategory : children) {
				String name ="--"+ subCategory.getName();
				hierarchiCategories.add(Category.copyFull(subCategory, name));
				
				listSubHierachicalCategories(hierarchiCategories, subCategory, 1);
			}
		}
		return hierarchiCategories;
	}
	
	private void listSubHierachicalCategories(List<Category> hierarchiCategories, Category parent, int sublevel) {

		Set<Category> children = parent.getChildren();
		int newSubLevel =sublevel +1;
		
		for (Category subCategory : children) {
			String name ="";
			for (int i = 0; i < newSubLevel; i++) {
				name+="--";
			}
			name+= subCategory.getName();
			
			
			hierarchiCategories.add(Category.copyFull(subCategory, name));
			
			listSubHierachicalCategories(hierarchiCategories, subCategory, newSubLevel);
		}
		
	}


	
	public Category getCategory() {
		Category computers=  cateRepo.getCategoryByName("Computers");
		return computers;
	}
	
//	public void setChildren (Set<Category>set, String underline){
//		   for (Category category : set) {
//			    String newName =underline + category.getName();
//			   	category.setName(newName); 
//				System.out.println(category.getName());
//				
//				if (category.getChildren() != null) {
//					setChildren(category.getChildren(), underline+"--");
//				}
//		   }
//		   
//		   
//	}
	
//	public boolean checkParent(Set<Category>set) {
//		for (Category category : set) {
//			if (category.getName().contains("--")) {
//				return false;
//			}
//		}
//		return true;
//	}
	
	
//		public List<Category> listCategories() { 
//			return (List<Category>) cateRepo.findAll();
//		}

		public List<Category> listCategoriesUsedInForm() {
			List<Category> categoriesUsedInform = new ArrayList<>();
			
			Iterable<Category> categoriesInDB = cateRepo.findRootCategories(Sort.by("name").ascending());
			
			for (Category category : categoriesInDB) {
				categoriesUsedInform.add(Category.copyIdAndName(category));
				
//				Set<Category> children = sortSubCategories(category.getChildren());
				Set<Category> children = category.getChildren();
				
				for (Category subCategory : categoriesInDB) {
					String name = "--"+subCategory.getName();
					categoriesUsedInform.add(Category.copyIdAndName(subCategory.getId(),name));
					
					listSubCategoriesUsedInForm(categoriesUsedInform, subCategory,1);
				}
			}
			return categoriesUsedInform;
		}

   private void listSubCategoriesUsedInForm(List<Category> categoriesUsedInform, 
		   Category parent, int subLevel) {

	   int newSubLevel = subLevel + 1;
//	   Set<Category> children = sortSubCategories(parent.getChildren());
	   
	   Set<Category> children = parent.getChildren();
	   
	   for (Category subCategory : children) {
		
		   String name = "";
		   
		   for(int i=0; i< newSubLevel; i++ ) {
			   name+="--";
		   }
		   name+= subCategory.getName();
		   
		   categoriesUsedInform.add(Category.copyIdAndName(subCategory.getId(),name));
		   
		   listSubCategoriesUsedInForm(categoriesUsedInform, subCategory, newSubLevel);
	}
	
   }
}
