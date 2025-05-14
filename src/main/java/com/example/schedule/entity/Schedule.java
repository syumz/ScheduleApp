package com.example.schedule.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class Schedule {

    // 1. 속성
    private Long id;
    private Long userId;
    private String password;
    private String name;
    private String email;
    private String title;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 2. 생성자
    public Schedule(String password, String name, String email, String title, String contents, LocalDateTime createdAt, LocalDateTime updatedAt){
        this.password = password;
        this.name = name;
        this.email = email;
        this.title = title;
        this.contents = contents;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
