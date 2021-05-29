package com.poker.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class MainController {
    @GetMapping("/result")
    String result(@RequestParam @Min String[] player1Hand, @RequestParam String[] player2Hand) {

        return "test";
    }
}
