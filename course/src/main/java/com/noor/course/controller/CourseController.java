package com.noor.course.controller;

import com.noor.course.model.Course;
import com.noor.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")

public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<?> addCourse(@RequestBody Course course,
                                       @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(courseService.addCourse(course, authHeader));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @RequestBody Course course) {
        return ResponseEntity.ok(courseService.updateCourse(id, course));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id ,  @RequestHeader("Authorization") String authHeader) {
       // courseService.deleteCourse(id ,authHeader );
        return courseService.deleteCourse(id ,authHeader );
    }

    @GetMapping("/available")
    public List<Course> availableCourses() {
        return courseService.getAvailableCourses();
    }

    @GetMapping("/pending")
    public List<Course> pendingCourses() {
        return courseService.getPendingCourses();
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<?> approveCourse(@PathVariable Long id,
                                           @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(courseService.approveCourse(id, authHeader));
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getCourse(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourse(id));
    }
}
