package com.example.demo.repository;

import com.example.demo.entity.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {
    boolean existsByLicensePlate(String licensePlate);  // ✅ Kiểm tra biển số có tồn tại không
}
