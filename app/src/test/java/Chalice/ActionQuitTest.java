package Chalice;

import org.junit.Test;

import edu.up.cs301.chalice.ActionQuit;
import edu.up.cs301.chalice.PlayerHuman;
import edu.up.cs301.game.GameFramework.GamePlayer;

import static org.junit.Assert.*;

public class ActionQuitTest{

        @Test
        public void ActionQuitConstructorTest(){
            GamePlayer testPlayer= new PlayerHuman("Malia");
            ActionQuit testQuit= new ActionQuit(testPlayer);
            assertEquals(testPlayer, testQuit.getPlayer());
        }
}