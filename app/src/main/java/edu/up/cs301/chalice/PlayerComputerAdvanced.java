package edu.up.cs301.chalice;

import java.util.ArrayList;

import edu.up.cs301.game.GameFramework.GameComputerPlayer;
import edu.up.cs301.game.GameFramework.infoMessage.GameInfo;
import edu.up.cs301.game.GameFramework.utilities.Tickable;

import static edu.up.cs301.chalice.Card.COINS;
import static edu.up.cs301.chalice.Card.CUPS;
import static edu.up.cs301.chalice.Card.SWORDS;
import static edu.up.cs301.chalice.Card.WANDS;

public class PlayerComputerAdvanced extends PlayerComputerSimple implements Tickable {

    //a variable to keep track of the possible cards this
    //AI's opponents could have.
    //if any entry is false, the AI has deduced that that player
    //no longer has any cards of that type
    //for index purposes, it includes itself (but does not update)
    public final boolean[][] possibleSuitsDefault = {
          //   0    CUPS SWORDS COINS WANDS
            {false, true, true, true, true}, //player 1
            {false, true, true, true, true}, //player 2
            {false, true, true, true, true}, //player 3
            {false, true, true, true, true}, //player 4
        };
    boolean [][] possibleSuits;

    gameStateHearts localState;

    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public PlayerComputerAdvanced(String name) {
        super(name);
        possibleSuits = possibleSuitsDefault;
        localState = null;
    }

    @Override
    protected void receiveInfo(GameInfo info) {
        if (!(info instanceof gameStateHearts)) {
            return;
        }
        localState = new gameStateHearts((gameStateHearts)info);

        //if it's a new hand, reset strategy variables
        possibleSuits = possibleSuitsDefault;

        //not my turn
        if (playerNum != localState.getWhoTurn()) {
            return;
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //observe the cards that have been played
        //if a player has gone against the suit, note that they have no more cards in that suit
        CheckPlayedCards();

        //play the 2 of coins if I am the first in the hand
        if(localState.getTricksPlayed() == 0 && localState.getTrickCardsPlayed().size() ==0) {
            Card coins2 = new Card(2,COINS);
            int cardIndex=-1;
            for (Card card: getMyHand(localState)) {
                if(Card.sameCard(card, coins2)) {
                    cardIndex = getMyHand(localState).indexOf(card);
                }
            }
            game.sendAction((new ActionPlayCard(this, this.playerNum,getMyHand(localState).get(cardIndex))));
            return;
        }
//        //Trick / Hand – Normal
//        if(getMyRunningPoints(localState) < 16) {
//
//            if(localState.getTrickCardsPlayed().size() == 0) { //if going first
//
//                ArrayList<Card> smallSwordsCards = getCardsCompare(getMyHand(localState), SWORDS, 6, true);
//                if(smallSwordsCards.size() > 0) { //have sword < 6, flushing out the queen
//                    game.sendAction(new ActionPlayCard(this, this.playerNum, smallSwordsCards.get(0)));
//
//                } else if (other player doesn’t have a suit) {
//
//                    Play card w/ least val in a different suit;
//
//                } else {
//
//                    Play a random card w/least val.
//
//                }
//
//            } else if(cardInSuit) { //not going first, but have a card in-suit
//
//                if(points) {
//
//                    Play the highest card that is lower than the current “winning” card;
//
//                } else if (last) {
//
//                    Play highest card;
//
//                } else {
//
//                    Play the highest card that is lower than the current “winning” card;
//
//                }
//
//            } else { //not going first, no card in suit
//
//                if(has point cards && hearts are broken) {
//
//                    Play highest point card;
//
//                } else if (just one card of a suit) {
//
//                    Play it;
//
//                } else {
//
//                    Play highest card;
//
//                }
//
//            }
//
//        }
//        //Trick / Hand – Shooting the moon
//        else {
//
//            if(myTurn && goingFirst) {
//
//                if (has heart/cups ) {
//
//                    Lead highest heart/cups;
//
//                } else if(has spades/swords) {
//
//                    Play highest spade/swords;
//
//                } else {
//
//                    Lead highest card;
//
//                }
//
//            } else if(cardInSuit) {
//
//                if(points){
//
//                    Play highest cardInSuit;
//
//                } else {
//
//                    Play lowest cardInSuit;
//
//                }
//
//            } else {
//
//                Don’t play a point card, play a low card of another suit.
//
//            }
//
//        }

    }

    /**
     * A method to update the AI's estimates of who has what suit
     */
    private void CheckPlayedCards() {
        Card[] cardsPlayed = { //indices correspond w/ player index
                localState.getP1CardPlayed(),
                localState.getP2CardPlayed(),
                localState.getP3CardPlayed(),
                localState.getP4CardPlayed()
        };
        for (int i = 0; i < cardsPlayed.length; i++){
            if (playerNum == i){
                continue;
            }
            if (cardsPlayed[i] != null){
                if (cardsPlayed[i].getCardSuit() != localState.getSuitLed()){
                    //this player has run out of this suit.
                    possibleSuits[i][localState.getSuitLed()] = false;
                }
            }
        }
    }

    /**
     * A method to get cards according to a suit, a value, and whether
     * you want the cards <= value or >= value.
     * @param cards     the list to search
     * @param suit      the desired suit
     * @param value     the desired value
     * @param lessThan  the desired comparison
     * @return          a list of the cards
     */
    public ArrayList<Card> getCardsCompare(ArrayList<Card> cards, int suit,
                                           int value, boolean lessThan){
        ArrayList<Card> searchedCards = new ArrayList<>();
        for (int i = 0; i < cards.size(); i++){
            Card currentCard = cards.get(i);
            if (currentCard.getCardSuit() == suit){
                if (lessThan){
                    if (currentCard.getCardVal() <= value){
                        searchedCards.add(currentCard);
                    }
                } else {
                    if (currentCard.getCardVal() >= value){
                        searchedCards.add(currentCard);
                    }
                }
            }
        }
        return searchedCards;
    }
}
