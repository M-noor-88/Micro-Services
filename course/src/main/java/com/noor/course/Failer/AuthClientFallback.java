package com.noor.course.Failer;

import com.noor.course.dto.UserDto;
import com.noor.course.util.AuthClient;
import org.springframework.stereotype.Component;

@Component
public class AuthClientFallback implements AuthClient {

    @Override
    public UserDto getUserById(Long id) {
        System.out.println("⚠️ Auth service unavailable: getUserById");
        return new UserDto(null, "unknown", "UNKNOWN"); // or return null
    }

    @Override
    public boolean hasRoleFromToken(String authHeader, String role) {
        System.out.println("⚠️ Auth service unavailable: hasRoleFromToken");
        return false;
    }

    @Override
    public boolean hasRole(String authHeader, String role) {
        System.out.println("⚠️ Auth service unavailable: hasRole");
        return false;
    }

    @Override
    public Long getUserId(String authHeader) {
        System.out.println("⚠️ Auth service unavailable: getUserId");
        return -1L;
    }
}
