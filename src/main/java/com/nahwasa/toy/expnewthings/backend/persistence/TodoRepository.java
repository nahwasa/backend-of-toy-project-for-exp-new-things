package com.nahwasa.toy.expnewthings.backend.persistence;

import com.nahwasa.toy.expnewthings.backend.model.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, String> {
    List<TodoEntity> findByUserId(String userId);
}
