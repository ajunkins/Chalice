package edu.up.cs301.chalice;

import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import edu.up.cs301.game.GameFramework.GameComputerPlayer;
import edu.up.cs301.game.GameFramework.GameMainActivity;
import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.LocalGame;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;
import static edu.up.cs301.chalice.Card.COINS;
import static edu.up.cs301.chalice.Card.CUPS;
import static edu.up.cs301.chalice.Card.SWORDS;

/**
 *Chalice Local Game class
 *
 * contains all gameplay functions for the cups (Chalice) game
 *
 * @version December 4, 2020
 * @author  Alex Junkins, Malia Lundstrom, Chloe Campbell, Addison Raak
 */
public class ChaliceLocalGame extends LocalGame {

    //declare instance of chaliceGameState
    private ChaliceGameState state;
    private static int gameLength = 50;
    private Card[][] allPassingCards;
    //the current passing pattern
    public static int passingPattern;
    private GameMainActivity myActivity;
    private static String TAG = "ChaliceLocalGame: ";

    /**
     * Initial constructor
     * Deals the cards to the player and sets the starting player to
     * whoever has the 2 of coins.
     *
     */
    public ChaliceLocalGame(GameMainActivity activity) {
        state = new ChaliceGameState();
        state.setMaxPoints(gameLength);
        state.dealCards();
        state.setSuitLed(COINS);
        allPassingCards = new Card[4][3];
        passingPattern = 0;
        myActivity = activity;
    } //ChaliceLocalGame


    /**
     * Deep-copy constructor
     * @param localGame game to be copied
     */
    public ChaliceLocalGame(ChaliceLocalGame localGame) {
        state = new ChaliceGameState(localGame.state);
        allPassingCards = localGame.allPassingCards;
        passingPattern = localGame.passingPattern;
        myActivity = localGame.myActivity;
    } //ChaliceLocalGame


    /**
     * starts the game (overridden)
     * modified to implement naming of AI
     *
     * @param players
     * 			the list of players who are playing in the game
     */
    @Override
    public void start(GamePlayer[] players) {
        super.start(players);

        //build a new list of names
        for (int i = 0; i < playerNames.length; i++){
            if (players[i] instanceof PlayerComputerSimple ||
                    players[i] instanceof PlayerComputerAdvanced){
                GamePlayer ai_ref = players[i];
                try{
                    Thread.sleep(30); //nothing worked without this line
                } catch (InterruptedException e){
                    //do nothing
                }
                String givenName = playerNames[i];
                //check if they are the default names
                if (givenName.equals(myActivity.getString(R.string.comp1Def)) ||
                        givenName.equals(myActivity.getString(R.string.comp2Def) ) ||
                        givenName.equals(myActivity.getString(R.string.comp3Def))) {
                    String newName = getAIPlayerName((GameComputerPlayer)ai_ref);
                    //ai_ref.setName(newName);
                    playerNames[i] = newName;
                }

            }
        }
        Log.i("LocalGame", "start: Hi!");
    } //start

    /**
     * A method to get one of the predetermined names
     * Will only send a new name to display if the player
     * is an AI.
     * @param AIPlayer  A reference to the AI player
     * @return          A string holding the name for the player
     */
    public String getAIPlayerName(GameComputerPlayer AIPlayer){
        String name = "No-Name Nathan";
        if (AIPlayer instanceof PlayerComputerSimple){
            PlayerComputerSimple simpleRef = (PlayerComputerSimple)AIPlayer;
            switch(simpleRef.getPlayerNum()){
                case 0:
                    name = myActivity.getString(R.string.dumbAI1);
                    break;
                case 1:
                    name = myActivity.getString(R.string.dumbAI2);
                    break;
                case 2:
                    name = myActivity.getString(R.string.dumbAI3);
                    break;
                case 3: //breadward
                    name = myActivity.getString(R.string.dumbAI4);
                    break;
                default: //how'd you get here?
                    name = "Quintuple D Action!";
                    break;
            }
        }
        if (AIPlayer instanceof PlayerComputerAdvanced){
            PlayerComputerSimple simpleRef = (PlayerComputerSimple)AIPlayer;
            switch(simpleRef.getPlayerNum()){
                case 0:
                    name = myActivity.getString(R.string.smartAI1);
                    break;
                case 1:
                    name = myActivity.getString(R.string.smartAI2);
                    break;
                case 2:
                    name = myActivity.getString(R.string.smartAI3);
                    break;
                case 3:
                    name = myActivity.getString(R.string.smartAI4);
                    break;
                default: //how'd you get here?
                    name = "The extra name";
                    break;
            }
        }
        return name;
    } //getAIPlayerName


