package edu.up.cs301.chalice;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import edu.up.cs301.game.GameFramework.Game;
import edu.up.cs301.game.GameFramework.GameHumanPlayer;
import edu.up.cs301.game.GameFramework.GameMainActivity;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;
import edu.up.cs301.game.GameFramework.infoMessage.GameInfo;

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
    private TextView P1ScoreText;
    private TextView P2ScoreText;
    private TextView P3ScoreText;
    private TextView P4ScoreText;

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

        //score updates:
       /* P1ScoreText.setText(""+state.getP1numCurrentPoints());
        P2ScoreText.setText(""+state.getP2numCurrentPoints());
        P3ScoreText.setText(""+state.getP3numCurrentPoints());
        P4ScoreText.setText(""+state.getP4numCurrentPoints());
*/

    }

    /**
     * this method gets called when the user clicks the '+' or '-' button. It
     * creates a new CounterMoveAction to return to the parent activity.
     *
     * @param button
     * 		the button that was clicked
     */
    public void onClick(View button) {
        setAsGui(myActivity);
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
        if (button.getId() == R.id.quitButton) {
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

        myActivity = activity;

        /* OLD COUNTER GUI UPDATE CODE
        // Load the layout resource for our GUI
        activity.setContentView(R.layout.counter_human_player);

        // make this object the listener for both the '+' and '-' 'buttons
        Button plusButton = (Button) activity.findViewById(R.id.plusButton);
        plusButton.setOnClickListener(this);
        Button minusButton = (Button) activity.findViewById(R.id.minusButton);
        minusButton.setOnClickListener(this);

        // remember the field that we update to display the counter's value
        this.counterValueTextView =
                (TextView) activity.findViewById(R.id.counterValueTextView);

         */

        // Load the layout for the Chalice GUI
        activity.setContentView(R.layout.chalice_gui);

        // Initialize the interactable GUI objects
        ImageButton card0 = (ImageButton) activity.findViewById(R.id.card0);
        ImageButton card1 = (ImageButton) activity.findViewById(R.id.card1);
        ImageButton card2 = (ImageButton) activity.findViewById(R.id.card2);
        ImageButton card3 = (ImageButton) activity.findViewById(R.id.card3);
        ImageButton card4 = (ImageButton) activity.findViewById(R.id.card4);
        ImageButton card5 = (ImageButton) activity.findViewById(R.id.card5);
        ImageButton card6 = (ImageButton) activity.findViewById(R.id.card6);
        ImageButton card7 = (ImageButton) activity.findViewById(R.id.card7);
        ImageButton card8 = (ImageButton) activity.findViewById(R.id.card8);
        ImageButton card9 = (ImageButton) activity.findViewById(R.id.card9);
        ImageButton card10 = (ImageButton) activity.findViewById(R.id.card10);
        ImageButton card11 = (ImageButton) activity.findViewById(R.id.card11);
        ImageButton card12 = (ImageButton) activity.findViewById(R.id.card12);

        Button playButton = (Button) activity.findViewById(R.id.playButton);
        Button menuButton = (Button) activity.findViewById(R.id.menuButton);
        Button quitButton = (Button) activity.findViewById(R.id.quitButton);

        // define the listeners for all of the interactable objects in our GUI
        card0.setOnClickListener(this);
        card1.setOnClickListener(this);
        card2.setOnClickListener(this);
        card3.setOnClickListener(this);
        card4.setOnClickListener(this);
        card5.setOnClickListener(this);
        card6.setOnClickListener(this);
        card7.setOnClickListener(this);
        card8.setOnClickListener(this);
        card9.setOnClickListener(this);
        card10.setOnClickListener(this);
        card11.setOnClickListener(this);
        card12.setOnClickListener(this);

        playButton.setOnClickListener(this);
        menuButton.setOnClickListener(this);
        quitButton.setOnClickListener(this);

        // if we have a game state, "simulate" that we have just received
        // the state from the game so that the GUI values are updated
        if (state != null) {
            receiveInfo(state);
        }
    }

}// class CounterHumanPlayer