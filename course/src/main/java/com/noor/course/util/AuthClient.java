package com.noor.course.util;

import com.noor.course.Failer.AuthClientFallback;
import com.noor.course.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth", fallback = AuthClientFallback.class)
public interface AuthClient {

    @GetMapping("/auth/users/{id}")
    UserDto getUserById(@PathVariable Long id);

    @GetMapping("/auth/validate-role")
    boolean hasRoleFromToken(@RequestHeader("Authorization") String authHeader,
                             @RequestParam String role);

    @GetMapping("/auth/has-role")
    boolean hasRole(@RequestHeader("Authorization") String authHeader,
                    @RequestParam String role);

    @GetMapping("/auth/extract-id")
    Long getUserId(@RequestHeader("Authorization") String authHeader);
}