package com.example.schedule.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Schedule {

    private Long id;
    private String name;
    private String password;
    private String title;
    private String contents;
//    private String createdAt;
//    private String updatedAt;

    public Schedule(String name, String password, String title, String contents){
        this.name = name;
        this.password = password;
        this.title = title;
        this.contents = contents;
//        this.createdAt = createdAt;
//        this.updatedAt = updatedAt;
    }
}
