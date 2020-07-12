package com.navdeep.pixogram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.navdeep.pixogram.model.Likes;
import com.navdeep.pixogram.repository.LikeRepository;

@Service
public class LikeService {
	
	@Autowired
	LikeRepository likeRepository;
	
	public boolean ifLikes(Long userId, Long imageId) {
		return likeRepository.checkIfLikes(userId, imageId);
	}

	public Likes save(Likes like) {
		return likeRepository.save(like);
	}

	public void removeLike(Long userId, Long imageId) {
		likeRepository.remove(userId, imageId);
	}
}
