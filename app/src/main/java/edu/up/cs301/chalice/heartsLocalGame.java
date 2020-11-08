/**
 * Hearts Local Game class
 * contains all gameplay functions for the Hearts (Chalice) game
 *
 * @version October 18, 2020
 * @author  Alex Junkins, Malia Lundstrom, Chloe Campbell, Addison Raak
 */

package edu.up.cs301.chalice;


import java.util.ArrayList;

import static edu.up.cs301.chalice.Card.CUPS;
import static edu.up.cs301.chalice.Card.SWORDS;

public class heartsLocalGame {

    private gameStateHearts state;

    /**
     * Blank constructor
     */
    public heartsLocalGame() {
        state = new gameStateHearts();
    }

    /**
     * Deep-copy constructor
     * @param localGame game to be copied
     */
    public heartsLocalGame(heartsLocalGame localGame) {
        state = new gameStateHearts(localGame.state);
    }

    /**
     * A tester method to randomize the game state's values
     * CAUTION: THIS WILL DESTROY ALL GAME DATA IF USED IN NORMAL GAME
     */
    public void Randomize(){
        state.Randomize();
    }

    boolean quit() {
        //you can always quit!!
        return true;
    }

    /**
     * A method to check if a card belongs to the leading suit
     *
     * @param card  card to check
     * @return      In suit
     */
    boolean isInSuit (Card card) {
        int suit = card.getCardSuit();
        if (state.getSuitLed() == suit) {
            return true;
        }
        return false;
    }

    /**
     * A method to retrieve the in-suit cards that have been played
     * @return  ArrayList of played cards in suit
     */
    ArrayList<Card> CardsInSuit() {
        ArrayList<Card> cardsInSuit = new ArrayList<>();
        if(isInSuit(state.getSelectedCard())) {
            cardsInSuit.add(state.getP1CardPlayed());
        }
        if(isInSuit(state.getP2CardPlayed())) {
            cardsInSuit.add(state.getP2CardPlayed());
        }
        if(isInSuit(state.getP3CardPlayed())) {
            cardsInSuit.add(state.getP3CardPlayed());
        }
        if(isInSuit(state.getP4CardPlayed())) {
            cardsInSuit.add(state.getP4CardPlayed());
        }
        return cardsInSuit;
    }

//Can this be automatic?
   /* boolean collectTrick () {
        //if suit of card played == suitLed
        int highVal=0;
        Card highCard = new Card(0, suitLed);
        for (Card card : CardsInSuit()) {
            if (highVal > card.getCardVal()) {
                highCard = card;
            }
        }
        if(highCard == p1CardPlayed){
            return true;
        }
        return false;
    }*/

    /**
     * A method to select a card
     *
     * @param card  the card to select
     * @return      success status
     */
    boolean selectCard(Card card) {
        if(state.getWhoTurn() == 1) {
            state.setSelectedCard(card);
            return true;
        }
        else { return false; }
    }

    /**
     * A method to check if a card is a valid play, given the current state of the game
     *
     * @param card      the card to check
     * @param p1Hand    the rest of the hand
     * @return          legality status of the card
     */
    public boolean isCardValid(ArrayList<Card> p1Hand, Card card) {
        if(isInSuit(card)) {
            return true;
        }
        else {
            for (Card c :p1Hand) {
                if (c.getCardSuit() == state.getSuitLed()) {
                    return false;
                }
            }
            if (state.isHeartsBroken()) {
                return true;
            } else {
                if (card.getCardSuit() == CUPS || (card.getCardSuit() == SWORDS && card.getCardSuit() == 12)) {
                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    /**
     * A method to check if it is legal to play the selected card in a player's hand
     *
     * @return  legality status
     */
    boolean playCard() {
        if(state.getWhoTurn() == 1 && isCardValid(state.getP1Hand(),state.getSelectedCard())) {
            state.getCardsPlayed().add(state.getSelectedCard());
            state.getP1Hand().remove(state.getSelectedCard());
            return true;
        }
        return false;
    }

    /**
     * A method to handle the passing-card phase of play
     * (may be modified to handle just one passing of a card)
     *
     * @return  true if successful, false if temporary hand is empty.
     */
    boolean passCard(){
        ArrayList<Card> tempHand = new ArrayList<>();
        while(state.getCardsPassed() < 4 && state.getWhoTurn() == 1 && state.getTricksPlayed() == 0) {
            if(state.getSelectedCard() !=null) {
                state.getP1Hand().remove(state.getSelectedCard());
                tempHand.add(state.getSelectedCard());
            }
            state.setCardsPassed(state.getCardsPassed()+1);
        }
        for(Card c : tempHand){
            state.getP2Hand().add(c);
            return true;
        }
        //returns false if temp hand is empty and they dont select cards.
        if(tempHand == null) {
            return false;
        }
        return false;
    }

    /**
     * this method prints the values of all of the variables in the game state by saving them all
     * to a String.
     *
     * @return  Current score of all players, running score of all players, number of cards in
     *          hand, references for suit numbers
     */
    @Override
    public String toString() {

        //sets the ArrayList to a String to be returned with the rest of the info
        String workingHandString = "";
        for(Card tempCard : state.getP1Hand()) {
            workingHandString = workingHandString + "Suit: " + tempCard.cardSuit + "\tValue: " + tempCard.cardVal + "\n";
        }
        state.setP1HandString(workingHandString);
        workingHandString = "";

        for(Card tempCard : state.getP2Hand()) {
            workingHandString = workingHandString + "Suit: " + tempCard.cardSuit + "\tValue: " + tempCard.cardVal + "\n";
        }
        state.setP2HandString(workingHandString);
        workingHandString = "";

        for(Card tempCard : state.getP3Hand()) {
            workingHandString = workingHandString + "Suit: " + tempCard.cardSuit + "\tValue: " + tempCard.cardVal + "\n";
        }
        state.setP3HandString(workingHandString);
        workingHandString = "";

        for(Card tempCard : state.getP4Hand()) {
            workingHandString = workingHandString + "Suit: " + tempCard.cardSuit + "\tValue: " + tempCard.cardVal + "\n";
        }
        state.setP4HandString(workingHandString);
        workingHandString = "";


        return //prints the CURRENT score of the players to the Logcat Info window
                "Player 1 Current Points: " + state.getP1numCurrentPoints() + "\n"
                        + "Player 2 Current Points: " + state.getP2numCurrentPoints() + "\n"
                        + "Player 3 Current Points: " + state.getP3numCurrentPoints() + "\n"
                        + "Player 4 Current Points: " + state.getP4numCurrentPoints() + "\n"

                        //prints the RUNNING score of the players to the Logcat Info window
                        + "Player 1 Running Points: " + state.getP1RunningPoints() + "\n"
                        + "Player 2 Running Points: " + state.getP2RunningPoints() + "\n"
                        + "Player 3 Running Points: " + state.getP3RunningPoints() + "\n"
                        + "Player 4 Running Points: " + state.getP4RunningPoints() + "\n"

                        //prints numCards
                        + "Number of Cards in Hands: " + state.getNumCards() + "\n"

                        //prints reference for what the suit numbers mean
                        + "1 = Cups\n2 = swords\n3 = coins\n4 = wands" + "\n"

                        //prints the hands of each player
                        + "Player 1 Hand:\n" + state.getP1HandString() + "\n"
                        + "Player 2 Hand:\n" + state.getP2HandString() + "\n"
                        + "Player 3 Hand:\n" + state.getP3HandString() + "\n"
                        + "Player 4 Hand:\n" + state.getP4HandString() + "\n" + " \n";
    }
}
