/**
 * Hearts Local Game class
 * contains all gameplay functions for the Hearts (Chalice) game
 *
 * @version October 18, 2020
 * @author  Alex Junkins, Malia Lundstrom, Chloe Campbell, Addison Raak
 */

package edu.up.cs301.chalice;


import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;

import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.LocalGame;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;

import static edu.up.cs301.chalice.Card.CUPS;
import static edu.up.cs301.chalice.Card.SWORDS;

public class heartsLocalGame extends LocalGame {

    private gameStateHearts state;

    /**
     * Blank constructor
     */
    public heartsLocalGame() {
        state = new gameStateHearts();
        state.dealCards();
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

    /**
     * Determines which player is meant to collect the trick
     *
     * @return the index of the player who takes the trick
     */
    int collectTrick () {
        //if suit of card played == suitLed
        int highVal = 0;
        int playerID=-1;
        Card highCard = new Card(0, state.getSuitLed());
        for (Card card : state.getCardsPlayed()) {
            if (card.getCardSuit() == state.getSuitLed()) {
                if (highVal > card.getCardVal()) {
                    highCard = card;
                }
            }
            if (highCard == state.getP1CardPlayed()) {
                state.setP1RunningPoints(state.getP1RunningPoints()+state.pointsInTrick());
                return 0;
            } else if (highCard == state.getP2CardPlayed()) {
                state.setP2RunningPoints(state.getP2RunningPoints()+state.pointsInTrick());
                return 1;
            } else if (highCard == state.getP3CardPlayed()) {
                state.setP3RunningPoints(state.getP3RunningPoints()+state.pointsInTrick());
                return 2;
            } else  {
                state.setP4RunningPoints(state.getP3RunningPoints()+state.pointsInTrick());
                return 3;
            }
        }
        return -1;
    }

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
                if (card.getCardSuit() == CUPS || (card.getCardSuit() == SWORDS && card.getCardSuit() == 11)) {
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
    //todo: CHLOE LOOK BACK HERE.
        switch (state.getWhoTurn()) {
            case 1:
                if (isCardValid(state.getP1Hand(), state.getSelectedCard())) {
                    state.getCardsPlayed().add(state.getSelectedCard());
                    state.getP1Hand().remove(state.getSelectedCard());
                    return true;
                }
                break;
            case 2:
                if (isCardValid(state.getP2Hand(), state.getSelectedCard())) {
                    state.getCardsPlayed().add(state.getSelectedCard());
                    state.getP1Hand().remove(state.getSelectedCard());
                    return true;
                }
                break;
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

    public ArrayList<Card> getCurrentPlayerHand(){
        switch(state.getWhoTurn()){
            case 0:
                return state.getP1Hand();
            case 1:
                return state.getP2Hand();
            case 2:
                return state.getP3Hand();
            case 3:
                return state.getP4Hand();
            default:
                return null;

        }
    }

    /**
     * send the updated status of the game to a given player
     */
    @Override
    protected void sendUpdatedStateTo(GamePlayer player) {
        player.sendInfo(new gameStateHearts(state));
    }

    /**
     * update all players at once
     * @param players An array of valid players
     */
    private void updateAllPlayers(GamePlayer[] players){
        for (GamePlayer player : players){
            sendUpdatedStateTo(player);
        }
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
            workingHandString = workingHandString + "Suit: " + tempCard.getCardSuit() + "\tValue: " + tempCard.getCardVal() + "\n";
        }
        state.setP1HandString(workingHandString);
        workingHandString = "";

        for(Card tempCard : state.getP2Hand()) {
            workingHandString = workingHandString + "Suit: " + tempCard.getCardSuit() + "\tValue: " + tempCard.getCardVal() + "\n";
        }
        state.setP2HandString(workingHandString);
        workingHandString = "";

        for(Card tempCard : state.getP3Hand()) {
            workingHandString = workingHandString + "Suit: " + tempCard.getCardSuit() + "\tValue: " + tempCard.getCardVal() + "\n";
        }
        state.setP3HandString(workingHandString);
        workingHandString = "";

        for(Card tempCard : state.getP4Hand()) {
            workingHandString = workingHandString + "Suit: " + tempCard.getCardSuit() + "\tValue: " + tempCard.getCardVal() + "\n";
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



// =================================================================================================



    //CODE FROM CounterLocalGame

    // When a counter game is played, any number of players. The first player
    // is trying to get the counter value to TARGET_MAGNITUDE; the second player,
    // if present, is trying to get the counter to -TARGET_MAGNITUDE. The
    // remaining players are neither winners nor losers, but can interfere by
    // modifying the counter.
    public static final int TARGET_MAGNITUDE = 10;

    /**
     * can this player move
     *
     * @return
     * 		true, because all player are always allowed to move at all times,
     * 		as this is a fully asynchronous game
     */
    @Override
    protected boolean canMove(int playerIdx) {
        if(state.getWhoTurn() == playerIdx) {
            return true;
        }
        return false;
    }

    /**
     * process action
     */
    @Override
    protected boolean makeMove(GameAction action) {
        Log.i("action", action.getClass().toString());

        if (action instanceof ActionPlayCard) {
            //todo play the card
            if (state.getCardsPlayed().size() == 0) {
                state.setSuitLed(((ActionPlayCard) action).playedCard().getCardSuit());
            }
            switch (state.getWhoTurn()) {
                case 1:
                    //check if card is valid
                    if(isCardValid(state.getP1Hand(),((ActionPlayCard) action).playedCard())) {
                        //set the played card to the correct players cardPlayed
                        state.setP1CardPlayed(((ActionPlayCard) action).playedCard());
                        //removes card from hand
                        state.setP1Hand(removeCard(state.getP1Hand(), ((ActionPlayCard) action).playedCard()));
                        //next players turn
                        if (!isTrickOver()) {
                            state.setWhoTurn(state.getWhoTurn() + 1);
                        }
                    }
                    break;
                case 2:
                    if(isCardValid(state.getP1Hand(),((ActionPlayCard) action).playedCard())) {
                        state.setP2CardPlayed(((ActionPlayCard) action).playedCard());
                        state.setP2Hand(removeCard(state.getP2Hand(), ((ActionPlayCard) action).playedCard()));
                        if (!isTrickOver()) {
                            state.setWhoTurn(state.getWhoTurn() + 1);
                        }
                    }
                    break;
                case 3:
                    if(isCardValid(state.getP1Hand(),((ActionPlayCard) action).playedCard())) {
                        state.setP3CardPlayed(((ActionPlayCard) action).playedCard());
                        state.setP3Hand(removeCard(state.getP3Hand(), ((ActionPlayCard) action).playedCard()));
                        if (!isTrickOver()) {
                            state.setWhoTurn(state.getWhoTurn() + 1);
                        }
                    }
                    break;
                case 4:
                    if(isCardValid(state.getP1Hand(),((ActionPlayCard) action).playedCard())) {
                        state.setP4CardPlayed(((ActionPlayCard) action).playedCard());
                        state.setP4Hand(removeCard(state.getP4Hand(), ((ActionPlayCard) action).playedCard()));
                        if (!isTrickOver()) {
                            state.setWhoTurn(0);
                        }
                    }
                    break;
            }
            //if it's a heart, set hearts broken to true
            if (((ActionPlayCard) action).playedCard().getCardSuit() == CUPS) {
                state.setHeartsBroken(true);
            }
            if(isTrickOver()) {
                int playerID = collectTrick();
                state.setWhoTurn(playerID);
            }
            return true;
        }
        else if (action instanceof ActionQuit){
            System.exit(0);
            return false; //placeholder code
        }
        else {
            // denote that this was an illegal move
            return false;
        }
    }//makeMove

    /**
     * Determines if the trick is over.
     *
     * @return whether or not there have been four cards played and the trick is over
     */
    protected boolean isTrickOver() {
        if(state.getCardsPlayed().size() == 4) {
            return true;
        }
        return false;
    }

    /**
     * Check if the game is over. It is over, return a string that tells
     * who the winner(s), if any, are. If the game is not over, return null;
     * todo this method needs to be retooled - for beta
     *
     * @return
     * 		a message that tells who has won the game, or null if the
     * 		game is not over
     */
    @Override
    protected String checkIfGameOver() {
        if(state.getTricksPlayed() == 13) {
            int min = 100;
            int playerNum=0;
            int[] scoreArr = {state.getP1numCurrentPoints(), state.getP2numCurrentPoints(),
                    state.getP3RunningPoints(), state.getP4numCurrentPoints()};
            for(int i=0; i <scoreArr.length; i++) {
                if(scoreArr[i] < min) {
                    min = scoreArr[i];
                    playerNum=i;
                }
            }
            return "Player "+ playerNum + " has won.";
        }
        /*
        // get the value of the counter
        int counterVal = this.gameState.getCounter();

        if (counterVal >= TARGET_MAGNITUDE) {
            // counter has reached target magnitude, so return message that
            // player 0 has won.
            return playerNames[0]+" has won.";
        }
        else if (counterVal <= -TARGET_MAGNITUDE) {
            // counter has reached negative of target magnitude; if there
            // is a second player, return message that this player has won,
            // otherwise that the first player has lost
            if (playerNames.length >= 2) {
                return playerNames[1]+" has won.";
            }
            else {
                return playerNames[0]+" has lost.";
            }
        }
        else {
            // game is still between the two limit: return null, as the game
            // is not yet over
            return null;
        }*/

        return null;
    }

    /**
     * Removes card from a hand so that the player cannot have it forever.
     *
     * @param cards -- the current hand
     * @param card -- the card that was played and needs to be removed.
     * @return the array list of cards with the one removed
     */
    ArrayList<Card> removeCard (ArrayList<Card> cards, Card card) {
        ArrayList<Card> temp= new ArrayList<>();
        //copy the cards to a new list
        for (int i = 0; i < cards.size(); i++){
            Card cardToAdd = new Card(cards.get(i));
            temp.add(cardToAdd);
        }
        temp.remove(card);
        return temp;
    }
}
