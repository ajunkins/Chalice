package edu.up.cs301.chalice;

import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;

/**
 * ActionPlayCard class
 * contains a play card action
 *
 * @version November 11, 2020
 * @author  Alex Junkins, Malia Lundstrom, Chloe Campbell, Addison Raak
 */
public class ActionPlayCard extends GameAction {

    // to satisfy the serializable interface
    private static final long serialVersionUID = 28062013L;

    private int playerNum;      //the id number of the player who played it
    private Card playedCard;    //the card object the player is trying to play

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
    } //ActionPlayCard

    /**
     * A getter for the number of the player that is sending the action
     * @return the player number
     */
    public int getPlayerNum() {
        return this.playerNum;
    } //getPlayerNum

    /**
     * getter method, to tell which card the action is attempting to play
     * @return the card meant to be played
     */
    public Card playedCard() {
        return this.playedCard;
    } //playedCard

} //actionPlayCard
