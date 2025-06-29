package com.noor.exam.controller;

import com.noor.exam.model.Exam;
import com.noor.exam.serivce.ExamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exams")
public class ExamController {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping("/course/{courseId}")
    public ResponseEntity<?> createExam(@PathVariable Long courseId,
                                        @RequestBody Exam exam) {
        return examService.createExam(courseId, exam);
    }

    @PostMapping("/submit/{courseId}")
    public ResponseEntity<?> submitExam(@PathVariable Long courseId,
                                        @RequestBody String answer,
                                        @RequestHeader("Authorization") String token) {
        return examService.submitExam(courseId, answer, token);
    }

    @GetMapping("/my-results")
    public ResponseEntity<?> getMyResults(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(examService.getUserResults(token));
    }
}

