package com.shoppingcart.admin.brand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;

public class BrandRestController {

	@Autowired
	private BrandService service;
	
	@PostMapping("/brands/check_name")
	public String checkDuplicateName(Integer id, String name) {
		return service.isNameUnique(id, name) ? "OK" : "Duplicated";	
	}
}
