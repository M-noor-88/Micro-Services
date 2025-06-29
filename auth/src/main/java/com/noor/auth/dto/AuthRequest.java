package com.noor.auth.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String name;
    private String password;
}