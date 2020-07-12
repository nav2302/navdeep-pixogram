package com.navdeep.pixogram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.navdeep.pixogram.model.CommentReplies;

@Repository
public interface CommentRepliesRepository extends JpaRepository<CommentReplies, Long> {

}