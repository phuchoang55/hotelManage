package com.hotelmanage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Role")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "role_name", nullable = false, unique = true)
    private String roleName;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<User> users;
}

