package com.noor.subscription.controller;

import com.noor.subscription.model.SubscriptionStatus;
import com.noor.subscription.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/{courseId}")
    public ResponseEntity<?> subscribe(@PathVariable Long courseId,
                                       @RequestHeader("Authorization") String token) {
        return subscriptionService.subscribeToCourse(courseId, token);
    }

    @GetMapping("/my")
    public ResponseEntity<?> mySubscriptions(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(subscriptionService.getUserSubscriptions(token));
    }

    @PutMapping("/status/{courseId}")
    public ResponseEntity<?> updateStatus(@PathVariable Long courseId,
                                          @RequestParam SubscriptionStatus status,
                                          @RequestHeader("Authorization") String token) {
        return subscriptionService.updateStatus(courseId, status, token);
    }

    @GetMapping("/check")
    public boolean checkIfSubscribed(@RequestHeader("Authorization") String token,
                                     @RequestParam Long courseId) {
        return subscriptionService.isUserSubscribedToCourse(token, courseId);
    }
}

