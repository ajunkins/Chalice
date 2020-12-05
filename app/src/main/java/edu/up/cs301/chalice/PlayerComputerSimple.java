package edu.up.cs301.chalice;

import android.util.Log;
import java.util.ArrayList;
import java.util.Random;
import edu.up.cs301.game.GameFramework.GameComputerPlayer;
import edu.up.cs301.game.GameFramework.infoMessage.GameInfo;
import edu.up.cs301.game.GameFramework.utilities.Tickable;
import static edu.up.cs301.chalice.Card.COINS;
import static edu.up.cs301.chalice.Card.CUPS;
import static edu.up.cs301.chalice.Card.SWORDS;

/**
 * Computer Player Simple class
 *
 * This is the simple computer version of a Chalice player.
 *
 * @version December 4, 2020
 * @author  Alex Junkins, Malia Lundstrom, Chloe Campbell, Addison Raak
 */
public class PlayerComputerSimple extends GameComputerPlayer implements Tickable {

    //declare the variables
    protected final String TAG = "PlayerAI";
    protected boolean haveIGreeted = false;

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
    } //PlayerComputerSimple

    /**
     * callback method--game's state has changed
     *
     * @param info
     * 		the information (presumably containing the game's state)
     */
    @Override
    protected void receiveInfo(GameInfo info) {
        // not a state update
        if (!(info instanceof ChaliceGameState)) {
            return;
        }
        ChaliceGameState state = new ChaliceGameState((ChaliceGameState)info);

        checkStartGameMessage(state);

        // not my turn
        if (playerNum != state.getWhoTurn()) {
            return;
        }
        // sleep for 1 second to allow the user to see what card is being
        // played by the computer players
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ActionPlayCard action = null;

        // if it is the very first trick, the first card that must be
        // played is the 2 of coins
        if(state.getTricksPlayed() == 0 &&
                state.getTrickCardsPlayed().size() == 0) {
            //behavior for passing cards
            if (state.getPassingCards()) {
                PickAndPassCards(state);
                return;
            }
        }
        //behavior for playing cards
        PickAndPlayCards(state);
    } //receiveInfo

    /**
     * a method to check if computer player should send a greeting to the user
     * @param localState
     */
    protected void checkStartGameMessage(ChaliceGameState localState){
        if (localState == null){
            return;
        }
        if (localState.getHandsPlayed() == 0 && haveIGreeted == false){
            haveIGreeted = true;
            sendSpeechAction(InfoDisplaySpeech.speechType.GREETING);
        }
    } //checkStartGameMessage

    protected void sendSpeechAction(InfoDisplaySpeech.speechType speech){
        game.sendAction(new ActionSpeak(this, speech));
    } //sendSpeechAction


    /**
     * A method to pick 3 cards to pass from the AI's hand
     */
    protected void PickAndPassCards(ChaliceGameState state) {
        ArrayList<Card> myHand = getMyHand(state, playerNum);
        Card[] pickedCards = new Card[3];
        for (int i = 0; i < 3; i++) {
            pickedCards[i] = myHand.get(i);
        }
        game.sendAction(new ActionPassCards(this,
                this.playerNum, pickedCards));
    } //PickAndPassCards


    /**
     * the method that handles playing cards during the AI's turn
     */
    protected void PickAndPlayCards(ChaliceGameState state){
        if(state.getTricksPlayed() == 0 &&
                state.getTrickCardsPlayed().size() ==0) {
            Card coins2 = new Card(2,COINS);
            int cardIndex = -1;
            for (Card card: getMyHand(state, playerNum)) {
                if(Card.sameCard(card, coins2)) {
                    cardIndex = getMyHand(state, playerNum).indexOf(card);
                }
            }
            game.sendAction(new ActionPlayCard(this, this.playerNum,
                    getMyHand(state, playerNum).get(cardIndex)));
            return;
        }

        //if the dumb AI is going first
        if (state.getTrickCardsPlayed().size() == 0) {
            simplePlayingFirst(state);
            return;
        }

        // if the computer has a card in the suit led
        if(getSuitCardsInHand(state, state.getSuitLed()).size() > 0 ) {
            simplePlayingInSuit(state);
        // if the computer does NOT have a card in the suit led
        } else {
            simplePlayingOutOfSuit(state);
        }
    } //PickAndPlayCards

    /**
     * The simple AI's logic for going first in a trick
     * @param state the current game state
     */
    private void simplePlayingFirst(ChaliceGameState state){
        //if cups is broken, pick a random card in my hand to play
        ArrayList<Card>  myHand = getMyHand(state, playerNum);
        Card playCard = null;
        if (state.isCupsBroken()) {
            playCard = myHand.get(0);
        }
        //if it's not, pick a non-point card from my hand and play it
        else {
            ArrayList<Card> nonPointCards =
                    getPointCardsFromList(myHand, false);

            if (nonPointCards.size() > 0){
                playCard = nonPointCards.get(0);
            } else {
                //I have no non-point cards
                playCard = myHand.get(0);
            }
        }
        game.sendAction(new ActionPlayCard(this, playerNum, playCard));
        return;
    } //simplePlayingFirst

    /**
     * The simple AI's logic for playing when it has a card in-suit
     * @param state the current game state
     */
    private void simplePlayingInSuit(ChaliceGameState state){
        if(pointsOnTheTable(state) > 0) {
            game.sendAction(new ActionPlayCard(this, this.playerNum,
                    getLowestCard(getSuitCardsInHand(state,
                            state.getSuitLed()))));
            return;
        } else if (getCardsPlayedThisTrick(state) == 3) {
            game.sendAction(new ActionPlayCard(this, this.playerNum,
                    getHighestCard(getSuitCardsInHand(state,
                            state.getSuitLed()))));
            return;
        } else {
            if (state.isCupsBroken()){
                game.sendAction(new ActionPlayCard(this, this.playerNum,
                        getSuitCardsInHand(state, state.getSuitLed()).get(0)));
                return;
            } else { //play a random card that isn't a cup
                Random r = new Random();
                int randSuit = r.nextInt(3);
                randSuit += 2;
                for (int i = 2; i <= randSuit; i++) {
                    if (getSuitCardsInHand(state, i).size() != 0) {
                        game.sendAction(new ActionPlayCard(this,
                                this.playerNum, getSuitCardsInHand(state,
                                        state.getSuitLed()).get(0)));
                        return;
                    }
                }
                //fun fact - this case below has a 1 in 6x10^11 chance of
                //occurring somehow, you have all cups and cups isn't broke
                game.sendAction(new ActionPlayCard(this, this.playerNum,
                        getSuitCardsInHand(state, state.getSuitLed()).get(0)));
            }
        }
    } //simplePlayingInSuit

    /**
     * The simple AI's logic for playing when it has a card in-suit
     * @param state the current game state
     */
    private void simplePlayingOutOfSuit(ChaliceGameState state){
        if(getPointCardsInHand(state).size() > 0 && state.isCupsBroken()) {
            game.sendAction(new ActionPlayCard(this, this.playerNum,
                    getHighestCard(getPointCardsInHand(state))));
        } else  {
            game.sendAction(new ActionPlayCard(this, this.playerNum,
                    getHighestCard(getMyHand(state, playerNum))));
        }
    } //simplePlayingOutOfSuit


    /**
     * A method to get all the point cards in a list
     * CAVEAT: this can return an empty list
     * @param cards the list to search
     * @return      the discovered cards
     */
    public static ArrayList<Card> getPointCardsFromList(ArrayList<Card> cards,
                                                    boolean getPoints){
        ArrayList<Card> pointCards = new ArrayList<Card>();
        ArrayList<Card> nonPointCards = new ArrayList<Card>();
        for (Card card : cards){
            if (card.getCardSuit() == CUPS){
                pointCards.add(card);
            } else if (card.getCardSuit() == SWORDS &&
                    card.getCardVal() == 12){
                pointCards.add(card);
            } else {
                nonPointCards.add(card);
            }
        }
        if (getPoints){
            return pointCards;
        } else {
            return nonPointCards;
        }
    } //getPointCardsFromList


    /**
     * method to get the highest card in an array of cards.
     * Returns null if passed an empty list.
     * @param cardStack
     *      stack of cards
     * @return highest card in the stack
     */
    public Card getHighestCard(ArrayList<Card> cardStack) {
        if (cardStack.size() == 0){
            return null;
        }
        Card highest = new Card(-1, -1);
        try{
            highest = cardStack.get(0);
        } catch (IndexOutOfBoundsException e){
            Log.e(TAG, "getHighestCard: " +
                    "could not access element zero of list " +
                    cardStack.toString());
            e.printStackTrace();
            return highest;
        }
        for (Card card : cardStack){
            int compareRes = Card.compareCardVals(highest, card);
            if (compareRes == -1 || compareRes == 0){
                highest = card;
            }
        }
        return highest;
    } //getHighestCard


    /**
     * method to get the lowest card in an array of cards.
     * returns null if passed a null or empty list
     *
     * @param cardStack
     *      stack of cards
     * @return lowest card in the stack
     */
    public Card getLowestCard(ArrayList<Card> cardStack) {
        if (cardStack == null){
            return null;
        }
        if (cardStack.isEmpty()){
            return null;
        }
        Card lowest = cardStack.get(0);
        for (Card card : cardStack){
            int compareRes = Card.compareCardVals(lowest, card);
            if (compareRes == 1 || compareRes == 0){
                lowest = card;
            }
        }
        return lowest;
    } //getLowestCard


    /**
     * method to get the number of cards played in the current trick.
     *
     * @param state
     *      current game state
     * @return number of cards played this trick
     */
    public int getCardsPlayedThisTrick(ChaliceGameState state) {
        return state.getCardsPlayed().size() % 4;
    } //getCardsPlayedThisTrick


    /**
     * method to get the amount of points that are on the table.
     *
     * @param state
     *      current game state
     * @return the amount of points that are on the table.
     */
    public int pointsOnTheTable(ChaliceGameState state){
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
    } //pointsOnTheTable


    /**
     * method to get the cards with points in player's hand.
     *
     * @param state
     *      current game state
     * @return arrayList of cards with points in the player's hand
     */
    public ArrayList<Card> getPointCardsInHand(ChaliceGameState state){
        ArrayList<Card> myHand = getMyHand(state, playerNum);
        ArrayList<Card> pointCards = new ArrayList<Card>();
        for (int i = 0; i < myHand.size(); i++){
            Card currentCard = myHand.get(i);
            if (currentCard.getCardSuit() == Card.CUPS){
                pointCards.add(currentCard);
            }
            else if (currentCard.getCardSuit() == Card.SWORDS &&
                    currentCard.getCardVal() == 12) {
                pointCards.add(currentCard);
            }
        }
        return pointCards;
    } //getPointCardsInHand


    /**
     * method to get the cards in player's hand that belong to given suit
     * CAVEAT: This is capable of returning a null list
     *
     * @param state current game state
     * @param suit  desired suit (1-4)
     * @return      arrayList of cards in given suit and in player's hand
     */
    public ArrayList<Card> getSuitCardsInHand(ChaliceGameState state, int suit){
        ArrayList<Card> myHand = getMyHand(state, playerNum);
        return getSuitCardsInList(myHand, suit);
    } //getSuitCardsInHand


    /**
     * a method to get the cards of a suit in a list of available cards
     * CAVEAT: This is capable of returning an empty
     * @param list  available cards
     * @param suit  desired suit (1-4)
     * @return      list of cards in the suit
     */
    public static ArrayList<Card> getSuitCardsInList(ArrayList<Card> list,
                                                     int suit){
        ArrayList<Card> cardsInSuit = new ArrayList<Card>();
        if (list == null){
            return cardsInSuit;
        }
        for (int i = 0; i < list.size(); i++){
            Card currentCard = list.get(i);
            if (currentCard.getCardSuit() == suit){
                cardsInSuit.add(currentCard);
            }
        }
        return cardsInSuit;
    } //getSuitCardsInList

    /**
     * a method to get all the cards in a list not of a suit
     * CAVEAT: This is capable of returning an empty
     * @param list  available cards
     * @param suit  suit to avoid (1-4)
     * @return      list of cards not in the suit
     */
    public static ArrayList<Card> getNonSuitCardsInList(ArrayList<Card> list,
                                                     int suit){
        ArrayList<Card> cardsInSuit = new ArrayList<Card>();
        if (list == null){
            return cardsInSuit;
        }
        for (int i = 0; i < list.size(); i++){
            Card currentCard = list.get(i);
            if (currentCard.getCardSuit() != suit){
                cardsInSuit.add(currentCard);
            }
        }
        return cardsInSuit;
    } //getNonSuitCardsInList

    /**
     * method to get player's hand.
     *
     * @param state
     *      current game state
     * @return arrayList of cards in player's hand
     */
    public static ArrayList<Card> getMyHand(ChaliceGameState state,
                                            int playerNum) {
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
                Log.e("PlayerNum Error",
                        "AI player had an invalid playerNum");
                return null;
        }
        return myHand;
    } //getMyHand

    /**
     * method to get player's running point count.
     * @return  points
     */
    protected int getMyRunningPoints(ChaliceGameState state){
        switch(playerNum){
            case 0:
                return state.getP1RunningPoints();
            case 1:
                return state.getP2RunningPoints();
            case 2:
                return state.getP3RunningPoints();
            case 3:
                return state.getP4RunningPoints();
            default:
                //shit's broken
                Log.e("PlayerNum Error",
                        "AI player had an invalid playerNum");
                return -1;
        }
    } //getMyRunningPoints

    /**
     * A method to search an arrayList for a specific card via
     * a suit and a value
     * @param list  the list to be searched
     * @param suit  the desired suit
     * @param val   the desired value
     * @return      the card. Returns null if card is not present
     */
    public static Card getCardInList(ArrayList<Card> list, int suit, int val){
        Card foundCard = null;
        for (Card card : list){
            if (card.getCardVal() == val && card.getCardSuit() == suit){
                foundCard = card;
            }
        }
        return foundCard;
    } //getCardInList

    //getters and setters
    public String getName(){
        return this.name;
    }

    public void setName(String newName){
        this.name = newName;
    }

    public int getPlayerNum(){
        return this.playerNum;
    }
}
