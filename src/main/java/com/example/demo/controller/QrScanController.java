// package com.example.demo.controller;

// import com.example.demo.service.QrScanService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.*;

// @RestController
// @RequestMapping("/scan")
// public class QrScanController {
//     @Autowired
//     private QrScanService qrScanService;

//     @PostMapping("/validate")
//     public String validateQrCode(@RequestParam String qrCode, @RequestParam Long busId) {
//         return qrScanService.scanQrCode(qrCode, busId);
//     }
// }
