package Chalice;

import org.junit.Test;
import edu.up.cs301.chalice.Card;
import edu.up.cs301.chalice.PlayerHuman;
import edu.up.cs301.chalice.R;
import static org.junit.Assert.*;

/**
 * PlayerHumanTest
 * Unit test for PlayerHuman
 *
 * @version December 4, 2020
 * @author  Alex Junkins, Malia Lundstrom, Chloe Campbell, Addison Raak
 */
public class PlayerHumanTest {

    /**
     * a method to test imageForCard() in the PlayerHuman class
     */
    @Test
    public void imageForCard() {
        PlayerHuman p1 = new PlayerHuman("Chloe");
        Card card1 = new Card(2,3);
        int cardNum = p1.imageForCard(card1);
        int[] arr = p1.getCardImages();
        assertEquals(R.drawable.coins2, arr[cardNum]);
    }
}