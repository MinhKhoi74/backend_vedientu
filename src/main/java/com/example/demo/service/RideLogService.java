package com.example.demo.service;
import com.example.demo.entity.RideLog;
import com.example.demo.entity.Ticket;
import com.example.demo.entity.User;
import com.example.demo.repository.RideLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class RideLogService {
    @Autowired
    private RideLogRepository rideLogRepository;

    public RideLog saveRideLog(User user, Ticket ticket, User driver, String route) {
        RideLog rideLog = new RideLog();
        rideLog.setUser(user);
        rideLog.setTicket(ticket);
        rideLog.setDriver(driver);
        rideLog.setRoute(route);
        rideLog.setRideTime(LocalDateTime.now());
        rideLog.setStatus(RideLog.Status.VALID);


        return rideLogRepository.save(rideLog);
    }

    public List<RideLog> getRideLogsByUser(User user) {
        return rideLogRepository.findByUser(user);
    }
    public List<RideLog> getPassengersByDriver(User driver) {
        return rideLogRepository.findByDriver(driver);
    }
    
}
 // ✅ Lấy danh sách lịch sử chuyến đi theo `userId`
    //  public List<RideLog> getRideLogsByUser(Long userId) {
    //     return rideLogRepository.findByUserId(userId);
    // }