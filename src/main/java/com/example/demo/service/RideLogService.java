package com.example.demo.service;

import com.example.demo.entity.RideLog;
import com.example.demo.entity.Ticket;
import com.example.demo.entity.User;
import com.example.demo.repository.RideLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RideLogService {

    @Autowired
    private RideLogRepository rideLogRepository;

    public RideLog saveRideLog(User user, Ticket ticket, User driver, String route) {
        RideLog rideLog = new RideLog();
        rideLog.setUser(user);

        // Lưu ID vé thay vì đối tượng vé
        rideLog.setTicketId(ticket != null ? ticket.getId() : null); // Nếu ticket null thì ticketId cũng là null

        // Lưu tài xế vào RideLog
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
