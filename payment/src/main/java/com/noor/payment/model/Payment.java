package com.noor.payment.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;      // learner ID from token
    private Long courseId;    // course being paid for

    private Double amount;
    private LocalDateTime paidAt;
    private String status;    // PAID / FAILED / PENDING
}
