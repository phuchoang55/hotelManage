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
    @Column(name = "room_type_id")
    private Integer roomTypeId;

    @Column(name = "room_type_name", nullable = false)
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

    @OneToMany(mappedBy = "roomType")
    private List<Room> rooms;

    @OneToMany(mappedBy = "roomType", fetch = FetchType.EAGER)
    private List<RoomTypeImage> images;

    @Transient
    private Integer availableCount;

    public String getPrimaryImageUrl() {
        if (images != null && !images.isEmpty()) {
            return images.stream()
                    .filter(img -> img != null && Boolean.TRUE.equals(img.getIsPrimary()))
                    .findFirst()
                    .map(RoomTypeImage::getImageUrl)
                    .filter(url -> url != null && !url.trim().isEmpty())
                    .orElseGet(() -> images.stream()
                            .map(RoomTypeImage::getImageUrl)
                            .filter(url -> url != null && !url.trim().isEmpty())
                            .findFirst()
                            .orElse("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='300' height='250'%3E%3Crect fill='%23e0e0e0' width='300' height='250'/%3E%3Ctext fill='%23999' font-size='16' x='50%25' y='50%25' text-anchor='middle' dy='.3em'%3ENo Image%3C/text%3E%3C/svg%3E"));
        }
        return "data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='300' height='250'%3E%3Crect fill='%23e0e0e0' width='300' height='250'/%3E%3Ctext fill='%23999' font-size='16' x='50%25' y='50%25' text-anchor='middle' dy='.3em'%3ENo Image%3C/text%3E%3C/svg%3E";
    }

}

