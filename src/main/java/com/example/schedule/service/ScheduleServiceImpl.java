package com.example.schedule.service;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
import com.example.schedule.repository.ScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService{

    // 1. 속성
    private final ScheduleRepository scheduleRepository;

    // 2. 생성자
    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    // 3. 기능(메서드)
    @Override
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto dto) {

        Schedule schedule = new Schedule(dto.getPassword(), dto.getName(), dto.getEmail(), dto.getTitle(), dto.getContents(), dto.getCreatedAt(), dto.getUpdatedAt());

        return scheduleRepository.saveSchedule(schedule);
    }

    @Override
    public List<ScheduleResponseDto> findAllSchedules(String name, LocalDate updatedAt) {
        List<ScheduleResponseDto> schedules = scheduleRepository.findAllSchedules(name, updatedAt);

        if (schedules.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "일정이 존재하지 않습니다.");
        }

        return scheduleRepository.findAllSchedules(name, updatedAt);
    }

    @Override
    public ScheduleResponseDto findScheduleById(Long userId) {
        Schedule schedule = scheduleRepository.findScheduleByIdOrElseThrow(userId);


        return new ScheduleResponseDto(schedule);
    }

    @Override
    public ScheduleResponseDto updateSchedule(Long userId, String password, String name, String title, String contents) {

        if(title == null || contents == null || name == null){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정할 이름, 내용, 제목을 입력해주세요");
        }

        int updatedRow = scheduleRepository.updateSchedule(userId, password, name, title, contents);

        if(updatedRow == 0){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "id, password 가 존재하지 않습니다" + userId + password);
        }

        Schedule schedule = scheduleRepository.findScheduleByIdPwOrElseThrow(userId, password);

        return new ScheduleResponseDto(schedule);
    }

    @Override
    public void deleteSchedule(Long userid, String password) {
        int deleteSchedule = scheduleRepository.deleteSchedule(userid, password);

        if(deleteSchedule == 0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "id, password가 존재하지 않습니다" + userid + password);
        }
    }
}
