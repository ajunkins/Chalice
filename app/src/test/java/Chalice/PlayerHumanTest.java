package Chalice;

import org.junit.Test;

import edu.up.cs301.chalice.Card;
import edu.up.cs301.chalice.ChaliceLocalGame;
import edu.up.cs301.chalice.PlayerComputerSimple;
import edu.up.cs301.chalice.PlayerHuman;
import edu.up.cs301.chalice.R;
import edu.up.cs301.game.GameFramework.LocalGame;
import edu.up.cs301.game.GameFramework.gameConfiguration.GameConfig;

import static org.junit.Assert.*;

public class PlayerHumanTest {

    @Test
    public void imageForCard() {
        PlayerHuman p1 = new PlayerHuman("Chloe");
        Card card1 = new Card(2,3);
        int cardNum = p1.imageForCard(card1);
        int[] arr = p1.getCardImages();
        assertEquals(R.drawable.coins2, arr[cardNum]);
    }
}