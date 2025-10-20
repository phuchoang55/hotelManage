package com.hotelmanage.service.room;

import com.hotelmanage.entity.Enum.RoomStatus;
import com.hotelmanage.entity.room.Room;
import com.hotelmanage.repository.room.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RoomService {

    private final RoomRepository roomRepository;

    /**
     * Tìm phòng available cho room type trong khoảng thời gian
     */
    public Room findAvailableRoom(Integer roomTypeId, LocalDate checkInDate, LocalDate checkOutDate) {
        log.info("Finding available room for roomType={}, checkIn={}, checkOut={}",
                roomTypeId, checkInDate, checkOutDate);

        return roomRepository.findAvailableRoomByRoomTypeAndDateRange(
                        roomTypeId, checkInDate, checkOutDate)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Không tìm thấy phòng trống cho loại phòng này trong thời gian đã chọn!"));
    }


}
