package com.navdeep.pixogram.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.navdeep.pixogram.model.User;
import com.navdeep.pixogram.model.Video;
import com.navdeep.pixogram.repository.VideoRepository;

@Service
public class VideoServiceImpl implements VideoService {
	
	@Autowired
	VideoRepository videoRepository;

	@Override
	public Video findByName(String name) {
		Video video = videoRepository.findByName(name);
		return video;
	}

	@Override
	public Video save(Video video) {
		return videoRepository.save(video);
	}

	@Override
	public List<Video> getAllVideos(User user) {
		return videoRepository.findAllByUsers(user);
	}

	@Override
	public Video findById(Long id) {
		// TODO Auto-generated method stub
		return videoRepository.findById(id);
	}

}
