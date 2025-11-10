package com.hotelmanage.service.dashboard;

import com.hotelmanage.repository.UserRepository;
import com.hotelmanage.repository.booking.BookingRepository;
import com.hotelmanage.repository.room.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    /**
     * Lấy tất cả thống kê cho dashboard
     */
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        try {

            Long totalRooms = roomRepository.countTotalRooms();
            stats.put("totalRooms", totalRooms != null ? totalRooms : 0L);


            Long availableRooms = roomRepository.countAvailableRooms();
            stats.put("availableRooms", availableRooms != null ? availableRooms : 0L);


            Long activeUsers = userRepository.countActiveUsers();
            stats.put("activeUsers", activeUsers != null ? activeUsers : 0L);


            Long totalBookings = bookingRepository.countTotalBookings();
            stats.put("totalBookings", totalBookings != null ? totalBookings : 0L);


            BigDecimal totalRevenue = bookingRepository.calculateTotalRevenue();
            stats.put("totalRevenue", totalRevenue != null ? totalRevenue : BigDecimal.ZERO);


            YearMonth currentMonth = YearMonth.now();
            BigDecimal monthlyRevenue = bookingRepository.calculateMonthlyRevenue(
                    currentMonth.getYear(),
                    currentMonth.getMonthValue()
            );
            stats.put("monthlyRevenue", monthlyRevenue != null ? monthlyRevenue : BigDecimal.ZERO);

            log.info("Dashboard stats retrieved successfully");

        } catch (Exception e) {
            log.error("Error retrieving dashboard stats", e);
            stats.put("totalRooms", 0L);
            stats.put("availableRooms", 0L);
            stats.put("activeUsers", 0L);
            stats.put("totalBookings", 0L);
            stats.put("totalRevenue", BigDecimal.ZERO);
            stats.put("monthlyRevenue", BigDecimal.ZERO);
        }

        return stats;
    }

    /**
     * Lấy doanh thu theo khoảng thời gian
     */
    public BigDecimal getRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            BigDecimal revenue = bookingRepository.calculateRevenueByDateRange(startDate, endDate);
            return revenue != null ? revenue : BigDecimal.ZERO;
        } catch (Exception e) {
            log.error("Error calculating revenue by date range", e);
            return BigDecimal.ZERO;
        }
    }

    /**
     * Lấy thống kê chi tiết
     */
    public Map<String, Object> getDetailedStats() {
        Map<String, Object> stats = getDashboardStats();


        Long activeCustomers = userRepository.countActiveCustomers();
        stats.put("activeCustomers", activeCustomers != null ? activeCustomers : 0L);

        return stats;
    }
}