    /**
     * a method to set the game length
     * @param length    the length of the game in maximum points
     */
    public static void setGameLength(int length){
        gameLength = length;
    } //setGameLength

    /**
     * a method to modify the game state to make the holder of the
     * 2 of Coins the player whose turn it is
     */
    private void setTrickStartingPlayer() {
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
    } //setTrickStartingPlayer

    /**
     * A method to check if a card belongs to the leading suit
     *
     * @param card  card to check
     * @return      In suit
     */
    boolean isInSuit (Card card) {
        int suit = card.getCardSuit();
        return state.getSuitLed() == suit;
    } //isInSuit

    /**
     * Determines which player is meant to collect the trick
     *
     * @return the index of the player who takes the trick
     */
    public int collectTrick() {
        //if suit of card played == suitLed
        int winnerID;
        int highVal = 0;
        Card highCard = new Card(2, state.getSuitLed());
        for (Card card : state.getTrickCardsPlayed()) {
            if (card.getCardSuit() == state.getSuitLed()) {
                if (card.getCardVal() == 1){
                    highVal = 13;
                    highCard = card;
                } else if (highVal < card.getCardVal()) {
                    highVal = card.getCardVal();
                    highCard = card;
                }
            }
        }
        if (Card.sameCard(highCard, state.getP1CardPlayed())) {
            state.setP1RunningPoints(state.getP1RunningPoints()
                    + state.pointsInTrick());
            winnerID = 0;
        } else if (Card.sameCard(highCard, state.getP2CardPlayed())) {
            state.setP2RunningPoints(state.getP2RunningPoints()
                    + state.pointsInTrick());
            winnerID =  1;
        } else if (Card.sameCard(highCard, state.getP3CardPlayed())) {
            state.setP3RunningPoints(state.getP3RunningPoints()
                    + state.pointsInTrick());
            winnerID =  2;
        } else  {
            state.setP4RunningPoints(state.getP4RunningPoints()
                    + state.pointsInTrick());
            winnerID =  3;
        }
        requestAISpeechTrickEnd(winnerID);
        state.setP1CardPlayed(null);
        state.setP2CardPlayed(null);
        state.setP3CardPlayed(null);
        state.setP4CardPlayed(null);
        Log.i("End Trick Report", "P1 running points: " + state.getP1RunningPoints());
        Log.i("End Trick Report", "P2 running points: " + state.getP2RunningPoints());
        Log.i("End Trick Report", "P3 running points: " + state.getP3RunningPoints());
        Log.i("End Trick Report", "P4 running points: " + state.getP4RunningPoints());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return winnerID;
    } //collectTrick



