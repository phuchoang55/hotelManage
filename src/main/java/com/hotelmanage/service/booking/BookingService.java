package com.hotelmanage.service.booking;

import com.hotelmanage.entity.Enum.BookingStatus;
import com.hotelmanage.entity.Enum.UserRole;
import com.hotelmanage.entity.Enum.UserStatus;
import com.hotelmanage.entity.User;
import com.hotelmanage.entity.booking.Booking;
import com.hotelmanage.entity.booking.Promotion;
import com.hotelmanage.entity.room.Room;
import com.hotelmanage.repository.UserRepository;
import com.hotelmanage.repository.booking.BookingRepository;
import com.hotelmanage.repository.booking.PromotionRepository;
import com.hotelmanage.repository.room.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final PromotionService promotionService;
    private final PromotionRepository promotionRepository;

    /**
     * Tạo booking mới
     */
    public Booking createBooking(String username,
                                 Integer roomTypeId,
                                 LocalDate checkInDate,
                                 LocalDate checkOutDate,
                                 BigDecimal totalPrice,
                                 Integer promotionId) {

        log.info("Creating booking for user: {}, room: {}", username, roomTypeId);

        // Tìm user
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        // Tìm phòng available của room type
        List<Room> availableRooms = roomRepository.findAvailableRoomByRoomTypeAndDateRange(
                roomTypeId, checkInDate, checkOutDate);

        if (availableRooms.isEmpty()) {
            throw new RuntimeException("Không còn phòng trống cho loại phòng này!");
        }

        // Lấy phòng đầu tiên
        Room selectedRoom = availableRooms.get(0);

        // Tạo booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setRoom(selectedRoom);
        booking.setCheckInDate(checkInDate);
        booking.setCheckOutDate(checkOutDate);
        booking.setTotalPrice(totalPrice);
        booking.setStatus(BookingStatus.PENDING);

        // Áp dụng promotion nếu có
        if (promotionId != null) {
            Promotion promotion = promotionService.findById(promotionId);
            booking.setPromotion(promotion);

            // Tăng số lần đã sử dụng promotion
            promotionService.incrementUsedCount(promotionId);
        }

        // Lưu booking
        Booking savedBooking = bookingRepository.save(booking);

        log.info("Booking created successfully: {}", savedBooking.getBookingId());
        return savedBooking;
    }

    /**
     * Tạo booking cho khách (guest) không đăng nhập
     */
    public Booking createGuestBooking(Integer roomTypeId,
                                      LocalDate checkInDate,
                                      LocalDate checkOutDate,
                                      BigDecimal totalPrice,
                                      Integer promotionId,
                                      String customerName,
                                      String customerPhone,
                                      String customerEmail,
                                      String customerAddress,
                                      String specialRequests) {

        log.info("Creating guest booking for email: {}", customerEmail);

        // Validate dates
        if (checkOutDate.isBefore(checkInDate) || checkOutDate.isEqual(checkInDate)) {
            throw new RuntimeException("Ngày trả phòng phải sau ngày nhận phòng!");
        }

        // Tạo/tìm guest user dựa trên email
        User guestUser = createGuestUser(customerEmail);

        List<Room> availableRooms = roomRepository.findAvailableRoomByRoomTypeAndDateRange(
                roomTypeId, checkInDate, checkOutDate);

        if (availableRooms.isEmpty()) {
            throw new RuntimeException("Không còn phòng trống!");
        }

        Room selectedRoom = availableRooms.get(0);

        // Tạo booking
        Booking booking = new Booking();
        booking.setUser(guestUser);
        booking.setRoom(selectedRoom);
        booking.setCheckInDate(checkInDate);
        booking.setCheckOutDate(checkOutDate);
        booking.setTotalPrice(totalPrice);
        booking.setStatus(BookingStatus.PENDING);

        // Set promotion nếu có
        if (promotionId != null) {
            Promotion promotion = promotionRepository.findById(promotionId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy promotion!"));
            booking.setPromotion(promotion);
            promotionService.incrementUsedCount(promotionId);
        }

        Booking saved = bookingRepository.save(booking);
        log.info("Created guest booking with ID: {} for email: {}", saved.getBookingId(), customerEmail);

        return saved;
    }

    /**
     * Tạo user guest tạm thời chỉ với email
     * User này có thể được nâng cấp lên CUSTOMER khi đăng ký tài khoản
     */
    private User createGuestUser(String customerEmail) {
        return userRepository.findByEmail(customerEmail)
                .orElseGet(() -> {
                    User guest = User.builder()
                            .username("guest_" + customerEmail.split("@")[0] + "_" + System.currentTimeMillis())
                            .password("") // Password trống
                            .email(customerEmail)
                            .role(UserRole.GUEST)
                            .status(UserStatus.INACTIVE)
                            .build();

                    User saved = userRepository.save(guest);
                    log.info("Created temporary guest user with email: {}", customerEmail);
                    return saved;
                });
    }


    }

