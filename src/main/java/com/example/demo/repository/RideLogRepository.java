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

    // ✅ Lấy lịch sử chuyến đi của người dùng, sắp xếp theo thời gian rideTime từ mới nhất
    List<RideLog> findByUserOrderByRideTimeDesc(User user); // Sắp xếp theo rideTime giảm dần (mới nhất trước)

    // ✅ Lấy lịch sử chuyến đi của tài xế, sắp xếp theo thời gian rideTime từ mới nhất
    List<RideLog> findByDriverOrderByRideTimeDesc(User driver); // Sắp xếp theo rideTime giảm dần (mới nhất trước)

    // ✅ Lấy lịch sử chuyến đi theo chuyến đi, sắp xếp theo thời gian rideTime từ mới nhất
    List<RideLog> findByTripOrderByRideTimeDesc(Trip trip); // Sắp xếp theo rideTime giảm dần (mới nhất trước)

    // ✅ Lấy lịch sử chuyến đi theo tripId, sắp xếp theo thời gian rideTime từ mới nhất
    List<RideLog> findByTripIdOrderByRideTimeDesc(Long tripId); // Sắp xếp theo rideTime giảm dần (mới nhất trước)

    @Query("SELECT COUNT(r.id) FROM RideLog r WHERE r.status = 'VALID'")
    Long countValidRides();  // ✅ Đếm số lượt hợp lệ

    @Query("SELECT COUNT(r.id) FROM RideLog r WHERE r.status = 'INVALID'")
    Long countInvalidRides();  // ✅ Đếm số lượt không hợp lệ
}
