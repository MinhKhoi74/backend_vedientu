package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QrScanRequest {
    private Long ticketId;  // ID của vé cần quét
    private Long busId;     // ID của xe buýt hiện tại
    private Long userId;    // ID của khách hàng (người sử dụng vé)
    private Long driverId;  // ID của tài xế (người quét vé)
}
