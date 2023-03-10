package com.shoppingcart.admin.product;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shoppingcart.admin.brand.BrandNotFoundException;
import com.shoppingcart.admin.brand.BrandRepository;
import com.shoppingcart.admin.category.CategoryRepository;
import com.shoppingcart.admin.entity.Brand;
import com.shoppingcart.admin.entity.Category;
import com.shoppingcart.admin.entity.Product;

@Service
@Transactional
public class ProductService {

	
	@Autowired
	private ProductRepository proRepo;

	public static final int PRODUCTS_PER_PAGE = 10;
	
	public List<Product> listAll() {
		return (List<Product>) proRepo.findAll(Sort.by("name").ascending());
	}
	
	
	public Product save (Product product) {
		return proRepo.save(product);
	}
	
	public Product get(Integer Id) throws ProductNotFoundException{
		try {
			return this.proRepo.findById(Id).get();
		}
		catch (NoSuchElementException ex) {
			throw new ProductNotFoundException("Could not find any product with ID "+ Id);
		}
	}
	
	public void updateProductEnabledStatus(Integer id, boolean enabled) {
		proRepo.updateEnabledStatus(enabled, id);
	}
	
	public void delete(Integer Id) throws ProductNotFoundException{
		Long countById = proRepo.countById(Id);
		if(countById == null|| countById==0) {
			throw new ProductNotFoundException("Could not find any product with ID "+ Id);
		}
		this.proRepo.deleteById(Id);
	}
	
	public String checkUnique(Integer id, String name, String alias) {
		   boolean isCreatingNew = (id == null|| id==0);
		   
		   Product productByName = proRepo.getProductByName(name);
		   
		   if (isCreatingNew) {
			   if(productByName != null) {
			return "DuplicateName";
		   } else {
		   Product productByAlias = proRepo.findByAlias(alias);
			if (productByAlias != null) {
				return "DuplicateAlias";
			}
		   }
	   } else { // ????y l?? edit, ???? c?? ID, trong tr?????ng h???p m?? thg Cate c?? name tr??ng v?? c??ng ID th?? n?? ??ang ki???m tra v???i ch??nh n??
		   if (productByName != null && productByName.getId() != id) {
			   return "DuplicateName";
		   }
		   
		   Product productByAlias = proRepo.findByAlias(alias);
		   if (productByAlias != null && productByAlias.getId() != id) {
			   return "DuplicateAlias";
		}
	   	}
		   
		   return "OK";
	   }
	
	public Page<Product> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
		
		Sort sort = Sort.by(sortField); // sortField ph???i gi???ng vs thu???c t??nh b??n Entity
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum -1, PRODUCTS_PER_PAGE,sort);
		
		if (keyword != null) {
			return proRepo.findAll(keyword, pageable);
		}
		return proRepo.findAll(pageable);
	}
	
	
	public Product getProductByName(String name) throws ProductNotFoundException{
		try {
			return this.proRepo.getProductByName(name);
		}
		catch (NoSuchElementException ex) {
			throw new ProductNotFoundException("Could not find any product with Name "+ name);
		}
	}
	
}
