package edu.up.cs301.chalice;

import java.util.ArrayList;

/**
 * Deck class
 * an object to manage the deck ArrayList
 * contains one copy of all 52 cards
 *
 * @version December 4, 2020
 * @author  Alex Junkins, Malia Lundstrom, Chloe Campbell, Addison Raak
 */
public class Deck {

    // An array of 52 cards.
    private ArrayList<Card> deck;

    /**
     * Keeps track of the number of cards that have been dealt from
     * the deck so far.
     */
    private int usedCards;

    /**
     * Constructs a regular 52-card poker deck.  Initially, the cards
     * are in a sorted order.  The shuffle() method can be called to
     * randomize the order.
     */
    public Deck() {
        deck = new ArrayList<>();
        int cardCt = 0; // How many cards have been created so far.
        while(cardCt <52) {
            for (int cardSuit = 1; cardSuit <= 4; cardSuit++) {
                for (int cardVal = 1; cardVal <= 13; cardVal++) {
                    Card newCard = new Card(cardVal, cardSuit);
                    deck.add(newCard);
                    cardCt++;
                }
            }
        }
        usedCards = 0;
    }//Deck

    /**
     * Deep-copy constructor for Deck. Nice deck, bro!
     * @param oldDeck   The previous deck
     */
    public Deck(Deck oldDeck){
        this.usedCards = oldDeck.usedCards;

        //deep-copy every card in the previous deck
        this.deck = new ArrayList<>();
        for(int cardNum = 0; cardNum < oldDeck.deck.size(); cardNum++) {
            deck.add(new Card(oldDeck.deck.get(cardNum)));
        }
    }//Deck copy

    /**
     * Put all the used cards back into the deck (if any), and
     * shuffle the deck into a random order.
     */
    public void shuffle() {
        for ( int i = deck.size()-1; i > 0; i-- ) {
            int rand = (int)(Math.random()*(i+1));
            Card temp = deck.get(i);
            deck.set(i, deck.get(rand));
            deck.set(rand, temp);
        }
        usedCards = 0;
    } //shuffle

    /**
     * Removes the next card from the deck and return it.  It is illegal
     * to call this method if there are no more cards in the deck.  You can
     * check the number of cards remaining by calling the cardsLeft() function.
     * @return the card which is removed from the deck.
     * @throws IllegalStateException if there are no cards left in the deck
     */
    public Card getNextCard() {
        if (usedCards == deck.size()) {
            throw new IllegalStateException("No cards are left in the deck."); }
         usedCards++;
         return deck.get(usedCards-1);
    }//getNextCard

    /**
     * method to get deck
     * @return deck
     */
    public ArrayList<Card> getDeck(){
        return deck;
    } //getDeck


} //Deck
