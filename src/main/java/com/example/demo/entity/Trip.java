package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "trip")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("tripId") // ✅ đổi tên key trong JSON
    private Long id;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    @JsonIgnore // ❌ không cần trả driver trong JSON
    private User driver;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL)
    @JsonProperty("passengers") // ✅ hiển thị danh sách hành khách là "passengers"
    private List<RideLog> rideLogs;
    public enum Status {
        OPEN, CLOSED
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus_id", nullable = false)
    private Bus bus;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "route") 
    private String route;
}
