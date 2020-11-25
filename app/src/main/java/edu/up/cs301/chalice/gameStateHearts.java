/**
 * GameStateHearts class
 * contains all information for the Hearts (Chalice) game state
 *
 * @version November 25, 2020
 * @author  Alex Junkins, Malia Lundstrom, Chloe Campbell, Addison Raak
 */

package edu.up.cs301.chalice;

import android.util.Log;

import java.util.*;

import edu.up.cs301.game.GameFramework.infoMessage.GameState;

import static edu.up.cs301.chalice.Card.COINS;
import static edu.up.cs301.chalice.Card.CUPS;
import static edu.up.cs301.chalice.Card.SWORDS;

public class gameStateHearts extends GameState {

    // to satisfy Serializable interface
    private static final long serialVersionUID = 7737393762469851826L;

    /**
     * instance variables
     */
    //points outside of the round/hand
    private int p1CurrentPoints, p2CurrentPoints, p3CurrentPoints, p4CurrentPoints;
    //points inside the hand
    private int p1RunningPoints, p2RunningPoints, p3RunningPoints, p4RunningPoints;

    private Deck deck;

    private int numCards;
    private Card selectedCard; //only used by PlayerHuman to update its GUI

    private String p1HandString;
    private String p2HandString;
    private String p3HandString;
    private String p4HandString;
    private ArrayList<Card> p1Hand;
    private ArrayList<Card> p2Hand;
    private ArrayList<Card> p3Hand;
    private ArrayList<Card> p4Hand;
    private ArrayList<Card> cardsPlayed;

    private boolean heartsBroken;
    private int suitLed;
    private int whoTurn;
    private int tricksPlayed;
    private boolean passingCards;
    private int cardsPassed;
    private int maxPoints;
    private Card p1CardPlayed, p2CardPlayed, p3CardPlayed, p4CardPlayed;

    /**
     * Empty Constructor
     * Initializes a game state with default values in its
     */
    public gameStateHearts() {
        p1CurrentPoints = 0;
        p2CurrentPoints = 0;
        p3CurrentPoints = 0;
        p4CurrentPoints = 0;
        maxPoints = 30;
        p1RunningPoints = 0;
        p2RunningPoints = 0;
        p3RunningPoints = 0;
        p4RunningPoints = 0;

        deck = new Deck(); //automatically created with default card order
        numCards = 13;
        heartsBroken = false;
        suitLed = COINS;
        tricksPlayed = 0;
        passingCards = true; //start every game by passing cards
        cardsPassed = 0;

        p1Hand = new ArrayList<>();
        p2Hand = new ArrayList<>();
        p3Hand = new ArrayList<>();
        p4Hand = new ArrayList<>();
        cardsPlayed = new ArrayList<>();
        selectedCard = null;
    }//gameStateHearts

    /**
     * Deep Copy Constructor for gameStateHearts
     * creates and returns a new game state with all the attributes
     * of a provided state
     *
     * @param oldState  the state to be copied
     */
    public gameStateHearts(gameStateHearts oldState) { //deep copy
        p1CurrentPoints = oldState.p1CurrentPoints;
        p2CurrentPoints = oldState.p2CurrentPoints;
        p3CurrentPoints = oldState.p3CurrentPoints;
        p4CurrentPoints = oldState.p4CurrentPoints;

        p1RunningPoints = oldState.p1RunningPoints;
        p2RunningPoints = oldState.p2RunningPoints;
        p3RunningPoints = oldState.p3RunningPoints;
        p4RunningPoints = oldState.p4RunningPoints;

        deck = new Deck(oldState.deck);
        numCards = oldState.numCards;
        selectedCard = oldState.selectedCard;
        heartsBroken = oldState.heartsBroken;
        suitLed = oldState.suitLed;
        tricksPlayed = oldState.tricksPlayed;
        maxPoints = oldState.maxPoints;
        whoTurn = oldState.whoTurn;
        passingCards = oldState.passingCards;
        cardsPassed = oldState.cardsPassed;
        p1CardPlayed = oldState.p1CardPlayed;
        p2CardPlayed = oldState.p2CardPlayed;
        p3CardPlayed = oldState.p3CardPlayed;
        p4CardPlayed = oldState.p4CardPlayed;

        p1Hand = handDeepCopy(oldState.p1Hand);
        p2Hand = handDeepCopy(oldState.p2Hand);
        p3Hand = handDeepCopy(oldState.p3Hand);
        p4Hand = handDeepCopy(oldState.p4Hand);
        cardsPlayed = handDeepCopy(oldState.cardsPlayed);
    }//gameStateHearts Copy

