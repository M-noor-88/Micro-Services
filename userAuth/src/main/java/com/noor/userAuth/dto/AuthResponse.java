package com.noor.userAuth.dto;

import lombok.*;

@Setter
@Getter
@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}