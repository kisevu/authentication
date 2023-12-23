package com.ameda.kevin.jwt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/protected")
    public ResponseEntity<?> resource(String name){
        return ResponseEntity.ok("Kevin Ameda is a java backend developer based in Kenya, almost relocating to " +
                "Berlin, Germany for work.");
    }
}
