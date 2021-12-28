package com.nahwasa.toy.expnewthings.backend.controller;

import com.nahwasa.toy.expnewthings.backend.dto.ResponseDTO;
import com.nahwasa.toy.expnewthings.backend.dto.TodoDTO;
import com.nahwasa.toy.expnewthings.backend.model.TodoEntity;
import com.nahwasa.toy.expnewthings.backend.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("todo")
public class TodoController {
    private static final String TEMP_USER_ID = "temporary-user";    // use until set spring security

    @Autowired
    TodoService service;

    @PostMapping
    public ResponseEntity<ResponseDTO> createTodo(@RequestBody TodoDTO dto) {
        try {
            TodoEntity entity = TodoDTO.toEntity(dto);
            entity.setId(null);
            entity.setUserId(TEMP_USER_ID);
            List<TodoEntity> entities = service.create(entity);

            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
            ResponseDTO<TodoDTO> response = new ResponseDTO<>();
            response.setData(dtos);

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            ResponseDTO<TodoDTO> response = new ResponseDTO<>();
            response.setError(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> retrieveTodoList() {
        List<TodoEntity> entities = service.retrieve(TEMP_USER_ID);
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        ResponseDTO<TodoDTO> responseDTO = new ResponseDTO<>();
        responseDTO.setData(dtos);

        return ResponseEntity.ok().body(responseDTO);
    }
}
