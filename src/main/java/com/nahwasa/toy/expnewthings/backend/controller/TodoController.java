package com.nahwasa.toy.expnewthings.backend.controller;

import com.nahwasa.toy.expnewthings.backend.dto.ResponseDTO;
import com.nahwasa.toy.expnewthings.backend.dto.TodoDTO;
import com.nahwasa.toy.expnewthings.backend.model.TodoEntity;
import com.nahwasa.toy.expnewthings.backend.service.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Entity;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("todo")
public class TodoController {
    private static final String TEMP_USER_ID = "temporary-user";    // use until set spring security

    @Autowired
    TodoService service;

    @PostMapping
    public ResponseEntity<ResponseDTO> createTodo(@RequestBody TodoDTO dto) {
        log.info("createTodo called. param: " + dto.toString());

        try {
            TodoEntity entity = TodoDTO.toEntity(dto);
            entity.setId(null);
            entity.setUserId(TEMP_USER_ID);
            entity.setDone(false);
            List<TodoEntity> entities = service.create(entity);

            List<TodoDTO> dtos = convertToDtoListFromEntityList(entities);
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
        log.info("retrieveTodoList called.");

        List<TodoEntity> entities = service.retrieve(TEMP_USER_ID);
        List<TodoDTO> dtos = convertToDtoListFromEntityList(entities);

        ResponseDTO<TodoDTO> responseDTO = new ResponseDTO<>();
        responseDTO.setData(dtos);

        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping
    public ResponseEntity<ResponseDTO> updateTodo(@RequestBody TodoDTO dto) {
        log.info("updateTodo called. param: " + dto.toString());

        TodoEntity entity = TodoDTO.toEntity(dto);
        entity.setUserId(TEMP_USER_ID);

        List<TodoEntity> entities = service.update(entity);
        List<TodoDTO> dtos = convertToDtoListFromEntityList(entities);

        ResponseDTO<TodoDTO> responseDTO = new ResponseDTO<>();
        responseDTO.setData(dtos);

        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping
    public ResponseEntity<ResponseDTO> deleteTodo(@RequestBody TodoDTO dto) {
        log.info("deleteTodo called. param: " + dto.toString());

        TodoEntity entity = TodoDTO.toEntity(dto);
        entity.setUserId(TEMP_USER_ID);

        List<TodoEntity> entities = service.delete(entity);
        List<TodoDTO> dtos = convertToDtoListFromEntityList(entities);

        ResponseDTO<TodoDTO> responseDTO = ResponseDTO.<TodoDTO>builder().data(dtos).build();
        return ResponseEntity.ok().body(responseDTO);
    }

    private List<TodoDTO> convertToDtoListFromEntityList(List<TodoEntity> from) {
        return from.stream().map(TodoDTO::new).collect(Collectors.toList());
    }
}
