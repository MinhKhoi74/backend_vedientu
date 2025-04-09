package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // ✅ Tìm người dùng theo email
    public User findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElse(null);
    }
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
     // ✅ Lấy tất cả người dùng
     public List<User> getAllUsers() {
        return userRepository.findAll();
    }
     // ✅ Cập nhật thông tin người dùng
    public void updateUser(User user) {
        userRepository.save(user);
    }
    // ✅ Xóa người dùng theo ID
    public boolean deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
