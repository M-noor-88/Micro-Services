package com.noor.subscription.repository;

import com.noor.subscription.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findAllByUserId(Long userId);
    boolean existsByUserIdAndCourseId(Long userId, Long courseId);
}

