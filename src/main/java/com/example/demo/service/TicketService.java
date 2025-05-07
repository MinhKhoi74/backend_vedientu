package com.example.demo.service;

import com.example.demo.entity.Ticket;
import com.example.demo.entity.User;
import com.example.demo.repository.TicketRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Base64;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    // ✅ Mua vé và tạo mã QR
    public Ticket buyTicket(User user, Ticket.TicketType ticketType) {
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setTicketType(ticketType);
        ticket.setPrice(getPriceByType(ticketType));
        ticket.setRemainingRides(getRidesByType(ticketType));
        ticket.setPurchaseDate(new Date());
        ticket.setExpiryDate(getExpiryDate());

        // ✅ Sinh mã QR duy nhất
        String qrCode = UUID.randomUUID().toString();
        ticket.setQrCode(qrCode);

        return ticketRepository.save(ticket);
    }

    // ✅ Lấy vé theo ID
    public Optional<Ticket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    // ✅ Lấy danh sách vé của một người dùng
    public List<Ticket> getTicketsByUser(User user) {
        return ticketRepository.findByUserOrderByPurchaseDateDesc(user);
    }

    // ✅ Lấy mã QR của vé dưới dạng hình ảnh Base64
    public String getTicketQRCode(Ticket ticket) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(ticket.getQrCode(), BarcodeFormat.QR_CODE, 250, 250);
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "png", baos);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (WriterException | java.io.IOException e) {
            throw new RuntimeException("Lỗi khi tạo mã QR", e);
        }
    }

    // ✅ Giá vé theo loại
    private double getPriceByType(Ticket.TicketType type) {
        return switch (type) {
            case SINGLE -> 6000.0;
            case VIP -> 50000.0;
            case MONTHLY -> 200000.0;
        };
    }

    // ✅ Số lượt sử dụng theo loại
    private int getRidesByType(Ticket.TicketType type) {
        return switch (type) {
            case SINGLE -> 1;
            case VIP -> 10;
            case MONTHLY -> Integer.MAX_VALUE; // biểu thị "không giới hạn"
        };
    }

    // ✅ Ngày hết hạn mặc định là 30 ngày kể từ ngày mua
    private Date getExpiryDate() {
        Date expiry = new Date();
        expiry.setTime(expiry.getTime() + (30L * 24 * 60 * 60 * 1000)); // 30 ngày
        return expiry;
    }

    // ✅ Cập nhật vé (giảm lượt sử dụng, v.v.)
    public Ticket saveTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

//     // ✅ Xóa vé
//     public void deleteTicket(Long ticketId) {
//         ticketRepository.deleteById(ticketId);
//     }
}
