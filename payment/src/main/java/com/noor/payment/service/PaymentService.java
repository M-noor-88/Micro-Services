package com.noor.payment.service;

import com.noor.payment.dto.CourseDto;
import com.noor.payment.model.Payment;
import com.noor.payment.repository.PaymentRepository;
import com.noor.payment.util.AuthClient;
import com.noor.payment.util.CourseClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepo;
    private final AuthClient authClient;
    private final CourseClient courseClient;

    public PaymentService(PaymentRepository paymentRepo, AuthClient authClient, CourseClient courseClient) {
        this.paymentRepo = paymentRepo;
        this.authClient = authClient;
        this.courseClient = courseClient;
    }

    public ResponseEntity<?> makePayment(Long courseId, String authHeader) {
        if (!authClient.hasRole(authHeader, "LEARNER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only learners can make payments.");
        }

        Long userId = authClient.getUserId(authHeader);
        Optional<Payment> existing = paymentRepo.findByUserIdAndCourseId(userId, courseId);
        if (existing.isPresent()) {
            return ResponseEntity.badRequest().body("You already paid for this course.");
        }

        CourseDto course = courseClient.getCourse(courseId).getBody();
        if (!course.isApproved()) {
            return ResponseEntity.badRequest().body("Course not found or not approved.");
        }

        Payment payment = new Payment();
        payment.setUserId(userId);
        payment.setCourseId(courseId);
        payment.setAmount(course.getPrice());
        payment.setPaidAt(LocalDateTime.now());
        payment.setStatus("PAID");

        paymentRepo.save(payment);
        return ResponseEntity.ok("Payment successful");
    }

    public boolean hasPaid(Long userId, Long courseId) {
        return paymentRepo.findByUserIdAndCourseId(userId, courseId).isPresent();
    }

    public List<Payment> getUserPayments(String authHeader) {
        Long userId = authClient.getUserId(authHeader);
        return paymentRepo.findAllByUserId(userId);
    }
}
