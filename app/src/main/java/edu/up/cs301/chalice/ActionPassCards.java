package edu.up.cs301.chalice;

import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;

/**
 * ActionPassCard class
 * contains a pass card action
 *
 * @version November 23, 2020
 * @author  Alex Junkins, Malia Lundstrom, Chloe Campbell, Addison Raak
 */public class ActionPassCards extends GameAction {

    // to satisfy the serializable interface
    private static final long serialVersionUID = 28062013L;

    private int playerNum;      //the id number of the player who played it
    private Card[] passedCards; //the card objects the player is trying to pass

    /**
     * Constructor for the ActionPassCard class.
     *
     * @param player
     *            the player making the move
     * @param card
     *            the cards the player is passing
     */
    public ActionPassCards(GamePlayer player, int num, Card[] card) {
        super(player);
        this.playerNum = num;
        this.passedCards = card;
    } //ActionPassCards

    /**
     * getter method, to tell which card the action is attempting to play
     * @return the cards meant to be passed
     */
    public Card[] passedCards() {
        return this.passedCards;
    } //passedCards

    /**
     * A getter for the number of the player that is sending the action
     * @return the player number
     */
    public int getPlayerNum() {
        return this.playerNum;
    } //getPlayerNum

 } //actionPassCard
