package com.example.demo.controller;

import com.example.demo.entity.Ticket;
import com.example.demo.entity.Trip;
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
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import com.example.demo.service.TripService;
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

    @Autowired
    private TripService tripService;
    
    // ✅ API quét mã QR để kiểm tra vé hợp lệ
    @PostMapping("/scan-qr")
    public ResponseEntity<Map<String, Object>> scanQRCode(@RequestHeader("Authorization") String token,
                                                          @RequestBody Map<String, String> qrData) {
        Map<String, Object> response = new HashMap<>();
        try {
            String jwt = token.replace("Bearer ", "");
            String driverEmail = jwtUtil.extractEmail(jwt);
            User driver = userService.findByEmail(driverEmail);
    
            if (driver == null || !driver.getRole().equals(User.Role.DRIVER)) {
                response.put("success", false);
                response.put("message", "❌ Tài xế không hợp lệ hoặc chưa đăng nhập");
                return ResponseEntity.status(401).body(response);
            }
    
            String qrContent = qrData.get("qrContent");
            if (qrContent == null || !qrContent.startsWith("TicketID")) {
                response.put("success", false);
                response.put("message", "❌ Mã QR không hợp lệ");
                return ResponseEntity.badRequest().body(response);
            }
    
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
    
            // ✅ Thêm mới: kiểm tra chuyến đang mở của tài xế
            Optional<Trip> openTrip = rideLogService.getOpenTripByDriver(driver);
            if (openTrip.isEmpty()) {
                response.put("success", false);
                response.put("message", "❌ Không có chuyến đang mở. Vui lòng mở chuyến trước.");
                return ResponseEntity.badRequest().body(response);
            }
    
            // ✅ Trừ lượt sử dụng vé
            ticket.setRemainingRides(ticket.getRemainingRides() - 1);
            ticketService.saveTicket(ticket);
    
            Bus bus = driver.getBus();
    
            // ✅ Lưu nhật ký lên xe có chuyến
            rideLogService.saveRideLog(ticket.getUser(), ticket, driver, bus, openTrip.get());
    
            response.put("success", true);
            response.put("message", "✅ Vé hợp lệ! Hành khách có thể lên xe.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "❌ Lỗi hệ thống: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    

    // ✅ API lấy danh sách hành khách của tài xế
    @GetMapping("/passengers")
    public ResponseEntity<Map<String, Object>> getPassengersByTrip(
            @RequestHeader("Authorization") String token,
            @RequestParam("tripId") Long tripId) {
    
        Map<String, Object> response = new HashMap<>();
        try {
            String jwt = token.replace("Bearer ", "");
            String driverEmail = jwtUtil.extractEmail(jwt);
            User driver = userService.findByEmail(driverEmail);
    
            if (driver == null || !driver.getRole().equals(User.Role.DRIVER)) {
                response.put("success", false);
                response.put("message", "❌ Tài xế không hợp lệ hoặc chưa đăng nhập");
                return ResponseEntity.status(401).body(response);
            }
    
            // Lấy chuyến đi theo ID
            Trip trip = tripService.findById(tripId); // bạn cần có tripService.findById()
    
            if (trip == null || !trip.getDriver().getId().equals(driver.getId())) {
                response.put("success", false);
                response.put("message", "❌ Không tìm thấy chuyến hoặc không thuộc quyền truy cập.");
                return ResponseEntity.status(403).body(response);
            }
    
            List<RideLog> rideLogs = rideLogService.getRideLogsByTrip(trip);
            if (rideLogs.isEmpty()) {
                response.put("success", false);
                response.put("message", "🚍 Không có hành khách nào trong chuyến.");
                return ResponseEntity.ok(response);
            }
    
            List<Map<String, Object>> passengerList = rideLogs.stream().map(ride -> {
                Map<String, Object> passengerData = new HashMap<>();
                passengerData.put("passengerId", ride.getUser().getId());
                passengerData.put("passengerName", ride.getUser().getFullName());
                passengerData.put("ticketId", ride.getTicketId());
                passengerData.put("rideTime", ride.getRideTime());
                passengerData.put("status", ride.getStatus());
                passengerData.put("route", ride.getRoute());
                return passengerData;
            }).toList();
    
            response.put("success", true);
            response.put("passengers", passengerList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "❌ Lỗi hệ thống: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    

    // ✅ API trả về danh sách các chuyến đi của tài xế và hành khách của từng chuyến
   @GetMapping("/ride-history")
public ResponseEntity<?> getDriverRideHistory(@RequestHeader("Authorization") String token) {
    try {
        String jwt = token.replace("Bearer ", "");
        String email = jwtUtil.extractEmail(jwt);
        User driver = userService.findByEmail(email);

        if (driver == null || !driver.getRole().equals(User.Role.DRIVER)) {
            return ResponseEntity.status(401).body("Tài xế không hợp lệ");
        }

        // Lấy các chuyến đi của tài xế
        List<Trip> trips = rideLogService.getTripsByDriver(driver);

        AtomicInteger totalPassengers = new AtomicInteger(0); // ✅ Sử dụng AtomicInteger

        // Trả về dữ liệu chi tiết chuyến đi
        List<Map<String, Object>> response = trips.stream().map(trip -> {
            Map<String, Object> tripData = new HashMap<>();
            tripData.put("tripId", trip.getId());
            tripData.put("startTime", trip.getStartTime());
            tripData.put("endTime", trip.getEndTime());
            tripData.put("route", trip.getDriver().getBus().getRoute());

            // Lấy hành khách trong chuyến này
            List<RideLog> rideLogs = rideLogService.getRideLogsByTrip(trip);

            // ✅ Cộng dồn số hành khách
            totalPassengers.addAndGet(rideLogs.size());

            List<Map<String, Object>> passengers = rideLogs.stream().map(ride -> {
                Map<String, Object> p = new HashMap<>();
                p.put("passengerId", ride.getUser().getId());
                p.put("passengerName", ride.getUser().getFullName());
                p.put("ticketId", ride.getTicketId());
                p.put("rideTime", ride.getRideTime());
                p.put("status", ride.getStatus());
                return p;
            }).toList();

            tripData.put("passengers", passengers);
            return tripData;
        }).toList();

        // Gói kết quả trả về
        Map<String, Object> finalResponse = new HashMap<>();
        finalResponse.put("totalTrips", trips.size());
        finalResponse.put("totalPassengers", totalPassengers.get()); // ✅ Lấy giá trị từ AtomicInteger
        finalResponse.put("tripDetails", response);

        return ResponseEntity.ok(finalResponse);
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Lỗi khi lấy lịch sử chuyến đi: " + e.getMessage());
    }
}

    // mở chuyến
@PostMapping("/open-trip")
public ResponseEntity<?> openTrip(@RequestHeader("Authorization") String token) {
    try {
        String jwt = token.replace("Bearer ", "");
        String driverEmail = jwtUtil.extractEmail(jwt);
        User driver = userService.findByEmail(driverEmail);

        if (driver == null || !driver.getRole().equals(User.Role.DRIVER)) {
            return ResponseEntity.status(401).body("❌ Tài xế không hợp lệ hoặc chưa đăng nhập");
        }

        // Kiểm tra tài xế đã có chuyến mở chưa
        Optional<Trip> existingTrip = rideLogService.getOpenTripByDriver(driver);
        if (existingTrip.isPresent()) {
            return ResponseEntity.badRequest().body("❌ Đã có chuyến mở. Vui lòng đóng chuyến trước đó.");
        }

        // ✅ Gán thông tin bus từ driver
        Bus bus = driver.getBus();
        if (bus == null) {
            return ResponseEntity.badRequest().body("❌ Tài xế chưa được gán với xe buýt nào.");
        }

        Trip newTrip = new Trip();
        newTrip.setDriver(driver);
        newTrip.setBus(bus); // ✅ Gán bus cho chuyến đi
        newTrip.setRoute(bus.getRoute()); // (nếu bạn muốn lưu route từ bus vào trip)
        newTrip.setStartTime(LocalDateTime.now());
        newTrip.setStatus(Trip.Status.OPEN);

        rideLogService.saveTrip(newTrip);

        return ResponseEntity.ok(Map.of(
        "success", true,
        "message", "✅ Mở chuyến thành công!"
        ));

    } catch (Exception e) {
        return ResponseEntity.status(500).body(Map.of(
        "success", false,
        "message", "❌ Lỗi khi mở chuyến: " + e.getMessage()
));

    }
}



@PostMapping("/close-trip")
public ResponseEntity<?> closeTrip(@RequestHeader("Authorization") String token) {
    try {
        String jwt = token.replace("Bearer ", "");
        String driverEmail = jwtUtil.extractEmail(jwt);
        User driver = userService.findByEmail(driverEmail);

        if (driver == null || !driver.getRole().equals(User.Role.DRIVER)) {
            return ResponseEntity.status(401).body("❌ Tài xế không hợp lệ hoặc chưa đăng nhập");
        }

        Optional<Trip> tripOpt = rideLogService.getOpenTripByDriver(driver);
        if (tripOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("❌ Không có chuyến đang mở để đóng.");
        }

        Trip trip = tripOpt.get();

        // ✅ Chuyển từ Date -> LocalDateTime
        LocalDateTime now = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        trip.setEndTime(now);

        trip.setStatus(Trip.Status.CLOSED);
        rideLogService.saveTrip(trip);

        return ResponseEntity.ok(Map.of(
        "success", true,
        "message", "✅ Đã đóng chuyến thành công!"
));

    } catch (Exception e) {
        return ResponseEntity.status(500).body(Map.of(
        "success", false,
        "message", "❌ Lỗi khi đóng chuyến: " + e.getMessage()
));

    }
}
@GetMapping("/trip-status")
public ResponseEntity<?> getTripStatus(@RequestHeader("Authorization") String token) {
    try {
        String jwt = token.replace("Bearer ", "");
        String driverEmail = jwtUtil.extractEmail(jwt);
        User driver = userService.findByEmail(driverEmail);

        if (driver == null || !driver.getRole().equals(User.Role.DRIVER)) {
            return ResponseEntity.status(401).body(Map.of(
                "success", false,
                "message", "Tài xế không hợp lệ hoặc chưa đăng nhập"
            ));
        }

        Optional<Trip> openTrip = rideLogService.getOpenTripByDriver(driver);
        boolean isTripOpen = openTrip.isPresent();

        return ResponseEntity.ok(Map.of(
            "success", true,
            "tripOpen", isTripOpen,
            "message", isTripOpen ? "Chuyến đang mở" : "Không có chuyến đang mở"
        ));
    } catch (Exception e) {
        return ResponseEntity.status(500).body(Map.of(
            "success", false,
            "message", "Lỗi hệ thống: " + e.getMessage()
        ));
    }
}

}
