package Chalice;

import org.junit.Test;

import java.util.ArrayList;

import edu.up.cs301.chalice.Card;
import edu.up.cs301.chalice.PlayerComputerSimple;

import static org.junit.Assert.*;

public class PlayerComputerSimpleTest {

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

    @Test
    public void getSuitCardsInList() {
        ArrayList<Card> cardList = new ArrayList<>();
        cardList.add(new Card(5, 2));
        cardList.add(new Card(4, 3));
        cardList.add(new Card(3, 4));
        cardList.add(new Card(2, 2));
        cardList.add(new Card(1, 1));
        cardList.add(new Card(12, 3));
        ArrayList<Card> suitCardsSword = PlayerComputerSimple.getSuitCardsInList(cardList, 2);
        boolean check = true;
        for (Card card : suitCardsSword){
            if (card.getCardSuit() != 2){ check = false; }
        }
        assertTrue(check);
        check = true;
        ArrayList<Card> suitCardsCoin = PlayerComputerSimple.getSuitCardsInList(cardList, 3);
        for (Card card : suitCardsCoin){
            if (card.getCardSuit() != 3){ check = false; }
        }
        assertTrue(check);
    }
}