package com.shoppingcart.admin.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


import com.shoppingcart.admin.user.UserService;

@RestController
public class CategoryRestController {
	@Autowired
	private CategoryService service;
	
	@PostMapping("/categories/check_name")
	public String checkDuplicatedName (Integer id, String name) {
		return service.isNameUnique(id, name) ? "OK" : "Duplicated";
	}
}
