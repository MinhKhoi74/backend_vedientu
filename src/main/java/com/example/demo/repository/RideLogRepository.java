package com.example.demo.repository;
import com.example.demo.entity.User;
import com.example.demo.entity.RideLog;
import com.example.demo.entity.Trip;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RideLogRepository extends JpaRepository<RideLog, Long> {
    // ✅ Lấy lịch sử chuyến đi của người dùng
    List<RideLog> findByUser(User user);
    List<RideLog> findByDriver(User driver);
    List<RideLog> findByTrip(Trip trip);
    List<RideLog> findByTripId(Long tripId);

    @Query("SELECT COUNT(r.id) FROM RideLog r WHERE r.status = 'VALID'")
    Long countValidRides();  // ✅ Đếm số lượt hợp lệ

    @Query("SELECT COUNT(r.id) FROM RideLog r WHERE r.status = 'INVALID'")
    Long countInvalidRides();  // ✅ Đếm số lượt không hợp lệ

}

