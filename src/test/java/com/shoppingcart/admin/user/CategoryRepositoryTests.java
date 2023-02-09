package com.shoppingcart.admin.user;

import static org.mockito.ArgumentMatchers.intThat;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.internal.invocation.mockref.MockStrongReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.fasterxml.jackson.core.sym.Name;
import com.shoppingcart.admin.category.CategoryRepository;
import com.shoppingcart.admin.entity.Category;

@DataJpaTest 
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback (false)
public class CategoryRepositoryTests {
	
	@Autowired
	CategoryRepository repository;
	
	
	
	
	
	@Test
	public void InsertData() {
		Category computers =  new Category("Computers"); 
		repository.save(computers);
		
		Category desktops =  new Category("Desktops", computers); 
		repository.save(desktops);
		
		Category laptops = new Category("Laptops",computers);
		repository.save(laptops);
		
		Category laptop1 = new Category("Laptop 1",laptops);
		repository.save(laptop1);
		
		Category laptop2 = new Category("Laptop 2",laptops);
		repository.save(laptop2);
		
		Category laptop21 = new Category("Laptop 21",laptop2);
		repository.save(laptop21);
		
		Category laptop211 = new Category("Laptop 211",laptop21);
		repository.save(laptop211);
		
		Category laptop3 = new Category("Laptop 3",laptops);
		repository.save(laptop3);
		
		Category computerComponents =  new Category("Computer Components", computers); 
		repository.save(computerComponents);
		
		Category memoryA = new Category("Memory A",computerComponents);
		repository.save(memoryA);
		
		Category a1 = new Category("a1",memoryA);
		repository.save(a1);
		
		Category a2 = new Category("a2",a1);
		repository.save(a2);
		
		Category a3 = new Category("a3",a2);
		repository.save(a3);
		
		Category a4 = new Category("a4",a3);
		repository.save(a4);
		
		Category memoryB = new Category("Memory B",computerComponents);
		repository.save(memoryB);
		
		Category b1 = new Category("b1",memoryB);
		repository.save(b1);
		
		Category b2 = new Category("b2",b1);
		repository.save(b2);
	}
	
	@Test
	public void testCreateRootCategory() {
		Scanner scan = new Scanner(System.in);
		System.out.println("Input Parent: ");
		String name = scan.next();
		
		switch (name) {
		case "Computers":
			Category category = repository.getCategoryByName(name);
			Set <Category> set = category.getChildren();
			int level =1;
			PrintChild( set, level);
			break;
		default:
			break;
		}
		
		
	}
	
	
	 
	
	public void PrintChild( Set <Category> set, int level) {
		   if(set == null) {
		      return ;
		   }
		   Iterator<Category> iterator = set.iterator();
		   while (iterator.hasNext()) {
		      Category object = (Category) iterator.next();
		      for (int i = 0; i < level; i++) {
		         System.out.print("--");
		      }
		      String CateName = object.getName();
		      Set<Category> newSet = object.getChildren();
		      
		      System.out.println(CateName);
		      PrintChild( newSet, level + 1);
		   }
		}



}
