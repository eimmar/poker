package com.poker.model;

import com.poker.enumerable.Combination;

public class Hand {
    private final Combination combination;

    private final int score;

    public Hand(Combination combination, int score) {
        this.combination = combination;
        this.score = score;
    }

    public Combination getCombination() {
        return combination;
    }

    public int getScore() {
        return score;
    }
}
