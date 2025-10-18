package com.hotelmanage.entity.room;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "room_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roomType_id")
    private Integer roomTypeId;

    @Column(name = "roomType_name", nullable = false)
    private String roomTypeName;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "device", columnDefinition = "TEXT")
    private String device;

    @Column(name = "utilities", columnDefinition = "TEXT")
    private String utilities;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "amount_person", nullable = false)
    private Integer amountPerson;

    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL)
    private List<Room> rooms;

    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RoomTypeImage> images;

    @Transient
    private Integer availableCount;

    public String getPrimaryImageUrl() {
        if (images != null && !images.isEmpty()) {
            return images.stream()
                    .filter(RoomTypeImage::getIsPrimary)
                    .findFirst()
                    .map(RoomTypeImage::getImageUrl)
                    .orElse(images.get(0).getImageUrl());
        }
        return "/images/default-room.jpg";
    }
}
