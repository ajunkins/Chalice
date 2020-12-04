package Chalice;

import org.junit.Test;

import edu.up.cs301.chalice.Card;
import edu.up.cs301.chalice.ChaliceGameState;
import edu.up.cs301.chalice.ChaliceLocalGame;
import edu.up.cs301.game.GameFramework.GameMainActivity;
import edu.up.cs301.game.GameFramework.LocalGame;
import edu.up.cs301.game.GameFramework.gameConfiguration.GameConfig;
import static org.junit.Assert.*;

public class ChaliceLocalGameTest {

    ChaliceLocalGame clg;

    @Test
    public void randomize() {
    }

    @Test
    public void quit() {
    }

    @Test
    public void isInSuit() {
    }

    @Test
    public void cardsInSuit() {
    }

    @Test
    public void selectCard() {
    }

    @Test
    public void isCardValid() {
        //ArrayList <Card> hand = new ArrayList<>();
        //Card card = new Card((int)(Math.random()*13)+1, (int)(Math.random()*4)+1);
        //assertTrue(hlg.isCardValid(hand, card));
    }

    @Test
    public void playCard() {
    }

    /**    @Test
    public void collectTrick() {
        GameMainActivity activity = new GameMainActivity() {
            @Override
            public GameConfig createDefaultConfig() {
                return null;
            }

            @Override
            public LocalGame createLocalGame() {
                return null;
            }
        };
        clg = new ChaliceLocalGame(activity);
        ChaliceGameState state = new ChaliceGameState();
        Card card1= new Card(1,3);
        state.setP1CardPlayed(card1);
        Card card2= new Card(2,3);
        state.setP2CardPlayed(card2);
        Card card3= new Card(3,3);
        state.setP3CardPlayed(card3);
        Card card4= new Card(4,3);
        state.setP4CardPlayed(card4);
        state.setTricksPlayed(1);
        state.setSuitLed(1);
        int winnerId = clg.collectTrick();
        assertEquals(0, winnerId);
    }
 */


    @Test
    public void passCard() {
    }

    @Test
    public void testToString() {
    }
}