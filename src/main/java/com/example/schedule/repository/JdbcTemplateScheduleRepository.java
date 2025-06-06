package com.example.schedule.repository;

import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcTemplateScheduleRepository implements ScheduleRepository {

    // 1. 속성
    private final JdbcTemplate jdbcTemplate;

    // 2. 생성자
    public JdbcTemplateScheduleRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 3. 기능(메서드)
    // 일정을 저장하는 메서드
    @Override
    public ScheduleResponseDto saveSchedule(Schedule schedule) {

        // 유저 테이블에 데이터를 삽입하기 위해 SimpleJdbcInsert 객체 생성
        SimpleJdbcInsert userInsert = new SimpleJdbcInsert(jdbcTemplate);
        userInsert.withTableName("user").usingGeneratedKeyColumns("user_id");

        // 유저 테이블에 삽입할 파라미터
        Map<String, Object> userParameters = new HashMap<>();
        userParameters.put("name", schedule.getName());
        userParameters.put("email", schedule.getEmail());
        userParameters.put("created_at", LocalDateTime.now());
        userParameters.put("updated_at", LocalDateTime.now());

        // 유저 테이블에 삽입 후 user_id 반환
        Number userKey = userInsert.executeAndReturnKey(new MapSqlParameterSource(userParameters));

        // 스케줄 테이블에 데이터를 삽입하기 위해 SimpleJdbcInsert 객체 생성
        SimpleJdbcInsert scheduleInsert = new SimpleJdbcInsert(jdbcTemplate);
        scheduleInsert.withTableName("schedule").usingGeneratedKeyColumns("id");

        // 스케줄 테이블에 삽입할 파라미터
        Map<String, Object> scheduleParameters = new HashMap<>();
        scheduleParameters.put("name", schedule.getName());
        scheduleParameters.put("user_id", userKey);
        scheduleParameters.put("password", schedule.getPassword());
        scheduleParameters.put("title", schedule.getTitle());
        scheduleParameters.put("contents", schedule.getContents());
        scheduleParameters.put("created_at", LocalDateTime.now());
        scheduleParameters.put("updated_at", LocalDateTime.now());

        // 유저 테이블에 삽입 후 id 반환
        Number scheduleKey = scheduleInsert.executeAndReturnKey(new MapSqlParameterSource(scheduleParameters));

        schedule.setCreatedAt(LocalDateTime.now());  // createdAt 설정
        schedule.setUpdatedAt(LocalDateTime.now());  // updatedAt 설정


        return new ScheduleResponseDto(userKey.longValue(), scheduleKey.longValue(), schedule.getName(), schedule.getTitle(), schedule.getContents(), schedule.getCreatedAt(), schedule.getUpdatedAt());
    }

    @Override
    public List<ScheduleResponseDto> findAllSchedules(String name, LocalDate updatedAt) {
        String sql = "";

        if (updatedAt != null && name != null) { // updatedAt과 name 둘 다 값이 있을 경우
            sql = "SELECT * FROM schedule LEFT JOIN user on schedule.user_id = user.user_id WHERE DATE_FORMAT(updated_at, '%Y-%m-%d') = ? AND name =? ORDER BY schedule.updated_at DESC";


            return jdbcTemplate.query(sql, memoRowMapperV1(), updatedAt, name);

        } else if (updatedAt == null && name != null) { // name 값만 있을 경우
            sql = "SELECT * FROM schedule LEFT JOIN user on schedule.user_id = user.user_id WHERE name =? ORDER BY schedule.updated_at DESC";


            return jdbcTemplate.query(sql, memoRowMapperV1(), name);

        } else if(updatedAt != null && name == null){ // updatedAt 값만 있을 경우
            sql = "SELECT * FROM schedule LEFT JOIN user on schedule.user_id = user.user_id WHERE name =? ORDER BY schedule.updated_at DESC";

            return jdbcTemplate.query(sql, memoRowMapperV1(), updatedAt);
        }
        return List.of();
    }

    @Override
    public Schedule findScheduleByIdOrElseThrow(Long userId) {
        // userId에 맞는 스케줄 조회
        List<Schedule> result = jdbcTemplate.query("SELECT * FROM schedule LEFT JOIN user on schedule.user_id = user.user_id WHERE user.user_id =?", memoRowMapperV2(), userId);
        return result.stream().findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + userId));
    }

    @Override
    public Schedule findScheduleByIdPwOrElseThrow(Long userId, String password) {
        // userId와 password 에 맞는 스케줄 조회
        List<Schedule> result = jdbcTemplate.query("SELECT * FROM schedule LEFT JOIN user on schedule.user_id = user.user_id WHERE user.user_id =? AND password =?", memoRowMapperV2(), userId, password);
        return result.stream().findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + userId));
    }

    @Override
    public int updateSchedule(Long userId, String password, String name, String title, String contents) {
        // userId와 password 에 맞는 스케줄 수정
        return jdbcTemplate.update("UPDATE schedule LEFT JOIN user ON schedule.user_id = user.user_id SET user.name = ?, schedule.title = ?, schedule.contents = ? where user.user_id =?", name, title, contents, userId);
    }

    @Override
    public int deleteSchedule(Long userid, String password) {
        // userId와 password 에 맞는 스케줄 삭제
        return jdbcTemplate.update("DELETE schedule, user FROM schedule LEFT JOIN user ON schedule.user_id = user.user_id where user.user_id =? AND schedule.password =? ", userid, password);
    }

    // ScheduleResponseDto 객체로 매핑하는 RowMapper 구현
    private RowMapper<ScheduleResponseDto> memoRowMapperV1(){
        return new RowMapper<ScheduleResponseDto>() {

            @Override
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                Timestamp createdTimestamp = rs.getTimestamp("created_at");
                Timestamp updatedTimestamp = rs.getTimestamp("updated_at");

                LocalDateTime createdAt = createdTimestamp != null ? createdTimestamp.toLocalDateTime() : null;
                LocalDateTime updatedAt = updatedTimestamp != null ? updatedTimestamp.toLocalDateTime() : null;

                return new ScheduleResponseDto(
                        rs.getLong("id"),
                        rs.getLong("user_id"),
                        rs.getString("name"),
                        rs.getString("title"),
                        rs.getString("contents"),
                        createdAt,
                        updatedAt

                );
            }
        };
    }

    // Schedule 객체로 매핑하는 RowMapper 구현
    private RowMapper<Schedule> memoRowMapperV2(){
        return new RowMapper<Schedule>() {

            @Override
            public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Schedule(
                        rs.getLong("id"),
                        rs.getLong("user_id"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("title"),
                        rs.getString("contents"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at").toLocalDateTime()

                );
            }
        };
    }
}

