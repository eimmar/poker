package com.poker.controller;

import com.poker.enumerable.Combination;
import com.poker.model.Card;
import com.poker.service.HandEvaluator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Size;
import java.util.Arrays;

@RestController
@Validated
public class MainController {
    private final HandEvaluator handEvaluator;

    MainController(HandEvaluator handEvaluator) {
        this.handEvaluator = handEvaluator;
    }

    @GetMapping("/result")
    String result(
            @RequestParam @Size(min=5, max=5) Card[] player1Hand,
            @RequestParam @Size(min=5, max=5) Card[] player2Hand
    ) {
        var player1Result = handEvaluator.evaluate(Arrays.asList(player1Hand));
        var player2Result = handEvaluator.evaluate(Arrays.asList(player2Hand));

        if (player1Result.getScore() == player2Result.getScore())
            return String.format("Both players had even hands with %s.", player1Result.getCombination());

        boolean player1Won = player1Result.getScore() > player2Result.getScore();
        boolean victoryByHighCard = player1Result.getCombination().equals(player2Result.getCombination());

        return String.format(
                "Player %s wins with %s%s. Player %s had %s.",
                player1Won ? "1" : "2",
                player1Won ? player1Result.getCombination() : player2Result.getCombination(),
                victoryByHighCard ? String.format(" and %s", Combination.HighCard) : "",
                player1Won ? "2" : "1",
                player1Won ? player2Result.getCombination() : player1Result.getCombination()
        );
    }
}
