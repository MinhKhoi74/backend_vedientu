package com.example.demo.controller;

import com.example.demo.entity.Ticket;
import com.example.demo.entity.User;
import com.example.demo.entity.Bus;
import com.example.demo.entity.RideLog;
import com.example.demo.service.TicketService;
import com.example.demo.service.UserService;
import com.example.demo.service.RideLogService;
import com.example.demo.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;


@RestController
@RequestMapping("/driver")
public class DriverController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private RideLogService rideLogService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    // ✅ API quét mã QR để kiểm tra vé hợp lệ
    @PostMapping("/scan-qr")
    public ResponseEntity<Map<String, Object>> scanQRCode(@RequestHeader("Authorization") String token,
                                                          @RequestBody Map<String, String> qrData) {
        Map<String, Object> response = new HashMap<>();
        try {
            // ✅ Log kiểm tra token & dữ liệu nhận được
            System.out.println("📌 Token nhận được: " + token);
            System.out.println("📌 Dữ liệu QR nhận từ Frontend: " + qrData);
    
            // ✅ Xác thực tài xế
            String jwt = token.replace("Bearer ", "");
            String driverEmail = jwtUtil.extractEmail(jwt);
            User driver = userService.findByEmail(driverEmail);
    
            if (driver == null || !driver.getRole().equals(User.Role.DRIVER)) {
                response.put("success", false);
                response.put("message", "❌ Tài xế không hợp lệ hoặc chưa đăng nhập");
                return ResponseEntity.status(401).body(response);
            }
    
            // ✅ Kiểm tra dữ liệu QR
            String qrContent = qrData.get("qrContent");
            if (qrContent == null || !qrContent.startsWith("TicketID")) {
                response.put("success", false);
                response.put("message", "❌ Mã QR không hợp lệ");
                return ResponseEntity.badRequest().body(response);
            }
    
            // ✅ Định dạng ID vé
            String[] parts = qrContent.split(":");
            if (parts.length < 2) {
                response.put("success", false);
                response.put("message", "❌ Mã QR không đúng định dạng");
                return ResponseEntity.badRequest().body(response);
            }
    
            Long ticketId = Long.parseLong(parts[1].trim());
            Optional<Ticket> ticketOpt = ticketService.getTicketById(ticketId);
    
            if (ticketOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "❌ Vé không tồn tại");
                return ResponseEntity.status(400).body(response);
            }
    
            Ticket ticket = ticketOpt.get();
            if (ticket.getRemainingRides() <= 0) {
                response.put("success", false);
                response.put("message", "❌ Vé đã hết lượt sử dụng");
                return ResponseEntity.badRequest().body(response);
            }
    
            // ✅ Trừ số lượt sử dụng
            ticket.setRemainingRides(ticket.getRemainingRides() - 1);
            ticketService.saveTicket(ticket);
    
            // ✅ Lưu lịch sử chuyến đi
            Bus bus = driver.getBus();
            rideLogService.saveRideLog(ticket.getUser(), ticket, driver, bus);
    
            response.put("success", true);
            response.put("message", "✅ Vé hợp lệ! Hành khách có thể lên xe.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "❌ Lỗi hệ thống: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    @GetMapping("/ride-history")
public ResponseEntity<?> getUserRideHistory(@RequestHeader("Authorization") String token) {
    try {
        String jwt = token.replace("Bearer ", "");
        String email = jwtUtil.extractEmail(jwt);
        User user = userService.findByEmail(email);

        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        List<RideLog> rideLogs = rideLogService.getRideLogsByUser(user);

        List<Map<String, Object>> response = rideLogs.stream().map(rideLog -> {
            Map<String, Object> logData = new HashMap<>();
            logData.put("id", rideLog.getId());

            // Thêm thông tin tài xế
            if (rideLog.getDriver() != null) {
                logData.put("driverId", rideLog.getDriver().getId()); // ID tài xế
                logData.put("driverName", rideLog.getDriver().getFullName()); // Tên tài xế
            } else {
                logData.put("driverId", null); // Nếu không có tài xế
                logData.put("driverName", "Unknown");
            }

            // Thêm các thông tin khác
            logData.put("ticketId", rideLog.getTicketId());
            logData.put("route", rideLog.getRoute());
            logData.put("rideTime", rideLog.getRideTime());
            logData.put("status", rideLog.getStatus());

            return logData;
        }).toList();

        return ResponseEntity.ok(response);
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Lỗi khi lấy lịch sử chuyến đi: " + e.getMessage());
    }
}
    
}