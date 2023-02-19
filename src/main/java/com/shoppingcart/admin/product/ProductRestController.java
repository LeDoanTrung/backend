package com.shoppingcart.admin.product;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoppingcart.admin.brand.BrandNotFoundException;
import com.shoppingcart.admin.brand.BrandService;
import com.shoppingcart.admin.category.CategoryService;
import com.shoppingcart.admin.entity.Brand;
import com.shoppingcart.admin.entity.Category;

@RestController
public class ProductRestController {
	@Autowired
	private ProductService service;
	
	
	@PostMapping("/products/check_unique")
	public String checkUnique (@Param("id") Integer id, @Param("name") String name, @Param("alias") String alias) {
		
		return service.checkUnique(id, name, alias);
	}
	
	
}
