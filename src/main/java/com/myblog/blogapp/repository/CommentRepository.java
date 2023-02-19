package com.myblog.blogapp.repository;

import com.myblog.blogapp.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findByPostId(long postId);//when we call this method spring boot automatically fetched the record based
    //based on postId,this method is not available in jpa so that's why we have to define it explicately.
    //List<Comment> findByEmail(String email);
   // List<Comment> findByName(String name);

}
