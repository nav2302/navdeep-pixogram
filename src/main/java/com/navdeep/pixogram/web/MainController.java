package com.navdeep.pixogram.web;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.navdeep.pixogram.model.Comments;
import com.navdeep.pixogram.model.Image;
import com.navdeep.pixogram.model.Likes;
import com.navdeep.pixogram.model.User;
import com.navdeep.pixogram.model.Video;
import com.navdeep.pixogram.repository.CommentRepository;
import com.navdeep.pixogram.service.ImageService;
import com.navdeep.pixogram.service.LikeService;
import com.navdeep.pixogram.service.UserService;
import com.navdeep.pixogram.service.VideoService;
import com.navdeep.pixogram.utils.ImageUtil;
import com.navdeep.pixogram.web.dto.ImageUpdationDao;
import com.navdeep.pixogram.web.dto.SearchUserDto;

@Controller
public class MainController {

	@Autowired
	UserService userService;

	@Autowired
	ImageService imageService;
	
	@Autowired
	VideoService videoService;
	
	@Autowired
	LikeService likeService;
	
	@Autowired
	CommentRepository commentRepository;
	
	@ModelAttribute("user")
	public ImageUpdationDao imageUpdationDto() {
		return new ImageUpdationDao();
	}

	@GetMapping("/")
	public ModelAndView root() {
		ModelAndView model = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findByEmail(auth.getName());
		model.addObject("userName", user.getFirstName() + " " + user.getLastName());
		model.setViewName("index");
		return model;
	}

	@GetMapping("/login")
	public String login(Model model) {
		return "login";
	}

	@GetMapping("/user")
	public String userIndex() {
		return "user/index";
	}

	@GetMapping("/upload-file")
	public String uploadForm(Model model) {
		return "uploadform";
	}

	@PostMapping("/upload-file")
	public String uploadMultipartFile(@RequestParam("files") MultipartFile[] files, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findByEmail(auth.getName());
		
		List<Image> userImages = user.getImage();
		List<Video> userVideos = user.getVideo();
		List<String> fileNames = new ArrayList<String>();
		HashMap<String, Image> userImagesMap = new HashMap<String, Image>();
		HashMap<String, Video> userVideosMap = new HashMap<String, Video>();
		for(Image image: userImages) {
			userImagesMap.put(image.getName(), image);
		}
		
		for(Video video: userVideos) {
			userVideosMap.put(video.getName(), video);
		}
		
		try {
			List<Image> storedFile = new ArrayList<>();
			List<Video> storedVideos = new ArrayList<>();

			boolean otherFiles = false;
			
			for (MultipartFile file : files) {
				if (file.getContentType().equalsIgnoreCase("image/jpeg")
						|| file.getContentType().equalsIgnoreCase("image/png")) {
				
					String userImageName = file.getOriginalFilename();

					if(userImagesMap.containsKey(userImageName)) {
						Image image = userImagesMap.get(userImageName);
						image.setPic(file.getBytes());
						fileNames.add(file.getOriginalFilename());
						imageService.save(image);
					}
					
					else {
						Image imageModel = new Image(file.getOriginalFilename(), file.getContentType(), file.getBytes(),"yes");
						fileNames.add(file.getOriginalFilename());
						storedFile.add(imageModel);
					}
				}
				else if (file.getContentType().equalsIgnoreCase("video/quicktime")) {
					String userVideoName = file.getOriginalFilename();

					if(userVideosMap.containsKey(userVideoName)) {
						Video video = userVideosMap.get(userVideoName);
						video.setPic(file.getBytes());
						fileNames.add(file.getOriginalFilename());
						videoService.save(video);
					}
					
					else {
						Video videoModel = new Video(file.getOriginalFilename(), file.getContentType(), file.getBytes());
						fileNames.add(file.getOriginalFilename());
						storedVideos.add(videoModel);
					}
				}
				else {
					System.out.println(file.getContentType());
					otherFiles = true;
					break;
				}

			}

			if (otherFiles) {
				fileNames = new ArrayList<String>();
				model.addAttribute("message", "May be you tried uploading an unsupported Format!  Please Try Again :-)");
				model.addAttribute("files", fileNames);
			}
			
			
			else {
				// Save all Files to database
				for (Image image : storedFile) {
					userImages.add(image);
//					imageService.save(image);
				}
				for (Video video : storedVideos) {
					user.addVideo(video);
					videoService.save(video);
				}
				userService.update(user);
				model.addAttribute("message", "All Files uploaded successfully!");
				model.addAttribute("files", fileNames);
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "Failed due to "+e.getMessage());
			model.addAttribute("files", fileNames);
		}

		return "uploadform";
	}

	@GetMapping("/files")
	public String seeFiles(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findByEmail(auth.getName());

		List<Image> images = imageService.getAllImages(user);
		List<String> userNames = userService.findAllNames();
		model.addAttribute("message", "Your Gallery is here");
		model.addAttribute("findUser", new SearchUserDto());
		model.addAttribute("images", images);
		model.addAttribute("users",userNames);
		model.addAttribute("imgUtil", new ImageUtil());
		return "listfiles";
	}

	@GetMapping("/files/addData/{id}")
	public String editImageDetails(@PathVariable("id") Long id, Model model) {
		Image currImage = imageService.findById(id);
		model.addAttribute("user", new ImageUpdationDao());
		model.addAttribute("image", currImage);
		model.addAttribute("message", "Enter values to Update");
		model.addAttribute("fail", 0);
		return "imageForm";
	}

