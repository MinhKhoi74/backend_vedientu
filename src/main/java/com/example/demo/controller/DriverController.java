package com.example.demo.controller;

import com.example.demo.entity.Ticket;
import com.example.demo.entity.User;
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
import java.util.ArrayList;

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
public ResponseEntity<?> scanQRCode(@RequestHeader("Authorization") String token,
                                    @RequestBody Map<String, String> qrData) {
    try {
        // Xác thực tài xế
        String jwt = token.replace("Bearer ", "");
        String driverEmail = jwtUtil.extractEmail(jwt);
        User driver = userService.findByEmail(driverEmail);

        if (driver == null || !driver.getRole().equals(User.Role.DRIVER)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        // Lấy dữ liệu từ mã QR
        String qrContent = qrData.get("qrContent");
        if (qrContent == null || !qrContent.startsWith("TicketID:")) {
            return ResponseEntity.badRequest().body("Mã QR không hợp lệ");
        }

        Long ticketId = Long.parseLong(qrContent.split(": ")[1]);
        Optional<Ticket> ticketOpt = ticketService.getTicketById(ticketId);

        if (ticketOpt.isEmpty()) {
            return ResponseEntity.status(400).body("❌ Vé không hợp lệ (không tồn tại)");
        }

        Ticket ticket = ticketOpt.get();

        if (ticket.getRemainingRides() <= 0) {
            return ResponseEntity.badRequest().body("❌ Vé đã hết lượt sử dụng");
        }

        ticket.setRemainingRides(ticket.getRemainingRides() - 1);
        ticketService.saveTicket(ticket);

        // Thêm thông tin tuyến đường
        String route = "Hà Nội - Hải Phòng"; // Giả định tuyến đường, có thể lấy từ `ticket`

        // Lưu lịch sử chuyến đi
        rideLogService.saveRideLog(ticket.getUser(), ticket, driver, route);

        return ResponseEntity.ok("✅ Vé hợp lệ! Hành khách có thể lên xe.");
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Lỗi: " + e.getMessage());
    }
}
    @GetMapping("/passengers")
    public ResponseEntity<?> getPassengers(@RequestHeader("Authorization") String token) {
        try {
            // 🔹 Xác thực tài xế
            String jwt = token.replace("Bearer ", "");
            String driverEmail = jwtUtil.extractEmail(jwt);
            User driver = userService.findByEmail(driverEmail);

            if (driver == null || !driver.getRole().equals(User.Role.DRIVER)) {
                return ResponseEntity.status(401).body("Unauthorized");
            }

            // 🔹 Lấy danh sách hành khách đã quét vé trên chuyến đi của tài xế
            List<RideLog> rideLogs = rideLogService.getPassengersByDriver(driver);

            if (rideLogs.isEmpty()) {
                return ResponseEntity.ok("🚍 Không có hành khách nào trên xe.");
            }

            // 🔹 Trả về danh sách hành khách
            List<Map<String, Object>> response = rideLogs.stream().map(ride -> {
                Map<String, Object> data = new HashMap<>();
                data.put("passengerId", ride.getUser().getId());
                data.put("passengerName", ride.getUser().getFullName());
                data.put("ticketId", ride.getTicket().getId());
                data.put("rideTime", ride.getRideTime());
                data.put("status", ride.getStatus());
                data.put("route", ride.getRoute()); // Lộ trình
                return data;
            }).toList();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi: " + e.getMessage());
        }
    }

}
