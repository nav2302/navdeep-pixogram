package com.navdeep.pixogram.web;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.navdeep.pixogram.model.Image;
import com.navdeep.pixogram.model.Likes;
import com.navdeep.pixogram.model.User;
import com.navdeep.pixogram.service.ImageService;
import com.navdeep.pixogram.service.LikeService;
import com.navdeep.pixogram.service.UserService;
import com.navdeep.pixogram.utils.ImageUtil;

@Controller
@RequestMapping("/users")
public class anotherUserController {
	
	@Autowired
	ImageService imageService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	LikeService likeService;
	
	@GetMapping("/gallery")
	public String searchUser(@RequestParam(value="email",required=false) String userEmail,@RequestParam(value="follow",required=false) String follow, Model model) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			User user = userService.findByEmail(auth.getName());
			
			User searchedUser = userService.findByEmail(userEmail);
			
			if(follow != null) {
				searchedUser.getFollowers().add(user);
				userService.update(searchedUser);
			}
			if(searchedUser!=null) {
				int flag=0;
				List<Image> images;
				if(searchedUser.getFollowers().contains(user)) {
					flag=1;
					images = imageService.getAllImagesWithShow(searchedUser.getId());
					model.addAttribute("follow",1);
				}
				else {
					 images = imageService.getAllImagesWithLimitShow(searchedUser.getId());
					 model.addAttribute("follow",0);
				}
				model.addAttribute("user",user);
				model.addAttribute("searchedUser",searchedUser);
				model.addAttribute("images", images);
				model.addAttribute("flag",flag);
				model.addAttribute("imgUtil", new ImageUtil());
				return "anotherUser/anotherUserListFiles";
			}
			else {
				model.addAttribute("error","No users with this Name Exists Try again!");
				return "errorPage";
			}
	}
	
	@GetMapping("/images/getData/{id}/{follow}")
	public String showImageDetails(@PathVariable("id") Long id, @PathVariable("follow") int follow,@RequestParam(value="searchedUser",required=true) String searchedUserMail, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findByEmail(auth.getName());
		User searchedUser = userService.findByEmail(searchedUserMail);
		Image currImage = imageService.findById(id);
		Long previousImageId = imageService.findPreviousImageId(searchedUser.getId(), id);
		Long nextImageId = imageService.findNextImageId(searchedUser.getId(), id);
		
		model.addAttribute("searchedUser",searchedUser);
		model.addAttribute("user",user);
		model.addAttribute("image",currImage);
		model.addAttribute("prevId", previousImageId);
		model.addAttribute("nextId",nextImageId);
		
		if (follow == 1) {
			if (searchedUser.getFollowers().contains(user)) {
				if (currImage.getShowImage().equalsIgnoreCase("yes")) {
					model.addAttribute("imgUtil", new ImageUtil());
					model.addAttribute("fail", 0);
					model.addAttribute("likes",currImage.getNo_of_likes());
					if(likeService.ifLikes(user.getId(), currImage.getId())) {
						model.addAttribute("yesHeLikes",1);
					}
					else {
						model.addAttribute("yesHeLikes",0);
					}
					
				} else {
					model.addAttribute("fail", 1);
					model.addAttribute("failText", "SORRY! This Image is Hidden by the User");
				}
			} else {
				model.addAttribute("fail", 2);
				model.addAttribute("failText", "Please follow the User to see all his detailed Contents");
			}
		}
		else {
			model.addAttribute("fail", 2);
			model.addAttribute("failText", "Please follow the User to see all his detailed Contents");
		}
		
		return "anotherUser/anotherUserImageDetails";
	}
	
	@GetMapping("/gallery/follow")
	public String followUser(@RequestParam(value="searchedUser",required=true) String searchedUserMail, @RequestParam(value="user",required=true) String userMail, @RequestParam(value="follow",required=true) int follow, @RequestParam(value="id",required=false) Long followId, Model model) {
		User user = userService.findByEmail(userMail);//Not needed if i get user object from authentication or session.
		User searchedUser = userService.findByEmail(searchedUserMail);
		if(follow == 1) {
			searchedUser.addFollower(user);
			userService.update(searchedUser);
			List<Image> images = imageService.getAllImagesWithShow(searchedUser.getId());
			model.addAttribute("user",user);
			model.addAttribute("searchedUser",searchedUser);
			model.addAttribute("images", images);
			model.addAttribute("flag",follow);
			model.addAttribute("imgUtil", new ImageUtil());
			model.addAttribute("follow",1);
			return "anotherUser/anotherUserListFiles";
		}
		else {
			searchedUser.removeFollower(user);
			userService.update(searchedUser);
			List<Image> images = imageService.getAllImagesWithLimitShow(searchedUser.getId());
			model.addAttribute("user", user);
			model.addAttribute("searchedUser", searchedUser);
			model.addAttribute("images", images);
			model.addAttribute("flag", follow);
			model.addAttribute("follow",0);
			model.addAttribute("imgUtil", new ImageUtil());
			return "anotherUser/anotherUserListFiles";
		}
	}
	
	@GetMapping("/profile")
	public String showProfile(@RequestParam(value="searchedUser",required=true) String searchedUserMail, Model model) {
		User searchedUser = userService.findByEmail(searchedUserMail);
		model.addAttribute("user",searchedUser);
		model.addAttribute("name", searchedUser.getFirstName()+" "+searchedUser.getLastName());
		model.addAttribute("followers",searchedUser.getFollowers().size());
		model.addAttribute("following",searchedUser.getFollowing().size());
		model.addAttribute("posts",searchedUser.getImage().size());
		return "anotherUser/anotherUserProfileDetails";
	}
	
	@GetMapping("/images/getData/")
	public String likeImage(@RequestParam(value="id",required=true) Long id, @RequestParam(value="likes",required=true) int likes, @RequestParam(value="userId",required=true) Long userId, RedirectAttributes redirAttrs) {
		Image currImage = imageService.findById(id);
		
		if(likes == 0) {
			Likes like = new Likes(userId, id);
			likeService.save(like);
			if(currImage.getNo_of_likes() != null) {
				currImage.setNo_of_likes(currImage.getNo_of_likes()+1);
			}
			else {
				currImage.setNo_of_likes((long) 1);
			}
		}
		else {
			likeService.removeLike(userId,id);
			currImage.setNo_of_likes(currImage.getNo_of_likes()-1);
		}
		imageService.save(currImage);

		redirAttrs.addAttribute("id", id);
		redirAttrs.addAttribute("follow",1);
		return "redirect:/images/getData/{id}/{follow}";
	}
	
	
	
	
}