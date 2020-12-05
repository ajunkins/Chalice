package Chalice;

import org.junit.Test;
import edu.up.cs301.chalice.Card;
import edu.up.cs301.chalice.Deck;
import static org.junit.Assert.*;

/**
 * DeckTest
 * Unit tests for Deck
 *
 * @version December 4, 2020
 * @author  Alex Junkins, Malia Lundstrom, Chloe Campbell, Addison Raak
 */
public class DeckTest {

    /**
     * a method to test the deep copy constructor in the Deck class
     */
    @Test
    public void testCopyDeck(){
        Deck testDeck= new Deck();
        Deck testDeck2= new Deck(testDeck);
        assertEquals(testDeck.getDeck().get(3).getCardVal(),
                testDeck2.getDeck().get(3).getCardVal());
        assertEquals(testDeck.getDeck().get(3).getCardSuit(),
                testDeck2.getDeck().get(3).getCardSuit());
    }

    /**
     * a method to test getNextCard() in the Deck class
     */
    @Test
    public void getNextCard() {
        Deck testDeck= new Deck();
        Card testCard= new Card(1,1);
        Card actualCard= testDeck.getNextCard();
        assertEquals(testCard.getCardSuit(), actualCard.getCardSuit());
        assertEquals(testCard.getCardVal(), actualCard.getCardVal());
    }
}