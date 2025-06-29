package com.noor.subscription.util;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "payment", url = "http://localhost:8094")
public interface PaymentClient {
    @GetMapping("/payments/check")
    boolean hasPaid(@RequestHeader("Authorization") String token,
                    @RequestParam Long courseId);
}
