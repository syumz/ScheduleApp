package com.example.schedule.controller;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/schedules")
public class ScheduleAddController {

    private final ScheduleService scheduleService;

    public ScheduleAddController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody ScheduleRequestDto dto) {

        return new ResponseEntity<>(scheduleService.saveSchedule(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public List<ScheduleResponseDto> findAllSchedules(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) LocalDate updatedAt){
        return scheduleService.findAllSchedules(name, updatedAt);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ScheduleResponseDto> findScheduleById(@PathVariable Long userId){
        return new ResponseEntity<>(scheduleService.findScheduleById(userId), HttpStatus.CREATED);
    }

    @PatchMapping("/{userid}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
            @PathVariable Long userid,
            @RequestBody ScheduleRequestDto dto){
        return new ResponseEntity<>(scheduleService.updateSchedule(userid, dto.getPassword(), dto.getName(),  dto.getTitle(), dto.getContents()), HttpStatus.OK);
    }

    @DeleteMapping("/{userid}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable Long userid,
            @RequestBody ScheduleRequestDto dto){
        scheduleService.deleteSchedule(userid, dto.getPassword());

        return new ResponseEntity<>(HttpStatus.OK);
    }

}