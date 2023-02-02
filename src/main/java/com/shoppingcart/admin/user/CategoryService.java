package com.shoppingcart.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

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
	
	public static final int CATEGORY_PER_PAGE = 3;
	
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
	
	public Page<Category> listByPage(int pageNum, String sortField, String sortDir,
			String keyword){
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum -1, CATEGORY_PER_PAGE, sort);
		if (keyword != null) {
			return cateRepo.findAll(keyword,pageable);
		}
		
		return cateRepo.findAll(pageable);
	}
}
