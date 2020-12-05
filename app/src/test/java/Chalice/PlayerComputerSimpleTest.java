package Chalice;

import org.junit.Test;
import java.util.ArrayList;
import edu.up.cs301.chalice.Card;
import edu.up.cs301.chalice.ChaliceGameState;
import edu.up.cs301.chalice.PlayerComputerSimple;
import static org.junit.Assert.*;

/**
 * PlayerComputerSimpleTest
 * Unit test for PlayerComputerSimple
 *
 * @version December 4, 2020
 * @author  Alex Junkins, Malia Lundstrom, Chloe Campbell, Addison Raak
 */
public class PlayerComputerSimpleTest {

    /**
     * a method to test getHighestCard() in the PlayerComputerSimple class
     */
    @Test
    public void getHighestCard() {
        PlayerComputerSimple testAI = new PlayerComputerSimple("Test");
        ArrayList<Card> cardList = new ArrayList<>();
        cardList.add(new Card(5, 2));
        cardList.add(new Card(4, 3));
        cardList.add(new Card(3, 4));
        cardList.add(new Card(2, 2));
        cardList.add(new Card(1, 1));
        cardList.add(new Card(12, 3));
        Card highCard = testAI.getHighestCard(cardList);
        assertEquals(true, Card.sameCard(new Card(1,1), highCard));
        cardList.remove(highCard);
        highCard = testAI.getHighestCard(cardList);
        assertEquals(true, Card.sameCard(new Card(12,3), highCard));
    }

    /**
     * a method to test getLowestCard() in the PlayerComputerSimple class
     */
    @Test
    public void getLowestCard() {
        PlayerComputerSimple testAI = new PlayerComputerSimple("Test");
        ArrayList<Card> cardList = new ArrayList<>();
        cardList.add(new Card(5, 2));
        cardList.add(new Card(4, 3));
        cardList.add(new Card(3, 4));
        cardList.add(new Card(2, 2));
        cardList.add(new Card(1, 1));
        cardList.add(new Card(12, 3));
        Card lowCard = testAI.getLowestCard(cardList);
        assertTrue(Card.sameCard(new Card(2, 2), lowCard));
        cardList.remove(lowCard);
        lowCard = testAI.getLowestCard(cardList);
        assertTrue(Card.sameCard(new Card(3, 4), lowCard));
    }

    /**
     * a method to test getSuitCardsInList() in the PlayerComputerSimple class
     */
    @Test
    public void getSuitCardsInList() {
        ArrayList<Card> cardList = new ArrayList<>();
        cardList.add(new Card(5, 2));
        cardList.add(new Card(4, 3));
        cardList.add(new Card(3, 4));
        cardList.add(new Card(2, 2));
        cardList.add(new Card(1, 1));
        cardList.add(new Card(12, 3));
        ArrayList<Card> suitCardsSword =
                PlayerComputerSimple.getSuitCardsInList(cardList, 2);
        boolean check = true;
        for (Card card : suitCardsSword){
            if (card.getCardSuit() != 2){ check = false; }
        }
        assertTrue(check);
        check = true;
        ArrayList<Card> suitCardsCoin =
                PlayerComputerSimple.getSuitCardsInList(cardList, 3);
        for (Card card : suitCardsCoin){
            if (card.getCardSuit() != 3){ check = false; }
        }
        assertTrue(check);
    }

    /**
     * a method to test getPointsCardsInHand() in the PlayerComputerSimple class
     */
    @Test
    public void getPointCardsInHand(){
        ChaliceGameState state = new ChaliceGameState();
        PlayerComputerSimple testAI = new PlayerComputerSimple("Test");
        ArrayList<Card> cardList = state.getP1Hand();
        cardList.add(new Card(5, 2));
        cardList.add(new Card(4, 3));
        cardList.add(new Card(3, 4));
        cardList.add(new Card(2, 2));
        cardList.add(new Card(1, 1));
        cardList.add(new Card(12, 2));
        int pointNum =testAI.getPointCardsInHand(state).size();
        assertEquals(2,pointNum);
    }

    /**
     * a method to test getPointCardsFromList() in the PlayerComputerSimple
     * class
     */
    @Test
    public void getPointCardsFromList() {
        ArrayList<Card> cardList = new ArrayList<>();
        cardList.add(new Card(5, 2));
        cardList.add(new Card(4, 3));
        cardList.add(new Card(3, 4));
        cardList.add(new Card(2, 2));
        cardList.add(new Card(1, 1));
        cardList.add(new Card(12, 2));
        ArrayList<Card> pointCards =
                PlayerComputerSimple.getPointCardsFromList(cardList, true);
        boolean check = true;
        for (Card card : pointCards){
            if (card.getCardSuit() != 1 &&
                    !(card.getCardSuit() ==2 && card.getCardVal()==12))
            { check = false; }
        }
        assertTrue(check);
    }

    /**
     * a method to test getCardsPlayedThisTrick() in the PlayerComputerSimple
     * class
     */
    @Test
    public void getCardsPlayedThisTrick() {
        ChaliceGameState state = new ChaliceGameState();
        PlayerComputerSimple testAI = new PlayerComputerSimple("Test");
        Card card1 = new Card(2,3);
        ArrayList<Card> cardsPlayed = new ArrayList<>(1);
        cardsPlayed.add(card1);
        state.setCardsPlayed(cardsPlayed);
        assertEquals(1,testAI.getCardsPlayedThisTrick(state));
    }
}