package com.sparta.todo.dto;

import com.sparta.todo.entity.Todo;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TodoResponseDto {
    private Long id;
    private String date;
    private String title;
    private String list;
    private String manager;


    public TodoResponseDto(Todo todo) {
        this.id = todo.getId();
        this.date = todo.getDate();
        this.title = todo.getTitle();
        this.list = todo.getList();
        this.manager = todo.getManager();

    }

    public TodoResponseDto(Long id, String date, String title, String list, String manager) {
        this.id = id;
        this. date = date;
        this.title = title;
        this.list = list;
        this.manager = manager;
    }
}




