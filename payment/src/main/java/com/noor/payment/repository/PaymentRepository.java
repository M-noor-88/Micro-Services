package com.noor.payment.repository;

import com.noor.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByUserIdAndCourseId(Long userId, Long courseId);
    List<Payment> findAllByUserId(Long userId);
}
