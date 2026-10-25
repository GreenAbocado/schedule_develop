package com.greenabocado.scheduledevelop.repository;

import com.greenabocado.scheduledevelop.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("""
           SELECT c
           FROM Comment c
           JOIN FETCH c.member m
           WHERE c.schedule.id = :scheduleId
           """)
    List<Comment> findAllByScheduleIdWithMember(@Param("scheduleId") Long scheduleId);
}