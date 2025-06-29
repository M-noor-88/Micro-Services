package com.noor.payment.util;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth", url = "http://localhost:8092")
public interface AuthClient {
    @GetMapping("/auth/has-role")
    boolean hasRole(@RequestHeader("Authorization") String authHeader,
                    @RequestParam String role);

    @GetMapping("/auth/extract-id")
    Long getUserId(@RequestHeader("Authorization") String authHeader);
}
