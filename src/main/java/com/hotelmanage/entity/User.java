package com.hotelmanage.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "User")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone", nullable = false, unique = true, length = 20)
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "status", nullable = false)
    private Boolean status = false;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
