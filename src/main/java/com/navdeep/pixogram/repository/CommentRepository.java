package com.navdeep.pixogram.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.navdeep.pixogram.model.Comments;

@Repository
public interface CommentRepository extends JpaRepository<Comments, Long> {
	
	@Query("select c from Comments c where c.image.id = :id order by c.modifyDate")
	List<Comments> getAllCommentsByDate(@Param("id")Long id);
}
