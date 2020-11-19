/**
 * Computer Player Simple class
 *
 * This is the simple computer version of a Chalice player.
 *
 * @version October 18, 2020
 * @author  Alex Junkins, Malia Lundstrom, Chloe Campbell, Addison Raak
 */

package edu.up.cs301.chalice;

import android.util.Log;
import java.util.ArrayList;
import edu.up.cs301.game.GameFramework.GameComputerPlayer;
import edu.up.cs301.game.GameFramework.infoMessage.GameInfo;
import edu.up.cs301.game.GameFramework.utilities.Tickable;

import static edu.up.cs301.chalice.Card.COINS;

public class PlayerComputerSimple extends GameComputerPlayer implements Tickable {

    /**
     * Constructor for objects of class CounterComputerPlayer1
     *
     * @param name
     * 		the player's name
     */
    public PlayerComputerSimple(String name) {
        // invoke superclass constructor
        super(name);

        // start the timer, ticking 20 times per second
        getTimer().setInterval(50);
        getTimer().start();
    }

    /**
     * callback method--game's state has changed
     *
     * @param info
     * 		the information (presumably containing the game's state)
     */
    @Override
    protected void receiveInfo(GameInfo info) {
        //not a state update
        if (!(info instanceof gameStateHearts)) {
            return;
        }
        gameStateHearts state = new gameStateHearts((gameStateHearts)info);


        //not my turn
        if (playerNum != state.getWhoTurn()) {
            return;
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(state.getTricksPlayed() == 0 && state.getTrickCardsPlayed().size() ==0) {
            Card coins2 = new Card(2,COINS);
            int cardIndex=-1;
            for (Card card: getMyHand(state)) {
                if(Card.sameCard(card, coins2)) {
                    cardIndex = getMyHand(state).indexOf(card);
                }
            }
            game.sendAction((new ActionPlayCard(this, this.playerNum,getMyHand(state).get(cardIndex))));
        }

        if(getSuitCardsInHand(state, state.getSuitLed()).size() > 0) {

            if(pointsOnTheTable(state) > 0) {
                game.sendAction(new ActionPlayCard(this, this.playerNum, getLowestCard(getSuitCardsInHand(state, state.getSuitLed()))));
            } else if (getCardsPlayedThisTrick(state) == 3) {
                game.sendAction(new ActionPlayCard(this, this.playerNum, getHighestCard(getSuitCardsInHand(state, state.getSuitLed()))));
            } else {
                game.sendAction(new ActionPlayCard(this, this.playerNum, getSuitCardsInHand(state, state.getSuitLed()).get(0)));
            }

        }else {

            if(getPointCardsInHand(state).size() > 0) {
                game.sendAction(new ActionPlayCard(this, this.playerNum, getHighestCard(getPointCardsInHand(state))));
            } else  {
                game.sendAction(new ActionPlayCard(this, this.playerNum, getHighestCard(getMyHand(state))));
            }

        }
    }

    /**
     * method to get the highest card in an array of cards.
     *
     * @param cardStack
     *      stack of cards
     * @return highest card in the stack
     */
    public Card getHighestCard(ArrayList<Card> cardStack){
        Card highest = cardStack.get(0);
        for (Card card : cardStack){
            if (card.getCardVal() > highest.getCardVal() || card.getCardVal() == 1){
                highest = card;
            }
        }
        return highest;
    }

    /**
     * method to get the lowest card in an array of cards.
     *
     * @param cardStack
     *      stack of cards
     * @return lowest card in the stack
     */
    public Card getLowestCard(ArrayList<Card> cardStack){
        Card lowest = cardStack.get(0);
        for (Card card : cardStack){
            if (card.getCardVal() < lowest.getCardVal() && card.getCardVal() != 1){
                lowest = card;
            }
        }
        return lowest;
    }

    /**
     * method to get the number of cards played in the current trick.
     *
     * @param state
     *      current game state
     * @return number of cards played this trick
     */
    public int getCardsPlayedThisTrick(gameStateHearts state){
        return state.getCardsPlayed().size() % 4;
    }


    /**
     * method to get the amount of points that are on the table.
     *
     * @param state
     *      current game state
     * @return the amount of points that are on the table.
     */
    public int pointsOnTheTable(gameStateHearts state){
        int currentPoints = 0;
        for (Card card : state.getCardsPlayed()) {
            if (card.getCardSuit() == Card.CUPS){
                currentPoints += 1;
            }
            else if (card.getCardSuit() == Card.SWORDS){
                if (card.getCardVal() == 12) { currentPoints += 13; }
            }
        }
        return currentPoints;
    }

    /**
     * method to get the cards with points in player's hand.
     *
     * @param state
     *      current game state
     * @return arrayList of cards with points in the player's hand
     */
    public ArrayList<Card> getPointCardsInHand(gameStateHearts state){
        ArrayList<Card> myHand = getMyHand(state);
        ArrayList<Card> pointCards = new ArrayList<Card>();
        for (int i = 0; i < myHand.size(); i++){
            Card currentCard = myHand.get(i);
            if (currentCard.getCardSuit() == Card.CUPS){
                pointCards.add(currentCard);
            }
            else if (currentCard.getCardSuit() == Card.SWORDS && currentCard.getCardVal() == 12){
                pointCards.add(currentCard);
            }
        }
        return pointCards;
    }

    /**
     * method to get the cards in player's hand that belong to given suit
     *
     * @param state, suit
     *      current game state
     * @return arrayList of cards in given suit and in player's hand
     */
    public ArrayList<Card> getSuitCardsInHand(gameStateHearts state, int suit){
        ArrayList<Card> myHand = getMyHand(state);
        ArrayList<Card> cardsInSuit = new ArrayList<Card>();
        for (int i = 0; i < myHand.size(); i++){
            Card currentCard = myHand.get(i);
            if (currentCard.getCardSuit() == suit){
                cardsInSuit.add(currentCard);
            }
        }
        return cardsInSuit;
    }

    /**
     * method to get player's hand.
     *
     * @param state
     *      current game state
     * @return arrayList of cards in player's hand
     */
    public ArrayList<Card> getMyHand(gameStateHearts state){
        ArrayList<Card> myHand;
        switch(playerNum){
            case 0:
                myHand = state.getP1Hand();
                break;
            case 1:
                myHand = state.getP2Hand();
                break;
            case 2:
                myHand = state.getP3Hand();
                break;
            case 3:
                myHand = state.getP4Hand();
                break;
            default:
                //shit's broken
                Log.e("PlayerNum Error", "AI player had an invalid playerNum");
                return null;
        }
        return myHand;
    }


}
