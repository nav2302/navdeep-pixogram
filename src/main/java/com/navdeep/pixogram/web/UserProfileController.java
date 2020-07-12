package com.navdeep.pixogram.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.navdeep.pixogram.model.User;
import com.navdeep.pixogram.service.UserService;

@Controller
@RequestMapping("/profile")
public class UserProfileController {
	
	@Autowired
	UserService userService;
	
	@GetMapping
	public String getDetails(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findByEmail(auth.getName());
		model.addAttribute("name",user.getFirstName()+" "+user.getLastName());
		model.addAttribute("user",user);
		return "user/profileDetails";
	}
	
	@PostMapping("/{id}")
	public String updateUser(@PathVariable("id") Long id, @ModelAttribute("updatedUser") @Valid User formUser, BindingResult result, Model model) {
		User updatedUser = userService.findById(id);
		updatedUser.setAbout(formUser.getAbout());
		updatedUser.setAddress(formUser.getAddress());
		updatedUser.setDob(formUser.getDob());
		updatedUser.setEmail(formUser.getEmail());
		updatedUser.setFirstName(formUser.getFirstName());
		updatedUser.setLastName(formUser.getLastName());
		updatedUser.setDob(formUser.getDob());
		updatedUser.setPhone(formUser.getPhone());
		userService.update(updatedUser);
		model.addAttribute("name",updatedUser.getFirstName()+" "+updatedUser.getLastName());
		model.addAttribute("user",updatedUser);
		return "user/profileDetails";
	}
	
}
