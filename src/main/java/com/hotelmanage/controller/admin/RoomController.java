package com.hotelmanage.controller.admin;

import com.hotelmanage.entity.room.Room;
import com.hotelmanage.service.room.RoomService;
import com.hotelmanage.service.room.RoomTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final RoomTypeService roomTypeService;

    @GetMapping
    public String listRooms(Model model) {
        List<Room> rooms = roomService.findAll();
        model.addAttribute("rooms", rooms);
        return "admin/room-list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("room", new Room());
        model.addAttribute("roomTypes", roomTypeService.findAll());
        return "admin/room-form";
    }

    @PostMapping("/create")
    public String createRoom(@Valid @ModelAttribute("room") Room room,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roomTypes", roomTypeService.findAll());
            return "admin/room-form";
        }
        try {
            roomService.save(room);
            redirectAttributes.addFlashAttribute("success", "Thêm phòng thành công!");
            return "redirect:/admin/rooms";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/admin/rooms/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Room room = roomService.findById(id);
        model.addAttribute("room", room);
        model.addAttribute("roomTypes", roomTypeService.findAll());
        return "admin/room-form";
    }

    @PostMapping("/update/{id}")
    public String updateRoom(@PathVariable Integer id,
                             @Valid @ModelAttribute("room") Room room,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roomTypes", roomTypeService.findAll());
            return "admin/room-form";
        }
        try {
            room.setRoomId(id);
            roomService.update(room);
            redirectAttributes.addFlashAttribute("success", "Cập nhật phòng thành công!");
            return "redirect:/admin/rooms";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/admin/rooms/edit/" + id;
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteRoom(@PathVariable Integer id,
                             RedirectAttributes redirectAttributes) {
        try {
            roomService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Đã xóa phòng thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/rooms";
    }
}
