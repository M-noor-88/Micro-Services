package com.noor.exam.repository;

import com.noor.exam.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    Optional<Exam> findByCourseId(Long courseId);
}