    /**
     * Method to update the current points for all players, handles shooting the moon
     */
    private void updatePoints() {
        int id = checkShootMoon();
        if(id !=-1) {
            requestAISpeechShotMoon(id);
            switch (id) {
                case 0:
                    state.setP2CurrentPoints(state.getP2CurrentPoints()+26);
                    state.setP3CurrentPoints(state.getP3CurrentPoints()+26);
                    state.setP4CurrentPoints(state.getP4CurrentPoints()+26);
                    break;
                case 1:
                    state.setP1CurrentPoints(state.getP1CurrentPoints()+26);
                    state.setP3CurrentPoints(state.getP3CurrentPoints()+26);
                    state.setP4CurrentPoints(state.getP4CurrentPoints()+26);
                    break;
                case 2:
                    state.setP1CurrentPoints(state.getP1CurrentPoints()+26);
                    state.setP2CurrentPoints(state.getP2CurrentPoints()+26);
                    state.setP4CurrentPoints(state.getP4CurrentPoints()+26);
                    break;
                case 3:
                    state.setP1CurrentPoints(state.getP1CurrentPoints()+26);
                    state.setP2CurrentPoints(state.getP2CurrentPoints()+26);
                    state.setP3CurrentPoints(state.getP3CurrentPoints()+26);
                    break;
            }
        }
        else {
            state.setP1CurrentPoints(
                    state.getP1CurrentPoints() + state.getP1RunningPoints());
            state.setP2CurrentPoints(
                    state.getP2CurrentPoints() + state.getP2RunningPoints());
            state.setP3CurrentPoints(
                    state.getP3CurrentPoints() + state.getP3RunningPoints());
            state.setP4CurrentPoints(
                    state.getP4CurrentPoints() + state.getP4RunningPoints());
        }
    } //updatePoints

    /**
     * a method to re-set a game state to the beginning of a hand,
     * while keeping the data necessary from previous rounds
     * note: this modifies this class's state instance variable
     */
    private void initializeHand() {
        //make running points into current points and reset running points
        updatePoints();
        checkIfGameOver();

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
        state.setCupsBroken(false);
        state.setSuitLed(COINS);
        state.setTricksPlayed(0);
        state.setWhoTurn(0);

        //setup the passing phase
        state.dealCards();
        if (passingPattern != 3){
            state.setPassingCards(true); //we are passing cards now
        } else {
            passingPattern = 0; //if on fourth hand, no pass
            state.setPassingCards(false);
            setTrickStartingPlayer();
        }
        state.setCardsPassed(0);
    } //initializeHand


    /**
     * A method to check if a card is a valid play, given the current state of the game
     *
     * @param card      the card to check
     * @param hand      the rest of the hand
     * @return          legality status of the card
     */
    public boolean isCardValid(ArrayList<Card> hand, Card card) {
        if (!isCardValidErrorChecks(card)){
            return false;
        }
        // specific case when ALL cards in hand are point cards and
        // cups haven't been broken when starting a trick
        for (Card c : hand) {
            if (state.getTricksPlayed() == 0 && (card.getCardSuit() == CUPS ||
                    (card.getCardSuit() == SWORDS && card.getCardVal() == 12)) &&
                    !state.isCupsBroken() && (c.getCardSuit() != CUPS ||
                    (c.getCardSuit() == SWORDS && c.getCardVal() == 12))) {
                return false;
            }
        }
        if (state.getTricksPlayed() == 0 && state.getTrickCardsPlayed().size() == 0) {
            return card.getCardSuit() == COINS && card.getCardVal() == 2;
        }
        // makes it so a cup cannot be played first if the cups have not been broken
        if (state.getTrickCardsPlayed().size() == 0 && !state.isCupsBroken() &&
            card.getCardSuit() == CUPS) { return false; }
        if(isInSuit(card)) { return true; }
        else {
            for (Card c : hand) {
                if (c.getCardSuit() == state.getSuitLed()) { return false; }
            }
            if (state.isCupsBroken()) {
                return true;
            } else {
                //Players are allowed to play a cup or the QoS if it's not the first trick.
                if (card.getCardSuit() == CUPS || (card.getCardSuit() == SWORDS &&
                        card.getCardSuit() == 12)) {
                    return state.getTricksPlayed() != 0;
                } else { return true; }
            }
        }
    } //isCardValid

    /**
     * A helper method to check for specifically invalid cards
     * @param card  The card to be checked
     * @return      Success value
     */
    private boolean isCardValidErrorChecks(Card card){
        if (card == null){
            Log.e(TAG, "isCardValid: card parameter was null");
            return false;
        }
        if (card.getCardSuit() < 1 || card.getCardSuit() > 4){
            Log.e("LocalGame: ", "Card with suit " +
                    card.getCardSuit() + " is invalid.");
            return false;
        }
        if (card.getCardVal() < 1 || card.getCardVal() > 13) {
            Log.e("LocalGame: ", "Card with value " +
                    card.getCardVal() + " is invalid.");
            return false;
        }
        return true;
    } //isCardValidErrorChecks

