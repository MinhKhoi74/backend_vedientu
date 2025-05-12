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

        // ✅ Kiểm tra biển số trùng với xe khác
        String newPlate = updatedBus.getLicensePlate();
        if (!bus.getLicensePlate().equals(newPlate) && busRepository.existsByLicensePlate(newPlate)) {
            return false; // Biển số mới đã tồn tại cho xe khác
        }

        // ✅ Cập nhật thông tin
        bus.setLicensePlate(newPlate);
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

public boolean deleteBus(Long id) {
    Optional<Bus> optionalBus = busRepository.findById(id);
    if (optionalBus.isPresent()) {
        Bus bus = optionalBus.get();

        // Gỡ liên kết tài xế nếu có
        User driver = bus.getDriver();
        if (driver != null) {
            driver.setBus(null);   // Gỡ liên kết từ phía tài xế
            userRepository.save(driver);  // Lưu cập nhật
        }

        busRepository.delete(bus);  // Xóa xe buýt
        return true;
    }
    return false;
}


}