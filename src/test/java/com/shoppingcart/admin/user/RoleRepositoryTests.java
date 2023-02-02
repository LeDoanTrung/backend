package com.shoppingcart.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shoppingcart.admin.entity.Role;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

//viết JUnit cho RoleRepositoryTests
@DataJpaTest 
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback (false)// mặc định sẽ rollback transaction ---> các thay đổi sẽ ko đc lưu, trả lại trạng thái trước đó
public class RoleRepositoryTests {
	@Autowired
	private RoleRepository repo;
	
	// bôi đen testCreateFirstRole nhấn chuột phải chọn Debug As > JUnit Test
	@Test
	public void testCreateFirstRole() {
		Role roleAdmin = new Role("Admin", "manage everything");
		Role savedRole = repo.save(roleAdmin);
		
		// kiểm tra nếu Id > 0 có nghĩa là đã save thành công
		assertThat(savedRole.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateRestRole() {
		Role roleSalesperson = new Role("Salesperson", "manage product price, " 
				+"customers, shipping, orders and sales report");
		
		Role roleEditor = new Role("Editor", "manage categories, brand, "+
		"products, articles and menus");
		
		Role roleShipper = new Role("Shipper", "view products, view orders " +
		"and update order status");
		
		Role roleAssistant = new Role("Assistant", "manage questions and reviews");
		
		repo.saveAll(List.of(roleSalesperson, roleEditor, roleShipper, roleAssistant));
	}
	
}
