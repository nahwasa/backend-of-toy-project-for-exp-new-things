package com.nahwasa.toy.expnewthings.backend.controller;

import com.nahwasa.toy.expnewthings.backend.dto.ResponseDTO;
import com.nahwasa.toy.expnewthings.backend.dto.UserDTO;
import com.nahwasa.toy.expnewthings.backend.model.UserEntity;
import com.nahwasa.toy.expnewthings.backend.security.TokenProvider;
import com.nahwasa.toy.expnewthings.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenProvider tokenProvider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        try {
            UserEntity user = UserDTO.toEntity(userDTO);
            UserEntity registeredUser = userService.create(user);
            UserDTO responseUserDTO = new UserDTO(registeredUser);

            return ResponseEntity.ok().body(responseUserDTO);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {

        UserEntity user = userService.getByCredentials(userDTO.getEmail(), userDTO.getPassword(), passwordEncoder);

        if (user == null) {
            ResponseDTO responseDTO = ResponseDTO.builder().error("Login failed.").build();
            return ResponseEntity.badRequest().body(responseDTO);
        }

        final String token = tokenProvider.create(user);
        final UserDTO responseUserDTO = new UserDTO(user, token);
        return ResponseEntity.ok().body(responseUserDTO);
    }
}