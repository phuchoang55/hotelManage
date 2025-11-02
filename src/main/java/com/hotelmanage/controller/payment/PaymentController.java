package com.hotelmanage.controller.payment;


import com.hotelmanage.service.payment.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/booking/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/{bookingId}")
    public String showPayment(@PathVariable Long bookingId, Model model) {
        paymentService.checkAndCancelExpiredPayment(bookingId);
        model.addAttribute("bookingId", bookingId);
        return "booking/payment";
    }

    @PostMapping("/create")
    public String createPayment(@RequestParam Long bookingId,
                                HttpServletRequest request,
                                Model model) {
        try {
            String paymentUrl = paymentService.createPaymentUrl(bookingId, request);
            return "redirect:" + paymentUrl;
        } catch (Exception e) {
            log.error("Error creating payment: ", e);
            model.addAttribute("error", e.getMessage());
            return "booking/payment-error";
        }
    }

    @GetMapping("/callback")
    public String paymentCallback(@RequestParam Map<String, String> params, Model model) {
        boolean isSuccess = paymentService.handlePaymentCallback(params);

        if (isSuccess) {
            model.addAttribute("message", "Thanh toán thành công!");
            return "booking/payment-success";
        } else {
            model.addAttribute("error", "Thanh toán thất bại!");
            return "booking/payment-failed";
        }
    }
}
