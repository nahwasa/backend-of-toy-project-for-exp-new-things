package com.nahwasa.toy.expnewthings.backend.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@Hidden
@RestController
@RequestMapping("test")
public class TestController {
    @Autowired
    private JavaMailSender javaMailSender;

    @GetMapping
    public String test() {
        return "Test Success";
    }

    @GetMapping("/path/{var}/end")
    public String testWithPath(@PathVariable String var) {
        return "Test Path : " + var + " Success";
    }

    @GetMapping("/sendMail")
    public ResponseEntity<String> sendMail() {
        ArrayList<String> mailList = new ArrayList<>();

        mailList.add("nahwasa@google.com");

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo((String[]) mailList.toArray(new String[mailList.size()]));
        msg.setSubject("여보세요");
        msg.setText("메일링 테스트");

        javaMailSender.send(msg);

        return ResponseEntity.ok().build();
    }
}
