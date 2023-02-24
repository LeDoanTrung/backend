package com.shoppingcart.admin.product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shoppingcart.admin.FileUploadUtil;
import com.shoppingcart.admin.brand.BrandService;
import com.shoppingcart.admin.category.CategoryService;
import com.shoppingcart.admin.entity.Brand;
import com.shoppingcart.admin.entity.Category;
import com.shoppingcart.admin.entity.Product;
import com.shoppingcart.admin.user.export.ProductCsvExporter;

@Controller
public class ProductController {
	
	@Autowired
	private ProductService service;
	
	@Autowired
	private BrandService brandService;
	
	@Autowired
	private  CategoryService categoryService;
	
	@GetMapping("/products")
	public String listAll(Model model) {
		return listByPage(1, model, "name", "asc", null);
	}
	
	
	@GetMapping("/products/new")
	public String createNew(Model model) {
		List<Brand> listBrands = brandService.listAll(); 
		Product product = new Product();
		product.setEnabled(true);
		product.setInStock(true);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("product", product);
		model.addAttribute("pageTitle", "Create New Product");
		model.addAttribute("numberOfExistingExtraImages", 0);
		
		return "products/product_form";
	}
	
	@PostMapping("/products/save")
	public String saveProduct (Product product, Model model, RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename()); // Amina Elshal.png--> Amina Elshal.png
			product.setMainImage(fileName);
			product.setCreateTime(new Date());
			Product savedProduct = service.save(product);
			
			String uploadDir = "product-images/" + savedProduct.getId(); // tạo folder user-photos theo id để lưu hình
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir,fileName, multipartFile);
		}
		else {
			if (product.getMainImage()==null || product.getMainImage().isEmpty()) product.setMainImage(null); { // nếu ko chọn image thì lưu null
				service.save(product);
			}
		}
		
		redirectAttributes.addFlashAttribute("message", "The product has been saved successfully.");
		
		return "redirect:/products";
	}
	
	@GetMapping("/products/edit/{id}")
	public String editProduct ( Model model, RedirectAttributes redirectAttributes, @PathVariable(name="id") Integer id) {
		try {
	
			List<Brand> listBrands = brandService.listAll(); 
			Product product = service.get(id);
			model.addAttribute("listBrands", listBrands);
			model.addAttribute("product", product);
			model.addAttribute("pageTitle", "Edit Product (ID: " + id +")");
			
			return "products/product_form";
			
		} catch (ProductNotFoundException ex) {
			
			redirectAttributes.addFlashAttribute("message",ex.getMessage());
			return "redirect:/products";
		}
		
	}
	
	@GetMapping("/products/delete/{id}")
	public String deleteCategory (@PathVariable(name="id") Integer id, Model model,  RedirectAttributes redirectAttributes ) {
		try {
			service.delete(id);
			redirectAttributes.addFlashAttribute("message", "Delete Product successfully");
			
			return "redirect:/products";
		} catch (ProductNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			
			return "redirect:/products";
		}
	}
	
	@GetMapping("/products/{id}/enabled/{status}")
	public String enabledCategory(@PathVariable(name="id") Integer id, @PathVariable(name="status") boolean enabled, Model model,RedirectAttributes redirectAttributes) {
		service.updateProductEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The Product ID "+ id + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);
				
		return "redirect:/products";
	}
	
	@GetMapping("/products/page/{pageNum}")
	public String listByPage(@PathVariable(name="pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword) {
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		if (sortDir == null || sortDir.isEmpty()) {
			sortDir="asc";
		}
		System.err.println("Sort Field:" + sortField);
		System.err.println("Sort Dir:"+sortDir);
		

		Page<Product> page = service.listByPage(pageNum, sortField, sortDir, keyword); 
		List<Product> listProducts = page.getContent();
		
		long startCount = (pageNum-1)*service.PRODUCTS_PER_PAGE+1;
		long endCount = startCount + service.PRODUCTS_PER_PAGE -1;
		
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("sortField", sortField);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		
		return "/products/products";
	}
	
	@GetMapping("/products/export/csv")
	public void exportCSV(HttpServletResponse response) throws IOException{
		List<Product> listProducts = service.listAll();
		ProductCsvExporter exporter = new ProductCsvExporter();
		exporter.export(listProducts, response);
	}
	
}
