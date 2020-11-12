package edu.up.cs301.chalice;

import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;

public class ActionPlayCard extends GameAction {

    // to satisfy the serializable interface
    private static final long serialVersionUID = 28062013L;

    private int playerNum;
    private Card playedCard;

    /**
     * Constructor for the ActionPlayCard class.
     *
     * @param player
     *            the player making the move
     * @param card
     *            the card the player is playing
     */
    public ActionPlayCard(GamePlayer player, int num, Card card) {
        super(player);
        this.playerNum = num;
        this.playedCard = card;
    }

    /**
     * getter method, to tell which card the action is attempting to play
     * @return the card meant to be played
     */
    public Card playedCard() {
        return this.playedCard;
    }

    /**
     * A getter for the number of the player that is sending the action
     * @return the player number
     */
    public int getPlayerNum() { return this.playerNum; }
} //actionPlayCard
