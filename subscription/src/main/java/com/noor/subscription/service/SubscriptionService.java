package com.noor.subscription.service;

import com.noor.subscription.model.Subscription;
import com.noor.subscription.model.SubscriptionStatus;
import com.noor.subscription.repository.SubscriptionRepository;
import com.noor.subscription.util.AuthClient;
import com.noor.subscription.util.PaymentClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service

public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepo;
    private final AuthClient authClient;
    private final PaymentClient paymentClient;

    public SubscriptionService(SubscriptionRepository subscriptionRepo, AuthClient authClient, PaymentClient paymentClient) {
        this.subscriptionRepo = subscriptionRepo;
        this.authClient = authClient;
        this.paymentClient = paymentClient;
    }

    public ResponseEntity<?> subscribeToCourse(Long courseId, String token) {
        Long userId = authClient.getUserId(token);

        if (subscriptionRepo.existsByUserIdAndCourseId(userId, courseId)) {
            return ResponseEntity.badRequest().body("Already subscribed to this course.");
        }

        if (!paymentClient.hasPaid(token, courseId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Payment required to subscribe.");
        }

        Subscription sub = new Subscription();
        sub.setUserId(userId);
        sub.setCourseId(courseId);
        sub.setStatus(SubscriptionStatus.NOT_STARTED);
        sub.setSubscribedAt(LocalDateTime.now());

        subscriptionRepo.save(sub);
        return ResponseEntity.ok("Subscribed successfully.");
    }

    public List<Subscription> getUserSubscriptions(String token) {
        Long userId = authClient.getUserId(token);
        return subscriptionRepo.findAllByUserId(userId);
    }

    public ResponseEntity<?> updateStatus(Long courseId, SubscriptionStatus newStatus, String token) {
        Long userId = authClient.getUserId(token);

        Optional<Subscription> subOpt = subscriptionRepo.findAllByUserId(userId)
                .stream()
                .filter(s -> s.getCourseId().equals(courseId))
                .findFirst();

        if (subOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Subscription not found.");
        }

        Subscription sub = subOpt.get();
        sub.setStatus(newStatus);
        subscriptionRepo.save(sub);
        return ResponseEntity.ok("Status updated.");
    }

    public boolean isUserSubscribedToCourse(String token, Long courseId) {
        Long userId = authClient.getUserId(token);
        return subscriptionRepo.existsByUserIdAndCourseId(userId, courseId);
    }
}
