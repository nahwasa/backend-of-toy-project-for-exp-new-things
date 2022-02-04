package com.nahwasa.toy.expnewthings.backend.controller;

import com.nahwasa.toy.expnewthings.backend.dto.ResponseDTO;
import com.nahwasa.toy.expnewthings.backend.dto.TodoDTO;
import com.nahwasa.toy.expnewthings.backend.model.TodoEntity;
import com.nahwasa.toy.expnewthings.backend.service.TodoService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("todo")
public class TodoController {
    @Autowired
    TodoService service;

    @GetMapping("/test")
    public ResponseEntity<?> testTodo() {
        List<String> list = Arrays.asList("ok");
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> retrieveTodoList(@Parameter(hidden = true) @AuthenticationPrincipal String userId) {
        // '@Parameter(hidden = true)' 부분은 스웨거 때문에 추가함. 실제론 SwaggerConfig 쪽에서 addAnnotationsToIgnore로 처리했지만,
        // 차후 필요할 것 같아 작성해둠. 이 때 스웨거가 없다면 @AuthenticationPrincipal만 있으면 됨.

        List<TodoEntity> entities = service.retrieve(userId);
        List<TodoDTO> dtos = convertToDtoListFromEntityList(entities);

        ResponseDTO<TodoDTO> responseDTO = new ResponseDTO<>();
        responseDTO.setData(dtos);

        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> createTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto) {
        try {
            TodoEntity entity = TodoDTO.toEntity(dto);
            entity.setId(null);
            entity.setUserId(userId);
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

    @PutMapping
    public ResponseEntity<ResponseDTO> updateTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto) {
        TodoEntity entity = TodoDTO.toEntity(dto);
        entity.setUserId(userId);

        List<TodoEntity> entities = service.update(entity);
        List<TodoDTO> dtos = convertToDtoListFromEntityList(entities);

        ResponseDTO<TodoDTO> responseDTO = new ResponseDTO<>();
        responseDTO.setData(dtos);

        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping
    public ResponseEntity<ResponseDTO> deleteTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto) {
        TodoEntity entity = TodoDTO.toEntity(dto);
        entity.setUserId(userId);

        List<TodoEntity> entities = service.delete(entity);
        List<TodoDTO> dtos = convertToDtoListFromEntityList(entities);

        ResponseDTO<TodoDTO> responseDTO = ResponseDTO.<TodoDTO>builder().data(dtos).build();
        return ResponseEntity.ok().body(responseDTO);
    }

    private List<TodoDTO> convertToDtoListFromEntityList(List<TodoEntity> from) {
        return from.stream().map(TodoDTO::new).collect(Collectors.toList());
    }
}
