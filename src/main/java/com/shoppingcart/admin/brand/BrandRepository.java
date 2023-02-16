package com.shoppingcart.admin.brand;

import com.shoppingcart.admin.entity.Brand;
import com.shoppingcart.admin.entity.Category;
import com.shoppingcart.admin.entity.IdBaseEntity;
import com.shoppingcart.admin.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;


public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer>{
	
		//checkDuplicateEmail
		@Query("SELECT u FROM Brand u WHERE u.name = :name")
		public Brand getBrandByName(@Param("name")String name); // ten phai giong cai param
		
		//countBy..., findBy... => JPA
		
		//=> tự tạo hàm cho riêng mình
											// nguyên tắc đặt: countBy + "tên thuộc tính" và viết hoa chữ cái đầu


		//@Query("SELECT u FROM User u Where u.firstName LIKE %?1% OR u.lastName LIKE %?1% OR u.email LIKE %?1%")
		@Query("SELECT u FROM Brand u Where CONCAT(u.id,' ',u.name) LIKE %?1%")
		public Page<Brand> findAll(String keyword, Pageable pageable);
		

	    public Long countById(Integer id);

	    public Brand findByName(String name); 
}