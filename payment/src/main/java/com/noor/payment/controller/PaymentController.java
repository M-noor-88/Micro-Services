package com.noor.payment.controller;

import com.noor.payment.service.PaymentService;
import com.noor.payment.util.AuthClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final AuthClient authClient;

    public PaymentController(PaymentService paymentService, AuthClient authClient) {
        this.paymentService = paymentService;
        this.authClient = authClient;
    }

    @PostMapping("/{courseId}")
    public ResponseEntity<?> pay(@PathVariable Long courseId,
                                 @RequestHeader("Authorization") String authHeader) {
        return paymentService.makePayment(courseId, authHeader);
    }

    @GetMapping("/my")
    public ResponseEntity<?> myPayments(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(paymentService.getUserPayments(authHeader));
    }

    @GetMapping("/check")
    public boolean checkPayment(@RequestParam Long courseId,
                                @RequestHeader("Authorization") String authHeader) {
        Long userId = authClient.getUserId(authHeader);
        return paymentService.hasPaid(userId, courseId);
    }
}

