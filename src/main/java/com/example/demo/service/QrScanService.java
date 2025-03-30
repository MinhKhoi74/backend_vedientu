// package com.example.demo.service;

// import com.example.demo.entity.*;
// import com.example.demo.repository.*;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.time.LocalDateTime;
// import java.util.Optional;

// @Service
// public class QrScanService {
//     @Autowired
//     private QrCodeRepository qrCodeRepository;

//     @Autowired
//     private TicketRepository ticketRepository;

//     @Autowired
//     private RideLogRepository rideLogRepository;

//     @Autowired
//     private BusRepository busRepository;

//     public String scanQrCode(String qrCode, Long busId) {
//         // Kiểm tra xe buýt có tồn tại không
//         Bus bus = busRepository.findById(busId)
//                 .orElseThrow(() -> new RuntimeException("Không tìm thấy xe buýt"));

//         // Tìm mã QR trong database
//         Optional<QrCode> qrCodeOptional = qrCodeRepository.findAll().stream()
//                 .filter(qr -> qr.getQrCode().equals(qrCode))
//                 .findFirst();

//         if (qrCodeOptional.isEmpty()) {
//             return "Mã QR không hợp lệ!";
//         }

//         Ticket ticket = qrCodeOptional.get().getTicket();
//         User user = ticket.getUser();

//         // Kiểm tra vé còn hợp lệ không
//         if (ticket.getTicketType() == Ticket.TicketType.SINGLE && ticket.getRemainingRides() <= 0) {
//             return "Vé đã hết lượt!";
//         }
//         if (ticket.getTicketType() == Ticket.TicketType.MONTHLY && ticket.getExpiryDate().isBefore(LocalDateTime.now().toLocalDate())) {
//             return "Vé đã hết hạn!";
//         }

//         // Nếu là vé SINGLE thì trừ số lượt còn lại
//         if (ticket.getTicketType() == Ticket.TicketType.SINGLE) {
//             ticket.setRemainingRides(ticket.getRemainingRides() - 1);
//             ticketRepository.save(ticket);
//         }

//         // Lưu lịch sử chuyến đi
//         RideLog rideLog = RideLog.builder()
//                 .user(user)
//                 .bus(bus)
//                 .qrCode(qrCode)
//                 .rideTime(LocalDateTime.now())
//                 .status(RideLog.Status.VALID)
//                 .build();
//         rideLogRepository.save(rideLog);

//         return "Vé hợp lệ! Hành khách có thể lên xe.";
//     }
// }
