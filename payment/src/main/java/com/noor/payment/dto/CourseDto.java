package com.noor.payment.dto;

import lombok.Data;

@Data
public class CourseDto {
    private Long id;
    private String title;
    private Double price;
    private boolean approved;
}
