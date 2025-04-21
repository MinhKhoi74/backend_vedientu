package com.example.demo.service;

import com.example.demo.entity.Trip;
import com.example.demo.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    public Trip findById(Long id) {
        return tripRepository.findById(id).orElse(null);
    }
}
