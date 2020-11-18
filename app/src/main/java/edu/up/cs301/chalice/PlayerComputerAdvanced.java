package edu.up.cs301.chalice;

import edu.up.cs301.game.GameFramework.GameComputerPlayer;
import edu.up.cs301.game.GameFramework.infoMessage.GameInfo;
import edu.up.cs301.game.GameFramework.utilities.Tickable;

import static edu.up.cs301.chalice.Card.COINS;

public class PlayerComputerAdvanced extends PlayerComputerSimple implements Tickable {

    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public PlayerComputerAdvanced(String name) {
        super(name);

        // start the timer, ticking 20 times per second
        getTimer().setInterval(50);
        getTimer().start();
    }

    @Override
    protected void receiveInfo(GameInfo info) {
        if (!(info instanceof gameStateHearts)) {
            return;
        }
        gameStateHearts state = new gameStateHearts((gameStateHearts)info);

        //not my turn
        if (playerNum != state.getWhoTurn()) {
            return;
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(state.getTricksPlayed() == 0 && state.getTrickCardsPlayed().size() ==0) {
            Card coins2 = new Card(2,COINS);
            int cardIndex=-1;
            for (Card card: getMyHand(state)) {
                if(Card.sameCard(card, coins2)) {
                    cardIndex = getMyHand(state).indexOf(card);
                }
            }
            game.sendAction((new ActionPlayCard(this, this.playerNum,getMyHand(state).get(cardIndex))));
        }

    }
}
