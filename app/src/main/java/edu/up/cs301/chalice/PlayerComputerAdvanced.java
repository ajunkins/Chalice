

package edu.up.cs301.chalice;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import edu.up.cs301.game.GameFramework.infoMessage.GameInfo;
import edu.up.cs301.game.GameFramework.utilities.Tickable;

import static edu.up.cs301.chalice.Card.COINS;
import static edu.up.cs301.chalice.Card.CUPS;
import static edu.up.cs301.chalice.Card.SWORDS;
import static edu.up.cs301.chalice.Card.WANDS;

/**
 * PlayerComputerAdvanced class
 * contains a pass card action
 *
 * @version November 25, 2020
 * @author  Alex Junkins, Malia Lundstrom, Chloe Campbell, Addison Raak
 */

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
            {false, true, true, true, true} //player 4
        };
    boolean [][] possibleSuits;

    ChaliceGameState localState;

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
        if (!(info instanceof ChaliceGameState)) {
            return;
        }
        localState = new ChaliceGameState((ChaliceGameState)info);

        //if it's a new hand, reset strategy variables
        if (localState.getTricksPlayed() == 0){
            possibleSuits = possibleSuitsDefault;
        }

        //not my turn
        if (playerNum != localState.getWhoTurn()) { return; }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //observe the cards that have been played
        //if a player has gone against the suit, note that they have no more
        // cards in that suit
        CheckPlayedCards();

        //check that my hand isn't null
        if (getMyHand(localState, playerNum) == null){
            Log.e(TAG, "receiveInfo: player hand was null.");
            return;
        }

        //behavior for passing cards
        if (localState.getPassingCards()){
            PickAndPassCards(localState);
            return;
        }

        //behavior for playing cards
        PickAndPlayCards(localState);
    }

    /**
     * A method to pick 3 cards to pass from the AI's hand
     */
    @Override
    protected void PickAndPassCards(ChaliceGameState state){
        //picks the 3 highest value cards in the player's hand
        ArrayList<Card> myHand = getMyHand(state, playerNum);
        Card[] pickedCards = new Card[3];
        for (int i = 0; i < 3; i++){
            Card pickedCard = getHighestCard(myHand);
            pickedCards[i] = pickedCard;
            myHand.remove(pickedCard);
        }
        game.sendAction(new ActionPassCards(this, this.playerNum, pickedCards));
    }

    /**
     * the method that handles playing cards during the AI's turn
     */
    @Override
    protected void PickAndPlayCards(ChaliceGameState state){
        //play the 2 of coins if I am the first in the hand
        if(localState.getTricksPlayed() == 0 &&
                localState.getTrickCardsPlayed().size() ==0) {
            Card coins2 = new Card(2,COINS);
            int cardIndex=-1;
            for (Card card: getMyHand(localState, playerNum)) {
                if(Card.sameCard(card, coins2)) {
                    cardIndex = getMyHand(localState, playerNum).indexOf(card);
                }
            }
            game.sendAction((new ActionPlayCard(this, this.playerNum,
                    getMyHand(localState, playerNum).get(cardIndex))));
            return;
        }
        ArrayList<Card> cardsInLedSuit =
                getSuitCardsInHand(localState, localState.getSuitLed());
        boolean success;
        //Trick / Hand – Normal
        if(getMyRunningPoints(localState) < 16) {
            success = playCardNormal(cardsInLedSuit);
        }
        //Trick / Hand – Shooting the moon
        else { success = playCardShootingMoon(cardsInLedSuit); }
        if (!success){
            //this is logically impossible to throw. If you did, congratulations.
            Log.e(TAG, "receiveInfo: AI player was unable to play a card!");
        }
    }

    /**
     * The method the AI uses to pick a card to play when it is not
     * trying to shoot the moon.
     * todo this method needs to be pared down to meet the coding standard
     * @return success status
     */
    protected boolean playCardNormal(ArrayList<Card> handCardsInLedSuit){
        ArrayList<Card> myHand =
                Objects.requireNonNull(getMyHand(localState, playerNum));
        //if going first
        if(localState.getTrickCardsPlayed().size() == 0) {
            return advancedNormalPlayGoingFirst(myHand);
        }
        //not going first, but have a card in-suit
        else if(handCardsInLedSuit.size() > 0) {
            return advancedNormalPlayInSuit(handCardsInLedSuit);
        } else { //I'm not going first, I have no card in the led suit
            return advancedNormalPlayOutSuit(myHand);
        }
    }

    /**
     * The method the AI uses to pick a card to play when it is not
     * trying to shoot the moon.
     * todo this method needs to be pared down to meet the coding standard
     * @return  success value
     */
    protected boolean playCardShootingMoon(ArrayList<Card> handCardsInLedSuit){
        //if I'm going first
        if(localState.getTrickCardsPlayed().size() == 0) {
            return advancedShootingPlayGoingFirst(handCardsInLedSuit);
        }
        //I'm not going first, but I have cards in-suit. I can still get points!
        else if(!handCardsInLedSuit.isEmpty()) {
            return advancedShootingPlayInSuit(handCardsInLedSuit);
        }
        //I can't win any points this round,
        //might as well play a low card of another suit
        else {
            return advancedShootingPlayOutSuit();
        }
    }



    /**
     * The advanced AI's logic for going first in a trick
     * Normal play
     * @param myHand    the AI's current hand
     * @return          success value
     */
    protected boolean advancedNormalPlayGoingFirst(ArrayList<Card> myHand){
        ArrayList<Card> smallSwordsCards =
                getCardsCompare(myHand, SWORDS, 6, true);
        //have sword < 6, flushing out the queen
        if(smallSwordsCards.size() > 0) {
            game.sendAction(new ActionPlayCard(this,
                    this.playerNum, smallSwordsCards.get(0)));
            return true;
        }
        //play against others' advantages -
        // avoid playing suits other have exhausted
        else if (opponentsMissingSuits()) {
            ArrayList<Integer> desiredSuits = getDesiredSuits();
            playSmallCardFromPriorityList(desiredSuits);
            return true;
        }
        //play a small card in a random suit to
        // minimize chances of taking points
        else {
            ArrayList<Integer> randomSuits = getRandomSuits();
            playSmallCardFromPriorityList(randomSuits);
            return true;
        }
    }

    /**
     * The advanced AI's logic for playing in-suit
     * Normal play
     * @param handCardsInLedSuit    the AI's current hand cards in the led suit
     * @return                      success value
     */
    protected boolean advancedNormalPlayInSuit(ArrayList<Card> handCardsInLedSuit){
        //if there are points on the table (ie avoid getting points)
        if(pointsOnTable() > 0) {
            //Play the highest card that is lower
            // than the current “winning” card;
            playBestCardForTrick(handCardsInLedSuit, localState.getSuitLed());
            return true;
        }
        //if no danger of taking points because I'm last. Shed highest card.
        else if (localState.getTrickCardsPlayed().size() == 3) {
            Card playCard = getHighestCard(handCardsInLedSuit);
            //check for queen of swords if cups isn't broke
            if (!localState.isCupsBroken()){
                if (playCard.getCardVal() == 12 &&
                        playCard.getCardSuit() == SWORDS){
                    if(handCardsInLedSuit.size() != 1) {
                        handCardsInLedSuit.remove(playCard);
                        playCard = getHighestCard(handCardsInLedSuit);
                    }
                }
            }
            game.sendAction(new ActionPlayCard(this,
                    playerNum, playCard));
            return true;
        }
        //no imminent danger to pick up cards, not going last
        //Play the highest card that is lower than the current “winning” card
        else {
            playBestCardForTrick(handCardsInLedSuit, localState.getSuitLed());
            return true;
        }
    }

    /**
     * The advanced AI's logic for playing out of suit
     * Normal play
     * @param   myHand    the in-suit cards the AI has
     * @return            success value
     */
    protected boolean advancedNormalPlayOutSuit(ArrayList myHand){
        //update suit-tracker thing
        possibleSuits[playerNum][localState.getSuitLed()] = false;
        ArrayList<Card> myPointCards =
                getPointCardsFromList(myHand, true);

        //If I have point cards and cups are broken
        if(myPointCards.size() != 0 && localState.isCupsBroken()) {
            //Play highest point card
            Card playCard = getHighestCard(myPointCards);
            game.sendAction(new ActionPlayCard(this,
                    playerNum, playCard));
            return true;
        }
        //If I have only one card of a non-point suit, play it to
        //give myself more options in the future.
        int oneLeft = 0; //stays 0 if none are found
        ArrayList<Card> nonPointCards =
                getPointCardsFromList(myHand, false);
        int[] suitCounts = getSuitCounts(nonPointCards);
        for (int i = 0; i < suitCounts.length; i++){
            if (suitCounts[i] == 1){
                oneLeft = i+1; //if 1 left, set to suit w/ 1 left
            }
        }
        if (oneLeft != 0) {
            ArrayList<Card> uselessList =
                    getSuitCardsInList(nonPointCards, oneLeft);
            Card playCard = uselessList.get(0);
            game.sendAction(new ActionPlayCard(this,
                    playerNum, playCard));
            return true;
        }
        //play highest card that is not a point card
        else {
            Card playCard = null;
            if (nonPointCards.size() != 0){
                //I have point cards
                playCard = getHighestCard(nonPointCards);
            } else {
                //I have no point cards
                playCard = getHighestCard(myHand);
            }
            game.sendAction(new ActionPlayCard(this,
                    playerNum, playCard));
            return true;
        }
    }

    /**
     * The advanced AI's logic for going first in a trick
     * Shooting the Moon
     * @param handCardsInLedSuit    the AI's current hand cards in the led suit
     * @return                      success value
     */
    protected boolean advancedShootingPlayGoingFirst(ArrayList<Card> handCardsInLedSuit){
        ArrayList<Card> myPointCards =
                getPointCardsFromList(handCardsInLedSuit, true);
        //if I have point cards, lead with my highest (primarily the QoS)
        if (!myPointCards.isEmpty()) {
            ArrayList<Card> possibleQueen =
                    getSuitCardsInList(myPointCards, SWORDS);
            Card playCard = null;
            if (possibleQueen.size() != 0){
                playCard = getHighestCard(possibleQueen);
            } else {
                playCard = getHighestCard(myPointCards);
            }
            game.sendAction(new ActionPlayCard(this,
                    playerNum, playCard));
            return true;
        }

        //if I have spades higher than 12, try to get the Queen
        ArrayList<Card> myHand = getMyHand(localState, playerNum);
        ArrayList<Card> queenCatchers =
                getCardsCompare(myHand, SWORDS, 12, false);
        Card playCard = null;
        if(!queenCatchers.isEmpty()) {
            ArrayList<Card> swordCards = getSuitCardsInList(myHand, SWORDS);
            playCard = getHighestCard(swordCards);
        } else {
            playCard = getHighestCard(myHand);
        }
        game.sendAction(new ActionPlayCard(this, playerNum, playCard));
        return true;
    }

    /**
     * The advanced AI's logic for playing in-suit
     * Shooting the Moon
     * @param handCardsInLedSuit    the AI's current hand cards in the led suit
     * @return                      success value
     */
    protected boolean advancedShootingPlayInSuit(ArrayList<Card> handCardsInLedSuit){
        Card playCard = null;
        //if point cards have been played, play my highest in-suit card
        if(!getPointCardsFromList(localState.getTrickCardsPlayed(),
                true).isEmpty()){
            playCard = getHighestCard(handCardsInLedSuit);
        } else {
            playCard = getLowestCard(handCardsInLedSuit);
        }
        game.sendAction(new ActionPlayCard(this, playerNum, playCard));
        return true;
    }

    /**
     * The advanced AI's logic for playing out of suit
     * Shooting the Moon
     * @return  success value
     */
    protected boolean advancedShootingPlayOutSuit(){
        ArrayList<Card> myHand = getMyHand(localState, playerNum);
        ArrayList<Card> nonPointCards = getPointCardsFromList(myHand, false);
        Card playCard = null;
        if (!nonPointCards.isEmpty()){
            playCard = getLowestCard(nonPointCards);
        } else {
            //I have no cards that aren't point cards
            //I'm forced to play my lowest point card
            playCard = getLowestCard(myHand);
        }
        game.sendAction(new ActionPlayCard(this, playerNum, playCard));
        return true;
    }

    /**
     * A method to check how many points are currently on the table.
     * @return  point sum
     */
    protected int pointsOnTable(){
        int pointSum = 0;
        ArrayList<Card> tableCards = localState.getTrickCardsPlayed();
        for (Card card : tableCards){
            if (card.getCardSuit() == CUPS){
                pointSum += 1;
            } else if (card.getCardSuit() == SWORDS && card.getCardVal() == 12){
                pointSum += 13;
            }
        }
        return pointSum;
    }

    /**
     * a method to play the smallest card available compared to the winning card
     * in a trick.
     */
    protected void playBestCardForTrick(ArrayList<Card> availableCards, int desiredSuit){
        ArrayList<Card> cardsInDesiredSuit =
                getSuitCardsInList(availableCards, desiredSuit);
        Card currentWinner = getHighestCard(localState.getTrickCardsPlayed());
        ArrayList<Card> safeCards = getCardsCompare(cardsInDesiredSuit,
                localState.getSuitLed(), currentWinner.getCardVal(), true);
        if (safeCards != null){
            if (safeCards.size() > 0){
                //I have safe cards to play
                Card playCard = getHighestCard(safeCards);
                game.sendAction(new ActionPlayCard(this,
                        playerNum, playCard));
                return;
            }
        }
        //I have no safe cards to play
        //pick the smallest card in the desired suit to
        //maximize chances of not having to take the trick
        ArrayList<Card> myHand = getMyHand(localState, playerNum);
        Card playCard = getLowestCard(getSuitCardsInList(myHand, desiredSuit));
        game.sendAction(new ActionPlayCard(this, playerNum, playCard));
    }

    /**
     * A method to play the smallest card available according to a priority
     * list of suits. Favors first suit in the list
     * CAVEAT: does not check for duplicate suits.
     * @param suitList  the suit priority list.
     */
    protected void playSmallCardFromPriorityList(ArrayList<Integer> suitList){
        for (int suit : suitList){
            if (suit == CUPS && !localState.isCupsBroken()){
                //if cups is not broken, we can't play cups
                continue;
            }
            ArrayList<Card> suitCards = getSuitCardsInHand(localState, suit);
            if (suitCards != null){
                for (Card card : suitCards){
                    if (card == null){
                        Log.e(TAG, "playSmallCardFromPriorityList: Null card in list");
                    }
                }

                if (suitCards.size() > 0){
                    Card smallCard = getLowestCard(suitCards);
                    if (smallCard.getCardSuit() == SWORDS &&
                            smallCard.getCardVal() == 12 &&
                            !localState.isCupsBroken()){
                        //if cups is not broken, we can't play QoS
                        suitCards.remove(smallCard);
                        if (suitCards.size() == 0){
                            continue;
                        }
                        smallCard = getLowestCard(suitCards);
                    }
                    if (smallCard == null){
                        Log.i(TAG,"playSmallCardFromPriorityList: car was null");
                    }
                    game.sendAction(new ActionPlayCard(this,
                            playerNum, smallCard));
                    return;
                }
            }
        }
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
     * A method to check if opponents are missing any suits
     * if not, return false
     * @return  missing status
     */
    private boolean opponentsMissingSuits(){
        for (int i = 0; i < 4; i++){
            if (i != playerNum) {
                for (int j = 1; j <= 4; j++){
                    if (!possibleSuits[i][j]) { return true; }
                }
            }
        }
        return false;
    }

    /**
     * A method to create a "desire" order in which to play cards,
     * based on which suits the other players have run out of
     * @return  a list of suits, ordered from most likely available to least
     */
    private ArrayList<Integer> getDesiredSuits(){
        //favor suits w/ highest 'true' count
        //idx 0-cups 1-swords 2-coins 3-wands
        //count suits that opponents still have (and self, but the AI will avoid
        int[] suitCounts = new int[4];
        for (int i = 0; i <= 3; i++){
            for (int j = 1; j <= 4; j++){
                if (possibleSuits[i][j]){
                    switch (j){
                        case CUPS:
                            suitCounts[0] += 1;
                            break;
                        case SWORDS:
                            suitCounts[1] += 1;
                            break;
                        case COINS:
                            suitCounts[2] += 1;
                            break;
                        case WANDS:
                            suitCounts[3] += 1;
                            break;
                        default:
                            Log.e(TAG, "getDesiredSuits: invalid suit value");

                    }
                }
            }
        }

        ArrayList<Integer> preferenceList = new ArrayList<Integer>();
        for (int h = 1; h <= 4; h++){
            int highestSuit = 0;
            int highestCount = 0;
            for (int i = 0; i < 4; i++){
                if (suitCounts[i] > highestCount){
                    highestCount = suitCounts[i];
                    highestSuit = i+1;
                }
            }
            preferenceList.add(highestSuit);
            suitCounts[highestSuit-1] = 0;
        }
        return preferenceList;

    }

    /**
     * a method to get the amount of cards in each suit in a
     * list of cards.
     * @return  the counts: [CUPS, SWORDS, COINS, WANDS]
     */
    protected int[] getSuitCounts(ArrayList<Card> cards){
        int[] suitCounts = new int[4];
        for (Card card : cards){
            suitCounts[card.getCardSuit()-1]++;
        }
        return suitCounts;
    }

    /**
     * A method to get cards according to a suit, a value, and whether
     * you want the cards < value or >= value.
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
                int compareRes = Card.compareCardVals(currentCard, new Card(value, suit));
                if (lessThan){
                    if (compareRes == -1){
                        searchedCards.add(currentCard);
                    }
                } else {
                    if (compareRes == 1){
                        searchedCards.add(currentCard);
                    }
                }
            }
        }
        return searchedCards;
    }

    /**
     * A method to get the four suits in a randomly shuffled list
     * @return  the list of suits
     */
    public static ArrayList<Integer> getRandomSuits(){
        ArrayList<Integer> suits = new ArrayList<Integer>();
        suits.add(CUPS);
        suits.add(SWORDS);
        suits.add(COINS);
        suits.add(WANDS);
        Collections.shuffle(suits);
        return suits;
    }
}
