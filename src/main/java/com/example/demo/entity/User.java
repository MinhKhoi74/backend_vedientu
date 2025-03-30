// (Khách hàng, tài xế, admin)
package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 15)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;  // CUSTOMER, DRIVER, ADMIN

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum Role {
        CUSTOMER, DRIVER, ADMIN
    }
    // Quan hệ với RideLog (Người dùng có nhiều lịch sử đi lại)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<RideLog> rideLogs;
}
