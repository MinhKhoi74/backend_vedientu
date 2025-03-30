package com.example.demo.service;

import com.example.demo.entity.Bus;
import com.example.demo.repository.BusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;
@Service
public class BusService {

    @Autowired
    private BusRepository busRepository;
    // ✅ Lấy tất cả xe buýt
    public List<Bus> getAllBuses() {
        return busRepository.findAll();
    }

    // ✅ Lấy thông tin xe buýt theo ID
    public Bus getBusById(Long id) {
        Optional<Bus> bus = busRepository.findById(id);
        return bus.orElse(null);
    }
    // ✅ Thêm xe buýt mới
    public ResponseEntity<?> addBus(Bus bus) {
        // Kiểm tra nếu biển số đã tồn tại
        if (busRepository.existsByLicensePlate(bus.getLicensePlate())) {
            return ResponseEntity.status(400).body("Biển số xe đã tồn tại!");
        }
        
        busRepository.save(bus);
        return ResponseEntity.ok("Xe buýt đã được thêm thành công!");
    }
    // ✅ Cập nhật thông tin xe buýt
    public boolean updateBus(Long id, Bus updatedBus) {
        Optional<Bus> existingBus = busRepository.findById(id);

        if (existingBus.isPresent()) {
            Bus bus = existingBus.get();
            bus.setLicensePlate(updatedBus.getLicensePlate());
            bus.setModel(updatedBus.getModel());
            bus.setCapacity(updatedBus.getCapacity());
            bus.setRoute(updatedBus.getRoute());

            busRepository.save(bus);
            return true;
        } else {
            return false;
        }
    }
    // ✅ Xóa xe buýt theo ID
    public boolean deleteBus(Long id) {
        Optional<Bus> existingBus = busRepository.findById(id);

        if (existingBus.isPresent()) {
            busRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}