    /**
     * A method to check if it is legal to play the selected card in a player's hand
     *
     * @return  legality status
     */
    boolean playCard(GameAction action) {
        switch (state.getWhoTurn()) {
            case 0:
                //check if card is valid
                if (isCardValid(state.getP1Hand(),
                        ((ActionPlayCard) action).playedCard())) {
                    //set the played card to the correct players cardPlayed
                    state.setP1CardPlayed(((ActionPlayCard) action).playedCard());
                    state.getCardsPlayed().add(((ActionPlayCard) action).playedCard());
                    //removes card from hand
                    state.setP1Hand(removeCard(state.getP1Hand(),
                            ((ActionPlayCard) action).playedCard()));
                    //next players turn and check if cups broken
                    changeTurnandCupsBroken(0,action);
                    return true;
                } else {
                    return false;
                }
            case 1:
                if (isCardValid(state.getP2Hand(),
                        ((ActionPlayCard) action).playedCard())) {
                    state.setP2CardPlayed(((ActionPlayCard) action).playedCard());
                    state.setP2Hand(removeCard(state.getP2Hand(),
                            ((ActionPlayCard) action).playedCard()));
                    state.getCardsPlayed().add(((ActionPlayCard) action).playedCard());
                    changeTurnandCupsBroken(1,action);
                    return true;
                } else {
                    return false;
                }
            case 2:
                if (isCardValid(state.getP3Hand(),
                        ((ActionPlayCard) action).playedCard())) {
                    state.setP3CardPlayed(((ActionPlayCard) action).playedCard());
                    state.setP3Hand(removeCard(state.getP3Hand(),
                            ((ActionPlayCard) action).playedCard()));
                    state.getCardsPlayed().add(((ActionPlayCard) action).playedCard());
                    changeTurnandCupsBroken(2,action);
                    return true;
                } else {
                    return false;
                }
            case 3:
                if (isCardValid(state.getP4Hand(),
                        ((ActionPlayCard) action).playedCard())) {
                    state.setP4CardPlayed(((ActionPlayCard) action).playedCard());
                    state.setP4Hand(removeCard(state.getP4Hand(),
                            ((ActionPlayCard) action).playedCard()));
                    state.getCardsPlayed().add(((ActionPlayCard) action).playedCard());
                    changeTurnandCupsBroken(3,action);
                    return true;
                } else {
                    return false;
                }
            default:
                return false;
        }
    } //playCard

    /**
     * Switches the turn to the next player. And breaks cups if needed
     * @param playerID -- the id of the current player
     * @param action -- the action played
     */
    public void changeTurnandCupsBroken(int playerID, GameAction action) {
        if(!isTrickOver()) {
            switch (playerID) {
                case 0: case 1: case 2:
                    state.setWhoTurn(state.getWhoTurn()+1);
                    break;
                case 3:
                    state.setWhoTurn(0);
                    break;
            }
            Card theCard = ((ActionPlayCard) action).playedCard();
            if(theCard.getCardSuit() == CUPS ||theCard.getCardVal() ==12
                    && theCard.getCardSuit() == SWORDS) {
                state.setCupsBroken(true);
            }
        }
    } //changeTurnandCupsBroken


    /**
     * send the updated status of the game to a given player
     */
    @Override
    protected void sendUpdatedStateTo(GamePlayer player) {
        player.sendInfo(new ChaliceGameState(state));
    } //sendUpdatedStateTo

