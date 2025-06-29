package com.noor.payment.util;

import com.noor.payment.dto.CourseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "course", url = "http://localhost:8093")
public interface CourseClient {
    @GetMapping("/courses/{id}")
    ResponseEntity<CourseDto> getCourse(@PathVariable("id") Long id);
}

