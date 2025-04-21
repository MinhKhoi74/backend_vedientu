package com.example.demo.repository;

import com.example.demo.entity.Trip;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface TripRepository extends JpaRepository<Trip, Long> {
    Optional<Trip> findByDriverAndStatus(User driver, Trip.Status status);
    List<Trip> findAllByDriver(User driver);
}
