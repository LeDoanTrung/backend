package com.shoppingcart.admin.account;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shoppingcart.admin.FileUploadUtil;
import com.shoppingcart.admin.entity.Role;
import com.shoppingcart.admin.entity.User;
import com.shoppingcart.admin.security.ShoppingUserDetails;
import com.shoppingcart.admin.user.UserNotFoundException;
import com.shoppingcart.admin.user.UserService;
@Controller
public class AccountController {
	@Autowired
	private UserService service;
	
	@GetMapping("/account")
	public String accountDetail(@AuthenticationPrincipal ShoppingUserDetails userLogged, Model model) throws UserNotFoundException {
		String email = userLogged.getUsername();
		User user = service.getUserByEmail(email);
		model.addAttribute("user", user);
		
		return "users/user_detail";
	}
	
	@PostMapping("/account/update")
	public String accountUpdate(User user,@AuthenticationPrincipal ShoppingUserDetails userDetails, RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {
		
		
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename()); // Amina Elshal.png--> Amina Elshal.png
			user.setPhotos(fileName);
			User savedUser = service.updateAccount(user);
			
			String uploadDir = "user-photos/" + savedUser.getId(); // tạo folder user-photos theo id để lưu hình
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir,fileName, multipartFile);
			
			
		}
		else {
			if (user.getPhotos().isEmpty()) user.setPhotos(null); { // nếu ko chọn image thì lưu null
				service.updateAccount(user);
				
			}
		}
	
		String firstNameUpdate= user.getFirstName();
		userDetails.setFirstName(firstNameUpdate);
		
		String lastNameUpdate= user.getLastName();
		userDetails.setLastName(lastNameUpdate);
		
		
		String photoUpdate = user.getPhotos();
		userDetails.setPhotos(photoUpdate);
		
		redirectAttributes.addFlashAttribute("message", "Your account has been updated successfully.");
		
		return "redirect:/account";
		
		//redirect sẽ trả về một thằng getMapping. Nếu chỉ để return "redirect:/"; 
		//thì nó trả về trang mặc định
	}
}
