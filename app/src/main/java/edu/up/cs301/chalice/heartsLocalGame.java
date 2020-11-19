/**
 * Hearts Local Game class
 *
 * contains all gameplay functions for the Hearts (Chalice) game
 *
 * @version October 18, 2020
 * @author  Alex Junkins, Malia Lundstrom, Chloe Campbell, Addison Raak
 */

package edu.up.cs301.chalice;

import android.util.Log;

import java.util.ArrayList;

import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.LocalGame;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;

import static edu.up.cs301.chalice.Card.COINS;
import static edu.up.cs301.chalice.Card.CUPS;
import static edu.up.cs301.chalice.Card.SWORDS;

public class heartsLocalGame extends LocalGame {

    //declare instance of gameStateHearts
    private gameStateHearts state;

    /**
     * Initial constructor
     * Deals the cards to the player and sets the starting player to whoever has the 2 of coins.
     *
     */
    public heartsLocalGame() {
        state = new gameStateHearts();
        state.dealCards();
        state.setSuitLed(COINS);
        setTrickStartingPlayer();
    }

    /**
     * Deep-copy constructor
     * @param localGame game to be copied
     */
    public heartsLocalGame(heartsLocalGame localGame) {
        state = new gameStateHearts(localGame.state);
    }

    /**
     * a method to modify the game state to make the holder of the
     * 2 of Coins the player whose turn it is
     */
    private void setTrickStartingPlayer(){
        Card clubs2 = new Card(2,COINS);
        for (Card card:state.getP1Hand()) {
            if(Card.sameCard(card,clubs2)) {
                state.setWhoTurn(0);
            }
        }
        for (Card card:state.getP2Hand()) {
            if(Card.sameCard(card,clubs2)) {
                state.setWhoTurn(1);
            }
        }
        for (Card card:state.getP3Hand()) {
            if(Card.sameCard(card,clubs2)) {
                state.setWhoTurn(2);
            }
        }
        for (Card card:state.getP4Hand()) {
            if(Card.sameCard(card,clubs2)) {
                state.setWhoTurn(3);
            }
        }
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
        int winnerID = -1;
        int highVal = 0;
        Card highCard = new Card(0, state.getSuitLed());
        for (Card card : state.getTrickCardsPlayed()) {
            if (card.getCardSuit() == state.getSuitLed()) {
                if (card.getCardVal() == 1){
                    highVal = 13;
                    highCard = card;
                }
                else if (highVal < card.getCardVal()) {
                    highVal = card.getCardVal();
                    highCard = card;
                }
            }
        }
        if (Card.sameCard(highCard, state.getP1CardPlayed())) {
            state.setP1RunningPoints(state.getP1RunningPoints() + state.pointsInTrick());
            winnerID = 0;
        } else if (Card.sameCard(highCard, state.getP2CardPlayed())) {
            state.setP2RunningPoints(state.getP2RunningPoints() + state.pointsInTrick());
            winnerID =  1;
        } else if (Card.sameCard(highCard, state.getP3CardPlayed())) {
            state.setP3RunningPoints(state.getP3RunningPoints() + state.pointsInTrick());
            winnerID =  2;
        } else  {
            state.setP4RunningPoints(state.getP4RunningPoints() + state.pointsInTrick());
            winnerID =  3;
        }
        state.setP1CardPlayed(null);
        state.setP2CardPlayed(null);
        state.setP3CardPlayed(null);
        state.setP4CardPlayed(null);
        Log.i("End Trick Report", "P1 running points: " + state.getP1RunningPoints());
        Log.i("End Trick Report", "P2 running points: " + state.getP2RunningPoints());
        Log.i("End Trick Report", "P3 running points: " + state.getP3RunningPoints());
        Log.i("End Trick Report", "P4 running points: " + state.getP4RunningPoints());
        return winnerID;
    }

    /**
     * a method to re-set a game state to the beginning of a hand,
     * while keeping the data necessary from previous rounds
     * note: this modifies this class's state instance variable
     */
    private void initializeHand(){
        //make running points into current points and reset running points
        state.setP1numCurrentPoints(
                state.getP1numCurrentPoints() + state.getP1RunningPoints());
        state.setP2numCurrentPoints(
                state.getP2numCurrentPoints() + state.getP2RunningPoints());
        state.setP3numCurrentPoints(
                state.getP3numCurrentPoints() + state.getP3RunningPoints());
        state.setP4numCurrentPoints(
                state.getP4numCurrentPoints() + state.getP4RunningPoints());
        state.setP1RunningPoints(0);
        state.setP2RunningPoints(0);
        state.setP3RunningPoints(0);
        state.setP4RunningPoints(0);

        //reset deck and player hands
        state.getDeck().shuffle();
        state.setCardsPlayed(new ArrayList<Card>());
        state.setP1Hand(new ArrayList<Card>());
        state.setP2Hand(new ArrayList<Card>());
        state.setP3Hand(new ArrayList<Card>());
        state.setP4Hand(new ArrayList<Card>());
        state.setP1CardPlayed(null);
        state.setP2CardPlayed(null);
        state.setP3CardPlayed(null);
        state.setP4CardPlayed(null);

        //misc variable correction
        state.setNumCards(13);
        state.setSelectedCard(null);
        state.setHeartsBroken(false);
        state.setSuitLed(COINS);
        state.setTricksPlayed(0);
        state.setCardsPassed(0); //is this how cardspassed is supposed to be used?

        //setup start of trick
        state.dealCards();
        setTrickStartingPlayer();
    }


