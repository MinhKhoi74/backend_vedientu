package com.example.demo.service;

import com.example.demo.dto.RideLogResponse;
import com.example.demo.entity.Bus;
import com.example.demo.entity.RideLog;
import com.example.demo.entity.Ticket;
import com.example.demo.entity.User;
import com.example.demo.repository.RideLogRepository;
import com.example.demo.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RideLogService {

    @Autowired
    private RideLogRepository rideLogRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // Lưu RideLog và trả về RideLogResponse
    public RideLogResponse saveRideLog(User user, Ticket ticket, User driver, Bus bus) {
        RideLog rideLog = new RideLog();
        
        // Gán các thuộc tính cho đối tượng RideLog
        rideLog.setUser(user);
        rideLog.setTicketId(ticket != null ? ticket.getId() : null);
        rideLog.setDriver(driver);
        rideLog.setBus(bus);
        rideLog.setRoute(bus.getRoute());
        rideLog.setRideTime(LocalDateTime.now());
        rideLog.setStatus(RideLog.Status.VALID);
        
        // Lấy tên người dùng và biển số xe (busCode)
        String userName = user != null ? user.getFullName() : "Không rõ";  // userName từ đối tượng User
        String busCode = bus != null ? bus.getLicensePlate() : "Không rõ"; // busCode từ đối tượng Bus
        
        // Bạn có thể đặt các giá trị tạm thời (transient) vào RideLog để sử dụng sau này
        rideLog.setUserName(userName);  // Gán tên người dùng vào đối tượng RideLog
        rideLog.setBusCode(busCode);    // Gán biển số xe vào đối tượng RideLog
        
        // Lưu RideLog vào cơ sở dữ liệu
        RideLog savedRideLog = rideLogRepository.save(rideLog);
    
        // Trả về đối tượng RideLogResponse đã ánh xạ từ đối tượng RideLog đã lưu
        return toRideLogResponse(savedRideLog); // Trả về đối tượng RideLogResponse
    }
    
    // Lấy danh sách RideLogs theo người dùng
    public List<RideLog> getRideLogsByUser(User user) {
        return rideLogRepository.findByUser(user);
    }

    // Lấy danh sách hành khách theo tài xế
    public List<RideLog> getPassengersByDriver(User driver) {
        return rideLogRepository.findByDriver(driver);
    }

    // Lấy chi tiết RideLog cho người dùng dựa trên ID và token
    public RideLogResponse getRideLogDetailForUser(Long rideLogId, String token) {
        // 1. Trích xuất email từ token
        String jwt = token.replace("Bearer ", "");
        String email = jwtUtil.extractEmail(jwt);
        User user = userService.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("Người dùng không tồn tại");
        }

        // 2. Tìm RideLog theo ID
        Optional<RideLog> rideLogOpt = rideLogRepository.findById(rideLogId);
        if (rideLogOpt.isEmpty()) return null;

        RideLog rideLog = rideLogOpt.get();

        // 3. Kiểm tra quyền sở hữu
        if (!rideLog.getUser().getId().equals(user.getId())) {
            return null;
        }

        // 4. Trả về kết quả đã ánh xạ sang DTO
        return toRideLogResponse(rideLog);
    }

    // Hàm ánh xạ entity -> DTO
    private RideLogResponse toRideLogResponse(RideLog log) {
        RideLogResponse res = new RideLogResponse();
        res.setId(log.getId());
        res.setUserName(log.getUser() != null ? log.getUser().getFullName() : "Không rõ");
        res.setTicketId(log.getTicketId());
        res.setDriverName(log.getDriver() != null ? log.getDriver().getFullName() : "Không rõ");
        res.setBusCode(log.getBus() != null ? log.getBus().getLicensePlate() : "Không rõ");
        res.setRoute(log.getRoute());
        res.setRideTime(log.getRideTime().toString());
        res.setStatus(log.getStatus().toString());
        return res;
    }
}
