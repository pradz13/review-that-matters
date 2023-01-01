package com.proj.repository;

import com.proj.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query("from Post as p where p.user.id = :userId")
    public Page<Post> getPostsByUser(@Param("userId") int userId, Pageable pageable);
}
