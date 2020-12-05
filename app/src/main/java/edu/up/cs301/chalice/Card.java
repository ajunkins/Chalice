package edu.up.cs301.chalice;

import android.util.Log;

/**
 *  Card class
 *  contains information about a card's suit and value
 *  card value should only be between 1 and 13
 *
 * @version December 4, 2020
 * @author  Alex Junkins, Malia Lundstrom, Chloe Campbell, Addison Raak
 */
public class Card {

    //declare card suits
    public static final int CUPS = 1;   //hearts
    public static final int SWORDS = 2; //spades
    public static final int COINS = 3;  //clubs
    public static final int WANDS = 4;  //diamonds

    //a card has value and a suit
    private int cardVal;
    private int cardSuit;

    /**
     * Parameter Constructor
     * returns a new card object initialized with the given values
     * returns a card with value and suit -1 if unsuccessful
     *
     * @param cardVal   a value between 1 and 13
     * @param cardSuit  a value between 1 and 4
     */
    public Card (int cardVal, int cardSuit) {
        //check card validity and suit
        int check = checkCardVals(cardVal, cardSuit);
        if (check < 0) {
            if (check == -1) {
//                Log.e("Card", "Tried to initialize a card " +
//                        "with a bad value: " + cardVal);
            }
            if (check == -2) {
//                Log.e("Card", "Tried to initialize a card " +
//                        "with a bad suit: " + cardSuit);
            }
            this.cardVal = -1;
            this.cardSuit = -1;
            return;
        }
        this.cardVal = cardVal;
        this.cardSuit = cardSuit;
    } //Card

    /**
     * Deep-copy Constructor
     * returns a new card object initialized with the values of a given card
     * returns a card with value and suit -1 if the given card is invalid
     *
     * @param oldCard   the card to be deep copied
     */
    public Card (Card oldCard) { //deep copy
        //check card validity and suit
        int check = checkCardVals(oldCard.cardVal, oldCard.cardSuit);
        if (check < 0) {
            if (check == -1) {
                Log.e("Card", "Tried to initialize a card " +
                        "with a bad value: " + oldCard.cardVal);
            }
            if (check == -2) {
                Log.e("Card", "Tried to initialize a card " +
                        "with a bad suit: " + oldCard.cardSuit);
            }
            this.cardVal = -1;
            this.cardSuit = -1;
            return;
        }
        this.cardVal = oldCard.cardVal;
        this.cardSuit = oldCard.cardSuit;
    }//Card Copy

    /**
     * a method to check if two card objects have the same values
     * @param card1 first card
     * @param card2 second card
     * @return  value
     */
    public static boolean sameCard(Card card1, Card card2) {
        if (card1 == card2) { return true;
        }
        boolean same = false;
        if (card1.getCardVal() == card2.getCardVal()) {
            if (card1.getCardSuit() == card2.getCardSuit()) {
                same = true;
            }
        }
        return same;
    } //sameCard

    /**
     * Invalid card value check
     * a helper method to check if card values are valid
     *
     * @param cardVal   value to test
     * @param cardSuit  suit to test
     * @return          0 if valid, -1 if value is wrong,
     *                  -2 if suit is wrong, -3 if both are wrong
     */
    private int checkCardVals(int cardVal, int cardSuit) {
        int returnMe = 0;
        if (cardVal < 1 || cardVal > 13){
            returnMe--;
        }
        if (cardSuit < 1 || cardSuit > 4){
            returnMe -= 2;
        }
        return returnMe;
    } //checkCardVals

    /**
     * A helper method to compare two cards' values,
     * accounting for aces high
     * @param first     the first card
     * @param second    the second card
     * @return          return value - -1: f<s, 0: f=s, 1: f>s
     */
    public static int compareCardVals(Card first, Card second){
        if (first.getCardVal() == second.getCardVal()){
            return 0;
        }
        if (first.getCardVal() == 1){
            return 1;
        }
        if (second.getCardVal() == 1){
            return -1;
        }
        if (first.getCardVal() < second.getCardVal()){
            return -1;
        } else {
            return 1;
        }
    } //compareCardVals

    /**
     * getter for a card's suit
     * @return  suit
     */
    public int getCardSuit() {
        return cardSuit;
    } //getCardSuit

    /**
     * getter for a card's value
     * @return  value
     */
    public int getCardVal() {
        return cardVal;
    } //getCardVal

} //Card
