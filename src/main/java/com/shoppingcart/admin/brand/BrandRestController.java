package com.shoppingcart.admin.brand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BrandRestController {

	@Autowired
	private BrandService service;
	
	@PostMapping("/brands/check_name")
	public String checkDuplicate(Integer id, String name) {
		return service.isNameUnique(id, name) ? "OK" : "Duplicated";	
	}
}
