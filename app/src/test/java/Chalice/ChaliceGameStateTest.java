package Chalice;

import org.junit.Test;
import java.util.ArrayList;
import edu.up.cs301.chalice.Card;
import edu.up.cs301.chalice.ChaliceGameState;
import static org.junit.Assert.*;

/**
 * ChaliceGameStateTest
 * Unit tests for ChaliceGameState
 *
 * @version December 4, 2020
 * @author  Alex Junkins, Malia Lundstrom, Chloe Campbell, Addison Raak
 */
public class ChaliceGameStateTest{

    /**
     * a method to test the deep copy constructor in ChaliceGameState
     */
    @Test
    public void ChaliceGameStateDeepCTest(){
        ChaliceGameState testState= new ChaliceGameState();
        testState.setP1CurrentPoints(30);
        testState.setSelectedCard(new Card(3,2));
        ChaliceGameState testCopyState= new ChaliceGameState(testState);
        testCopyState.setP1CurrentPoints(10);

        assertEquals(30, testState.getP1CurrentPoints());
        assertEquals(10, testCopyState.getP1CurrentPoints());
        assertEquals(3, testCopyState.getSelectedCard().getCardVal());
        assertEquals(2, testCopyState.getSelectedCard().getCardSuit());
    }

    /**
     * a method to test dealCards() in ChaliceGameState
     */
    @Test
    public void dealCards() {
        ChaliceGameState testState= new ChaliceGameState();
        testState.setP1Hand(new ArrayList<Card>());
        testState.setP2Hand(new ArrayList<Card>());
        testState.setP3Hand(new ArrayList<Card>());
        testState.setP4Hand(new ArrayList<Card>());

        testState.dealCards();

        assertEquals(13, testState.getP1Hand().size());
        assertEquals(13, testState.getP2Hand().size());
        assertEquals(13, testState.getP3Hand().size());
        assertEquals(13, testState.getP4Hand().size());
    }

    /**
     * a method to test getTrickCardsPlayed() in ChaliceGameState
     */
    @Test
    public void getTrickCardsPlayed() {
        ChaliceGameState testState= new ChaliceGameState();
        Card testCard= new Card(1,1);
        Card testCard2= new Card(2,2);
        ArrayList<Card> testCardsPlayed= new ArrayList<Card>();
        testState.setP1CardPlayed(testCard);
        testState.setP2CardPlayed(testCard2);
        testCardsPlayed.add(testCard);
        testCardsPlayed.add(testCard2);

        assertEquals(testCardsPlayed, testState.getTrickCardsPlayed());
    }

    /**
     * a method to test pointsInTrick() in ChaliceGameState
     */
    @Test
    public void pointsInTrick() {
        ChaliceGameState testState= new ChaliceGameState();
        Card testQueenSword= new Card(12, 2);
        Card testHeart= new Card(3, 1);
        Card testNormCard= new Card(5, 3);
        testState.setP1CardPlayed(testQueenSword);
        testState.setP2CardPlayed(testHeart);
        testState.setP3CardPlayed(testNormCard);

        assertEquals(14, testState.pointsInTrick());
    }
}