package Chalice;

import org.junit.Test;
import edu.up.cs301.chalice.Card;

import static org.junit.Assert.*;

public class CardTest{

    @Test
    public void testSameCard() {
        Card testCard1= new Card(1,3);
        Card testCard2= new Card(1,3);
        Card testCard3= new Card(10,2);
        assertEquals(true, Card.sameCard(testCard1,testCard2));
        assertEquals(false, Card.sameCard(testCard1,testCard3));
    }

    @Test
    public void testCompareCardValues() {
        Card testCard1= new Card(1,3);
        Card testCard2= new Card(1,2);
        Card testCard3= new Card(5,4);
        assertEquals(0, Card.compareCardVals(testCard1,testCard2));
        assertEquals(1, Card.compareCardVals(testCard1, testCard3));
        assertEquals(-1, Card.compareCardVals(testCard3, testCard1));
    }

}