    /**
     * this method prints the values of all of the variables in the
     * game state by saving them all to a String.
     *
     * @return  Current score of all players, running score of all players,
     *          number of cards in hand, references for suit numbers
     */
    @Override
    public String toString() {

        //sets the ArrayList to a String to be returned with the rest of the info
        String workingHandString = "";
        for (Card tempCard : state.getP1Hand()) {
            workingHandString = workingHandString + "Suit: " +
                    tempCard.getCardSuit() +
                    "\tValue: " + tempCard.getCardVal() + "\n";
        }
        state.setP1HandString(workingHandString);
        workingHandString = "";

        for (Card tempCard : state.getP2Hand()) {
            workingHandString = workingHandString +
                    "Suit: " + tempCard.getCardSuit() +
                    "\tValue: " + tempCard.getCardVal() + "\n";
        }
        state.setP2HandString(workingHandString);
        workingHandString = "";

        for (Card tempCard : state.getP3Hand()) {
            workingHandString = workingHandString + "Suit: " +
                    tempCard.getCardSuit() + "\tValue: " +
                    tempCard.getCardVal() + "\n";
        }
        state.setP3HandString(workingHandString);
        workingHandString = "";

        for (Card tempCard : state.getP4Hand()) {
            workingHandString = workingHandString + "Suit: " +
                    tempCard.getCardSuit() + "\tValue: " +
                    tempCard.getCardVal() + "\n";
        }
        state.setP4HandString(workingHandString);

        //prints the CURRENT score of the players to the Logcat Info
        return printToStringHelper();
    }//toString

    /**
     * print helper method for toString
     */
    private String printToStringHelper() {

        return "Player 1 Current Points: " + state.getP1CurrentPoints() + "\n" +
                "Player 2 Current Points: " + state.getP2CurrentPoints() + "\n" +
                "Player 3 Current Points: " + state.getP3CurrentPoints() + "\n" +
                "Player 4 Current Points: " + state.getP4CurrentPoints() + "\n"

                //prints the RUNNING score of the players to the
                //Logcat Info window
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
    }//printToStringHelper

    /**
     * can this player move
     *
     * @return
     * 		true, because all player are always allowed to move at all times,
     * 		as this is a fully asynchronous game
     */
    @Override
    protected boolean canMove(int playerIdx) {
        return state.getWhoTurn() == playerIdx;
    } //canMove

    /**
     * process action
     *
     * @param action, an action that can be quit or play
     */
    @Override
    protected boolean makeMove(GameAction action) {
        Log.i("action", action.getClass().toString());
        if (action instanceof ActionPassCards) {
            return makeMoveActionPassCards(action);
        }
        else if (action instanceof  ActionPlayCard){
            return makeMoveActionPlayCard(action);
        }
        else if (action instanceof ActionQuit){
            System.exit(0);
            return false; //placeholder code
        } else if (action instanceof ActionSpeak){
            return makeMoveActionSpeak(action);
        }
        else {
            // denote that this was an illegal move
            return false;
        }
    } //makeMove

    /**
     * A method to ask for a speech response from the AIs when
     * a trick is over
     * @param winnerID  The winner of the trick
     */
    public void requestAISpeechTrickEnd(int winnerID){
        ArrayList<Card> cardsPlayed = state.getTrickCardsPlayed();
        ArrayList<Card> pointCardsPlayed =
                PlayerComputerSimple.getPointCardsFromList(cardsPlayed, true);
        //if the winner took points, have them react appropriately
        if (PlayerComputerSimple.getCardInList(pointCardsPlayed,
                SWORDS, 12) != null){
            //winner took the queen of swords
            for (int i = 0; i < players.length; i++){
                GamePlayer queenTaker = players[i];
                if (i == winnerID){
                    makeMoveActionSpeak(queenTaker,
                            InfoDisplaySpeech.speechType.ANGRY);
                } else {
                    makeMoveActionSpeak(queenTaker,
                            InfoDisplaySpeech.speechType.HAPPY);
                }
            }
        } else if (pointCardsPlayed.size() > 0){
            //winner took normal points
            GamePlayer pointTaker = players[winnerID];
            makeMoveActionSpeak(pointTaker,
                    InfoDisplaySpeech.speechType.SAD);
        }
    } //requestAISpeechTrickEnd

