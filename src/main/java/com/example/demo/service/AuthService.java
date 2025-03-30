package com.example.demo.service;

import com.example.demo.config.JwtUtil;
import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // ✅ Đăng ký (Thêm kiểm tra dữ liệu trống)
    public ResponseEntity<?> register(User user) {
        Map<String, String> response = new HashMap<>();

        // Kiểm tra dữ liệu trống
        if (user.getFullName() == null || user.getFullName().isEmpty() ||
            user.getEmail() == null || user.getEmail().isEmpty() ||
            user.getPassword() == null || user.getPassword().isEmpty() ||
            user.getPhone() == null || user.getPhone().isEmpty()) {
            
            response.put("message", "Không được để trống bất kỳ trường nào!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Kiểm tra email đã tồn tại chưa
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            response.put("message", "Email đã tồn tại!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Kiểm tra số điện thoại đã tồn tại chưa
        if (userRepository.findByPhone(user.getPhone()).isPresent()) {
            response.put("message", "Số điện thoại đã tồn tại!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Mã hóa mật khẩu trước khi lưu
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        response.put("message", "Đăng ký thành công!");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // ✅ Đăng nhập (Thêm kiểm tra dữ liệu trống)
    public ResponseEntity<?> login(AuthRequest request) {
        // Kiểm tra dữ liệu trống
        if (request.getEmail() == null || request.getEmail().isEmpty() ||
            request.getPassword() == null || request.getPassword().isEmpty()) {

            return new ResponseEntity<>(Map.of("message", "Email và mật khẩu không được để trống!"), 
                                        HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user == null) {
            return new ResponseEntity<>(Map.of("message", "Tài khoản không tồn tại!"), HttpStatus.UNAUTHORIZED);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new ResponseEntity<>(Map.of("message", "Sai mật khẩu!"), HttpStatus.UNAUTHORIZED);
        }

        // ✅ Tạo JWT token với cả email và role
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return new ResponseEntity<>(new AuthResponse(token, user.getRole().name()), HttpStatus.OK);
    }
}
