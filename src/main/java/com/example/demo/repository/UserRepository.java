package com.example.demo.repository;

import com.example.demo.entity.Bus;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);  // Thêm phương thức kiểm tra số điện thoại
    List<User> findAllByOrderByIdDesc();
    Optional<User> findByBus(Bus bus);


}
