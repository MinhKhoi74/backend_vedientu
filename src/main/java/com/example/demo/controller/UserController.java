package com.example.demo.controller;

import com.example.demo.entity.RideLog;
import com.example.demo.entity.Ticket;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.config.JwtUtil;
import com.example.demo.dto.TicketRequest;
import com.example.demo.service.QRService;
import com.example.demo.service.TicketService;
import com.example.demo.service.RideLogService;
import com.example.demo.repository.RideLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TicketService ticketService; // ✅ Thêm TicketService

    // ✅ API lấy thông tin người dùng
    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String token) {
        try {
            String jwt = token.replace("Bearer ", ""); // Lấy token từ header
            String email = jwtUtil.extractEmail(jwt); // Giải mã token để lấy email
            User user = userService.findByEmail(email);

            if (user == null) {
                return ResponseEntity.status(404).body("User not found");
            }

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("fullName", user.getFullName());
            userInfo.put("email", user.getEmail());
            userInfo.put("phone", user.getPhone());
            userInfo.put("role", user.getRole());

            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token");
        }
    }

    // ✅ API cập nhật thông tin người dùng
    @PutMapping("/update")
    public ResponseEntity<?> updateUserInfo(@RequestHeader("Authorization") String token,
                                            @RequestBody Map<String, String> updates) {
        try {
            String jwt = token.replace("Bearer ", ""); // Lấy token từ header
            String email = jwtUtil.extractEmail(jwt); // Giải mã token để lấy email
            User user = userService.findByEmail(email);

            if (user == null) {
                return ResponseEntity.status(404).body("User not found");
            }

            // Cập nhật thông tin (chỉ cập nhật nếu dữ liệu không null)
            if (updates.containsKey("fullName")) {
                user.setFullName(updates.get("fullName"));
            }
            if (updates.containsKey("phone")) {
                user.setPhone(updates.get("phone"));
            }
            if (updates.containsKey("email")) {
                user.setEmail(updates.get("email")); // Nếu đổi email cần xác minh
            }

            userService.updateUser(user); // Lưu cập nhật vào DB
            return ResponseEntity.ok("Thông tin người dùng đã được cập nhật!");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Cập nhật thất bại: " + e.getMessage());
        }
    }
    // ✅ API lấy danh sách vé của người dùng
    @Autowired // ✅ Thêm Autowired cho QRService
    private QRService qrService;
   @GetMapping("/tickets")
public ResponseEntity<?> getUserTickets(@RequestHeader("Authorization") String token) {
    try {
        String jwt = token.replace("Bearer ", ""); // Lấy token từ header
        String email = jwtUtil.extractEmail(jwt); // Giải mã token để lấy email
        User user = userService.findByEmail(email);

        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        List<Ticket> tickets = ticketService.getTicketsByUser(user);
        List<Map<String, Object>> ticketResponses = new ArrayList<>();

        for (Ticket ticket : tickets) {
            Map<String, Object> ticketData = new HashMap<>();
            ticketData.put("id", ticket.getId());
            ticketData.put("ticketType", ticket.getTicketType());
            ticketData.put("price", ticket.getPrice());
            ticketData.put("remainingRides", ticket.getRemainingRides());
            ticketData.put("purchaseDate", ticket.getPurchaseDate());
            ticketData.put("expiryDate", ticket.getExpiryDate());

            // ✅ Tạo mã QR từ thông tin vé
            String qrContent = "TicketID: " + ticket.getId() + ", UserID: " + user.getId();
            String qrBase64 = qrService.generateQRCode(qrContent);
            ticketData.put("qrCode", qrBase64);

            ticketResponses.add(ticketData);
        }

        return ResponseEntity.ok(ticketResponses);
    } catch (Exception e) {
        return ResponseEntity.status(400).body("Lỗi khi lấy danh sách vé: " + e.getMessage());
    }
}
// ✅ API lấy lịch sử chuyến đi của người dùng
    @Autowired
    private RideLogService rideLogService; 
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
            logData.put("userId", rideLog.getUser().getId());
            logData.put("ticketId", rideLog.getTicket().getId());
            logData.put("rideTime", rideLog.getRideTime());
            logData.put("status", rideLog.getStatus());
            logData.put("driverName", rideLog.getDriver().getFullName()); // Tên tài xế
            logData.put("route", rideLog.getRoute()); // Lộ trình
            return logData;
        }).toList();

        return ResponseEntity.ok(response);
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Lỗi khi lấy lịch sử chuyến đi: " + e.getMessage());
    }
}

}