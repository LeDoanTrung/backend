package com.shoppingcart.admin.user;

//Tạo Spring Bean RoleRepository
import org.springframework.data.repository.CrudRepository;


import com.shoppingcart.admin.entity.Role;


public interface RoleRepository extends CrudRepository<Role, Integer>{
	
}
