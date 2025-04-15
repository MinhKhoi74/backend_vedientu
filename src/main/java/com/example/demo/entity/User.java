package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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
    
    @OneToOne(mappedBy = "driver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Bus bus;
    
    // Danh sách lịch sử chuyến đi của hành khách
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "user-rideLogs")
    private List<RideLog> rideLogs;

    // Danh sách lịch sử chuyến đi do tài xế thực hiện
    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "driver-rideLogs")
    private List<RideLog> drivenLogs;
}
