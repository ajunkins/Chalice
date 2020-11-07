package edu.up.cs301.chalice;

import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;

public class ActionPlayCard extends GameAction {

    // to satisfy the serializable interface
    private static final long serialVersionUID = 28062013L;

    //whether this move is a plus (true) or minus (false)
    private boolean isPlus;

    private Card playedCard;

    /**
     * Constructor for the ActionPlayCard class.
     *
     * @param player
     *            the player making the move
     * @param card
     *            the card the player is playing
     */
    public ActionPlayCard(GamePlayer player, Card card) {
        super(player);
        this.playedCard = card;
    }

    /**
     * getter method, to tell whether the move is a "plus"
     *
     * @return
     * 		the card meant to be played
     */
    public Card playedCard() {
        return this.playedCard;
    }
} //actionPlayCard
