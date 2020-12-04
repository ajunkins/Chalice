/**
 * PlayerComputerAdvanced class
 * contains a pass card action
 *
 * @version November 25, 2020
 * @author  Alex Junkins, Malia Lundstrom, Chloe Campbell, Addison Raak
 */

package edu.up.cs301.chalice;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

import edu.up.cs301.game.GameFramework.infoMessage.GameInfo;
import edu.up.cs301.game.GameFramework.infoMessage.GameState;
import edu.up.cs301.game.GameFramework.utilities.Tickable;

import static edu.up.cs301.chalice.Card.COINS;
import static edu.up.cs301.chalice.Card.CUPS;
import static edu.up.cs301.chalice.Card.SWORDS;
import static edu.up.cs301.chalice.Card.WANDS;


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
    personalityType personality;
    boolean queenPlayed = false;
    boolean takenPoints = false; //if the ai has taken points this round

    enum personalityType {
        DFLT,
        VOIDER,
        SHERIFF,
        LOAF
    }

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
        if (personality == null){
            initializePersonality();
        }
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
        //also, check for the Queen of Swords
        CheckPlayedCards();

        //check that my hand isn't null
        if (getMyHand(localState, playerNum) == null){
            Log.e(TAG, "receiveInfo: player hand was null.");
            return;
        }

        AdvancedAIGameBehavior();
    }

    public void initializePersonality() {
        if(playerNum == 3 && (name.equals("Computer") ||
                name.equals("Computer2") || name.equals("Computer3"))){
            personality = personalityType.LOAF; //breadward
        } else {
            Random r = new Random();
            int p = r.nextInt(3);
            switch (p){
                case 0:
                    personality = personalityType.DFLT;
                    break;
                case 1:
                    personality = personalityType.VOIDER;
                    break;
                case 2:
                    personality = personalityType.SHERIFF;
                    break;
            }
        }
    }

    /**
     * the master method for the advanced AI's game behavior
     * Contains the branches for each AI personality's style of play
     */
    private void AdvancedAIGameBehavior(){
        //behavior for passing cards
        if (localState.getPassingCards()){
            switch(personality){
                case DFLT:
                    PickAndPassCards(localState);
                    break;
                case VOIDER:
                    PickAndPassCardsVoider(localState);
                    break;
                case SHERIFF:
                    PickAndPassCardsSheriff(localState);
                    break;
                case LOAF:
                    PickAndPassCardsMalicious(localState);
                    break;
            }
            return;
        }

        //behavior for playing cards
        PickAndPlayCards(localState);
    }

    /**
     * The default AI's passing behavior
     * prioritizes the Queen of Swords and high-value cards
     * @param state the game state
     */
    @Override
    protected void PickAndPassCards(ChaliceGameState state){
        //prioritize passing 2 of coins
        //picks the 3 highest value cards in the player's hand
        ArrayList<Card> myHand = getMyHand(state, playerNum);
        Card[] pickedCards = new Card[3];
        Card twoCoins = getCardInList(myHand, COINS, 2);
        int start = 0;
        if (twoCoins != null){
            myHand.remove(twoCoins);
            pickedCards[0] = twoCoins;
            start = 1;
        }
        for (int i = start; i < 3; i++){
            Card pickedCard = getHighestCard(myHand);
            pickedCards[i] = pickedCard;
            myHand.remove(pickedCard);
        }
        game.sendAction(new ActionPassCards(this, this.playerNum, pickedCards));
    }

    /**
     * The Voider's passing behavior
     * passes cards from the smallest suit, but tries to keep
     * spades if in possession of the Queen of Swords
     * @param state the game state
     */
    private void PickAndPassCardsVoider(ChaliceGameState state){
        ArrayList<Card> myHand = getMyHand(state, playerNum);
        ArrayList<Card> myHandNoSpades = getNonSuitCardsInList(myHand, SWORDS);

        Card[] pickedCards = new Card[3];

        //if I have only spades, swap to default behavior
        if (myHandNoSpades.size() == 0) {
            PickAndPassCards(state);
            return;
        }

        for (int i = 0; i < 3; i++){
            Card pickedCard = null;
            if (myHandNoSpades.size() > 0){
                //if I still have non-spades
                int lowestSuit = -1;
                int lowestSuitCount = 13;
                for (int j = 1; j <= 4; j++ ){

                    int count = getSuitCardsInList(myHandNoSpades, j).size();
                    if (count < lowestSuitCount && count > 0){
                        lowestSuit = j;
                        lowestSuitCount = count;
                    }
                }
                pickedCard = getLowestCard(getSuitCardsInList(myHandNoSpades, lowestSuit));
                myHand.remove(pickedCard);
                myHandNoSpades.remove(pickedCard);
            } else {
                //if i have only spades
                pickedCard = getHighestCard(myHand);
                myHand.remove(pickedCard);
            }
            pickedCards[i] = pickedCard;
            myHand.remove(pickedCard);
        }

        game.sendAction(new ActionPassCards(this, this.playerNum, pickedCards));
    }

    /**
     * The sheriff's passing behavior
     * keeps swords if in possession of the queen, does default
     * behavior otherwise
     * @param state the game state
     */
    private void PickAndPassCardsSheriff(ChaliceGameState state){
        Card[] pickedCards = new Card[3];
        ArrayList<Card> myHand = getMyHand(state, playerNum);
        Card QoS = getCardInList(myHand, SWORDS, 12);
        ArrayList<Card> swords = getSuitCardsInHand(state, SWORDS);
        if (QoS != null && swords.size() > 4){
            ArrayList<Card> nonSwords = getNonSuitCardsInList(myHand, SWORDS);
            if (nonSwords.size() >= 3){
                for (int i = 0; i < 3; i++){
                    Card pickedCard = getHighestCard(nonSwords);
                    pickedCards[i] = pickedCard;
                    nonSwords.remove(pickedCard);
                }
            } else { //i have > 10 spades
                for (int i = 0; i < 3; i++){
                    Card pickedCard = getLowestCard(myHand);
                    pickedCards[i] = pickedCard;
                    myHand.remove(pickedCard);
                }
            }
            game.sendAction(new ActionPassCards(this, this.playerNum, pickedCards));
            return;
        } else {
            PickAndPassCardsVoider(state);
        }

    }

    /**
     * Antagonistic passing behavior
     * looks for especially brutal pairs to recieve. If none are present,
     * switches to default passing behavior
     * @param state the game state
     */
    private void PickAndPassCardsMalicious(ChaliceGameState state){
        //check for mean pairs
        //two of coins and ace of coins, optional high coin
        if (passStrategyCoins(state)){ return; }
        //Ace or King of Swords + a coin, optional King Swords
        if (passStrategySwordsAndCoins(state)){ return; }
        //A low cup and a high cup, optional second high cup
        if (passStrategyCups(state)){ return; }
        //highest coins and highest wand
        if (passStrategyMulti(state)){ return; }
        //none triggered, do voiding behavior
        PickAndPassCardsVoider(state);
    }

    /**
     * A helper method to hold the coins passing strategy
     * a combination of high and low coins
     * returns with no effect if strategy did not trigger
     * @param state the AI's local game state
     */
    private boolean passStrategyCoins(ChaliceGameState state){
        ArrayList<Card> myHand = getMyHand(state, playerNum);
        Card cardA = getCardInList(myHand, COINS, 2);
        Card cardB = getCardInList(myHand, COINS, 1);
        if (cardA != null && cardB != null){
            //check for another high coin
            Card highCoin = getHighestCard(getSuitCardsInList(myHand, COINS));
            if (highCoin != null){
                if (highCoin.getCardVal() >= 10){
                    passCardsThreePicked(cardA, cardB, highCoin);
                    return true;
                }
            }
            passCardsTwoPicked(state, cardA, cardB);
            return true;
        }
        return false;
    }

    /**
     * A helper method to hold the swords and coins passing strategy
     * a combination of high swords and low coins
     * returns with no effect if strategy did not trigger
     * @param state the AI's local game state
     */
    private boolean passStrategySwordsAndCoins(ChaliceGameState state){
        ArrayList<Card> myHand = getMyHand(state, playerNum);
        Card cardA = getCardInList(myHand, SWORDS, 1);
        if (cardA == null){ cardA = getCardInList(myHand, SWORDS, 13); }
        Card cardB = getLowestCard(getSuitCardsInList(myHand, COINS));
        if (cardA != null && cardB != null){
            Card KSword = getCardInList(myHand, SWORDS, 13);
            if (KSword != null){
                passCardsThreePicked(cardA, cardB, KSword);
                return true;
            }
            passCardsTwoPicked(state, cardA, cardB);
            return true;
        }
        return false;
    }

    /**
     * A helper method to hold the cups passing strategy
     * passing high and a low cup to make taking points more likely
     * returns with no effect if strategy did not trigger
     * @param state the AI's local game state
     */
    private boolean passStrategyCups(ChaliceGameState state){
        ArrayList<Card> myHand = getMyHand(state, playerNum);
        ArrayList<Card> myCups = getSuitCardsInList(myHand, CUPS);
        if (myCups.size() > 1){
            Card cardA = getLowestCard(myCups);
            Card cardB = getHighestCard(myCups);
            if (cardA.getCardVal() <= 4 && cardB.getCardVal() >= 10){
                if (myCups.size() > 2){
                    myCups.remove(cardA);
                    myCups.remove(cardB);
                    Card cardC = getHighestCard(myCups);
                    if (cardC.getCardVal() >= 10){
                        passCardsThreePicked(cardA, cardB, cardC);
                        return true;
                    }
                }
                passCardsTwoPicked(state, cardA, cardB);
                return true;
            }
        }
        return false;
    }

    /**
     * A helper method to hold the multi-suit passing strategy
     * a combination of high cards in different suits, to
     * get in the way of voiding
     * returns with no effect if strategy did not trigger
     * @param state the AI's local game state
     */
    private boolean passStrategyMulti(ChaliceGameState state){
        ArrayList<Card> myHand = getMyHand(state, playerNum);
        ArrayList<Card> myCoins = getSuitCardsInList(myHand, COINS);
        ArrayList<Card> myWands = getSuitCardsInList(myHand, WANDS);
        if (myCoins.size() > 0 && myWands.size() > 0){
            Card cardA = getHighestCard(myCoins);
            Card cardB = getHighestCard(myWands);
            ArrayList<Card> myCups = getSuitCardsInList(myHand, CUPS);
            if(myCups.size() > 0){
                Card cardC = getHighestCard(myCups);
                passCardsThreePicked(cardA, cardB, cardC);
                return true;
            }
            passCardsTwoPicked(state, cardA, cardB);
            return true;
        }
        return false;
    }

    /**
     * A helper method to pick two cards, plus a high card in the AI's hand and send
     * them as a pass move. checks for null cards
     * @param cardA The first card
     * @param cardB The second card
     */
    private void passCardsTwoPicked(ChaliceGameState state, Card cardA, Card cardB){
        if (cardA != null && cardB != null){
            Card[] pickedCards = new Card[3];
            ArrayList<Card> myHand = getMyHand(state, playerNum);
            pickedCards[0] = cardA;
            myHand.remove(cardA);
            pickedCards[1] = cardB;
            myHand.remove(cardB);
            pickedCards[2] = getHighestCard(myHand);
            game.sendAction(new ActionPassCards(this, this.playerNum, pickedCards));
        } else {
            Log.e(TAG, "passCardsTwoPicked: Attempted to pass a null card!");
        }
    }

    /**
     * A helper method to pick three specific cards. Checks for null cards.
     * @param cardA The first card
     * @param cardB The second card
     * @param cardC The third card
     */
    private void passCardsThreePicked(Card cardA, Card cardB, Card cardC){
        if (cardA != null && cardB != null && cardC != null){
            Card[] pickedCards = new Card[3];
            pickedCards[0] = cardA;
            pickedCards[1] = cardB;
            pickedCards[2] = cardC;
            game.sendAction(new ActionPassCards(this, this.playerNum, pickedCards));
        } else {
            Log.e(TAG, "passCardsThreePicked: Attempted to pass a null card!");
        }
    }

    /**
     * the method that handles playing cards during the AI's turn
     * for the all AI personalities
     * @param state the game state
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
            Log.e(TAG, "PickAndPlayCards: AI player was unable to play a card!");
        }
    }

    /**
     * The method the AI uses to pick a card to play when it is not
     * trying to shoot the moon for all personalities
     * @return success status
     */
    protected boolean playCardNormal(ArrayList<Card> handCardsInLedSuit){
        ArrayList<Card> myHand =
                Objects.requireNonNull(getMyHand(localState, playerNum));
        //if going first
        if(localState.getTrickCardsPlayed().size() == 0) {
            switch (personality){
                case DFLT:
                case SHERIFF:
                    return advancedNormalPlayGoingFirst(myHand);
                case VOIDER:
                    return advancedVoiderPlayGoingFirst(myHand);
                case LOAF:
                    return advancedLoafPlayGoingFirst(myHand);
            }
        }
        //not going first, but have a card in-suit
        else if(handCardsInLedSuit.size() > 0) {
            switch(personality){
                case DFLT:
                case VOIDER:
                case LOAF:
                    return advancedNormalPlayInSuit(handCardsInLedSuit);
                case SHERIFF:
                    return advancedSheriffPlayInSuit(handCardsInLedSuit);

            }
        } else { //I'm not going first, I have no card in the led suit
            switch (personality){
                case DFLT:
                    return advancedNormalPlayOutSuit(myHand);
                case VOIDER:
                case LOAF:
                    return advancedVoiderPlayOutSuit(myHand);
                case SHERIFF:
                    return advancedSheriffPlayOutSuit(myHand);
            }
        }
        return false;
    }

    /**
     * The advanced AI's logic for going first in a trick
     * for the default/low-layer personality
     * prioritizes suits that other players are likely to
     * have cards in
     * @param myHand    the AI's current hand
     * @return          success value
     */
    protected boolean advancedNormalPlayGoingFirst(ArrayList<Card> myHand){
        ArrayList<Card> smallSwordsCards =
                getCardsCompare(myHand, SWORDS, 6, true);
        Card QoS = getCardInList(myHand, SWORDS, 12);
        //have sword < 6 and no QoS,
        //flushing out the queen ( if the queen is not out)
        if(smallSwordsCards.size() > 0 && !queenPlayed && QoS != null) {
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
     * The advanced AI's logic for going first in a trick
     * for the voider personality
     * chooses to lead with the suit it has lowest in
     * avoids swords if it has the queen
     * @param myHand    the AI's current hand
     * @return          success value
     */
    protected boolean advancedVoiderPlayGoingFirst(ArrayList<Card> myHand){
        //use non-point cards if cups not broken
        ArrayList<Card> nonPointCards = getPointCardsFromList(myHand, false);
        ArrayList<Card> validCards = null;
        if (localState.isCupsBroken()){
            validCards = myHand;
        } else {
            validCards = nonPointCards;
        }

        if (validCards.size() > 0){
            //lead with my suit with the fewest cards
            //sort my hand according to the ideal pattern
            int[] suitOrder = { COINS, WANDS, CUPS, SWORDS };
            ArrayList<Card> sortedHand = sortList(myHand, suitOrder);
            //find the suit with the fewest cards and play its lowest card
            int lowestSuit = -1;
            int lowestCount = 99;
            for (int suit : suitOrder){
                ArrayList<Card> suitCards = getSuitCardsInList(myHand, suit);
                if (suitCards.size() < lowestCount && suitCards.size() > 0){
                    lowestSuit = suit;
                    lowestCount = suitCards.size();
                }
            }
            validCards = getSuitCardsInList(myHand, lowestSuit);
            Card pickedCard = getLowestCard(validCards);
            game.sendAction(new ActionPlayCard(this,
                    playerNum, pickedCard));
            return true;
        } else {
            //if I have no valid cards, cups is not broken but i have only
            //point cards. in this case, I can operate as if hearts has
            //been broken.
            localState.setCupsBroken(true);
            return advancedVoiderPlayGoingFirst(myHand);
        }
    }

    /**
     * A helper method to sort a list by an order of suits
     * No functionality to deal with multiple suits, so careful
     * @param list      the list to be sorted
     * @param suitOrder the order to sort the list by
     * @return          the sorted list
     */
    private ArrayList<Card> sortList(ArrayList<Card> list, int[] suitOrder){
        //value does not matter
        ArrayList<Card> sortedHand = new ArrayList<Card>();
        for (int suit : suitOrder){
            for (Card card : list){
                if (card.getCardSuit() == suit){
                    sortedHand.add(card);
                }
            }
        }
        return sortedHand;
    }

    /**
     * The advanced AI's logic for going first in a trick
     * for the loaf personality
     * attempts to avoid playing swords if it has the queen
     * plays like a low-layer if the queen has not been played and it
     * is at risk of taking it, and like a voider otherwise
     * @param myHand    the AI's current hand
     * @return          success value
     */
    protected boolean advancedLoafPlayGoingFirst(ArrayList<Card> myHand){
        Card QoS = getCardInList(myHand, SWORDS, 12);
        ArrayList<Card> mySwords = getSuitCardsInList(myHand, SWORDS);
        ArrayList<Card> nonSwords = getNonSuitCardsInList(myHand, SWORDS);
        if (QoS != null){
            //if I need to defend against a siege, play defensively.
            //avoid setting swords as a suit
            if (nonSwords.size() > 0){
                return advancedNormalPlayGoingFirst(nonSwords);
            } else {
                //I have only swords, I  either have nothing to worry about
                //or there's nothing I can do
                return advancedNormalPlayGoingFirst(myHand);
            }
        } else {
            //if not, play offensively
            //avoid swords if I'm at risk of taking QoS and she hasn't
            //been played
            if (mySwords.size() > 0 && !queenPlayed){
                Card highestSword = getHighestCard(mySwords);
                if (highestSword.getCardVal() > 12 && nonSwords.size() > 0){
                    return advancedNormalPlayGoingFirst(nonSwords);
                }
            }
            //if the queen's been played, swords are no longer more dangerous
            //than any other suit
            return advancedVoiderPlayGoingFirst(myHand);
        }
    }

    /**
     * The advanced AI's logic for playing in-suit
     * Normal play
     * plays low cards to avoid taking points
     * @param handCardsInLedSuit    the AI's current hand cards in the led suit
     * @return                      success value
     */
    protected boolean advancedNormalPlayInSuit(ArrayList<Card> handCardsInLedSuit){
        //if there are points on the table (ie avoid getting points)
        if(pointsOnTable(localState) > 0) {
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
     * The advanced AI's logic for playing in-suit
     * with the sheriff personality
     * attempts to give the queen of spades to the winning player if possible
     * takes at least a few cups per hand to thwart shooters
     * @param handCardsInLedSuit    the AI's current hand cards in the led suit
     * @return                      success value
     */
    protected boolean advancedSheriffPlayInSuit(ArrayList<Card> handCardsInLedSuit){
        //if playing in swords
        if (handCardsInLedSuit.get(0).getCardSuit() == SWORDS){
            Card QoS = getCardInList(handCardsInLedSuit, SWORDS, 12);
            if (QoS != null){
                int gameWinner = localState.getWinningPlayerNum();
                int trickWinner = localState.getCurrentTrickWinnerNum();
                if (gameWinner == trickWinner){
                    //if the current winner is winning the trick, give
                    //that sucker the queen
                    game.sendAction(new ActionPlayCard(this,
                            playerNum, QoS));
                    return true;
                } else {
                    //I want to avoid playing the queen if possible
                    handCardsInLedSuit.remove(QoS);
                    Card playCard = null;
                    if (handCardsInLedSuit.size() > 0){
                        playCard = getLowestCard(handCardsInLedSuit);
                        game.sendAction(new ActionPlayCard(this,
                                playerNum, playCard));
                        return true;
                    } else {
                        //i have no choice but to play the queen
                        playCard = QoS;
                    }
                    game.sendAction(new ActionPlayCard(this,
                            playerNum, playCard));
                    return true;
                }
            } else {
                //if I don't have the Queen, play my lowest sword
                Card playCard = getLowestCard(handCardsInLedSuit);
                game.sendAction(new ActionPlayCard(this,
                        playerNum, playCard));
                return true;
            }
        } else if (queenPlayed) {
            Card playCard = null;
            if (!takenPoints){
                //thwart any stm attempts
                playCard = getHighestCard(handCardsInLedSuit);
            } else {
                playCard = getLowestCard(handCardsInLedSuit);
            }
            game.sendAction(new ActionPlayCard(this,
                    playerNum, playCard));
            return true;
        } else {
            Card playCard = getLowestCard(handCardsInLedSuit);
            game.sendAction(new ActionPlayCard(this,
                    playerNum, playCard));
            return true;
        }
    }

    /**
     * The advanced AI's logic for playing out of suit
     * Normal play
     * sheds point cards, then high cards
     * @param   myHand    the in-suit cards the AI has
     * @return            success value
     */
    protected boolean advancedNormalPlayOutSuit(ArrayList<Card> myHand){
        //update suit-tracker thing
        possibleSuits[playerNum][localState.getSuitLed()] = false;
        ArrayList<Card> myPointCards =
                getPointCardsFromList(myHand, true);

        //If I have point cards and cups are broken
        if(myPointCards.size() != 0 && localState.isCupsBroken()) {
            //if I have the QoS, play that
            Card QoS = getCardInList(myHand, SWORDS, 12);
            Card playCard = null;
            if (QoS != null){
                playCard = QoS;
            } else {
                //Play highest point card
                playCard = getHighestCard(myPointCards);
            }
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
            if (nonPointCards.size() > 0){
                //I have non point cards, i can break cups
                playCard = getHighestCard(nonPointCards);
            } else {
                //I have only point cards
                playCard = getHighestCard(myHand);
            }
            game.sendAction(new ActionPlayCard(this,
                    playerNum, playCard));
            return true;
        }
    }

    /**
     * The advanced AI's logic for playing out of suit
     * with the sheriff personality
     * attempts to break hearts as early as possible
     * @param   myHand    the in-suit cards the AI has
     * @return            success value
     */
    protected boolean advancedSheriffPlayOutSuit(ArrayList<Card> myHand){
        if (!localState.isCupsBroken()){
            ArrayList<Card> cups = getSuitCardsInList(myHand, CUPS);
            if (cups.size() > 0){
                Card playCard = getLowestCard(cups);
                game.sendAction(new ActionPlayCard(this,
                        playerNum, playCard));
                return true;
            } else {
                return advancedNormalPlayOutSuit(myHand);
            }
        } else {
            return advancedNormalPlayOutSuit(myHand);
        }
    }

    /**
     * The advanced AI's logic for playing out of suit
     * with the voider personality
     * sheds dangerous cards first, then cups
     * @param   myHand    the in-suit cards the AI has
     * @return            success value
     */
    protected boolean advancedVoiderPlayOutSuit(ArrayList<Card> myHand){
        Card playCard = null;
        //avoid bad cards: QoS, then high swords, then high cups.
        Card QoS = getCardInList(myHand, SWORDS, 12);
        ArrayList<Card> mySwords = getSuitCardsInList(myHand, SWORDS);
        ArrayList<Card> myCups = getSuitCardsInList(myHand, CUPS);
        if (QoS != null){
            playCard = QoS;
        } else if (mySwords.size() > 0){
            Card highSword = getHighestCard(mySwords);
            if (highSword.getCardVal() == 1 || highSword.getCardVal() == 13){
                playCard = highSword;
            }
        } else if (myCups.size() > 0){
            playCard = getHighestCard(myCups);
        } else {
            playCard = getHighestCard(myHand);
        }
        game.sendAction(new ActionPlayCard(this,
                playerNum, playCard));
        return true;
    }

    /**
     * The method the AI uses to pick a card to play when it is not
     * trying to shoot the moon. All personalities attempt to shoot
     * the moon when they have high enough points
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
    public static int pointsOnTable(ChaliceGameState localState){
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
     * also checks to see if the queen of swords has been played
     * also checks if the AI has taken points
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
        //check for QoS
        ArrayList<Card> trickCards = localState.getTrickCardsPlayed();
        ArrayList<Card> playedCards = localState.getCardsPlayed();
        if (getCardInList(trickCards, SWORDS, 12) != null) { queenPlayed = true; }
        if (getCardInList(playedCards, SWORDS, 12) != null) { queenPlayed = true; }
        //if its a new hand, reset the variable
        if (playedCards.size() == 0) {
            queenPlayed = false;
            takenPoints = false;
        }
        //check if I have taken points
        if (localState.getRunningPointsByPlayerNum(playerNum) > 0){
            takenPoints = true;
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
