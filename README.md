# Poker hand evaluator
Sample program to evaluate 2 poker hands and determine the winning pair.

## Usage
Each card description consists of 2 parts, suit and rank (eg, H10 is 10 of hearts, DA is Ace of diamonds).  
Each hand consists of 5 cards.

To evaluate two hands of cards one must make a GET request to `/result` with `player1Hand` and `player2Hand` 
parameters as a list of each player's cards.  
For example `/result?player1Hand=HA,H2,H3,H4,H5&player2Hand=H2,H3,H4,H5,H6`  
The result is then printed out in on the screen as a string.

## Available Suits
            "H" - Hearts
            "S" - Spades
            "D" - Diamonds
            "C" - Clubs

## Available Ranks
            "2" - Two
            "3" - Three
            "4" - Four
            "5" - Five
            "6" - Six
            "7" - Seven
            "8" - Eight
            "9" - Nine
            "10" - Ten
            "J" - Joker
            "Q" - Queen
            "K" - King
            "A" - Ace
