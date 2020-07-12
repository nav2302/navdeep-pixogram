package com.navdeep.pixogram.service;

import java.util.List;

import com.navdeep.pixogram.model.User;
import com.navdeep.pixogram.model.Video;

public interface VideoService {
	Video findByName(String name);

	Video save(Video video);

	List<Video> getAllVideos(User user);

	Video findById(Long id);
}
