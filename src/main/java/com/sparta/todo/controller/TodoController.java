package com.sparta.todo.controller;


import com.sparta.todo.dto.TodoRequestDto;
import com.sparta.todo.dto.TodoResponseDto;
import com.sparta.todo.entity.Todo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import java.util.List;


///api" 하위 경로에 매핑
@RestController
@RequestMapping("/api")
public class TodoController {

    private final JdbcTemplate jdbcTemplate;

    public TodoController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    @PostMapping("/todos")
    public TodoResponseDto createTodo(@RequestBody TodoRequestDto requestDto) {
        // RequestDto -> Entity
        Todo todo = new Todo(requestDto);


        // DB 저장
        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체

        String sql = "INSERT INTO todo (date, title, list, manager, password) VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update( con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, todo.getDate());
                    preparedStatement.setString(2, todo.getTitle());
                    preparedStatement.setString(3, todo.getList());
                    preparedStatement.setString(4, todo.getManager());
                    preparedStatement.setString(5, todo.getPassword());


                    return preparedStatement;
                },
                keyHolder);

        // DB Insert 후 받아온 기본키 확인
        Long id = keyHolder.getKey().longValue();
        todo.setId(id);

        // Entity -> ResponseDto
        TodoResponseDto todoResponseDto = new TodoResponseDto(todo);

        return todoResponseDto;
    }


    @GetMapping("/todos")
    public List<TodoResponseDto> getTodos() {
        String sql = "SELECT * FROM todo order by date desc ";

        return jdbcTemplate.query(sql, new RowMapper<TodoResponseDto>() {
            @Override
            public TodoResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                // SQL 의 결과로 받아온 할일 데이터들을 TodoResponseDto 타입으로 변환해줄 메서드
                Long id = rs.getLong("id");
                String date = rs.getString("date");
                String title = rs.getString("title");
                String list = rs.getString("list");
                String manager = rs.getString("manager");
                return new TodoResponseDto(id, date, title, list, manager);
            }
        });
    }

    @PutMapping("/todos/{id}")
    public Long updateTodo(@PathVariable Long id, @RequestBody TodoRequestDto requestDto) {
        //DB에 존재하는지 확인
        Todo todo = findById(id);
        if(todo != null) {
            // 할일 수정
            if (!todo.getPassword().equals(requestDto.getPassword())) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
            String sql = "UPDATE todo SET date = ?, title = ?, list = ?, manager = ? WHERE id = ?";
            jdbcTemplate.update(sql, requestDto.getDate(), requestDto.getTitle(), requestDto.getList(), requestDto.getManager(), id);

            // 수정된 일정의 정보 반환
            Todo updatedTodo = findById(id);
            return id;

        } else {
            throw new IllegalArgumentException("선택한 할일은 존재하지 않습니다.");
        }
    }

    @DeleteMapping("/todos/{id}")
    public Long deleteTodo(@PathVariable Long id) {
        //DB에 존재하는지 확인
        Todo todo = findById(id);
        if(todo != null) {
            if (!todo.getPassword().equals(todo.getPassword())) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
            // 할일 삭제
            String sql = "DELETE FROM todo WHERE id = ?";
            jdbcTemplate.update(sql, id);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 할일은 존재하지 않습니다.");
        }
    }



    private Todo findById(Long id) {
        // DB 조회
        String sql = "SELECT * FROM todo WHERE id = ?";

        return jdbcTemplate.query(sql, resultSet -> {
            if(resultSet.next()) {
                Todo todo = new Todo();
                todo.setId(resultSet.getLong("id"));
                todo.setDate(resultSet.getString("date"));
                todo.setTitle(resultSet.getString("title"));
                todo.setList(resultSet.getString("list"));
                todo.setManager(resultSet.getString("manager"));
                todo.setPassword(resultSet.getString("password"));

                return todo;
            } else {
                return null;
            }
        }, id);
    }
}




