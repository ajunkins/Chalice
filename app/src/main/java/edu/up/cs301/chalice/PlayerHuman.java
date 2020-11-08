package edu.up.cs301.chalice;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import edu.up.cs301.game.GameFramework.Game;
import edu.up.cs301.game.GameFramework.GameHumanPlayer;
import edu.up.cs301.game.GameFramework.GameMainActivity;
import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;
import edu.up.cs301.game.GameFramework.actionMessage.GameOverAckAction;
import edu.up.cs301.game.GameFramework.actionMessage.MyNameIsAction;
import edu.up.cs301.game.GameFramework.actionMessage.ReadyAction;
import edu.up.cs301.game.GameFramework.infoMessage.BindGameInfo;
import edu.up.cs301.game.GameFramework.infoMessage.GameInfo;
import edu.up.cs301.game.GameFramework.infoMessage.GameOverInfo;
import edu.up.cs301.game.GameFramework.infoMessage.StartGameInfo;
import edu.up.cs301.game.GameFramework.infoMessage.TimerInfo;
import edu.up.cs301.game.GameFramework.utilities.GameTimer;
import edu.up.cs301.game.GameFramework.utilities.Logger;
import edu.up.cs301.game.GameFramework.utilities.MessageBox;
import edu.up.cs301.game.GameFramework.utilities.Tickable;

/**
 * THIS WAS ORIGINALLY - CounterHumanPlayer - NEEDS TO BE READ THROUGH AND ADAPTED TO CHALICE
 * as far as I can tell, it manages the Counter Gui and player actions right now.
 *
 * A GUI of a counter-player. The GUI displays the current value of the counter,
 * and allows the human player to press the '+' and '-' buttons in order to
 * send moves to the game.
 *
 * Just for fun, the GUI is implemented so that if the player presses either button
 * when the counter-value is zero, the screen flashes briefly, with the flash-color
 * being dependent on whether the player is player 0 or player 1.
 *
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll
 * @version July 2013
 */
public class PlayerHuman extends GameHumanPlayer implements View.OnClickListener {

    /* instance variables */

    // The TextView the displays the current counter value
    private TextView counterValueTextView;

    // the most recent game state, as given to us by the CounterLocalGame
    private gameStateHearts state;

    // the android activity that we are running
    private GameMainActivity myActivity;

    /**
     * constructor
     * @param name
     * 		the player's name
     */
    public PlayerHuman(String name) {
        super(name);
    }

    /**
     * Returns the GUI's top view object
     *
     * @return
     * 		the top object in the GUI's view heirarchy
     */
    public View getTopView() {
        return myActivity.findViewById(R.id.top_gui_layout);
    }

    /**
     * sets the counter value in the text view
     */
    protected void updateDisplay() {
        //todo update the scores, hands cards, selected cards, and played cards - maybe menu
        //counterValueTextView.setText("" + state.getCounter()); <- old counter code
    }

    /**
     * this method gets called when the user clicks the '+' or '-' button. It
     * creates a new CounterMoveAction to return to the parent activity.
     *
     * @param button
     * 		the button that was clicked
     */
    public void onClick(View button) {
        // if we are not yet connected to a game, ignore
        if (game == null) return;

        // Construct the action and send it to the game
        GameAction action = null;
        //todo - player must be able to select and play a card
        if (button.getId() == R.id.playButton) { //the player pressed the "play card" button with a legal card selected
            //do nothing for now
            return;
        }
        //todo - player must be able to quit the game with the "quit  game" button
        else if (button.getId() == R.id.quitButton) {
            // minus button: create "quit" action
            action = new ActionQuit(this);
        }
        //todo - player must be able to open the menu with the "menu" button
        else if (button.getId() == R.id.menuButton) {
            //do nothing for now
            return;
        }

        else {
            // something else was pressed: ignore
            return;
        }

        game.sendAction(action); // send action to the game
    }// onClick

    /**
     * callback method when we get a message (e.g., from the game)
     *
     * @param info
     * 		the message
     */
    @Override
    public void receiveInfo(GameInfo info) {
        // ignore the message if it's not a CounterState message
        if (!(info instanceof gameStateHearts)) return;

        // update our state; then update the display
        this.state = (gameStateHearts) info;
        updateDisplay();
    }

    /**
     * callback method--our game has been chosen/rechosen to be the GUI,
     * called from the GUI thread
     *
     * @param activity
     * 		the activity under which we are running
     */
    public void setAsGui(GameMainActivity activity) {

        // remember the activity
        myActivity = activity;

        //Load the layout resource for our GUI
        activity.setContentView(R.layout.activity_main);

        // make MainActivity object the listener for both the 'play' and 'quit' 'buttons
        Button playButton = (Button) activity.findViewById(R.id.playButton);
        playButton.setOnClickListener(this);
        Button quitButton = (Button) activity.findViewById(R.id.quitButton);
        quitButton.setOnClickListener(this);
        Button menuButton = (Button) activity.findViewById(R.id.menuButton);

        // remember the fields that we update to display the scores
        TextView player1Score = (TextView) activity.findViewById(R.id.p1Score);
        TextView player2Score = (TextView) activity.findViewById(R.id.p2Score);
        TextView player3Score = (TextView) activity.findViewById(R.id.p3Score);
        TextView player4Score = (TextView) activity.findViewById(R.id.p4Score);

        //todo - this needs needs to get references to all the proper gui elements


        if (state != null) {
            receiveInfo(state);
        }
    }

}// class CounterHumanPlayer