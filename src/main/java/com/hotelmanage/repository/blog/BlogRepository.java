package com.hotelmanage.repository.blog;

import com.hotelmanage.entity.blog.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findAllByOrderByIdDesc();
}

