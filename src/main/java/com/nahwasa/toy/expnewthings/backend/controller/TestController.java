package com.nahwasa.toy.expnewthings.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    @GetMapping
    public String testRoot() {
        return "Test Success";
    }

    @GetMapping("/path/{var}/end")
    public String testWithPath(@PathVariable String var) {
        return "Test Path : " + var + " Success";
    }
}
