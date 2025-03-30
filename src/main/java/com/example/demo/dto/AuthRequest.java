package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
    private String fullName;
    private String email;
    private String password;
    private String phone;
}
