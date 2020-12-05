package Chalice;

import org.junit.Test;
import edu.up.cs301.chalice.ActionPassCards;
import edu.up.cs301.chalice.Card;
import edu.up.cs301.chalice.PlayerHuman;
import edu.up.cs301.game.GameFramework.GamePlayer;
import static org.junit.Assert.*;

/**
 * ActionPassCardsTest
 * Unit test for ActionPassCards
 *
 * @version December 4, 2020
 * @author  Alex Junkins, Malia Lundstrom, Chloe Campbell, Addison Raak
 */
public class ActionPassCardsTest{

   /**
    * method to test the constructor of ActionPassCards
    */
   @Test
    public void ActionPassCardsConstructorTest(){
       GamePlayer testPlayer= new PlayerHuman("Malia");
       Card[] testCards= new Card[]{new Card(1,1),
               new Card(2,2), new Card(3, 3)};
       ActionPassCards testPass= new ActionPassCards(testPlayer, 1, testCards);
       assertEquals(testPlayer, testPass.getPlayer());
       assertEquals(1, testPass.getPlayerNum());
       assertArrayEquals(testCards, testPass.passedCards());

   }
}