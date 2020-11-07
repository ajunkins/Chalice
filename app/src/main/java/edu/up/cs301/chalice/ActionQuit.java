package edu.up.cs301.chalice;

import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;

public class ActionQuit extends GameAction {

    // to satisfy the serializable interface
    private static final long serialVersionUID = 28062013L;

    /**
     * Constructor for the ActionPlayCard class.
     *
     * @param player
     *            the player making the move
     */
    public ActionQuit(GamePlayer player) {
        super(player);
    }

} //actionPlayCard
