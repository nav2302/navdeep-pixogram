package com.navdeep.pixogram.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.navdeep.pixogram.model.Likes;

@Repository
public interface LikeRepository extends JpaRepository<Likes, Long> {

	@Query("select count(l)>0 from Likes l where l.user_id = :userId and l.image_id = :imageId")
	public boolean checkIfLikes(@Param("userId") Long userId, @Param("imageId") Long imageId);

	@Transactional
	@Modifying
	@Query("delete from Likes l where l.user_id = :userId and l.image_id = :imageId")
	public void remove(@Param("userId") Long userId, @Param("imageId") Long imageId);
}