package com.example.schedule.repository;

import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository {

    ScheduleResponseDto saveSchedule(Schedule schedule);

    List<ScheduleResponseDto> findAllSchedules(String name, LocalDate updatedAt);

    Schedule findScheduleByIdOrElseThrow(Long userId);

    Schedule findScheduleByIdPwOrElseThrow(Long userId, String password);

    int updateSchedule(Long userId, String password, String name, String title, String contents);

    int deleteSchedule(Long userid, String password);
}
