package com.szd.boxgo.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirements
public class Greeting {
    @GetMapping("/hi")
    String greeting() {
        return "Welcome To JoinGO!";
    }
}
