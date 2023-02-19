package com.shoppingcart.admin.brand;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoppingcart.admin.entity.Brand;
import com.shoppingcart.admin.entity.Category;

@RestController
public class BrandRestController {

	@Autowired
	private BrandService service;
	
	@PostMapping("/brands/check_name")
	public String checkDuplicate(Integer id, String name) {
		return service.isNameUnique(id, name) ? "OK" : "Duplicated";	
	}
	
	@GetMapping("/brands/{id}/category")
	public List<Category> listCateByBrand(@PathVariable(name="id") Integer id) throws BrandNotFoundException{
		Brand brand = service.get(id);
		Set<Category>setCategories = brand.getCategories();
		List<Category> listCategories = new ArrayList();
		for (Category category : setCategories) {
			Category newCategory=  Category.copyIdAndName(category.getId(), category.getName());
			
			listCategories.add(newCategory);
			
		}
		return listCategories;
	}
}
