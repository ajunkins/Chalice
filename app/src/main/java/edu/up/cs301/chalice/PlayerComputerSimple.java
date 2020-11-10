package edu.up.cs301.chalice;

import android.util.Log;

import java.util.ArrayList;

import edu.up.cs301.game.GameFramework.GameComputerPlayer;
import edu.up.cs301.game.GameFramework.infoMessage.GameInfo;
import edu.up.cs301.game.GameFramework.utilities.Tickable;

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
        if (!(info instanceof gameStateHearts)){
            return;
        }
        gameStateHearts state = new gameStateHearts((gameStateHearts)info);

        //not my turn
        if (playerNum != state.getWhoTurn()){
            return;
        }

        if(getSuitCardsInHand(state, state.getSuitLed()).size() > 0) {

            if(pointsOnTheTable(state) > 0) {
                game.sendAction(new ActionPlayCard(this, getLowestCard(getSuitCardsInHand(state, state.getSuitLed()))));
            } else if (getCardsPlayedThisTrick(state) == 3) {
                game.sendAction(new ActionPlayCard(this, getHighestCard(getSuitCardsInHand(state, state.getSuitLed()))));
            } else {
                game.sendAction(new ActionPlayCard(this, getSuitCardsInHand(state, state.getSuitLed()).get(0)));
            }

        }else {

            if(getPointCardsInHand(state).size() > 0) {
                game.sendAction(new ActionPlayCard(this, getHighestCard(getPointCardsInHand(state))));
            } else  {
                game.sendAction(new ActionPlayCard(this, getHighestCard(getMyHand(state))));
            }

        }
    }


    public Card getHighestCard(ArrayList<Card> cardStack){
        Card highest = cardStack.get(0);
        for (Card card : cardStack){
            if (card.getCardVal() > highest.getCardVal() || card.getCardVal() == 1){
                highest = card;
            }
        }
        return highest;
    }

    public Card getLowestCard(ArrayList<Card> cardStack){
        Card lowest = cardStack.get(0);
        for (Card card : cardStack){
            if (card.getCardVal() < lowest.getCardVal() && card.getCardVal() != 1){
                lowest = card;
            }
        }
        return lowest;
    }

    public int getCardsPlayedThisTrick(gameStateHearts state){
        return state.getCardsPlayed().size() % 4;
    }

    public int pointsOnTheTable(gameStateHearts state){
        int currentPoints = 0;
        for (Card card : state.getCardsPlayed()) {
            if (card.getCardSuit() == Card.CUPS){
                currentPoints += 1;
            }
            else if (card.getCardSuit() == Card.SWORDS){
                if (card.getCardVal() == 12) { currentPoints += 12; }
            }
        }
        return currentPoints;
    }


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
