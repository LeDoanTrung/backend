package com.shoppingcart.admin.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoppingcart.admin.user.UserService;

@RestController  //return data thay vi view
public class UserRestController {

	@Autowired
	private UserService service;
	
	@PostMapping("/users/check_email")
	public String checkDuplicateEmail(Integer id, String email) {
		return service.isEmailunique(id, email) ? "OK" : "Duplicated";	
	}
}
