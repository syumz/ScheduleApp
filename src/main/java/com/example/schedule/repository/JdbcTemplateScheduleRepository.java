package com.example.schedule.repository;

import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcTemplateScheduleRepository implements ScheduleRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateScheduleRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public ScheduleResponseDto saveSchedule(Schedule schedule) {

        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("schedule").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", schedule.getName());
        parameters.put("password", schedule.getPassword());
        parameters.put("title", schedule.getTitle());
        parameters.put("contents", schedule.getContents());
        parameters.put("created_at", LocalDateTime.now());
        parameters.put("updated_at", LocalDateTime.now());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        return new ScheduleResponseDto(key.longValue(), schedule.getName(), schedule.getTitle(), schedule.getContents());
    }

    @Override
    public List<ScheduleResponseDto> findAllSchedules(String name, LocalDate updatedAt) {
        String sql = "";

        if (updatedAt != null && name != null) {
            sql = "SELECT * FROM schedule WHERE DATE_FORMAT(updated_at, '%Y-%m-%d') = ? AND name =? ORDER BY updated_at DESC";


            return jdbcTemplate.query(sql, new RowMapper<ScheduleResponseDto>() {
                @Override
                public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                    // SQL 의 결과로 받아온 Memo 데이터들을 MemoResponseDto 타입으로 변환해줄 메서드
                    Long id = rs.getLong("id");
                    String name = rs.getString("name");
                    String title = rs.getString("title");
                    String contents = rs.getString("contents");
                    return new ScheduleResponseDto(id, name, title, contents);
                }
            }, updatedAt, name);

        } else if (updatedAt == null && name != null) {
            sql = "SELECT * FROM schedule WHERE name =? ORDER BY updated_at DESC";


            return jdbcTemplate.query(sql, new RowMapper<ScheduleResponseDto>() {
                @Override
                public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                    // SQL 의 결과로 받아온 Memo 데이터들을 MemoResponseDto 타입으로 변환해줄 메서드
                    Long id = rs.getLong("id");
                    String name = rs.getString("name");
                    String title = rs.getString("title");
                    String contents = rs.getString("contents");
                    return new ScheduleResponseDto(id, name, title, contents);
                }
            }, name);
        } else if(updatedAt != null && name == null){
            sql = "SELECT * FROM schedule WHERE name =? ORDER BY updated_at DESC";


            return jdbcTemplate.query(sql, new RowMapper<ScheduleResponseDto>() {
                @Override
                public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                    // SQL 의 결과로 받아온 Memo 데이터들을 MemoResponseDto 타입으로 변환해줄 메서드
                    Long id = rs.getLong("id");
                    String name = rs.getString("name");
                    String title = rs.getString("title");
                    String contents = rs.getString("contents");
                    return new ScheduleResponseDto(id, name, title, contents);
                }
            }, updatedAt);
        }
        return List.of();
    }
}

