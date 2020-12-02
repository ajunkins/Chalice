package Chalice;

import org.junit.Test;

import edu.up.cs301.chalice.ActionPassCards;
import edu.up.cs301.chalice.ActionPlayCard;
import edu.up.cs301.chalice.Card;
import edu.up.cs301.chalice.PlayerHuman;
import edu.up.cs301.game.GameFramework.GamePlayer;

import static org.junit.Assert.*;

public class ActionPlayCardTest{

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