    /**
     * A method to ask for a speech response from the AIs when
     * a player shoots the moon
     * @param shooterID  The winner of the trick
     */
    public void requestAISpeechShotMoon(int shooterID){
        for (int i = 0; i < players.length; i++){
            if (i == shooterID){
                //if I shot the moon, say something happy
                makeMoveActionSpeak(players[i],
                        InfoDisplaySpeech.speechType.HAPPY);
            } else {
                //if someone else did, say something surprised
                makeMoveActionSpeak(players[i],
                        InfoDisplaySpeech.speechType.SURPRISE);
            }
        }
    } //requestAISpeechShotMoon

    /**
     * A helper method to find the human player
     * returns null if it cannot locate a PlayerHuman in the
     * players instance array
     * CAVEAT: if this returns null, there is somehow no human
     *         player.
     * @return  a reference to the player
     */
    private PlayerHuman findHumanPlayer(){
        for (GamePlayer p : players){
            if (p instanceof PlayerHuman){
                return (PlayerHuman)p;
            }
        }
        return null;
    } //findHumanPlayer

    /**
     * A method to process an AI's speech action to the player, that will carry
     * the information to show what the AI is saying
     * @param action    The speech action from the AI
     * @return          success value
     */
    private boolean makeMoveActionSpeak(GameAction action){
        SpeechRunner runner = new SpeechRunner((ActionSpeak)action);
        Thread t = new Thread(runner);
        t.start();
        return false;
    } //makeMoveActonSpeak

    /**
     * An alternate method to start an AI's speech thread with the
     * SpeechRunner helper class
     * @param speaker       a reference to the AI speaker
     * @param speech        the type of speech
     */
    private void makeMoveActionSpeak(GamePlayer speaker,
                                     InfoDisplaySpeech.speechType speech){
        SpeechRunner runner = new SpeechRunner(speaker, speech);
        Thread t = new Thread(runner);
        t.start();

    } //makeMoveActionSpeak

    /**
     * SpeechRunner Class
     * A helper class to manage a short thread to send a speech update display to
     * the player
     * @version December 3, 2020
     * @author  Alex Junkins, Malia Lundstrom, Chloe Campbell, Addison Raak
     */
    private class SpeechRunner implements Runnable{

        //a reference to the human player
        PlayerHuman human;
        //the AI that spoke
        GamePlayer speaker;
        //the speaker's personality
        PlayerComputerAdvanced.personalityType personality;
        //the type of speech
        InfoDisplaySpeech.speechType speech;

        /**
         * constructor
         * @param actionRef the reference to the action
         */
        public SpeechRunner(ActionSpeak actionRef){
            /**
             External Citation
             Date: 4 December 2020
             Problem: Could not figure out how to run a short method in a
             separate thread
             Resource:
             https://stackoverflow.com/questions/3489543/
             how-to-call-a-method-with-a-separate-thread-in-java
             Solution:
             Create a helper class that implements runnable and have it
             manage the new thread
             */
            this.human = findHumanPlayer();
            this.speaker = actionRef.getPlayer();
            if (actionRef.getPlayer() instanceof PlayerComputerAdvanced){
                personality = ((PlayerComputerAdvanced)speaker).getPersonality();
            } else {
                personality = null;
            }
            this.speech = actionRef.getSpeech();
        } //SpeechRunner

        /**
         * Alternate constructor to avoid creating an action
         * @param speaker   the AI that spoke
         * @param speech        the type of speech
         */
        public SpeechRunner(GamePlayer speaker,
                            InfoDisplaySpeech.speechType speech){
            this.human = findHumanPlayer();
            this.speaker = speaker;
            if (speaker instanceof PlayerComputerAdvanced){
                personality = ((PlayerComputerAdvanced)speaker).getPersonality();
            } else {
                personality = null;
            }
            this.speech = speech;
        } //SpeechRunner


        @Override
        public void run() {
            //create and send the message
            InfoDisplaySpeech playerSpeechMessage =
                    new InfoDisplaySpeech(speaker, personality, speech);
            human.sendInfo(playerSpeechMessage);

            //wait for 5 seconds
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //send the reset message
            InfoDisplaySpeech endMessage =
                    new InfoDisplaySpeech(speaker,
                            null, null);
            human.sendInfo(endMessage);
        } //run
    } //SpeechRunner

