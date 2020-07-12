package com.navdeep.pixogram.service;

import java.util.List;

import com.navdeep.pixogram.model.Image;
import com.navdeep.pixogram.model.User;

public interface ImageService {

	Image findByName(String name);

	Image save(Image image);

	List<Image> getAllImages(User user);

	Image findById(Long id);

	void updateHideOrShow(String showValue,Long id);

	List<Image> getAllImagesWithShow(Long id);

	List<Image> getAllImagesWithLimitShow(Long id);

	Long findPreviousImageId(Long userId, Long imageId);
	
	Long findNextImageId(Long userId, Long imageId);
}
