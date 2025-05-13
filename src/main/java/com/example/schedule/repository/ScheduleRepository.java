package com.example.schedule.repository;

import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository {

    ScheduleResponseDto saveSchedule(Schedule schedule);

    List<ScheduleResponseDto> findAllSchedules(String name, LocalDate updatedAt);

    Schedule findScheduleByIdOrElseThrow(Long id);

    Schedule findScheduleByIdPwOrElseThrow(Long id, String password);

    int updateSchedule(Long id, String password, String name, String title, String contents);

    int deleteSchedule(Long id, String password);
}
