package com.poker.service;

import com.poker.model.Card;
import com.poker.model.HandResult;
import com.poker.enumerable.Combination;
import org.springframework.stereotype.Component;
import java.util.*;

import static com.poker.service.HandEvaluator.*;
import static java.util.stream.Collectors.*;

@Component
public class HandEvaluator {
    static int calculateStraightScore(List<Card> straightFlush, Combination combination) {
        int score = straightFlush.stream().mapToInt(Card::getScore).sum();

        if (straightFlush.get(0).getRank().equals("A") && straightFlush.get(4).getRank().equals("2"))
            score = score - Card.RankScore.get("A") + 1;

        return score + combination.getComboScore();
    }

    static boolean isStraight(List<Card> hand) {
        for (int i = 0; i < hand.size() - 1; i++)
            if (hand.get(i).getScore() - hand.get(i + 1).getScore() != 1)
                return false;

        return true;
    }

    static boolean areAllTheSameRank(Card value, Card... values) {
        for (Card c: values)
            if (!value.getRank().equals(c.getRank()))
                return false;

        return true;
    }

    private final List<PokerCombination> combinations = Arrays.asList(
            new RoyalFlush(),
            new StraightFlush(),
            new Flush(),
            new Straight(),
            new FourOfAKind(),
            new FullHouse(),
            new ThreeOfAKind(),
            new TwoPairs(),
            new Pair()
    );

    public HandResult evaluate(List<Card> hand) {
        validate(hand);
        hand.sort(Comparator.comparing(Card::getScore));

        for (var combination : combinations)
            if (combination.getResult(hand) != null)
                return combination.getResult(hand);

        return findHighCard(hand);
    }

    private void validate(List<Card> hand) {
        var pairsBySuit = hand
                .stream()
                .collect(groupingBy(Card::getSuit));
        var distinctCards = hand.stream().distinct().collect(toList());

        if (distinctCards.size() != hand.size())
            throw new IllegalArgumentException("Hand cannot have duplicating cards.");

        if (hand.size() != 5)
            throw new IllegalArgumentException("Hand must contain exactly 5 cards.");

        if (pairsBySuit.size() == 1)
            throw new IllegalArgumentException("Hand cannot contain more than 4 cards of the same rank.");
    }

    private HandResult findHighCard(List<Card> hand) {
        int multiplier = hand.size() - 1;
        int maxCardScore = Card.RankScore.get("A");
        int score = 0;

        for (int i = 0; i < multiplier; i++)
            score += hand.get(i).getScore() + (maxCardScore * multiplier - i);

        return new HandResult(Combination.HighCard, score);
    }
}

interface PokerCombination {
    HandResult getResult(List<Card> hand);
}

class RoyalFlush implements PokerCombination {
    @Override
    public HandResult getResult(List<Card> hand) {
        var pairsBySuit = hand
                .stream()
                .collect(groupingBy(Card::getSuit));

        if (pairsBySuit.size() == 1 &&
                hand.get(0).getRank().equals("A") &&
                hand.get(1).getRank().equals("K") &&
                hand.get(2).getRank().equals("Q") &&
                hand.get(3).getRank().equals("J") &&
                hand.get(4).getRank().equals("10"))
            return new HandResult(Combination.RoyalFlush, Combination.RoyalFlush.getComboScore());

        return null;
    }
}

class StraightFlush implements PokerCombination {
    @Override
    public HandResult getResult(List<Card> hand) {
        var pairsBySuit = hand
                .stream()
                .collect(groupingBy(Card::getSuit));

        if (pairsBySuit.size() != 1)
            return null;

        if (isStraight(hand) && (hand.get(0).getRank().equals("A") &&
                hand.get(1).getRank().equals("5") &&
                hand.get(2).getRank().equals("4") &&
                hand.get(3).getRank().equals("3") &&
                hand.get(4).getRank().equals("2")))
            return new HandResult(Combination.StraightFlush, calculateStraightScore(hand, Combination.StraightFlush));

        return null;
    }
}

class Straight implements PokerCombination {
    @Override
    public HandResult getResult(List<Card> hand) {
        if (isStraight(hand) && (hand.get(0).getRank().equals("A") &&
                hand.get(1).getRank().equals("5") &&
                hand.get(2).getRank().equals("4") &&
                hand.get(3).getRank().equals("3") &&
                hand.get(4).getRank().equals("2")))
            return new HandResult(Combination.Straight, calculateStraightScore(hand, Combination.Straight));

        return null;
    }
}

class Flush implements PokerCombination {
    @Override
    public HandResult getResult(List<Card> hand) {
        var pairsBySuit = hand
                .stream()
                .collect(groupingBy(Card::getSuit));

        if (pairsBySuit.size() == 1)
            return new HandResult(
                    Combination.Flush,
                    hand.stream().mapToInt(Card::getScore).sum() + Combination.Flush.getComboScore()
            );

        return null;
    }
}

