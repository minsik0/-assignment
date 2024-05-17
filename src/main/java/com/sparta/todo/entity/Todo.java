package com.sparta.todo.entity;

import com.sparta.todo.dto.TodoRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor

public class Todo {
    private Long id;
    private String date;
    private String title;
    private String list;
    private String manager;
    private String password;


    public Todo(TodoRequestDto requestDto) {

        this.date = requestDto.getDate();
        this.title = requestDto.getTitle();
        this.list = requestDto.getList();
        this.manager = requestDto.getManager();
        this.password = requestDto.getPassword();
    }

    public void update(TodoRequestDto requestDto) {

        this.date = requestDto.getDate();
        this.title = requestDto.getTitle();
        this.list = requestDto.getList();
        this.manager = requestDto.getManager();
        this.password = requestDto.getPassword();

    }


}

