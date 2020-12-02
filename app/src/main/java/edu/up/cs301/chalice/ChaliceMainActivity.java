package edu.up.cs301.chalice;

import android.view.View;
import android.widget.RadioGroup;

import java.util.ArrayList;
import edu.up.cs301.game.GameFramework.GameMainActivity;
import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.LocalGame;
import edu.up.cs301.game.GameFramework.ProxyPlayer;
import edu.up.cs301.game.GameFramework.gameConfiguration.GameConfig;
import edu.up.cs301.game.GameFramework.gameConfiguration.GamePlayerType;
import edu.up.cs301.game.GameFramework.utilities.Logger;

/**
 * this is the primary activity for Chalice game
 * Project h: Beta Release
 *
 * The game is complete and fully playable!
 * Chalices supports all the rules that we committed to in our requirements
 * All functionality of the GUI we specified in the requirements are present.
 * Smart and Dumb AI are present and clearly different in play style and effectiveness
 * All graphic elements are complete and in final form. (very visually appealing)
 * Our requirements specified 4 players, and 4 players are working.
 * GUI is functional and effective
 * Known Bugs: When the human player ends a trick, the GUI updates slowly,
 * the cause is unknown.
 * Additional Features: Two lengths of gameplay! The AI's have fun names and
 * you can change them. A fully custom card deck inspired by Tarot cards.
 *
 * @version November 25, 2020
 * @author Alex Junkins, Malia Lundstrom, Chloe Campbell, Addison Raak
 *
 */
public class ChaliceMainActivity extends GameMainActivity
        implements View.OnClickListener, RadioGroup.OnCheckedChangeListener{

    //declare variables
    private static final int PORT_NUMBER = 2234;

    /**
     * Create the default configuration for this game:
     * - one human player vs. one computer player
     * - minimum of 1 player, maximum of 2
     * - one kind of computer player and one kind of human player available
     *
     * @return
     * 		the new configuration object, representing the default configuration
     */
    @Override
    public GameConfig createDefaultConfig() {

        // Define the allowed player types
        ArrayList<GamePlayerType> playerTypes = new ArrayList<GamePlayerType>();

        // a human player player type (player type 0)
        playerTypes.add(new GamePlayerType(getString(R.string.localHumText)) {
            public GamePlayer createPlayer(String name) {
                return new PlayerHuman(name); }});

        // a computer player type (dumb AI)
        playerTypes.add(new GamePlayerType(getString(R.string.dumbCompText)) {
            public GamePlayer createPlayer(String name) {
                return new PlayerComputerSimple(name); }});

        // a computer player type (smart AI)
        playerTypes.add(new GamePlayerType(getString(R.string.smartCompText)) {
            public GamePlayer createPlayer(String name) {
                return new PlayerComputerAdvanced(name); }});

        playerTypes.add(new GamePlayerType("WiFi Player"){
            public GamePlayer createPlayer(String name) {
                int portNum = PORT_NUMBER;
                return new ProxyPlayer(portNum);
            }
        });

        // Create a game configuration class for Chalice:
        // - player types as given above
        // - 4 players
        // - name of game is "Chalice"
        // - port number as defined above
        GameConfig defaultConfig = new GameConfig(playerTypes,4, 4, "Chalice"
                , PORT_NUMBER);

        // Add the default players to the configuration
        defaultConfig.addPlayer(getString(R.string.humText), 0); // player 1: a human player
        defaultConfig.addPlayer(getString(R.string.comp1Text), 1); //player 2: a computer player
        defaultConfig.addPlayer(getString(R.string.comp2Text), 1);//player 3: a computer player
        defaultConfig.addPlayer(getString(R.string.comp3Text), 1);//player 4: a computer player


        // Set the default remote-player setup:
        // - player name: "Remote Player"
        // - IP code: (empty string)
        // - default player type: human player
        defaultConfig.setRemoteData("Remote Player", "", 0);

        // return the configuration
        return defaultConfig;
    }//createDefaultConfig

    @Override
    protected void initStarterGui() {
        super.initStarterGui();
        RadioGroup radio = findViewById(R.id.rGroup);
        radio.setOnCheckedChangeListener(this);
    }


    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        int checkedId = radioGroup.getCheckedRadioButtonId();
        if(checkedId == R.id.longLength1) {
            ChaliceLocalGame.setGameLength(100);
        }
        else {
            ChaliceLocalGame.setGameLength(50);
        }
    }
    /**
     * create a local game
     *
     * @return
     * 		the local game, a counter game
     */
    @Override
    public LocalGame createLocalGame() {
        return new ChaliceLocalGame(this);
    }

}//Main