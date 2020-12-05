package Chalice;

import org.junit.Test;
import edu.up.cs301.chalice.ActionQuit;
import edu.up.cs301.chalice.PlayerHuman;
import edu.up.cs301.game.GameFramework.GamePlayer;
import static org.junit.Assert.*;

/**
 * ActionQuitTest
 * Unit test for ActionQuit
 *
 * @version December 4, 2020
 * @author  Alex Junkins, Malia Lundstrom, Chloe Campbell, Addison Raak
 */
public class ActionQuitTest{

        /**
        * method to test the constructor of ActionQuit
        */
        @Test
        public void ActionQuitConstructorTest(){
            GamePlayer testPlayer= new PlayerHuman("Malia");
            ActionQuit testQuit= new ActionQuit(testPlayer);
            assertEquals(testPlayer, testQuit.getPlayer());
        }
}