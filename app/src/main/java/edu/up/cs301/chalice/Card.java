/**
 *  Card class
 *  contains information about a card's suit and value
 *  card value should only be between 1 and 13
 *
 * @version October 8, 2020
 * @author  Alex Junkins, Malia Lundstrom, Chloe Campbell, Addison Raak
 */

package edu.up.cs301.chalice;

import android.media.Image;
import android.util.Log;

public class Card {
    //hearts = cups
    //swords = spades
    //coins = clubs
    //wands = diamonds

    public static final int CUPS = 1;   //hearts
    public static final int SWORDS = 2; //spades
    public static final int COINS = 3;  //clubs
    public static final int WANDS = 4;  //diamonds
    public int cardFace = R.drawable.back_of_card_full; // default to other deck back

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
                Log.e("Card", "Tried to initialize a card with a bad value: " + cardVal);
            }
            if (check == -2) {
                Log.e("Card", "Tried to initialize a card with a bad suit: " + cardSuit);
            }
            this.cardVal = -1;
            this.cardSuit = -1;
            return;
        }
        this.cardVal = cardVal;
        this.cardSuit = cardSuit;
    }

    /**
     * Deep-copy Constructor
     * returns a new card object initialized with the values of a given card
     * returns a card with value and suit -1 if the given card is invalid
     *
     * @param oldCard   the card to be deep copied
     */
    public Card (Card oldCard){ //deep copy
        //check card validity and suit
        int check = checkCardVals(oldCard.cardVal, oldCard.cardSuit);
        if (check < 0) {
            if (check == -1) {
                Log.e("Card", "Tried to initialize a card with a bad value: " + oldCard.cardVal);
            }
            if (check == -2) {
                Log.e("Card", "Tried to initialize a card with a bad suit: " + oldCard.cardSuit);
            }
            this.cardVal = -1;
            this.cardSuit = -1;
            return;
        }
        this.cardVal = oldCard.cardVal;
        this.cardSuit = oldCard.cardSuit;
    }

    /**
     * Invalid card value check
     * a helper method to check if card values are valid
     *
     * @param cardVal   value to test
     * @param cardSuit  suit to test
     * @return          0 if valid, -1 if value is wrong, -2 if suit is wrong, -3 if both are wrong
     */
    private int checkCardVals(int cardVal, int cardSuit){
        int returnMe = 0;
        if (cardVal < 1 || cardVal > 13){
            returnMe--;
        }
        if (cardSuit < 1 || cardSuit > 4){
            returnMe -= 2;
        }
        return returnMe;
    }

    /**
     * TODO: Sets the image value of the card
     */
    public int setCardImage() {
        return 0;
    }

    /**
     * getter for a card's suit
     * @return  suit
     */
    public int getCardSuit() {
        return cardSuit;
    }

    /**
     * setter for a card's suit
     * @param newSuit   must be within 1-4
     */
    public void setCardSuit(int newSuit){
        if (newSuit < 1 || newSuit > 4){
            Log.e("Card", "Tried to give a card with a bad suit: " + newSuit);
            return;
        }
        this.cardSuit = newSuit;
    }

    /**
     * getter for a card's value
     * @return  value
     */
    public int getCardVal() {
        return cardVal;
    }

    /**
     * setter for a card's value
     * @param newVal    must be within 1-13
     */
    public void setCardVal(int newVal){
        if (newVal < 1 || newVal > 13){
            Log.e("Card", "Tried to give a card with a bad value: " + newVal);
            return;
        }
        this.cardVal = newVal;
    }

}