    /**
     * a helper method to deep-copy a hand
     * @param oldHand   the hand to be copied
     * @return          the new hand
     */
    private ArrayList<Card> handDeepCopy(ArrayList<Card> oldHand) {
        ArrayList<Card> newList = new ArrayList<>();
        for (int i = 0; i < oldHand.size(); i++){
            Card cardToAdd = new Card(oldHand.get(i));
            newList.add(cardToAdd);
        }
        return newList;
    }// handDeepCopy

    /**
     * A method that deals the cards out to each player
     *
     */
    public void dealCards() {
        deck.shuffle();
        for(int i=0; i<13; i++){
            p1Hand.add(deck.getNextCard());
            p2Hand.add(deck.getNextCard());
            p3Hand.add(deck.getNextCard());
            p4Hand.add(deck.getNextCard());
        }
    } //dealCards

    /**
     * A method that strings the cards that were
     * played in the current trick together
     *
     * @return an array with just the four cards that were played
     */
    public ArrayList<Card> getTrickCardsPlayed() {
        ArrayList<Card> cards = new ArrayList<>();
        if (p1CardPlayed != null) {
            cards.add(p1CardPlayed);
        }
        if (p2CardPlayed != null) {
            cards.add(p2CardPlayed);
        }
        if (p3CardPlayed != null) {
            cards.add(p3CardPlayed);
        }
        if (p4CardPlayed != null) {
            cards.add(p4CardPlayed);
        }
        return cards;
    }//getTrickCardsPlayed

    /**
     * The method determines how many points are in the current trick
     * (coins or queen of swords)
     *
     * @return the number of points in the trick
     */
    int pointsInTrick() {
        int points = 0;
        for (Card card : getTrickCardsPlayed()) {
            if(card.getCardSuit() == CUPS) {
                points++;
            }
            else if (card.getCardSuit() == SWORDS && card.getCardVal()==12) {
                points = points + 13;
            }
        }
        return points;
    }

    /** Setters for instance variables **/
    public int getP1CurrentPoints() {
        return p1CurrentPoints;
    }

    public int getP2CurrentPoints() {
        return p2CurrentPoints;
    }

    public int getP3CurrentPoints() {
        return p3CurrentPoints;
    }

    public int getP4CurrentPoints() {
        return p4CurrentPoints;
    }

    public int getP1RunningPoints() {
        return p1RunningPoints;
    }

    public int getP2RunningPoints() {
        return p2RunningPoints;
    }

    public int getP3RunningPoints() {
        return p3RunningPoints;
    }

    public int getP4RunningPoints() {
        return p4RunningPoints;
    }

