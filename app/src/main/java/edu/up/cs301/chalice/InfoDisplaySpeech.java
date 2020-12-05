package edu.up.cs301.chalice;

import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.infoMessage.GameInfo;

/**
 * A message from the game to a player that tells the player that
 * an AI is saying a message. When sent to an AI, it's alerting
 * it of a situation where it needs to pick something to say, so
 * the message is empty
 *
 * @version December 4, 2020
 * @author  Alex Junkins, Malia Lundstrom, Chloe Campbell, Addison Raak
 */

public class InfoDisplaySpeech extends GameInfo {


    //create speech types
    public enum speechType {
        GREETING,
        HAPPY,
        SAD,
        ANGRY,
        SURPRISE,
        MYSTERIOUS
    } //speechType

    //Tag for logging
    private static final String TAG = "GameOverInfo";
    // to satisfy the Serializable interface
    private static final long serialVersionUID = -8005304466588509849L;

    //the reference to the player that spoke
    private GamePlayer player;
    // the personality type (null if dumb AI)
    private PlayerComputerAdvanced.personalityType personality;
    private speechType speech;

    /**
     * Constructor
     * @param player        The player
     * @param personality   The player's personality type
     * @param speech          The speech type
     */
    public InfoDisplaySpeech(GamePlayer player,
                             PlayerComputerAdvanced.personalityType personality,
                             speechType speech) {
        this.player = player;
        this.personality = personality;
        this.speech = speech;
    } //InfoDisplaySpeech

    /**
     * getter method for the speech
     * @return  the message, telling the result of the game
     */
    public speechType getSpeech() {
        return speech;
    } //getSpeech


    /**
     * getter method for the personality
     * @return  the message, telling the result of the game
     */
    public PlayerComputerAdvanced.personalityType getPersonality() {
        return personality;
    } //getPersonality

    /**
     * getter for the player reference
     * @retuurn the player reference
     */
    public GamePlayer getPlayer(){
        return player;
    } //getPlayer

}
