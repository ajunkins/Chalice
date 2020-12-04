/**
 * ActionSpeak class
 * contains a speech action
 * an AI only action
 *
 * @version December 3, 2020
 * @author  Alex Junkins, Malia Lundstrom, Chloe Campbell, Addison Raak
 */

package edu.up.cs301.chalice;

import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;

public class ActionSpeak extends GameAction{

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
}
