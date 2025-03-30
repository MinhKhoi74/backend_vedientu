package com.example.demo.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;

import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Base64;

@Service
public class QRService {

    // ✅ Tạo mã QR từ nội dung và trả về Base64
    public String generateQRCode(String content) {
        try {
            int width = 300;
            int height = 300;
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height);
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            
            // ✅ Chuyển QR thành Base64 để gửi qua API
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "png", baos);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Lỗi khi tạo mã QR", e);
        }
    }
}
