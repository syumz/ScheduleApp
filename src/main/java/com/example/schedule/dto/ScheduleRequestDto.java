package com.example.schedule.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleRequestDto {

    // 1. 속성
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;

    @NotBlank(message = "제목은 필수 입력값입니다.")
    private String name;

    @Email
    private String email;

    private String title;

    @NotBlank(message = "내용은 필수 입력값입니다.")
    @Pattern(
            regexp = "^[a-zA-Z0-9가-힣.]{1,200}$",
            message = "내용은 영문 대/소문자, 숫자, 한글, 마침표만 사용 가능하며 200자리까지만 가능 합니다."
    )
    private String contents;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
