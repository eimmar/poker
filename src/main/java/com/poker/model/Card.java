package com.poker.model;

import java.util.*;

import static java.util.Map.entry;

public class Card {
    final static Map<String, Integer> AvailableRanks = Map.ofEntries(
            entry("2", 2),
            entry("3", 3),
            entry("4", 4),
            entry("5", 5),
            entry("6", 6),
            entry("7", 7),
            entry("8", 8),
            entry("9", 9),
            entry("10", 10),
            entry("J", 11),
            entry("Q", 12),
            entry("K", 13),
            entry("A", 14)
    );

    final static List<String> AvailableSuits = Arrays.asList(
            "H", // Hearts
            "S", // Spades
            "D", // Diamonds
            "C" // Clubs
    );

    private final String rank;
    private final String suit;
    private final Integer score;

    public Card(String rank, String suit) {
        if (!AvailableSuits.contains(suit) || !AvailableRanks.containsKey(rank))
            throw new IllegalArgumentException("Card suit and/or rank is incorrect.");

        this.rank = rank;
        this.suit = suit;
        this.score = AvailableRanks.get(rank);
    }

    public String getRank() {
        return rank;
    }

    public String getSuit() {
        return suit;
    }

    public Integer getScore() {
        return score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;

        return Objects.equals(rank, card.rank);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank);
    }
}
