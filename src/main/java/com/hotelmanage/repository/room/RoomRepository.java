package com.hotelmanage.repository.room;

import com.hotelmanage.entity.Enum.RoomStatus;
import com.hotelmanage.entity.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

    /**
     * Tìm phòng available cho room type trong khoảng thời gian
     */
    @Query("""
        SELECT r
        FROM Room r
        WHERE r.roomType.roomTypeId = :roomTypeId
        AND r.deletedAt IS NULL
        AND r.status = 'AVAILABLE'
        AND r.roomId NOT IN (
            SELECT b.room.roomId
            FROM Booking b
            WHERE b.status IN ('PENDING', 'CONFIRMED')
            AND (
                (b.checkInDate <= :checkOutDate AND b.checkOutDate >= :checkInDate)
            )
        )
        ORDER BY r.roomNumber ASC
    """)
    List<Room> findAvailableRoomByRoomTypeAndDateRange(
            @Param("roomTypeId") Integer roomTypeId,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate);

    /**
     * Tìm theo room type
     */
    List<Room> findByRoomType_RoomTypeIdAndDeletedAtIsNull(Integer roomTypeId);

    /**
     * Tìm theo status
     */
    List<Room> findByStatusAndDeletedAtIsNull(RoomStatus status);

    /**
     * Kiểm tra room number tồn tại
     */
    boolean existsByRoomNumberAndDeletedAtIsNull(String roomNumber);

    /**
     * Đếm số phòng theo room type
     */
    long countByRoomType_RoomTypeIdAndDeletedAtIsNull(Integer roomTypeId);

    /**
     * Đếm số phòng theo status
     */
    long countByStatusAndDeletedAtIsNull(RoomStatus status);
}
