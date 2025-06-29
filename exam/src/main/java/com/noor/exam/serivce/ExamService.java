package com.noor.exam.serivce;

import com.noor.exam.model.Exam;
import com.noor.exam.model.ExamResult;
import com.noor.exam.model.ExamStatus;
import com.noor.exam.repository.*;
import com.noor.exam.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ExamService {

    private final ExamRepository examRepo;
    private final ExamResultRepository resultRepo;
    private final AuthClient authClient;
    private final SubscriptionClient subscriptionClient;

    public ExamService(ExamRepository examRepo, ExamResultRepository resultRepo, AuthClient authClient, SubscriptionClient subscriptionClient) {
        this.examRepo = examRepo;
        this.resultRepo = resultRepo;
        this.authClient = authClient;
        this.subscriptionClient = subscriptionClient;
    }

    public ResponseEntity<?> createExam(Long courseId, Exam exam) {
        if (examRepo.findByCourseId(courseId).isPresent()) {
            return ResponseEntity.badRequest().body("Exam already exists for this course.");
        }
        exam.setCourseId(courseId);
        return ResponseEntity.ok(examRepo.save(exam));
    }

    public ResponseEntity<?> submitExam(Long courseId, String answer, String token) {
        Long userId = authClient.getUserId(token);

        if (!subscriptionClient.isSubscribed(token, courseId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You must be subscribed to the course.");
        }

        Optional<Exam> examOpt = examRepo.findByCourseId(courseId);
        if (examOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Exam not found.");
        }

        Exam exam = examOpt.get();
        boolean correct = exam.getCorrectAnswer().trim().equalsIgnoreCase(answer.trim());

        ExamResult result = new ExamResult();
        result.setUserId(userId);
        result.setCourseId(courseId);
        result.setSubmittedAnswer(answer);
        result.setScore(correct ? 100 : 0);
        result.setStatus(correct ? ExamStatus.PASSED : ExamStatus.FAILED);
        result.setSubmittedAt(LocalDateTime.now());

        resultRepo.save(result);
        return ResponseEntity.ok(result.getStatus());
    }

    public List<ExamResult> getUserResults(String token) {
        Long userId = authClient.getUserId(token);
        return resultRepo.findAllByUserId(userId);
    }
}
