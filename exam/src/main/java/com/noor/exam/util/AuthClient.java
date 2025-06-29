package com.noor.exam.util;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth", url = "http://localhost:8092")
public interface AuthClient {
    @GetMapping("/auth/extract-id")
    Long getUserId(@RequestHeader("Authorization") String token);
}
