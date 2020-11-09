package edu.up.cs301.chalice;


//import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.Normalizer;
import java.util.ArrayList;

import edu.up.cs301.game.GameFramework.GameMainActivity;
import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.LocalGame;
import edu.up.cs301.game.GameFramework.gameConfiguration.GameConfig;
import edu.up.cs301.game.GameFramework.gameConfiguration.GamePlayerType;


public class MainActivity extends GameMainActivity implements View.OnClickListener {

    //declare variables
    private static final int PORT_NUMBER = 2234;

    //this is commented out because caused the game_config_main.xml buttons not to work
    //todo - handle onClicks for shortLength and longLength games
   /* @Override
    public void onClick(View button) {
        //side note- not sure if this should be here or in GameMainActivity onClick
        //todo - player should be able to click "Play to 50" and update game length to 50
        if(button.getId() == R.id.shortLength){
            //for now do nothing
        }
        //todo - player should be able to click "Play to 50" and update game length to 50
        if(button.getId() == R.id.longLength){
            //for now do nothing
        }

    }*/
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