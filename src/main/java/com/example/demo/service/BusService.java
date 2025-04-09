package com.example.demo.service;
import com.example.demo.dto.BusRequest;
import com.example.demo.entity.Bus;
import com.example.demo.repository.BusRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;
@Service
public class BusService {

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private UserRepository userRepository;

    // ✅ Lấy tất cả xe buýt
    public List<Bus> getAllBuses() {
        return busRepository.findAll();
    }

    // ✅ Lấy thông tin xe buýt theo ID
    public Bus getBusById(Long id) {
        return busRepository.findById(id).orElse(null);
    }

    // ✅ Thêm xe buýt mới
    public ResponseEntity<?> addBus(Bus bus) {
        if (busRepository.existsByLicensePlate(bus.getLicensePlate())) {
            return ResponseEntity.status(400).body("Biển số xe đã tồn tại!");
        }

        busRepository.save(bus);
        return ResponseEntity.ok("Xe buýt đã được thêm thành công!");
    }

    // ✅ Cập nhật thông tin xe buýt (sửa lại dùng BusRequest)
    public boolean updateBus(Long id, BusRequest updatedBus) {
        Optional<Bus> existingBus = busRepository.findById(id);

        if (existingBus.isPresent()) {
            Bus bus = existingBus.get();

            bus.setLicensePlate(updatedBus.getLicensePlate());
            bus.setModel(updatedBus.getModel());
            bus.setCapacity(updatedBus.getCapacity());
            bus.setRoute(updatedBus.getRoute());

            // ✅ Gán lại tài xế nếu driverId tồn tại
            if (updatedBus.getDriverId() != null) {
                Optional<User> driver = userRepository.findById(updatedBus.getDriverId());
                if (driver.isPresent()) {
                    bus.setDriver(driver.get());
                } else {
                    return false; // Tài xế không tồn tại
                }
            }

            busRepository.save(bus);
            return true;
        }

        return false;
    }

    // ✅ Xóa xe buýt theo ID
    public boolean deleteBus(Long id) {
        if (busRepository.existsById(id)) {
            busRepository.deleteById(id);
            return true;
        }
        return false;
    }
}