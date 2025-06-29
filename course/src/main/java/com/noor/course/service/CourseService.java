package com.noor.course.service;

import com.noor.course.model.Course;
import com.noor.course.repository.CourseRepository;
import com.noor.course.util.AuthClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CourseService {



    private final CourseRepository courseRepo;
    private final AuthClient authClient;

    public CourseService(CourseRepository courseRepo, AuthClient authClient) {
        this.courseRepo = courseRepo;
        this.authClient = authClient;
    }


    public Course addCourse(Course course, String authHeader) {
        if (!authClient.hasRole(authHeader, "TRAINER")) {
            throw new RuntimeException("Only TRAINERs can add courses.");
        }
        Long trainerId = authClient.getUserId(authHeader); // ✅ extract from token
        course.setTrainerId(trainerId); // ✅ set it here, not in request body
        course.setApproved(false); // default: needs approval
        return courseRepo.save(course);
    }

    public Course updateCourse(Long id, Course updatedCourse) {
        Course course = courseRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        course.setTitle(updatedCourse.getTitle());
        course.setDescription(updatedCourse.getDescription());
        course.setPrice(updatedCourse.getPrice());

        return courseRepo.save(course);
    }

    public ResponseEntity<?> deleteCourse(Long id , String authHeader) {
        if (!authClient.hasRole(authHeader, "TRAINER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("YOU CAN'T DELETE THIS COURSE — Only TRAINERs are allowed.");

        }
        Course course = courseRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        Long courseId = course.getTrainerId();
        Long userId = authClient.getUserId(authHeader);

        if(!Objects.equals(courseId, userId))
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("YOU ARE NOT THE OWNER OF THIS COURSE");

        }
        courseRepo.deleteById(id);
        return ResponseEntity.ok("Course deleted successfully.");
    }

    public List<Course> getAvailableCourses() {
        return courseRepo.findByApprovedTrue();
    }

    public List<Course> getPendingCourses() {
        return courseRepo.findByApprovedFalse();
    }

    public Course approveCourse(Long id, String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        if (!authClient.hasRoleFromToken(token, "ADMIN")) {
            throw new RuntimeException("Only ADMIN can approve courses.");
        }

        Course course = courseRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        course.setApproved(true);
        return courseRepo.save(course);
    }

    public Course getCourse(Long id) {
        return courseRepo.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));
    }
}
