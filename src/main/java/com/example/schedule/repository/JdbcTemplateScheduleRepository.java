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

        schedule.setCreatedAt(LocalDateTime.now());  // createdAt 설정
        schedule.setUpdatedAt(LocalDateTime.now());  // updatedAt 설정
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        return new ScheduleResponseDto(key.longValue(), schedule.getName(), schedule.getTitle(), schedule.getContents(), schedule.getCreatedAt(), schedule.getUpdatedAt());
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
                    Timestamp timestamp1 = rs.getTimestamp("created_at");
                    LocalDateTime createdAt = timestamp1 != null ? timestamp1.toLocalDateTime() : null;
                    Timestamp timestamp = rs.getTimestamp("updated_at");
                    LocalDateTime updatedAt = timestamp != null ? timestamp.toLocalDateTime() : null;

                    return new ScheduleResponseDto(id, name, title, contents, createdAt, updatedAt);
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
                    Timestamp timestamp1 = rs.getTimestamp("created_at");
                    LocalDateTime createdAt = timestamp1 != null ? timestamp1.toLocalDateTime() : null;
                    Timestamp timestamp = rs.getTimestamp("updated_at");
                    LocalDateTime updatedAt = timestamp != null ? timestamp.toLocalDateTime() : null;
                    return new ScheduleResponseDto(id, name, title, contents, createdAt, updatedAt);
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
                    Timestamp timestamp1 = rs.getTimestamp("created_at");
                    LocalDateTime createdAt = timestamp1 != null ? timestamp1.toLocalDateTime() : null;
                    Timestamp timestamp = rs.getTimestamp("updated_at");
                    LocalDateTime updatedAt = timestamp != null ? timestamp.toLocalDateTime() : null;
                    return new ScheduleResponseDto(id, name, title, contents, createdAt, updatedAt);
                }
            }, updatedAt);
        }
        return List.of();
    }

    @Override
    public Schedule findScheduleByIdOrElseThrow(Long id) {
        List<Schedule> result = jdbcTemplate.query("select * from schedule where id =?", memoRowMapperV2(), id);
        return result.stream().findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id));
    }

    @Override
    public Schedule findScheduleByIdPwOrElseThrow(Long id, String password) {
        List<Schedule> result = jdbcTemplate.query("select * from schedule where id =? AND password =?", memoRowMapperV2(), id, password);
        return result.stream().findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id));
    }

    @Override
    public int updateSchedule(Long id, String password, String name, String title, String contents) {
        return jdbcTemplate.update("update schedule set name = ?, title = ?, contents = ? where id =?", name, title, contents, id);
    }

    @Override
    public int deleteSchedule(Long id, String password) {
        return jdbcTemplate.update("delete from schedule where id =? AND password =? ", id, password);
    }


    private RowMapper<Schedule> memoRowMapperV2(){
        return new RowMapper<Schedule>() {

            @Override
            public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Schedule(
                        rs.getLong("id"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("title"),
                        rs.getString("contents"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at").toLocalDateTime()

                );
            }
        };
    }
}