class FourOfAKind implements PokerCombination {
    @Override
    public HandResult getResult(List<Card> hand) {
        if (areAllTheSameRank(hand.get(0), hand.get(1), hand.get(2), hand.get(3)))
            return new HandResult(Combination.FourOfAKind, calculateFourOfAKindScore(hand.get(0), hand.get(4)));

        if (areAllTheSameRank(hand.get(1), hand.get(2), hand.get(3), hand.get(4)))
            return new HandResult(Combination.FourOfAKind, calculateFourOfAKindScore(hand.get(1), hand.get(0)));

        return null;
    }

    private int calculateFourOfAKindScore(Card comboCard, Card highCard) {
        return (comboCard.getScore() + Card.RankScore.get("A")) * 4
                + highCard.getScore()
                + Combination.FourOfAKind.getComboScore();
    }
}

class FullHouse implements PokerCombination {
    @Override
    public HandResult getResult(List<Card> hand) {
        if (areAllTheSameRank(hand.get(0), hand.get(1), hand.get(2)) && areAllTheSameRank(hand.get(3), hand.get(4)))
            return new HandResult(Combination.FullHouse, calculateFullHouseScore(hand.get(0), hand.get(3)));

        if (areAllTheSameRank(hand.get(0), hand.get(1)) && areAllTheSameRank(hand.get(2), hand.get(3), hand.get(4)))
            return new HandResult(Combination.FullHouse, calculateFullHouseScore(hand.get(2), hand.get(0)));

        return null;
    }

    private int calculateFullHouseScore(Card mainComboCard, Card secondaryComboCard) {
        return (mainComboCard.getScore() + Card.RankScore.get("A")) * 3
                + secondaryComboCard.getScore() * 2
                + Combination.FullHouse.getComboScore();
    }
}

class ThreeOfAKind implements PokerCombination {
    @Override
    public HandResult getResult(List<Card> hand) {
        if (areAllTheSameRank(hand.get(0), hand.get(1), hand.get(2)))
            return new HandResult(
                    Combination.ThreeOfAKind,
                    calculateThreeOfAKindScore(hand.get(0), hand.get(3), hand.get(4))
            );

        if (areAllTheSameRank(hand.get(1), hand.get(2), hand.get(3)))
            return new HandResult(
                    Combination.ThreeOfAKind,
                    calculateThreeOfAKindScore(hand.get(1), hand.get(0), hand.get(4))
            );

        if (areAllTheSameRank(hand.get(2), hand.get(3), hand.get(4)))
            return new HandResult(
                    Combination.ThreeOfAKind,
                    calculateThreeOfAKindScore(hand.get(2), hand.get(0), hand.get(1))
            );

        return null;
    }

    private int calculateThreeOfAKindScore(Card comboCard, Card highCard, Card secondHighCard) {
        int maxCardScore = Card.RankScore.get("A");

        return (comboCard.getScore() + maxCardScore * 2) * 3
                + highCard.getScore() + maxCardScore
                + secondHighCard.getScore()
                + Combination.ThreeOfAKind.getComboScore();
    }
}

class TwoPairs implements PokerCombination {
    @Override
    public HandResult getResult(List<Card> hand) {
        if (areAllTheSameRank(hand.get(0), hand.get(1)) && areAllTheSameRank(hand.get(2), hand.get(3)))
            return new HandResult(
                    Combination.TwoPairs,
                    calculateTwoPairsScore(hand.get(0), hand.get(2), hand.get(4))
            );

        if (areAllTheSameRank(hand.get(1), hand.get(2)) && areAllTheSameRank(hand.get(3), hand.get(4)))
            return new HandResult(
                    Combination.TwoPairs,
                    calculateTwoPairsScore(hand.get(1), hand.get(3), hand.get(0))
            );

        if (areAllTheSameRank(hand.get(0), hand.get(1)) && areAllTheSameRank(hand.get(3), hand.get(4)))
            return new HandResult(
                    Combination.TwoPairs,
                    calculateTwoPairsScore(hand.get(0), hand.get(3), hand.get(2))
            );

        return null;
    }

    private int calculateTwoPairsScore(Card pair1Card, Card pair2Card, Card highCard) {
        int maxCardScore = Card.RankScore.get("A");

        return (pair1Card.getScore() + maxCardScore) * 2
                + (pair2Card.getScore() + maxCardScore) * 2
                + highCard.getScore()
                + Combination.TwoPairs.getComboScore();
    }
}

class Pair implements PokerCombination {
    @Override
    public HandResult getResult(List<Card> hand) {
        for (int i = 0; i < hand.size() - 1; i++)
            if (areAllTheSameRank(hand.get(i), hand.get(i + 1))) {
                Card pairCard = hand.get(i);
                hand.removeAll(Arrays.asList(hand.get(i), hand.get(i + 1)));

                return new HandResult(Combination.Pair, calculatePairScore(pairCard, hand));
            }

        return null;
    }

    private int calculatePairScore(Card pairCard, List<Card> highCards) {
        int multiplier = highCards.size();
        int maxCardScore = Card.RankScore.get("A");
        int pairScore = (pairCard.getScore() + maxCardScore * multiplier) * 2 + Combination.Pair.getComboScore();

        for (int i = 0; i < multiplier; i++)
            pairScore += highCards.get(i).getScore() + (maxCardScore * multiplier - i - 1);

        return pairScore;
    }
}