	@PostMapping("/files/addData/{id}")
	public String editImageSubmit(@PathVariable("id") Long id,
			@ModelAttribute("user") @Valid ImageUpdationDao imageUpdationDto, BindingResult result, Model model, RedirectAttributes r) {
		
		Image updatedImage = imageService.findById(id);
		
		if(imageUpdationDto.getName().isEmpty()) {
			model.addAttribute("fail", 1);
			model.addAttribute("failText", "Name Cannot be empty");
			model.addAttribute("user", new ImageUpdationDao());
			model.addAttribute("image", updatedImage);
			model.addAttribute("message", "Enter values to Update");
			return "imageForm";
		}
		if(imageUpdationDto.getCaption().isEmpty()) {
			model.addAttribute("user", new ImageUpdationDao());
			model.addAttribute("image", updatedImage);
			model.addAttribute("message", "Enter values to Update");
			model.addAttribute("fail", 2);
			model.addAttribute("failText", "Caption cannot be Empty");
			return "imageForm";
		}
		if(imageUpdationDto.getDescription().isEmpty()) {
			model.addAttribute("user", new ImageUpdationDao());
			model.addAttribute("image", updatedImage);
			model.addAttribute("message", "Enter values to Update");
			model.addAttribute("fail", 3);
			model.addAttribute("failText", "Description cannot be Empty");
			return "imageForm";
		}
		updatedImage.setCaption(imageUpdationDto.getCaption());
		updatedImage.setDescription(imageUpdationDto.getDescription());
		updatedImage.setName(imageUpdationDto.getName());
		imageService.save(updatedImage);
		r.addAttribute("id", id);
		return "redirect:/files/getData/{id}";
	}
	
	@GetMapping("/files/getData/{id}")
	public String showImageDetails(@PathVariable("id") Long id,@RequestParam(value="dataUrl",required=false) String dataUrl, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findByEmail(auth.getName());
		Image currImage = imageService.findById(id);
		Long previousImageId = imageService.findPreviousImageId(user.getId(), id);
		Long nextImageId = imageService.findNextImageId(user.getId(), id);
		List<Comments> comments = commentRepository.getAllCommentsByDate(id);
		if(likeService.ifLikes(user.getId(), currImage.getId())) {
			model.addAttribute("yesHeLikes",1);
		}
		else {
			model.addAttribute("yesHeLikes",0);
		}
		model.addAttribute("likes",currImage.getNo_of_likes());
		model.addAttribute("image", currImage);
		model.addAttribute("prevId", previousImageId);
		model.addAttribute("nextId",nextImageId);
		model.addAttribute("user",user);
		model.addAttribute("comments", comments);
		model.addAttribute("imgUtil", new ImageUtil());
		return "imageDetails";
	}
	
	@GetMapping("/files/getData/commentPost")
	public String postComment(@RequestParam(value="id",required=true) Long id, 
			@RequestParam(value="userId",required=true) Long userId,
			@RequestParam(value="message", required=true) String message,
			RedirectAttributes redir) {
		
		User user = userService.findById(userId);
		Image currImage = imageService.findById(id);
		Comments comment = new Comments(new Date(), new Date(), user, currImage, message);
		currImage.addComment(comment);
		user.addComment(comment);
		commentRepository.save(comment);
		redir.addAttribute("id", id);
		return "redirect:/files/getData/{id}";
	}
	
	@GetMapping("/files/getData")
	public String likeImage(@RequestParam(value="id",required=true) Long id, 
			@RequestParam(value="likes",required=true) int likes, 
			@RequestParam(value="userId",required=true) Long userId, 
			RedirectAttributes redirAttrs) {
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
		return "redirect:/files/getData/{id}";
	}
	
	@GetMapping("/files/getData/{id}/edit")
	public String getImageforEdit(@PathVariable("id") Long imageId, Model model) {
		Image currImage = imageService.findById(imageId);
		model.addAttribute("image", currImage);
		model.addAttribute("imgUtil", new ImageUtil());
		return "editImage";
	}
	
	@PostMapping("/edit")
	public String postEditedImage(@RequestParam(value="id",required=false) Long imageId, @RequestParam(value="dataUrl",required=false) String dataUrl, RedirectAttributes redirAttrs) {
		
		if(dataUrl != null) {
			Image image = imageService.findById(imageId);
			try {
				byte[] decodedImg = Base64.getDecoder().decode(dataUrl.getBytes(StandardCharsets.UTF_8));
	            System.out.println(new ImageUtil().getImgData(decodedImg));
	            image.setPic(decodedImg); 
	            imageService.save(image);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
		else {
			System.out.println("The url is nONe");
		}
		redirAttrs.addAttribute("id", imageId);
		return "redirect:/files/getData/{id}";
	}
	
	@GetMapping("/hide")
	public String hideImage(@RequestParam(value="id",required=true) Long id,@RequestParam(value="show",required=true) String show, RedirectAttributes redirAttrs) {
		if(show.equalsIgnoreCase("yes")) {
			imageService.updateHideOrShow("no",id);
		}
		else {
			imageService.updateHideOrShow("yes",id);
		}
		redirAttrs.addAttribute("id", id);
		return "redirect:/files/getData/{id}";
	}
	
	
	@GetMapping("/files/getData/deleteOrEditComments")
	public String deleteComments(@RequestParam(value="id",required=true) Long id, 
			@RequestParam(value="commentId", required = false) Long commId, RedirectAttributes redir) {
		commentRepository.delete(commId);
		redir.addAttribute("id", id);
		return "redirect:/files/getData/{id}";
	}
}
