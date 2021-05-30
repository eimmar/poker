package com.poker.enumerable;

public enum Combination {
    HighCard(14),
    Pair(1000),
    TwoPairs(2000),
    ThreeOfAKind(3000),
    Straight(4000),
    Flush(5000),
    FullHouse(6000),
    FourOfAKind(7000),
    StraightFlush(8000),
    RoyalFlush(9000);

    final int comboScore;

    Combination(int comboScore) {
        this.comboScore = comboScore;
    }

    public int getComboScore() {
        return comboScore;
    }
}
