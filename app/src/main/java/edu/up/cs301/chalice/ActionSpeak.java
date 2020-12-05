package edu.up.cs301.chalice;

import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;

/**
 * ActionSpeak class
 * contains a speech action
 * an AI only action
 *
 * @version December 4, 2020
 * @author  Alex Junkins, Malia Lundstrom, Chloe Campbell, Addison Raak
 */
public class ActionSpeak extends GameAction{

    //declare instance variables
    private InfoDisplaySpeech.speechType speech;

    /**
     * constructor for the ActionSpeak class
     *
     * @param player the player who created the action
     */
    public ActionSpeak(GamePlayer player,
                       InfoDisplaySpeech.speechType speech) {
        super(player);
        this.speech = speech;
    }

    /**
     * A getter for speech
     * @return  the speechType
     */
    public InfoDisplaySpeech.speechType getSpeech() { return speech; }
}
