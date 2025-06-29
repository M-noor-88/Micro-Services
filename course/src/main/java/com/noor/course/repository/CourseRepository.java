package com.noor.course.repository;

import com.noor.course.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByApprovedTrue();
    List<Course> findByApprovedFalse();
    List<Course> findByTrainerId(Long trainerId);
}