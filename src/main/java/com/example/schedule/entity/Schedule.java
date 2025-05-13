package com.example.schedule.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class Schedule {

    private Long id;
    private String password;
    private String name;
    private String title;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Schedule( String password, String name, String title, String contents, LocalDateTime createdAt, LocalDateTime updatedAt){
        this.password = password;
        this.name = name;
        this.title = title;
        this.contents = contents;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Schedule(String password, String name, String title, String contents) {
        this.password = password;
        this.name = name;
        this.title = title;
        this.contents = contents;
    }
}