    /**
     * A method to check if a card is a valid play, given the current state of the game
     *
     * @param card      the card to check
     * @param hand      the rest of the hand
     * @return          legality status of the card
     */
    public boolean isCardValid(ArrayList<Card> hand, Card card) {
        if(state.getTricksPlayed() == 0 && state.getTrickCardsPlayed().size() == 0) {
            if(card.getCardSuit() == COINS && card.getCardVal() == 2) {
                return true;
            }
            else {
                return false;
            }
        }
        if(isInSuit(card)) {
            return true;
        }
        else {
            for (Card c : hand) {
                if (c.getCardSuit() == state.getSuitLed()) {
                    return false;
                }
            }
            if (state.isHeartsBroken()) {
                return true;
            } else {
                if (card.getCardSuit() == CUPS || (card.getCardSuit() == SWORDS &&
                        card.getCardSuit() == 12)) {
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
    boolean playCard(GameAction action) {
        switch (state.getWhoTurn()) {
            case 0:
                //check if card is valid
                if (isCardValid(state.getP1Hand(), ((ActionPlayCard) action).playedCard())) {
                    //set the played card to the correct players cardPlayed
                    state.setP1CardPlayed(((ActionPlayCard) action).playedCard());
                    state.getCardsPlayed().add(((ActionPlayCard) action).playedCard());
                    //removes card from hand
                    state.setP1Hand(removeCard(state.getP1Hand(), ((ActionPlayCard) action).playedCard()));
                    //next players turn
                    if (!isTrickOver()) {
                        state.setWhoTurn(state.getWhoTurn() + 1);
                    }
                    return true;
                } else {
                    return false;
                }
            case 1:
                if (isCardValid(state.getP2Hand(), ((ActionPlayCard) action).playedCard())) {
                    state.setP2CardPlayed(((ActionPlayCard) action).playedCard());
                    state.setP2Hand(removeCard(state.getP2Hand(), ((ActionPlayCard) action).playedCard()));
                    state.getCardsPlayed().add(((ActionPlayCard) action).playedCard());
                    if (!isTrickOver()) {
                        state.setWhoTurn(state.getWhoTurn() + 1);
                    }
                    return true;
                } else {
                    return false;
                }
            case 2:
                if (isCardValid(state.getP3Hand(), ((ActionPlayCard) action).playedCard())) {
                    state.setP3CardPlayed(((ActionPlayCard) action).playedCard());
                    state.setP3Hand(removeCard(state.getP3Hand(), ((ActionPlayCard) action).playedCard()));
                    state.getCardsPlayed().add(((ActionPlayCard) action).playedCard());
                    if (!isTrickOver()) {
                        state.setWhoTurn(state.getWhoTurn() + 1);
                    }
                    return true;
                } else {
                    return false;
                }
            case 3:
                if (isCardValid(state.getP4Hand(), ((ActionPlayCard) action).playedCard())) {
                    state.setP4CardPlayed(((ActionPlayCard) action).playedCard());
                    state.setP4Hand(removeCard(state.getP4Hand(), ((ActionPlayCard) action).playedCard()));
                    state.getCardsPlayed().add(((ActionPlayCard) action).playedCard());
                    if (!isTrickOver()) {
                        state.setWhoTurn(0);
                    }
                    return true;
                } else {
                    return false;
                }
            default:
                return false;
        }
    }

    /**
     * A method to handle the passing-card phase of play
     * (may be modified to handle just one passing of a card)
     *
     * @return  true if successful, false if temporary hand is empty.
     */
    boolean passCard() {
        ArrayList<Card> tempHand = new ArrayList<>();
        while(state.getCardsPassed() < 4 && state.getWhoTurn() == 1 &&
                state.getTricksPlayed() == 0) {

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
        //returns false if temp hand is empty and they don't select cards.
        if(tempHand == null) {
            return false;
        }
        return false;
    }

    /**
     * Gives the hand of whoever is currently playing
     *
     * @return the hand of the current player
     */
    public ArrayList<Card> getCurrentPlayerHand() {
        switch(state.getWhoTurn()) {
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
            workingHandString = workingHandString + "Suit: " + tempCard.getCardSuit() + "\tValue: "
                    + tempCard.getCardVal() + "\n";
        }
        state.setP1HandString(workingHandString);
        workingHandString = "";

        for(Card tempCard : state.getP2Hand()) {
            workingHandString = workingHandString + "Suit: " + tempCard.getCardSuit() +
                    "\tValue: " + tempCard.getCardVal() + "\n";
        }
        state.setP2HandString(workingHandString);
        workingHandString = "";

        for(Card tempCard : state.getP3Hand()) {
            workingHandString = workingHandString + "Suit: " + tempCard.getCardSuit() +
                    "\tValue: " + tempCard.getCardVal() + "\n";
        }
        state.setP3HandString(workingHandString);
        workingHandString = "";

        for(Card tempCard : state.getP4Hand()) {
            workingHandString = workingHandString + "Suit: " + tempCard.getCardSuit() +
                    "\tValue: " + tempCard.getCardVal() + "\n";
        }
        state.setP4HandString(workingHandString);


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
     *
     * @param action, an action that can be quit or play
     */
    @Override
    protected boolean makeMove(GameAction action) {
        Log.i("action", action.getClass().toString());
        if (action instanceof ActionPlayCard) {
            if (state.getTrickCardsPlayed().size() == 0 && state.getTricksPlayed() !=0) {
                state.setSuitLed(((ActionPlayCard) action).playedCard().getCardSuit());
            }
            boolean validCard = playCard(action);
            if (!validCard){
                Log.i("debugging alert", "Makemove: card  in suit " +
                        ((ActionPlayCard) action).playedCard().getCardSuit() +
                        " with value " +
                        ((ActionPlayCard) action).playedCard().getCardVal() +
                        "was deemed illegal for play.");
                return false;
            }
            //if it's a heart, set hearts broken to true
            if (((ActionPlayCard) action).playedCard().getCardSuit() == CUPS) {
                state.setHeartsBroken(true);
            }
            if(isTrickOver()) {
                updateAllPlayers(players);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int playerID = collectTrick();
                state.setWhoTurn(playerID);
                state.setSuitLed(-1);
                state.setTricksPlayed(state.getTricksPlayed()+1);
            }
            if (isHandOver()){
                initializeHand();
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
     * @return whether or not there have been four cards played and the trick
     * is over
     */
    protected boolean isTrickOver() {
        if(state.getTrickCardsPlayed().size() == 4) {
            return true;
        }
        return false;
    }

    /**
     * Determines if the hand is over.
     *
     * @return whether or not all cards in all hands have been played and the
     * hand is over
     */
    protected boolean isHandOver(){
        int cardsRemaining = 0;
        cardsRemaining += state.getP1Hand().size();
        cardsRemaining += state.getP2Hand().size();
        cardsRemaining += state.getP3Hand().size();
        cardsRemaining += state.getP4Hand().size();
        return cardsRemaining == 0;
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
        //check if it is the start of a hand
        if(state.getCardsPlayed().size() != 0){
            return null;
        }

        //check if any of the players have met or passed the max score
        boolean scoreMet = false;
        if (state.getP1numCurrentPoints() >= state.getMaxPoints()){
            scoreMet = true;
        }
        if (state.getP2numCurrentPoints() >= state.getMaxPoints()){
            scoreMet = true;
        }
        if (state.getP3numCurrentPoints() >= state.getMaxPoints()){
            scoreMet = true;
        }
        if (state.getP4numCurrentPoints() >= state.getMaxPoints()){
            scoreMet = true;
        }
        if (!scoreMet){
            return null;
        }

        //other checks for game over

        //old alpha game ending
        if(state.getTricksPlayed() == 13) {
            int min = 100;
            int playerNum=0;
            int[] scoreArr = {state.getP1RunningPoints(), state.getP2RunningPoints(),
                    state.getP3RunningPoints(), state.getP4RunningPoints()};
            for(int i=0; i <scoreArr.length; i++) {
                if(scoreArr[i] == 26) {
                    return "Player " + (i+1) + " has shot the moon! They won!";
                }
                if(scoreArr[i] < min) {
                    min = scoreArr[i];
                    playerNum=i+1;
                }
            }
            return "Player "+ playerNum + " has won.";
        }

        //to start the game back over!
        //myActivity.recreate(); // restart the game!
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
        for (int i = 0; i < temp.size(); i++){
            if (Card.sameCard(card, temp.get(i))){
                temp.remove(i);
            }
        }
        return temp;
    }
}
