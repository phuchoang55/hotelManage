package com.hotelmanage.controller.roomtype;

import com.hotelmanage.entity.room.RoomType;
import com.hotelmanage.service.room.RoomTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/room-types")
@RequiredArgsConstructor
public class UserRoomTypeController {

    private final RoomTypeService roomTypeService;

    @GetMapping
    public String listRoomTypes(Model model) {
        List<RoomType> roomTypes = roomTypeService.findAll();
        model.addAttribute("roomTypes", roomTypes);
        return "users/room-type-list";
    }

    @GetMapping("/{id}")
    public String viewRoomTypeDetail(@PathVariable Integer id, Model model) {
        RoomType roomType = roomTypeService.findById(id);
        model.addAttribute("roomType", roomType);
        return "users/room-type-detail";
    }
}
