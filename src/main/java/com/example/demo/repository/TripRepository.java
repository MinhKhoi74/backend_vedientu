package com.example.demo.repository;

import com.example.demo.entity.Trip;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long> {

    Optional<Trip> findByDriverAndStatus(User driver, Trip.Status status);

    // ✅ Lấy tất cả chuyến đi theo tài xế, sắp xếp từ mới nhất (giả sử theo startTime)
    List<Trip> findAllByDriverOrderByStartTimeDesc(User driver);

    // ✅ Nếu muốn lấy tất cả chuyến đi, mới nhất trước
    List<Trip> findAllByOrderByStartTimeDesc();
}
