package Chalice;

import org.junit.Test;

import edu.up.cs301.chalice.Card;
import edu.up.cs301.chalice.PlayerComputerAdvanced;
import edu.up.cs301.chalice.chaliceGameState;

import static org.junit.Assert.*;

public class PlayerComputerAdvancedTest {
    @Test
    public void pointsOnTable() {
        PlayerComputerAdvanced testAI = new PlayerComputerAdvanced("Test");
        chaliceGameState state = new chaliceGameState();
        state.setP1CardPlayed(new Card(5,1));
        state.setP2CardPlayed(new Card(2,1));
        state.setP3CardPlayed(new Card(13,1));
        int sum = testAI.pointsOnTable(state);
        assertEquals(3, sum);
    }
}