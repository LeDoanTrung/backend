package com.shoppingcart.admin.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import com.shoppingcart.admin.entity.Category;
import com.shoppingcart.admin.entity.User;
import com.shoppingcart.admin.user.CategoryNotFoundException;
import com.shoppingcart.admin.user.CategoryService;
import com.shoppingcart.admin.user.export.CategoryCsvExporter;
import com.shoppingcart.admin.user.export.CategoryExcelExporter;
import com.shoppingcart.admin.user.export.CategoryPdfExporter;
import com.shoppingcart.admin.user.export.UserCsvExporter;

@Controller
public class CategoryController {

	@Autowired
	private CategoryService service;
	
	@GetMapping("/categories")
	public String listAll(Model model) {
//		List<Category> listCategories = service.listAll();
//		model.addAttribute("listCategories",listCategories);
		
		return listByPage(1, model, "name", "asc", null);
	}
	
	@GetMapping("/categories/new")
	public String createNew(Model model) {
		Category category = new Category();
		model.addAttribute("category", category);
		model.addAttribute("pageTitle", "Create Category");
		
		return "categories/categories_form";
	}
	
	@PostMapping("/categories/save")
	public String saveCategory (Category category, Model model, RedirectAttributes redirectAttributes,
			@RequestParam("fileImage") MultipartFile multipartFile) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename()); // Amina Elshal.png--> Amina Elshal.png
			category.setImage(fileName);
			Category savedCate = service.save(category);
			
			String uploadDir = "category-photos/" + savedCate.getId(); // tạo folder user-photos theo id để lưu hình
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir,fileName, multipartFile);
		}
		else {
			if (category.getImage().isEmpty()) category.setImage(null); { // nếu ko chọn image thì lưu null
				service.save(category);
			}
		}
		
		redirectAttributes.addFlashAttribute("message", "The category has been saved successfully.");
		
		return "redirect:/categories";
	}
	
	@GetMapping("/categories/edit/{id}")
	public String editCategory ( Model model, RedirectAttributes redirectAttributes, @PathVariable(name="id") Integer id) {
		try {
			Category category = service.get(id);
			model.addAttribute("category", category);
			model.addAttribute("pageTitle", "Edit Category (ID: " + id +")");
			
			return "categories/categories_form";
			
		} catch (CategoryNotFoundException ex) {
			
			redirectAttributes.addFlashAttribute("message",ex.getMessage());
			return "redirect:/categories";
		}
	}
	
	@GetMapping("/categories/delete/{id}")
	public String deleteCategory (@PathVariable(name="id") Integer id, Model model,  RedirectAttributes redirectAttributes ) {
		try {
			service.delete(id);
			redirectAttributes.addFlashAttribute("message", "Delete Category successfully");
			
			return "redirect:/categories";
		} catch (CategoryNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			
			return "redirect:/categories";
		}
	}
	
	
	@GetMapping("/categories/{id}/enabled/{status}")
	public String enabledCategory(@PathVariable(name="id") Integer id, @PathVariable(name="status") boolean enabled, Model model,RedirectAttributes redirectAttributes) {
		service.updateCategoryEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The Category ID "+ id + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);
				
		return "redirect:/categories";
	}
	
	@GetMapping("/categories/page/{pageNum}")
	public String listByPage(@PathVariable(name="pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword) {
		
		System.out.println("Sort Field:" + sortField);
		System.err.println("Sort Dir:"+sortDir);
		
		Page<Category> page = service.listByPage(pageNum,sortField,sortDir, keyword);
		List<Category> categories = page.getContent();
		
		long startCount = (pageNum-1)*service.CATEGORY_PER_PAGE+1;
		long endCount = startCount + service.CATEGORY_PER_PAGE -1;
		
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("listCategories", categories);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("sortField", sortField);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		
		return "/categories/categories";
	}
	
	
	@GetMapping("/categories/export/csv")
	public void exportCSV(HttpServletResponse response) throws IOException{
		List<Category> listCategories = service.listAll();
		CategoryCsvExporter exporter = new CategoryCsvExporter();
		exporter.export(listCategories, response);
	}
	
	@GetMapping("/categories/export/excel")
	public void exportExcel(HttpServletResponse response) throws IOException {
		List<Category> listCategories = service.listAll();
		CategoryExcelExporter exporter = new CategoryExcelExporter();
		exporter.export(listCategories, response);
	}
	
	@GetMapping("/categories/export/pdf")
	public void exportPDF(HttpServletResponse response) throws IOException {
		List<Category> listCategories = service.listAll();
		CategoryPdfExporter exporter = new CategoryPdfExporter();
		exporter.export(listCategories, response);
	} 
}
