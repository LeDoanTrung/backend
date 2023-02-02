package com.shoppingcart.admin.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
// Tạo Spring Bean userRepository
// JPA sử dụng tên Entity và tên thuộc tính để truy vấn, ko sử dụng table và column trong db
// Spring data JPA
import com.shoppingcart.admin.entity.User;

//public interface UserRepository extends CrudRepository<User, Integer>{ //tham số đầu tiên là Entity, tham số thứ hai là kiểu của khóa chính
	public interface UserRepository extends PagingAndSortingRepository<User, Integer>{
	//checkDuplicateEmail
	@Query("SELECT u FROM User u WHERE u.email = :email")
	public User getUserByEmail(@Param("email")String email); // ten phai giong cai param
	
	//countBy..., findBy... => JPA
	
	public Long countById(Integer id); //=> tự tạo hàm cho riêng mình
										// nguyên tắc đặt: countBy + "tên thuộc tính" và viết hoa chữ cái đầu

	//Câu truy vấn sẽ dựa trên @Entity và tên của thuộc tính của Entity đó.
	//truyen param theo index
	@Query("update User u set u.enabled = ?1 where u.id = ?2") // truyền param theo thứ tự index
	@Modifying // Bắt buộc phải có khi dùng UPDATE/ INSERT/ DELETE
	public void updateEnabledStatus(boolean enabled, Integer Id);


	@Query("SELECT u FROM User u Where u.firstName LIKE %?1% OR u.lastName LIKE %?1% OR u.email LIKE %?1%")
	public Page<User> findAll(String keyword, Pageable pageable);
}
