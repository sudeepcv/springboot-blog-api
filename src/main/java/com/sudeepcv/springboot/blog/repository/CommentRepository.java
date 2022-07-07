package com.sudeepcv.springboot.blog.repository;

import com.sudeepcv.springboot.blog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//SimpleJpaRepository already annotated with @Repository so here we don't need to annotate with the same

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByPostId(long postId);

}
