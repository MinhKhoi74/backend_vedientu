// package com.example.demo.controller;

// import com.example.demo.dto.QrScanRequest;
// import com.example.demo.dto.QrScanResponse;
// import com.example.demo.service.RideService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.web.bind.annotation.*;

// @RestController
// @RequestMapping("/ride")
// public class RideController {
//     @Autowired
//     private RideService rideService;

//     @PreAuthorize("hasRole('DRIVER')")
//     @PostMapping("/check")
//     public QrScanResponse scanQrCode(@RequestBody QrScanRequest request) {
//         return rideService.scanQrCode(request);
//     }
// }