    /**
     * A method to handle behavior when the game receives a playCard action
     * @param action    the action
     * @return          legality status
     */
    private boolean makeMoveActionPassCards(GameAction action){
        if (!state.getPassingCards()){
            return false;
        }
        ActionPassCards actionRef = (ActionPassCards)action;
        if (state.getWhoTurn() != actionRef.getPlayerNum()){
            return false;
        }
        allPassingCards[actionRef.getPlayerNum()] = actionRef.passedCards();
        //remove received cards from player hand
        for (Card card : actionRef.passedCards()) {
            ArrayList<Card> playerHand = PlayerComputerSimple.getMyHand(state,
                    actionRef.getPlayerNum());
            for (int i = 0; i < playerHand.size(); i++) {
                if (Card.sameCard(playerHand.get(i), card)) {
                    playerHand.remove(i);
                }
            }
        }
        state.setCardsPassed(state.getCardsPassed() + 3);
        state.setWhoTurn(state.getWhoTurn() + 1);
        if (state.getWhoTurn() == 4){
            state.setWhoTurn(0);
        }

        //check if all players have passed cards. If not, return true;
        for (int i = 0; i < 4; i++){
            if (allPassingCards[i][0] == null){
                return true;
            }
        }

        //if all players have passed cards, redistribute the cards
        distributePassedCards();

        //once all cards are distributed, transfer to normal play
        setTrickStartingPlayer();
        state.setPassingCards(false);
        allPassingCards = new Card[4][3];

        return true;
    } //makeMoveActionPassCards

    /**
     * method to transfer passed cards into hands
     */
    private void distributePassedCards(){
        switch (passingPattern){
            case 0:
                passArrayIntoHand(state.getP1Hand(), allPassingCards[3]);
                passArrayIntoHand(state.getP2Hand(), allPassingCards[0]);
                passArrayIntoHand(state.getP3Hand(), allPassingCards[1]);
                passArrayIntoHand(state.getP4Hand(), allPassingCards[2]);
                break;
            case 1:
                passArrayIntoHand(state.getP1Hand(), allPassingCards[1]);
                passArrayIntoHand(state.getP2Hand(), allPassingCards[2]);
                passArrayIntoHand(state.getP3Hand(), allPassingCards[3]);
                passArrayIntoHand(state.getP4Hand(), allPassingCards[0]);
                break;
            case 2:
                passArrayIntoHand(state.getP1Hand(), allPassingCards[2]);
                passArrayIntoHand(state.getP2Hand(), allPassingCards[3]);
                passArrayIntoHand(state.getP3Hand(), allPassingCards[0]);
                passArrayIntoHand(state.getP4Hand(), allPassingCards[1]);
                break;
            case 3:
                //no passing
                break;
            default: //how are you here
                Log.e("distributePassedCards: ", "switch error in DPC");
        }
        //increment passingPattern
        passingPattern += 1;
        if (passingPattern == 4){
            passingPattern = 0;
        }
    } //distributePassedCards

    /**
     * Adds the passed cards to the hand
     *
     * @param hand -- hand that needs the cards
     * @param passingCards -- the chosen 3 to be given
     */
    private void passArrayIntoHand(ArrayList<Card> hand, Card[] passingCards) {
        hand.addAll(Arrays.asList(passingCards));
    } //passArrayIntoHand

