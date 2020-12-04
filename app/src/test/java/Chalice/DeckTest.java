package Chalice;

import org.junit.Test;

import edu.up.cs301.chalice.Card;
import edu.up.cs301.chalice.Deck;

import static org.junit.Assert.*;

public class DeckTest {

    @Test
    public void testCopyDeck(){
        Deck testDeck= new Deck();
        Deck testDeck2= new Deck(testDeck);
        assertEquals(testDeck.getDeck().get(3).getCardVal(),
                testDeck2.getDeck().get(3).getCardVal());
        assertEquals(testDeck.getDeck().get(3).getCardSuit(),
                testDeck2.getDeck().get(3).getCardSuit());
    }

    @Test
    public void getNextCard() {
        Deck testDeck= new Deck();
        Card testCard= new Card(1,1);
        Card actualCard= testDeck.getNextCard();
        assertEquals(testCard.getCardSuit(), actualCard.getCardSuit());
        assertEquals(testCard.getCardVal(), actualCard.getCardVal());
    }
}