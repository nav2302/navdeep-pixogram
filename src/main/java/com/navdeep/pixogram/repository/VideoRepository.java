package com.navdeep.pixogram.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.navdeep.pixogram.model.User;
import com.navdeep.pixogram.model.Video;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
	public Video findByName(String name);

	List<Video> findAllByUsers(User user);

	public Video findById(Long id);
}
