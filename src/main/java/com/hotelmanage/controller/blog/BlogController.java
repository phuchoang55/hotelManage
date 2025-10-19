package com.hotelmanage.controller.blog;

import com.hotelmanage.repository.blog.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class BlogController {

    private final BlogRepository blogRepository;

    @GetMapping("/blogs")
    public String listBlogs(Model model) {
        model.addAttribute("blogs", blogRepository.findAllByOrderByIdDesc());
        return "blog/list";
    }
}


