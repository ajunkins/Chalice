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
 * Project i: Final Release
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
 * Additional Features:
 * -Options to play in English, Spanish, and French.
 * -Two lengths of gameplay - play to 50 points or 100 points
 * -A "rules" option in the in-game menu to help when during play
 * -A fully custom card deck with art inspired by Tarot cards!
 * -Advanced AIs that can pose a real challenge!
 * -All AIs are given names if the default names are unchanged, and display reactions
 *      to developments in the game!
 * -Additionally, the advanced AIs have unique personalities which are assigned at
 *      the start of the game and determine their in-game behavior and strategies,
 *      as well as giving them unique reactions!
 * -All AI chat messages also have translations!
 *
 * @version December 4, 2020
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
        ArrayList<GamePlayerType> playerTypes = new ArrayList<>();

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
        defaultConfig.setRemoteData(getString(R.string.remotePlayText), "", 0);

        // return the configuration
        return defaultConfig;
    } //createDefaultConfig

    /**
     * External Citation
     * Date: 11/19/2020
     * Problem: adding a radio button to change the length of the game on the
     * game_config_main.xml
     * Resource: Teams call with Dr. Vegdahl
     * Solution: had to made a static variable so we could access it
     */

    /**
     * places the data from this.config into the GUI.
     */
    @Override
    protected void initStarterGui() {
        super.initStarterGui();
        RadioGroup radio = findViewById(R.id.rGroup);
        radio.setOnCheckedChangeListener(this);
    } //initStarterGui

     /**
      * a method to check if the radio button for game length was changed
      * @param radioGroup   The group of radio buttons
      * @param i            index
     */
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        int checkedId = radioGroup.getCheckedRadioButtonId();
        if(checkedId == R.id.longLength1) {
            ChaliceLocalGame.setGameLength(100);
        }
        else {
            ChaliceLocalGame.setGameLength(50);
        }
    } //onCheckedChanged

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