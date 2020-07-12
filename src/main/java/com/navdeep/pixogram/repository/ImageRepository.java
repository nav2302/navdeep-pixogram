package com.navdeep.pixogram.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.navdeep.pixogram.model.Image;
import com.navdeep.pixogram.model.User;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
	public Image findByName(String name);

	List<Image> findAllByUsers(User user);

	public Image findById(Long id);
	
	@Modifying
	@Transactional
	@Query("update Image m set m.showImage = :showValue where m.id = :id")
	void updateShow(@Param("showValue") String showValue, @Param("id") Long id);
}