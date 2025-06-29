package com.noor.exam.util;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "subscription", url = "http://localhost:8095")
public interface SubscriptionClient {
    @GetMapping("/subscriptions/check")
    boolean isSubscribed(@RequestHeader("Authorization") String token,
                         @RequestParam Long courseId);
}
