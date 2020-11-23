package edu.up.cs301.chalice;

import android.view.View;
import android.widget.RadioGroup;

import java.util.ArrayList;
import edu.up.cs301.game.GameFramework.GameMainActivity;
import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.LocalGame;
import edu.up.cs301.game.GameFramework.gameConfiguration.GameConfig;
import edu.up.cs301.game.GameFramework.gameConfiguration.GamePlayerType;

/**
 * this is the primary activity for Chalice game
 *
 * @version October 18, 2020
 * @author Alex Junkins, Malia Lundstrom, Chloe Campbell, Addison Raak
 *
 */
public class MainActivity extends GameMainActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener{

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
        playerTypes.add(new GamePlayerType("Local Human Player") {
            public GamePlayer createPlayer(String name) {
                return new PlayerHuman(name);
            }});

        // a computer player type (dumb AI)
        playerTypes.add(new GamePlayerType("Computer Player") {
            public GamePlayer createPlayer(String name) {
                return new PlayerComputerSimple(name);
            }});

        // Create a game configuration class for Chalice:
        // - player types as given above
        // - 4 players
        // - name of game is "Chalice"
        // - port number as defined above
        GameConfig defaultConfig = new GameConfig(playerTypes, 4, 4, "Chalice",
                PORT_NUMBER);

        // Add the default players to the configuration
        defaultConfig.addPlayer("Human", 0); // player 1: a human player
        defaultConfig.addPlayer("Computer", 1); //player 2: a computer player
        defaultConfig.addPlayer("Computer2", 1);//player 3: a computer player
        defaultConfig.addPlayer("Computer3", 1);//player 4: a computer player


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
            heartsLocalGame.setGameLength(100);
        }
        else {
            heartsLocalGame.setGameLength(50);
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
        return new heartsLocalGame();
    }

}