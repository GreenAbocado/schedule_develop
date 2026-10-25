package com.greenabocado.scheduledevelop.repository;

import com.greenabocado.scheduledevelop.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
