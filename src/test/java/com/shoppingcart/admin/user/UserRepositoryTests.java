package com.shoppingcart.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shoppingcart.admin.entity.Role;
import com.shoppingcart.admin.entity.User;

@DataJpaTest 
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback (false)
public class UserRepositoryTests {
	@Autowired
	private UserRepository repo;
	
	// ko thể dùng repo.find(Role.Class, 1) --> vì repo có entity là user --> phải
	// dùng TestEntity để sử dụng entityManager.find(Role.class, 1)
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test 
	public void testCreateNewUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User user = new User("nhbtuyen2702@gmail.com", "123456", "Nguyen Hoang Bao", "Tuyen");
		user.setEnabled(true);
		user.setPhotos("TuyenNHB.png");
		user.addRole(roleAdmin);
		
		User savedUser = repo.save(user);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateNewUserWithTwoRoles() {
		
		User userTrung = new User ("ledoantrung1999@gmail.com", "123456", "Le Doan","Trung");
		
		Role roleEditor = new Role(4); // Role roleEditor = entityManager.find(Role.class, 3);
		Role roleAssistant = new Role(5); // Role roleAssistant entityManager.find(Role.class, 5);
		
		userTrung.addRole(roleAssistant);
		userTrung.addRole(roleEditor);
		
		User savedUser = repo.save(userTrung);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = repo.findAll();
		for (User user2 : listUsers) {
			System.out.println(user2);
		}
		// listUsers.forEach(user -> System.out.println(user)); --> cách viết theo round function
	}
}
