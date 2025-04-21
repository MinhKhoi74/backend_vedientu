package com.example.demo.service;

import com.example.demo.dto.RideLogResponse;
import com.example.demo.entity.Bus;
import com.example.demo.entity.RideLog;
import com.example.demo.entity.Ticket;
import com.example.demo.entity.Trip;
import com.example.demo.entity.User;
import com.example.demo.repository.RideLogRepository;
import com.example.demo.repository.TripRepository;
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

    @Autowired
    private TripRepository tripRepository;

    // Lấy tất cả các chuyến đi theo tài xế
    public List<Trip> getTripsByDriver(User driver) {
        return tripRepository.findAllByDriver(driver);
    }

    // Lấy chuyến đi đang mở theo tài xế
    public Optional<Trip> getOpenTripByDriver(User driver) {
        return tripRepository.findByDriverAndStatus(driver, Trip.Status.OPEN);
    }

    public void saveTrip(Trip trip) {
        tripRepository.save(trip);
    }

    // Lấy tất cả các log đi theo chuyến
    public List<RideLog> getRideLogsByTrip(Trip trip) {
        return rideLogRepository.findByTrip(trip);
    }
    // Lưu RideLog và trả về RideLogResponse
    public RideLogResponse saveRideLog(User user, Ticket ticket, User driver, Bus bus, Trip trip) {
        RideLog rideLog = new RideLog();
    
        rideLog.setUser(user);
        rideLog.setTicketId(ticket != null ? ticket.getId() : null);
        rideLog.setDriver(driver);
        rideLog.setBus(bus);
        rideLog.setRoute(bus.getRoute());
        rideLog.setRideTime(LocalDateTime.now());
        rideLog.setStatus(RideLog.Status.VALID);
    
        rideLog.setUserName(user != null ? user.getFullName() : "Không rõ");
        rideLog.setBusCode(bus != null ? bus.getLicensePlate() : "Không rõ");
    
        // ✅ Thêm phần mới: Gán chuyến đi (trip)
        if (trip != null) {
            rideLog.setTrip(trip);
        }
    
        RideLog savedRideLog = rideLogRepository.save(rideLog);
        return toRideLogResponse(savedRideLog);
    }
    

    public List<RideLog> getRideLogsByUser(User user) {
        return rideLogRepository.findByUser(user);
    }

    public List<RideLog> getPassengersByDriver(User driver) {
        return rideLogRepository.findByDriver(driver);
    }

    public RideLogResponse getRideLogDetailForUser(Long rideLogId, String token) {
        String jwt = token.replace("Bearer ", "");
        String email = jwtUtil.extractEmail(jwt);
        User user = userService.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("Người dùng không tồn tại");
        }

        Optional<RideLog> rideLogOpt = rideLogRepository.findById(rideLogId);
        if (rideLogOpt.isEmpty()) return null;

        RideLog rideLog = rideLogOpt.get();
        if (!rideLog.getUser().getId().equals(user.getId())) {
            return null;
        }

        return toRideLogResponse(rideLog);
    }

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
