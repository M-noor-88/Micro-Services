package com.noor.course.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String role;

    public UserDto(Long id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }
}