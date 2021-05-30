package com.poker.enumerable;

public enum Combination {
    HighCard(100),
    Pair(10000),
    TwoPairs(20000),
    ThreeOfAKind(30000),
    Straight(40000),
    Flush(50000),
    FullHouse(60000),
    FourOfAKind(70000),
    StraightFlush(80000),
    RoyalFlush(90000);

    final int comboScore;

    Combination(int comboScore) {
        this.comboScore = comboScore;
    }

    public int getComboScore() {
        return comboScore;
    }
}
