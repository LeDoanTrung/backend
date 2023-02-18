package com.shoppingcart.admin.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import com.shoppingcart.admin.entity.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer>{
	
	@Query("SELECT u FROM Product u WHERE u.name = :name")
	public Product getProductByName(@Param("name")String name);
	
	@Query("SELECT u FROM Product u Where CONCAT(u.id,' ',u.name,' ',u.alias) LIKE %?1%")
	public Page<Product> findAll(String keyword, Pageable pageable);
	
	public Long countById(Integer id);

	public Product findByName(String name); 
	
	public Product findByAlias(String alias);
	
	@Query("update Product u set u.enabled = ?1 where u.id = ?2") 
	@Modifying 
	public void updateEnabledStatus(boolean enabled, Integer Id);
}
