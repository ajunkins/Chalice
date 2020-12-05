package Chalice;

import org.junit.Test;
import edu.up.cs301.chalice.ActionPlayCard;
import edu.up.cs301.chalice.Card;
import edu.up.cs301.chalice.PlayerHuman;
import edu.up.cs301.game.GameFramework.GamePlayer;
import static org.junit.Assert.*;

/**
 * ActionPlayCardTest
 * Unit test for ActionPlayCard
 *
 * @version December 4, 2020
 * @author  Alex Junkins, Malia Lundstrom, Chloe Campbell, Addison Raak
 */
public class ActionPlayCardTest{

    /**
     * method to test the constructor of ActionPlayCard
     */
    @Test
    public void ActionPlayCardConstructorTest(){
        GamePlayer testPlayer= new PlayerHuman("Malia");
        Card testCard= new Card(3, 3);
        ActionPlayCard testPlay= new ActionPlayCard(testPlayer, 1, testCard);
        assertEquals(testPlayer, testPlay.getPlayer());
        assertEquals(1, testPlay.getPlayerNum());
        assertEquals(testCard, testPlay.playedCard());
    }
}