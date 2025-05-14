package com.example.schedule.controller;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/schedules")
public class ScheduleAddController {

    // 1.. 속성
    private final ScheduleService scheduleService;

    // 2. 생성자
    public ScheduleAddController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // 3. 기능(메서드)
    // 스케줄 생성 메서드
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody @Valid ScheduleRequestDto dto) {

        return new ResponseEntity<>(scheduleService.saveSchedule(dto), HttpStatus.CREATED);
    }

    // 이름이나 수정일로 스케줄을 조회하는 메서드
    @GetMapping
    public List<ScheduleResponseDto> findAllSchedules(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) LocalDate updatedAt){
        return scheduleService.findAllSchedules(name, updatedAt);
    }

    // 유저 id로 스케줄 조회 메서드
    @GetMapping("/{userId}")
    public ResponseEntity<ScheduleResponseDto> findScheduleById(@PathVariable Long userId){
        return new ResponseEntity<>(scheduleService.findScheduleById(userId), HttpStatus.CREATED);
    }

    // 스케줄 수정 메서드
    @PatchMapping("/{userid}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
            @PathVariable Long userid,
            @RequestBody ScheduleRequestDto dto){
        return new ResponseEntity<>(scheduleService.updateSchedule(userid, dto.getPassword(), dto.getName(),  dto.getTitle(), dto.getContents()), HttpStatus.OK);
    }

    // 스케줄 삭제 메서드
    @DeleteMapping("/{userid}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable Long userid,
            @RequestBody ScheduleRequestDto dto){
        scheduleService.deleteSchedule(userid, dto.getPassword());

        return new ResponseEntity<>(HttpStatus.OK);
    }

}