    /**
     * A method to handle behavior when the game receives a playCard action
     * @param action    the action
     * @return          legality status
     */
    public boolean makeMoveActionPlayCard(GameAction action){
        if (state.getPassingCards()){
            return false;
        }
        if (state.getTrickCardsPlayed().size() == 0 && state.getTricksPlayed() !=0) {
            state.setSuitLed(((ActionPlayCard) action).playedCard().getCardSuit());
            Log.i("makeMoveActionPlayCard", "makeMoveActionPlayCard: "
                    + ((ActionPlayCard) action).playedCard().getCardSuit());
        }
        boolean validCard = playCard(action);
        if (!validCard){
            Log.i("debugging alert", "Makemove: card  in suit " +
                    ((ActionPlayCard) action).playedCard().getCardSuit() +
                    " with value " +
                    ((ActionPlayCard) action).playedCard().getCardVal() +
                    " was deemed illegal for play.");
            return false;
        }
        //if it's a cup, set cups broken to true
        if (((ActionPlayCard) action).playedCard().getCardSuit() == CUPS) {
            state.setCupsBroken(true);
        }
        if(isTrickOver()) {
            for (GamePlayer player : players){
                if (player instanceof PlayerHuman){
                    sendUpdatedStateTo(player);
                }
            }
            int playerID = collectTrick();
            state.setWhoTurn(playerID);
            state.setSuitLed(-1);
            state.setTricksPlayed(state.getTricksPlayed()+1);
        }
        if (isHandOver()){
            state.setHandsPlayed(state.getHandsPlayed() + 1);
            initializeHand();
        }
        return true;
    } //makeMoveActionPlayCard

    /**
     * Determines if the trick is over.
     *
     * @return whether or not there have been four cards played and the trick
     * is over
     */
    protected boolean isTrickOver() {
        return state.getTrickCardsPlayed().size() == 4;
    } //isTrickOver

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
    } //isHandOver

    /**
     * Check if the game is over. It is over, return a string that tells
     * who the winner(s), if any, are. If the game is not over, return null;
     *
     * @return
     * 		a message that tells who has won the game, or null if the
     * 		game is not over
     */
    @Override
    protected String checkIfGameOver() {
        //check if any of the players have met or passed the max score
        boolean scoreMet = false;
        if (state.getP1CurrentPoints() >= state.getMaxPoints()){
            scoreMet = true;
        }
        if (state.getP2CurrentPoints() >= state.getMaxPoints()){scoreMet = true;}
        if (state.getP3CurrentPoints() >= state.getMaxPoints()){scoreMet = true;}
        if (state.getP4CurrentPoints() >= state.getMaxPoints()){scoreMet = true;}
        if (!scoreMet){
            return null;
        }
        if (scoreMet) {
            int min = state.getMaxPoints();
            int playerNum = -1;
            int tiePlayer1 = -1;
            int tiePlayer2 = -1;
            int tiePlayer3 = -1;
            int[] scoreArr = {state.getP1CurrentPoints(), state.getP2CurrentPoints(),
                    state.getP3CurrentPoints(), state.getP4CurrentPoints()};
            for (int i = 0; i < scoreArr.length; i++) {
                if (scoreArr[i] < min) {
                    min = scoreArr[i];
                    playerNum = i;
                }
            }
            for(int i = 0; i < scoreArr.length; i++) {
                if (scoreArr[i] == min && playerNum != i) {
                    if(tiePlayer1 == -1) {
                        tiePlayer1 = i;
                    } else if (tiePlayer2 == -1) {
                        tiePlayer2 = i;
                    } else {
                        tiePlayer3 = i;
                    }
                }
            }
            if(tiePlayer1 == -1) {
                return playerNames[playerNum] + " has won. ";
            } else if (tiePlayer2 == -1) {
                return playerNames[playerNum] + " and " +
                        playerNames[tiePlayer1] + " has won! ";
            } else if (tiePlayer3 == -1) {
                return playerNames[playerNum] + " and " +
                        playerNames[tiePlayer1] + " and " +
                        playerNames[tiePlayer2] + " has won! ";
            } else {
                return "You all tied! How did you manage that? ";
            }
        }
        return null;
    } //checkIfGameOver

    /**
     * Method to handle if somebody got 26 points and shot the moon!
     *
     * @return playerID - the id of the player who shot the moon
     */
    protected int checkShootMoon() {
        int playerID = -1;
        int[] scoreArr = {state.getP1RunningPoints(), state.getP2RunningPoints(),
                state.getP3RunningPoints(), state.getP4RunningPoints()};
        for (int i = 0; i < scoreArr.length; i++) {
            if (scoreArr[i] == 26) {
                playerID=i;
            }
        }
        return playerID;
    } //checkShootMoon

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
    } //removeCard
} //ChaliceLocalGame
