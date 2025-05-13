package com.example.schedule.service;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {
    ScheduleResponseDto saveSchedule(ScheduleRequestDto dto);

    List<ScheduleResponseDto> findAllSchedules(String name, LocalDate updatedAt);

    ScheduleResponseDto findScheduleById(Long userId);

    ScheduleResponseDto updateSchedule(Long userid, String password, String name, String title, String contents);

    void deleteSchedule(Long userid, String password);
}
