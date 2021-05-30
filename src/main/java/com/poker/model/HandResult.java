package com.poker.model;

import com.poker.enumerable.Combination;

public class HandResult {
    private final Combination combination;

    private final double score;

    public HandResult(Combination combination, double score) {
        this.combination = combination;
        this.score = score;
    }

    public Combination getCombination() {
        return combination;
    }

    public double getScore() {
        return score;
    }
}
