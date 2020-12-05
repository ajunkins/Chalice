package Chalice;

import org.junit.Test;
import edu.up.cs301.chalice.Card;
import static org.junit.Assert.*;

/**
 * CardTest
 * Unit tests for Card
 *
 * @version December 4, 2020
 * @author  Alex Junkins, Malia Lundstrom, Chloe Campbell, Addison Raak
 */
public class CardTest{

    /**
     * a method to test sameCard() in the Card class
     */
    @Test
    public void sameCard() {
        Card testCard1= new Card(1,3);
        Card testCard2= new Card(1,3);
        Card testCard3= new Card(10,2);
        assertEquals(true, Card.sameCard(testCard1,testCard2));
        assertEquals(false, Card.sameCard(testCard1,testCard3));
    }

    /**
     * a method to test compareCardVals() in the Card class
     */
    @Test
    public void compareCardVals() {
        Card testCard1= new Card(1,3);
        Card testCard2= new Card(1,2);
        Card testCard3= new Card(5,4);
        assertEquals(0, Card.compareCardVals(testCard1,testCard2));
        assertEquals(1, Card.compareCardVals(testCard1, testCard3));
        assertEquals(-1, Card.compareCardVals(testCard3, testCard1));
    }

}