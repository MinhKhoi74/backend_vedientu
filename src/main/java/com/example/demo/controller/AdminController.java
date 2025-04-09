package com.example.demo.controller;

import com.example.demo.dto.BusRequest;
import com.example.demo.entity.Bus;
import com.example.demo.entity.User;
import com.example.demo.service.BusService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')") // ✅ Chỉ ADMIN mới có quyền truy cập tất cả API trong class này
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private BusService busService;

    // ✅ API lấy danh sách người dùng
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // ✅ API xóa người dùng
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        boolean isDeleted = userService.deleteUser(id);
        return isDeleted ? ResponseEntity.ok("Đã xóa thành công!") 
                         : ResponseEntity.status(404).body("Không tìm thấy người dùng!");
    }

    // ✅ API lấy danh sách tất cả xe buýt
    @GetMapping("/buses")
    public ResponseEntity<List<Bus>> getAllBuses() {
        return ResponseEntity.ok(busService.getAllBuses());
    }

    // ✅ API lấy thông tin chi tiết một xe buýt
    @GetMapping("/buses/{id}")
    public ResponseEntity<?> getBusById(@PathVariable Long id) {
        Bus bus = busService.getBusById(id);
        return (bus != null) ? ResponseEntity.ok(bus) 
                             : ResponseEntity.status(404).body("Không tìm thấy xe buýt!");
    }

    // ✅ API thêm xe buýt
    @PostMapping("/buses")
    public ResponseEntity<?> addBus(@RequestBody BusRequest request) {
    // Tìm tài xế
    User driver = userService.getUserById(request.getDriverId());
    if (driver == null) {
        return ResponseEntity.badRequest().body("Không tìm thấy tài xế");
    }

    // Tạo Bus entity từ request
    Bus bus = new Bus();
    bus.setLicensePlate(request.getLicensePlate());
    bus.setModel(request.getModel());
    bus.setCapacity(request.getCapacity());
    bus.setRoute(request.getRoute());
    bus.setDriver(driver); // ✅ gán driver

    return busService.addBus(bus);
}


    // ✅ API cập nhật thông tin xe buýt
    @PutMapping("/buses/{id}")
    public ResponseEntity<?> updateBus(@PathVariable Long id, @RequestBody BusRequest busRequest) {
    boolean isUpdated = busService.updateBus(id, busRequest);

    return isUpdated
        ? ResponseEntity.ok("Cập nhật xe buýt thành công!")
        : ResponseEntity.status(404).body("Không tìm thấy xe buýt hoặc tài xế!");
}


    // ✅ API xóa xe buýt
    @DeleteMapping("/buses/{id}")
    public ResponseEntity<?> deleteBus(@PathVariable Long id) {
        boolean isDeleted = busService.deleteBus(id);
        return isDeleted ? ResponseEntity.ok("Xe buýt đã được xóa thành công!") 
                         : ResponseEntity.status(404).body("Không tìm thấy xe buýt!");
    }
}
