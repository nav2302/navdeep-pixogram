package com.navdeep.pixogram.web;

import java.util.List;

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
import com.navdeep.pixogram.model.Video;
import com.navdeep.pixogram.service.UserService;
import com.navdeep.pixogram.service.VideoService;
import com.navdeep.pixogram.utils.ImageUtil;
import com.navdeep.pixogram.web.dto.ImageUpdationDao;

@Controller
@RequestMapping("/videos")
public class VideoController {

	@Autowired
	private UserService userService;

	@Autowired
	private VideoService videoService;

	@ModelAttribute("user")
	public ImageUpdationDao imageUpdationDto() {
		return new ImageUpdationDao();
	}

	@GetMapping
	public String showVideos(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findByEmail(auth.getName());

		List<Video> videos = videoService.getAllVideos(user);
		model.addAttribute("message", "Your Video Gallery is here");
		model.addAttribute("videos", videos);
		model.addAttribute("imgUtil", new ImageUtil());
		model.addAttribute("classActiveSettings", "active");
		return "videos";
	}
	
	@GetMapping("/addData/{id}")
	public String editVideoDetails(@PathVariable("id") Long id, Model model) {
		Video currVideo = videoService.findById(id);
		model.addAttribute("user", new ImageUpdationDao());
		model.addAttribute("video", currVideo);
		model.addAttribute("message", "Enter values to Update");
		model.addAttribute("fail", 0);
		return "videoForm";
	}

	@PostMapping("/addData/{id}")
	public String editVideoSubmit(@PathVariable("id") Long id,
			@ModelAttribute("user") @Valid ImageUpdationDao imageUpdationDto, BindingResult result, Model model) {
		
		Video updatedVideo = videoService.findById(id);
		
		if(imageUpdationDto.getName().isEmpty()) {
			model.addAttribute("fail", 1);
			model.addAttribute("failText", "Name Cannot be empty");
			model.addAttribute("user", new ImageUpdationDao());
			model.addAttribute("video", updatedVideo);
			model.addAttribute("message", "Enter values to Update");
			return "videoForm";
		}
		if(imageUpdationDto.getCaption().isEmpty()) {
			model.addAttribute("user", new ImageUpdationDao());
			model.addAttribute("video", updatedVideo);
			model.addAttribute("message", "Enter values to Update");
			model.addAttribute("fail", 2);
			model.addAttribute("failText", "Caption cannot be Empty");
			return "videoForm";
		}
		if(imageUpdationDto.getDescription().isEmpty()) {
			model.addAttribute("user", new ImageUpdationDao());
			model.addAttribute("video", updatedVideo);
			model.addAttribute("message", "Enter values to Update");
			model.addAttribute("fail", 3);
			model.addAttribute("failText", "Description cannot be Empty");
			return "videoForm";
		}
		updatedVideo.setCaption(imageUpdationDto.getCaption());
		updatedVideo.setDescription(imageUpdationDto.getDescription());
		updatedVideo.setName(imageUpdationDto.getName());
		videoService.save(updatedVideo);
		return "redirect:/videos";
	}

}
