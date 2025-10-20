package com.hotelmanage.controller.admin;

import com.hotelmanage.entity.room.RoomType;
import com.hotelmanage.service.room.RoomTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/room-types")
@RequiredArgsConstructor
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    @GetMapping
    public String listRoomTypes(Model model) {
        List<RoomType> roomTypes = roomTypeService.findAll();
        model.addAttribute("roomTypes", roomTypes);
        return "admin/room-type-list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("roomType", new RoomType());
        return "admin/room-type-form";
    }

    @PostMapping("/create")
    public String createRoomType(@Valid @ModelAttribute RoomType roomType,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/room-type-form";
        }

        try {
            roomTypeService.save(roomType);
            redirectAttributes.addFlashAttribute("success", "Thêm loại phòng thành công!");
            return "redirect:/admin/room-types";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/admin/room-types/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        RoomType roomType = roomTypeService.findById(id);
        model.addAttribute("roomType", roomType);
        return "admin/room-type-form";
    }

    @PostMapping("/update/{id}")
    public String updateRoomType(@PathVariable Integer id,
                                 @Valid @ModelAttribute RoomType roomType,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/room-type-form";
        }

        try {
            roomType.setRoomTypeId(id);
            roomTypeService.update(roomType);
            redirectAttributes.addFlashAttribute("success", "Cập nhật loại phòng thành công!");
            return "redirect:/admin/room-types";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/admin/room-types/edit/" + id;
        }
    }


    /**
     * Xóa loại phòng (soft delete)
     */
    @PostMapping("/{id}/delete")
    public String deleteRoomType(@PathVariable Integer id,
                                 RedirectAttributes redirectAttributes) {
        try {
            roomTypeService.delete(id);
            redirectAttributes.addFlashAttribute("success",
                    "✅ Đã xóa loại phòng thành công!");
        } catch (RuntimeException e) {
            System.err.println("Error deleting room type: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error",
                    "❌ " + e.getMessage());
        }
        return "redirect:/admin/room-types";
    }
}
