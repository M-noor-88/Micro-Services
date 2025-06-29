package com.noor.exam.repository;

import com.noor.exam.model.ExamResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {
    Optional<ExamResult> findByUserIdAndCourseId(Long userId, Long courseId);
    List<ExamResult> findAllByUserId(Long userId);
}