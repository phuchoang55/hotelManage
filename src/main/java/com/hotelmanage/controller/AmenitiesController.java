package com.hotelmanage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class AmenitiesController {

    @GetMapping("/amenities")
    public String viewAmenities(Model model) {
        List<Map<String, String>> amenities = Arrays.asList(
                Map.of("name", "Swimming Pool", "desc", "Open daily from 6 AM - 10 PM", "image", "/images/pool.jpg", "time", "6:00 - 22:00"),
                Map.of("name", "Fitness Center", "desc", "Open 24 hours, fully equipped gym", "image", "/images/gym.jpg", "time", "24/7"),
                Map.of("name", "Spa & Sauna", "desc", "Relax with premium spa and sauna services", "image", "/images/spa.jpg", "time", "8:00 - 21:00"),
                Map.of("name", "Restaurant", "desc", "Buffet breakfast and fine dining options", "image", "/images/restaurant.jpg", "time", "7:00 - 23:00")
        );

        model.addAttribute("amenities", amenities);
        return "amenities/list";
    }
}
