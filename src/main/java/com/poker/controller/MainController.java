package com.poker.controller;

import com.poker.service.HandEvaluator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Size;

@RestController
@Validated
public class MainController {
    private HandEvaluator handEvaluator;

    MainController(HandEvaluator handEvaluator) {
        this.handEvaluator = handEvaluator;
    }

    // http://localhost:8080/result?player1Hand=1,2,3,4,5&player2Hand=2,3,4,5,6
    @GetMapping("/result")
    String result(
            @RequestParam @Size(min=5, max=5) String[] player1Hand,
            @RequestParam @Size(min=5, max=5) String[] player2Hand
    ) {
        return String.join(",", player1Hand) + " " + String.join(",", player2Hand);
    }
}
