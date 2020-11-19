/**
 * GameStateHearts class
 * contains all information for the Hearts (Chalice) game state
 *
 * @version October 8, 2020
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
    private int p1numCurrentPoints, p2numCurrentPoints, p3numCurrentPoints, p4numCurrentPoints;
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
    private int cardsPassed;
    private int maxPoints;
    private Card p1CardPlayed, p2CardPlayed, p3CardPlayed, p4CardPlayed;

    /**
     * Empty Constructor
     * Initializes a game state with default values in its
     */
    public gameStateHearts() {
        p1numCurrentPoints = 0;
        p2numCurrentPoints = 0;
        p3numCurrentPoints = 0;
        p4numCurrentPoints = 0;
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
        cardsPassed = 0;

        p1Hand = new ArrayList<>();
        p2Hand = new ArrayList<>();
        p3Hand = new ArrayList<>();
        p4Hand = new ArrayList<>();
        cardsPlayed = new ArrayList<>();
        selectedCard = null;
    }

    /**
     * Deep Copy Constructor
     * creates and returns a new game state with all the attributes
     * of a provided state
     *
     * @param oldState  the state to be copied
     */
    public gameStateHearts(gameStateHearts oldState) { //deep copy gameStateHearts
        p1numCurrentPoints = oldState.p1numCurrentPoints;
        p2numCurrentPoints = oldState.p2numCurrentPoints;
        p3numCurrentPoints = oldState.p3numCurrentPoints;
        p4numCurrentPoints = oldState.p4numCurrentPoints;

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
        p1CardPlayed = oldState.p1CardPlayed;
        p2CardPlayed = oldState.p2CardPlayed;
        p3CardPlayed = oldState.p3CardPlayed;
        p4CardPlayed = oldState.p4CardPlayed;

        p1Hand = handDeepCopy(oldState.p1Hand);
        p2Hand = handDeepCopy(oldState.p2Hand);
        p3Hand = handDeepCopy(oldState.p3Hand);
        p4Hand = handDeepCopy(oldState.p4Hand);
        cardsPlayed = handDeepCopy(oldState.cardsPlayed);

    }

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
    }

    /**
     * a tester method to fill the game-state with random but
     * probably-realistically expectable values
     * - assumes 100 points is winning score
     * I wrote this at 2 am so it doesn't follow the fit on one screen rule
     * or a bunch of other code standard rules
     * sorry not sorry
     */
    public void Randomize() {
        this.Reset();

        // pick a random amount of completed rounds to have been played and assign
        // points up to that round. Only do so if no player's points go over 100
        Random rn = new Random();
        int prevPointsP1 = 0;
        int prevPointsP2 = 0;
        int prevPointsP3 = 0;
        int prevPointsP4 = 0;

        int prevRounds = rn.nextInt(7); //the QoS can only be taken 7 times without
        //a player going over 100
        //loop for previous rounds
        for (int i = 0; i < prevRounds; i++){
            //save values in case this round puts us over
            prevPointsP1 = p1numCurrentPoints;
            prevPointsP2 = p2numCurrentPoints;
            prevPointsP3 = p3numCurrentPoints;
            prevPointsP4 = p4numCurrentPoints;

            //off-the-top-of-my-head chance for a STM: 1 in 20
            int STM = rn.nextInt(20);
            if (STM == 7){ //my favorite number
                //pick a random player and give everyone else 26 points
                int luckyducky = rn.nextInt(4);
                switch (luckyducky) {
                    case 0:
                        p2numCurrentPoints += 26;
                        p3numCurrentPoints += 26;
                        p4numCurrentPoints += 26;
                        break;
                    case 1:
                        p1numCurrentPoints += 26;
                        p3numCurrentPoints += 26;
                        p4numCurrentPoints += 26;
                        break;
                    case 2:
                        p1numCurrentPoints += 26;
                        p2numCurrentPoints += 26;
                        p4numCurrentPoints += 26;
                        break;
                    case 3:
                        p1numCurrentPoints += 26;
                        p2numCurrentPoints += 26;
                        p3numCurrentPoints += 26;
                        break;
                    default: //in an emergency, player 3 is the lucky ducky
                        Log.d("Randomization Error",
                                "Shoot the Moon generation error");
                        p1numCurrentPoints += 26;
                        p2numCurrentPoints += 26;
                        p4numCurrentPoints += 26;
                        break;
                }
            } else {
                //if nobody shot the moon, assign 13 points randomly between
                //players, and randomly add another 13 for QoS on the 8th loop
                for(int j = 0; j < 13; j++){
                    int player = rn.nextInt(4);
                    switch (player){
                        case 0:
                            p1numCurrentPoints += 1;
                            if (j == 7) { p1numCurrentPoints += 13; }
                            break;
                        case 1:
                            p2numCurrentPoints += 1;
                            if (j == 7) { p2numCurrentPoints += 13; }
                            break;
                        case 2:
                            p3numCurrentPoints += 1;
                            if (j == 7) { p3numCurrentPoints += 13; }
                            break;
                        case 3:
                            p4numCurrentPoints += 1;
                            if (j == 7) { p4numCurrentPoints += 13; }
                            break;
                        default: //in an emergency, player 1 is picked
                            Log.d("Randomization Error",
                                    "Cup point generation error");
                            p1numCurrentPoints += 1;
                            if (j == 7) { p1numCurrentPoints += 13; }
                            break;

                    }
                }
            }

            //check if we went over. If so, reset values
            if (p1numCurrentPoints >= 100 ||
                    p2numCurrentPoints >= 100 ||
                    p3numCurrentPoints >= 100 ||
                    p4numCurrentPoints >= 100){
                p1numCurrentPoints = prevPointsP1;
                p2numCurrentPoints = prevPointsP2;
                p3numCurrentPoints = prevPointsP3;
                p4numCurrentPoints = prevPointsP4;
                break;
            }
        }

        //pick random amount of point cards played for the current round
        int cupsPlayed = rn.nextInt(13); //not to be confused with the instance var. Sorry.
        Boolean queenPlayed = rn.nextBoolean();
        if (cupsPlayed > 0 || queenPlayed) { heartsBroken = true; }
        //NOTE: while unlikely, this could potentially result in a situation where
        //the QoS has been played but hearts/cups have not been broken
        //fun fact: the chance is approximately 1 in 26

        //randomly assign running points
        for (int i = 0; i < cupsPlayed; i++){
            this.cardsPlayed.add(deck.removeCup());
            int player = rn.nextInt(4);
            switch (player){
                case 0:
                    p1RunningPoints += 1;
                    break;
                case 1:
                    p2RunningPoints += 1;
                    break;
                case 2:
                    p3RunningPoints += 1;
                    break;
                case 3:
                    p4RunningPoints += 1;
                    break;
                default: //in an emergency, player 1 is picked
                    Log.d("Randomization Error",
                            "Cup running point generation error");
                    p1RunningPoints += 1;
                    break;

            }
        }
        if (queenPlayed){
            this.cardsPlayed.add(deck.removeQueen());
            int player = rn.nextInt(4);
            switch (player){
                case 0:
                    p1RunningPoints += 13;
                    break;
                case 1:
                    p2RunningPoints += 13;
                    break;
                case 2:
                    p3RunningPoints += 13;
                    break;
                case 3:
                    p4RunningPoints += 13;
                    break;
                default: //in an emergency, player 1 is picked
                    Log.d("Randomization Error",
                            "Queen running point generation error");
                    p1RunningPoints += 13;
                    break;

            }
        }

        //remove cards until the deck is divisible by four, calculate tricksPlayed
        while(deck.cardsLeft() % 4 != 0){
            this.cardsPlayed.add(deck.getNextCard());
        }
        tricksPlayed = (52 - deck.cardsLeft()) / 4;

        //deal out the cards to the remaining players
        while(deck.cardsLeft() > 0){
            p1Hand.add(deck.getNextCard());
            p2Hand.add(deck.getNextCard());
            p3Hand.add(deck.getNextCard());
            p4Hand.add(deck.getNextCard());
        }

        //always assumes player 1 leads the current trick
        int cardsPlayed = rn.nextInt(3); //if it was 4, the trick would be over
        numCards = p1Hand.size() * 4;
        numCards -= cardsPlayed;
        Card playedCard;
        if (cardsPlayed >= 1) {
            //play 1st card in p1's hand
            playedCard = p1Hand.get(0);
            p1CardPlayed = playedCard;
            p1Hand.remove(playedCard);
            suitLed = playedCard.getCardSuit();
            this.cardsPlayed.add(playedCard);
        }
        if (cardsPlayed >= 2) {
            //find first card of cardSuit
            int cardToPlay = 0;
            for (int i = 0; i < p2Hand.size(); i++){
                if (p2Hand.get(i).getCardSuit() == suitLed){
                    cardToPlay = i;
                    break;
                }
                //if not, try for a heart or the queen of Spades
                if (heartsBroken) {
                    if (p2Hand.get(i).getCardSuit() == 1 || (p2Hand.get(i).getCardSuit() == 2 &&
                            p2Hand.get(i).getCardVal() == 12)) {
                        cardToPlay = i;
                        break;
                    }
                }
            }
            //if no legal cards, play the first card in the hand
            playedCard = p2Hand.get(cardToPlay);
            p2CardPlayed = playedCard;
            p2Hand.remove(playedCard);
            this.cardsPlayed.add(playedCard);
        }
        if (cardsPlayed >= 3){
            //find first card of cardSuit
            int cardToPlay = 0;
            for (int i = 0; i < p3Hand.size(); i++){
                if (p3Hand.get(i).getCardSuit() == suitLed){
                    cardToPlay = i;
                    break;
                }
                //if not, try for a heart or the queen of Spades
                if (heartsBroken){
                    if (p3Hand.get(i).getCardSuit() == 1 || (p3Hand.get(i).getCardSuit() == 2 &&
                            p3Hand.get(i).getCardVal() == 12)){
                        cardToPlay = i;
                        break;
                    }
                }
            }
            //if no legal cards, play the first card in the hand
            playedCard = p3Hand.get(cardToPlay);
            p3CardPlayed = playedCard;
            p3Hand.remove(playedCard);
            this.cardsPlayed.add(playedCard);
        }

        //does not change cards passed b/c random state assumedly takes place mid-round
        Log.d("Randomize Debug", "finished randomization.");
    }

    /**
     * a helper method to reset all values of a state to a blank state
     */
    private void Reset(){
        p1numCurrentPoints = 0;
        p2numCurrentPoints = 0;
        p3numCurrentPoints = 0;
        p4numCurrentPoints = 0;

        p1RunningPoints = 0;
        p2RunningPoints = 0;
        p3RunningPoints = 0;
        p4RunningPoints = 0;

        deck = new Deck();
        deck.shuffle();
        numCards = 13;
        heartsBroken = false;
        suitLed = COINS;
        tricksPlayed = 0;
        cardsPassed = 0;

        p1Hand = new ArrayList<>();
        p2Hand = new ArrayList<>();
        p3Hand = new ArrayList<>();
        p4Hand = new ArrayList<>();
        cardsPlayed = new ArrayList<>();
    }

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
    }

    /**
     * A method that strings the cards that were played in the current trick together
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
    }

    /**
     * The method determines how many points are in the current trick (coins or queen of swords)
     *
     * @return the number of points in the trick
     */
    int pointsInTrick() {
        int points = 0;
        for (Card card : getTrickCardsPlayed()) {
            if(card.getCardSuit() == CUPS) {
                points++;
            }else if (card.getCardSuit() == SWORDS && card.getCardVal()==12) {
                points = points + 13;
            }
        }
        return points;
    }

    /** Setters for instance variables **/
    public int getP1numCurrentPoints() {
        return p1numCurrentPoints;
    }

    public int getP2numCurrentPoints() {
        return p2numCurrentPoints;
    }

    public int getP3numCurrentPoints() {
        return p3numCurrentPoints;
    }

    public int getP4numCurrentPoints() {
        return p4numCurrentPoints;
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

    public int getMaxPoints() { return maxPoints;}

    public Deck getDeck() { return deck;}

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
    public void setP1numCurrentPoints(int p1numCurrentPoints) {
        this.p1numCurrentPoints = p1numCurrentPoints;
    }

    public void setP2numCurrentPoints(int p2numCurrentPoints) {
        this.p2numCurrentPoints = p2numCurrentPoints;
    }

    public void setP3numCurrentPoints(int p3numCurrentPoints) {
        this.p3numCurrentPoints = p3numCurrentPoints;
    }

    public void setP4numCurrentPoints(int p4numCurrentPoints) {
        this.p4numCurrentPoints = p4numCurrentPoints;
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

    public void setDeck(Deck newDeck) { this.deck = newDeck; }

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

    public void setMaxPoints(int maxPoints) {this.maxPoints = maxPoints; }

    public void setTricksPlayed(int tricksPlayed) {
        this.tricksPlayed = tricksPlayed;
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