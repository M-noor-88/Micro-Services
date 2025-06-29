package com.noor.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestTestController {

    @GetMapping("/students/data")
    public ResponseEntity<String> studentEndpoint(){
        return ResponseEntity.ok("Hello LEARNER or Admin, you can see this.");
    }

    @GetMapping("/parent/data")
    public ResponseEntity<String> parentEndpoint() {
        return ResponseEntity.ok("Hello TRAINER or Admin, you can see this.");
    }
}