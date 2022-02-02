package com.nahwasa.toy.expnewthings.backend.dto;

import com.nahwasa.toy.expnewthings.backend.model.TodoEntity;
import com.nahwasa.toy.expnewthings.backend.model.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String token;
    private String email;
    private String username;
    private String password;
    private String id;

    public UserDTO(final UserEntity entity) {
        this.id = entity.getId();
        this.email = entity.getEmail();
        this.username = entity.getUsername();
    }

    public static UserEntity toEntity(final UserDTO dto) {
        return UserEntity.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())    // entity -> dto 일땐 있으면 안됨.
                .build();
    }
}