    public int getNumCards() {
        return numCards;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public Deck getDeck() {
        return deck;
    }

    public Card getSelectedCard() {
        return selectedCard;
    }

    public String getP1HandString() {
        return p1HandString;
    }

    public String getP2HandString() {
        return p2HandString;
    }

    public String getP3HandString() {
        return p3HandString;
    }

    public String getP4HandString() {
        return p4HandString;
    }

    public ArrayList<Card> getP1Hand() {
        return p1Hand;
    }

    public ArrayList<Card> getP2Hand() {
        return p2Hand;
    }

    public ArrayList<Card> getP3Hand() {
        return p3Hand;
    }

    public ArrayList<Card> getP4Hand() {
        return p4Hand;
    }

    public ArrayList<Card> getCardsPlayed() {
        return cardsPlayed;
    }

    public boolean isHeartsBroken() {
        return heartsBroken;
    }

    public int getSuitLed() {
        return suitLed;
    }

    public int getWhoTurn() {
        return whoTurn;
    }

    public int getTricksPlayed() {
        return tricksPlayed;
    }

    public boolean getPassingCards() {
        return passingCards;
    }

    public int getCardsPassed() {
        return cardsPassed;
    }

    public Card getP1CardPlayed() {
        return p1CardPlayed;
    }

    public Card getP2CardPlayed() {
        return p2CardPlayed;
    }

    public Card getP3CardPlayed() {
        return p3CardPlayed;
    }
    public Card getP4CardPlayed() {
        return p4CardPlayed;
    }

    /**Setters for instance variables**/
    public void setP1CurrentPoints(int p1CurrentPoints) {
        this.p1CurrentPoints = p1CurrentPoints;
    }

    public void setP2CurrentPoints(int p2CurrentPoints) {
        this.p2CurrentPoints = p2CurrentPoints;
    }

    public void setP3CurrentPoints(int p3CurrentPoints) {
        this.p3CurrentPoints = p3CurrentPoints;
    }

    public void setP4CurrentPoints(int p4CurrentPoints) {
        this.p4CurrentPoints = p4CurrentPoints;
    }

    public void setP1RunningPoints(int p1RunningPoints) {
        this.p1RunningPoints = p1RunningPoints;
    }

    public void setP2RunningPoints(int p2RunningPoints) {
        this.p2RunningPoints = p2RunningPoints;
    }

    public void setP3RunningPoints(int p3RunningPoints) {
        this.p3RunningPoints = p3RunningPoints;
    }

    public void setP4RunningPoints(int p4RunningPoints) {
        this.p4RunningPoints = p4RunningPoints;
    }

    public void setNumCards(int numCards) {
        this.numCards = numCards;
    }

    public void setSelectedCard(Card selectedCard) {
        this.selectedCard = selectedCard;
    }

    public void setP1HandString(String p1HandString) {
        this.p1HandString = p1HandString;
    }

    public void setP2HandString(String p2HandString) {
        this.p2HandString = p2HandString;
    }

    public void setP3HandString(String p3HandString) {
        this.p3HandString = p3HandString;
    }

    public void setP4HandString(String p4HandString) {
        this.p4HandString = p4HandString;
    }

    public void setP1Hand(ArrayList<Card> p1Hand) {
        this.p1Hand = p1Hand;
    }

    public void setP2Hand(ArrayList<Card> p2Hand) {
        this.p2Hand = p2Hand;
    }

    public void setP3Hand(ArrayList<Card> p3Hand) {
        this.p3Hand = p3Hand;
    }

    public void setP4Hand(ArrayList<Card> p4Hand) {
        this.p4Hand = p4Hand;
    }

    public void setCardsPlayed(ArrayList<Card> cardsPlayed) {
        this.cardsPlayed = cardsPlayed;
    }

    public void setHeartsBroken(boolean heartsBroken) {
        this.heartsBroken = heartsBroken;
    }

    public void setSuitLed(int suitLed) {
        this.suitLed = suitLed;
    }

    public void setWhoTurn(int whoTurn) {
        this.whoTurn = whoTurn;
    }

    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }

    public void setTricksPlayed(int tricksPlayed) {
        this.tricksPlayed = tricksPlayed;
    }

    public void setPassingCards(boolean passingCards) {
        this.passingCards = passingCards;
    }

    public void setCardsPassed(int cardsPassed) {
        this.cardsPassed = cardsPassed;
    }

    public void setP1CardPlayed(Card p1CardPlayed) {
        this.p1CardPlayed = p1CardPlayed;
    }

    public void setP2CardPlayed(Card p2CardPlayed) {
        this.p2CardPlayed = p2CardPlayed;
    }

    public void setP3CardPlayed(Card p3CardPlayed) {
        this.p3CardPlayed = p3CardPlayed;
    }

    public void setP4CardPlayed(Card p4CardPlayed) {
        this.p4CardPlayed = p4CardPlayed;
    }

} // gameStateHearts