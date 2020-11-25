/**
 * Human Player class
 *
 * A GUI of a chalice player. The GUI displays the players hand and the cards played.
 * It allows human player to select and play a card.
 *
 * @version October 18, 2020
 * @author  Alex Junkins, Malia Lundstrom, Chloe Campbell, Addison Raak
 */

package edu.up.cs301.chalice;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

import edu.up.cs301.game.GameFramework.GameHumanPlayer;
import edu.up.cs301.game.GameFramework.GameMainActivity;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;
import edu.up.cs301.game.GameFramework.infoMessage.GameInfo;
import edu.up.cs301.game.GameFramework.infoMessage.IllegalMoveInfo;

public class PlayerHuman extends GameHumanPlayer implements View.OnClickListener {

    /* instance variables */


    private TextView P1ScoreText;
    private TextView P2ScoreText;
    private TextView P3ScoreText;
    private TextView P4ScoreText;
    private TextView GameInfo;
    private Button playButton;
    private Card[] cardsToPass;

    /**
     * External Citation
     *   Date:     11 November 2020
     *   Problem:  Could not reference images of cards.
     *   Resource:
     *      https://developer.android.com/reference/android/R.drawable
     *   Solution: Used R.drawable.image .
     */
    private int[] cardImages = {
        R.drawable.cupsa, R.drawable.cups2, R.drawable.cups3, R.drawable.cups4, R.drawable.cups5,
        R.drawable.cups6, R.drawable.cups7, R.drawable.cups8, R.drawable.cups9, R.drawable.cups10,
        R.drawable.cupsj, R.drawable.cupsq, R.drawable.cupsk,

        R.drawable.swordsa, R.drawable.swords2, R.drawable.swords3, R.drawable.swords4, R.drawable.swords5,
        R.drawable.swords6, R.drawable.swords7, R.drawable.swords8, R.drawable.swords9, R.drawable.swords10,
        R.drawable.swordsj, R.drawable.swordsq, R.drawable.swordsk,

        R.drawable.coinsa, R.drawable.coins2, R.drawable.coins3, R.drawable.coins4, R.drawable.coins5,
        R.drawable.coins6, R.drawable.coins7, R.drawable.coins8, R.drawable.coins9, R.drawable.coins10,
        R.drawable.coinsj, R.drawable.coinsq, R.drawable.coinsk,

        R.drawable.wandsa, R.drawable.wands2, R.drawable.wands3, R.drawable.wands4, R.drawable.wands5,
        R.drawable.wands6, R.drawable.wands7, R.drawable.wands8, R.drawable.wands9, R.drawable.wands10,
        R.drawable.wandsj, R.drawable.wandsq, R.drawable.wandsk};

    private int cardBack = R.drawable.back_of_card;

    ArrayList <ImageButton> cardButtonList = new ArrayList<>(13);
    ArrayList <ImageView> playedCardImageList = new ArrayList<>(4);

    // the most recent game state, as given to us by the heartsLocalGame
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
        cardsToPass = new Card[3];
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
     * A method to check if a button is one of the GUI's occupied card buttons
     * Does not return positive if the button does not correspond with a card in  the player's hand
     *
     * @param button    button to be checked
     * @return  id if the button is one of the GUI's occupied cardButtons, -1 otherwise
     */
    public int isCardButton(View button){
        if (!(button instanceof ImageButton)) { return -1; }
        for (int i = 0; i < state.getP1Hand().size(); i++){
            if (button.getId() == cardButtonList.get(i).getId()) {
                return i;
            }
        }
        return -1;
    }

    /**
     *
     * @param button
     * 		the button that was clicked
     */
    public void onClick(View button) {
        // if we are not yet connected to a game, ignore
        if (game == null) return;

        GameAction action = null;

        //GameInfo.setText("Info");

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                255
        );

        //if the player clicks one of the card buttons and it holds a card, select it
        //if positive, index functions as the index of both the correct ImageButton in the
        //button list and the correct Card in state.P1Hand
        int index = isCardButton(button);
        if (index >= 0){
            if (cardsToPass[2] == null){ //check that we don't have 3 cards selected to pass
                //set as the selected card
                ArrayList<Card> myHand = PlayerComputerSimple.getMyHand(state, playerNum);
                state.setSelectedCard(myHand.get(index));

                //make the gui element a little larger, set all others to normal scale.
                for (ImageButton cardButton : cardButtonList){
                    if (cardButton.getId() == cardButtonList.get(index).getId()) {
                        cardButton.setScaleX(1.2f);
                        cardButton.setScaleY(1.2f);
                        if (cardButtonList.indexOf(cardButton) != 12) {
                            params.setMargins(0, 0, 0, 0);
                        }
                    }
                    else{
                        cardButton.setScaleX(1f);
                        cardButton.setScaleY(1f);
                        params.setMargins(0, 0, -100, 0);
                    }
                    cardButton.setLayoutParams(params);
                    cardButton.invalidate();
                }
                updateDisplay();
            } else {
                flash(Color.RED, 10);
            }

        }

        //if the play button is pressed, check if there is a card selected, check if it's the user's turn,
        //create actionPlayCard, and remove the card from the hand.
        // Construct the action and send it to the game
        //If in passing phase (shown by state.passingCards == true), instead add cards to the passing
        //array
        else if (button.getId() == R.id.playButton) { //the player pressed the "play card" button with a legal card selected
            if (!state.getPassingCards()){
                action = playerPlayCard();
            } else {
                action = playerPassCard();
            }
        }

        //todo - player must be able to quit the game with the "quit  game" button
        //       make it so when the player hits the button the menu screen works properly - for beta
//        else if (button.getId() == R.id.quitButton) {
//            // create "quit" action
//            action = new ActionQuit(this);
//            // change ActionQuit to do this + make menu work? - this wouldn't work - this does nothing to affect the localGame, which is controlling the game. If the game is going to be reset, it likely needs to come from there.
//            // myActivity.setContentView(R.layout.game_config_main);
//            //or System.exit(0);
//            //myActivity.recreate(); // restart the game!
//        }
        else if (button.getId() == R.id.menuButton) {
            final GameAction quitAction = new ActionQuit(this);

            /**
             * External Citation: https://www.tutlane.com/tutorial/android/android-popup-menu-with-examples
             */
            // a popup menu shows on the screen when the menu button is pressed
            final PopupMenu popup = new PopupMenu(myActivity,
                    myActivity.findViewById(R.id.menuButton));
            final MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.game_main, popup.getMenu());

            // an alert dialog that tells the user what the rules are
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(myActivity);
            // set the listener for the popup menu
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.rules:
                            dialogBuilder.setTitle("Rules");
                            dialogBuilder.setView(R.layout.rules_layout);
                            dialogBuilder.setCancelable(true);
                            AlertDialog dialog = dialogBuilder.create();
                            dialogBuilder.show();
                            return true;
                        case R.id.quitButton1:
                            game.sendAction(quitAction);
                            return true;
                        default:
                            return false;
                    }
                }
            });
            popup.show();
            return;
        }

        else {
            // something else was pressed: ignore
            return;
        }
        if (action != null){
            Log.i("action: ", "was sent");
            game.sendAction(action); // send action to the game
        }
    }// onClick


    /**
     * helper method for playButton's pass cards behavior
     * cut from onClick to pare it down
     * @return  the pass cards action. null if not passing cards
     */
    private GameAction playerPassCard(){
        GameAction action = null;
        if(state.getWhoTurn() == this.playerNum){
            if (cardsToPass[2] == null){
                if (state.getSelectedCard() != null) {
                    //add our selected card to the first null space in cardsToPass
                    //remove it from selectedCard and hand
                    boolean success = addCardToPassArray(state.getSelectedCard());
                    ArrayList<Card> myHand = PlayerComputerSimple.getMyHand(state, playerNum);
                    myHand.remove(state.getSelectedCard());
                    state.setSelectedCard(null);
                    updateDisplay();
                } else {
                    flash(Color.RED, 10);
                }
            } else {
                //create array and reset cardsToPass
                Card[] actionArray = new Card[3];
                System.arraycopy(cardsToPass,0, actionArray, 0, 3);
                cardsToPass = new Card[3];
                action = new ActionPassCards(this, this.playerNum, actionArray);

            }
        } else {
            flash(Color.RED, 10);
        }
        return action;
    }

    /**
     * A helper method for playerPassCard
     * replaces first null entry in cardsToPass with the card
     * @return  success state
     */
    private boolean addCardToPassArray(Card card) {
        for (int i = 0; i < cardsToPass.length; i++){
            if (cardsToPass[i] == null){
                cardsToPass[i] = card;
                return true;
            }
        }
        Log.e("assignment error", "addCardToPassArray: array was full.");
        return false;
    }

    /**
     * helper method for playButton's play card behavior
     * cut from onClick to pare it down
     * @return  the play cards action. Null if not playing a card.
     */
    private GameAction playerPlayCard(){
        GameAction action = null;
        if (state.getSelectedCard() != null) {
            if (state.getWhoTurn() == this.playerNum) {
                //define the action
                action = new ActionPlayCard(this, this.playerNum, state.getSelectedCard());
                ArrayList<Card> tempHand = state.getP1Hand(); //temporary holder for p1Hand

                /**
                 * External Citation
                 *   Date:     11 November 2020
                 *   Problem:  Could not remember how to loop through arrayList
                 *   Resource:
                 *      https://stackoverflow.com/questions/25538511/iterate-through-arraylistt-java
                 *   Solution: I used the first suggestion on this post.
                 */
                //remove the selected card from the hand
                for (Card currentCard : tempHand) {
                    if (currentCard.equals(state.getSelectedCard())) {
                        tempHand.remove(currentCard);      //the game should automatically be able to update the hand. It's not a big deal because this change will get overwritten once the localGame sends an update.
                        //set temporary hand as the P1Hand
                        state.setP1Hand(tempHand); //this and the below should get handled in heartsLocalGame's playCard method, because any changes the PlayerHuman makes to its gameState are
                        state.setP1CardPlayed(currentCard);  //overridden when it gets an updated state from the localGame
                        updateDisplay();
                        break;
                    }
                }
            }
        }
        return action;
    }


    /**
     * callback method when we get a message (e.g., from the game)
     *
     * @param info
     * 		the message
     */
    @Override
    public void receiveInfo(GameInfo info) {
        // ignore the message if it's not a CounterState message
        //todo maybe clean up this mess of if statements
        if (!(info instanceof gameStateHearts)) {
            if (!(info instanceof IllegalMoveInfo)){
                return;
            }
            else {
                IllegalMoveInfo illegalInfo = (IllegalMoveInfo)info;
                GameInfo.setText("Illegal move.");
                updateDisplay();
                flash(Color.RED, 10);
                return;
            }
        }

        if (((gameStateHearts) info).getWhoTurn() == playerNum){
            Log.i("Turn update", "receiveInfo: It is the human player's turn");
            if (GameInfo.getText() != "Illegal move."){
                GameInfo.setText("Your turn.");
            }

            if (((gameStateHearts) info).getPassingCards()) {
                GameInfo.setText("Pick 3 cards to pass.");
            }
        }
        else {
            if (((gameStateHearts) info).getPassingCards()) {
                GameInfo.setText("Other players passing.");
            } else {
                GameInfo.setText("Not your turn.");
            }
        }

        if(((gameStateHearts) info).getTricksPlayed() == 0 &&
                !((gameStateHearts) info).getPassingCards()) {
            GameInfo.setText("New Hand!");
        }

        // update our state; then update the display
        this.state = (gameStateHearts) info;
        updateDisplay();
    }

    /**
     * sets the counter value in the text view
     */
    protected void updateDisplay() {
        //todo add menu functionality - move quit button into menu and add other needed options - for beta

        //score updates:
        //(heartsLocalGame)game.GetPlayers();
        updateScores();

        //change play button if we are passing cards
        if(state.getPassingCards()){
            if (cardsToPass[2] != null){
                playButton.setText("                PASS                    ");
            }else {
                playButton.setText("                PICK                    ");
            }
        } else {
            playButton.setText("                PLAY                    ");
        }

        /**
         External Citation
            Date: 11 November 2020
            Problem: Could not figure out how to programmatically hide/draw buttons
            Resource:
                https://stackoverflow.com/questions/11169360/android-remove-button-dynamically
            Solution: button.setVisibility(View.GONE) && ...Visibility(View.VISIBLE)
                View.GONE makes it so the button doesn't take up space anymore
         */

        //card image updates
        //cards in hand
        int i;
        ArrayList<Card> myHand = PlayerComputerSimple.getMyHand(state, playerNum);
        for (i = 0; i < myHand.size(); i++){
            cardButtonList.get(i).setVisibility(View.VISIBLE);
            cardButtonList.get(i).setImageResource(cardImages[imageForCard(myHand.get(i))]);
        }
        //update the empty buttons to be empty if the card has been played
        for (i = i; i < cardButtonList.size(); i++) {
            //set to empty
            cardButtonList.get(i).setVisibility(View.GONE);
        }
        //selected card
        if (state.getSelectedCard() == null){
            for (ImageButton cardButton : cardButtonList){
                cardButton.setScaleX(1f);
                cardButton.setScaleY(1f);
            }
        }

        //set middle card images
        if (state.getPassingCards()){
            ShowCardsPassing();
        } else {
            ShowCardsPlaying();
        }

//        if (state.getWinnerID() == 0) {
//            playedCardImageList.get(0).setColorFilter(Color.BLACK);
//            playedCardImageList.get(0).invalidate();
//            getTimer().setInterval(500);
//            getTimer().start();
//            playedCardImageList.get(0).setColorFilter(null);
//            playedCardImageList.get(0).invalidate();
//        } else if (state.getWinnerID() == 1) {
//            playedCardImageList.get(0).setColorFilter(Color.BLACK);
//            playedCardImageList.get(0).invalidate();
//            getTimer().setInterval(500);
//            getTimer().start();
//            playedCardImageList.get(0).setColorFilter(null);
//            playedCardImageList.get(0).invalidate();
//        } else if (state.getWinnerID() == 2) {
//            playedCardImageList.get(0).setColorFilter(Color.BLACK);
//            playedCardImageList.get(0).invalidate();
//            getTimer().setInterval(500);
//            getTimer().start();
//            playedCardImageList.get(0).setColorFilter(null);
//            playedCardImageList.get(0).invalidate();
//        } else if (state.getWinnerID() == 3) {
//            playedCardImageList.get(0).setColorFilter(Color.BLACK);
//            playedCardImageList.get(0).invalidate();
//            getTimer().setInterval(500);
//            getTimer().start();
//            playedCardImageList.get(0).setColorFilter(null);
//            playedCardImageList.get(0).invalidate();
//        }

        Log.i("updateDisplay: ", "finished updating display");
    }


    /**
     * A method to update the scores
     * human player can be any playerNum 0-3
     */
    private void updateScores(){
        String str0 = allPlayerNames[0] + "'s\nScore: " + state.getP1CurrentPoints();
        String str1 = allPlayerNames[1] + "'s\nScore: " + state.getP2CurrentPoints();
        String str2 = allPlayerNames[2] + "'s\nScore: " + state.getP3CurrentPoints();
        String str3 = allPlayerNames[3] + "'s\nScore: " + state.getP4CurrentPoints();
        String strPlayer = allPlayerNames[playerNum] + "'s Score: " +
                getPlayerNumCurrentPoints(state, playerNum);
        switch(playerNum){
            case 0:
                P1ScoreText.setText(strPlayer);
                P2ScoreText.setText(str1);
                P3ScoreText.setText(str2);
                P4ScoreText.setText(str3);
                break;
            case 1:
                P1ScoreText.setText(strPlayer);
                P2ScoreText.setText(str0);
                P3ScoreText.setText(str2);
                P4ScoreText.setText(str3);
                break;
            case 2:
                P1ScoreText.setText(strPlayer);
                P2ScoreText.setText(str1);
                P3ScoreText.setText(str0);
                P4ScoreText.setText(str3);
                break;
            case 3:
                P1ScoreText.setText(strPlayer);
                P2ScoreText.setText(str1);
                P3ScoreText.setText(str2);
                P4ScoreText.setText(str0);
                break;
            default:
                return;
        }
    }

    /**
     * A method to show the middle cards while the player is picking cards to pass
     */
    public void ShowCardsPassing() {
        // 0 trickBottom -  cardsToPass[1]
        if (cardsToPass[1] != null) {
            int img = imageForCard(cardsToPass[1]);
            playedCardImageList.get(0).setImageResource(cardImages[img]);
            playedCardImageList.get(0).setVisibility(View.VISIBLE);
        }
        else {
            playedCardImageList.get(0).setVisibility(View.INVISIBLE);
        }

        // 1 trickLeft - cardsToPass[0]
        if (cardsToPass[0] != null) {
            int img = imageForCard(cardsToPass[0]);
            playedCardImageList.get(1).setImageResource(cardImages[img]);
            playedCardImageList.get(1).setVisibility(View.VISIBLE);
        }
        else {
            playedCardImageList.get(1).setVisibility(View.INVISIBLE);
        }

        // 2 trickTop - always empty
        playedCardImageList.get(2).setVisibility(View.INVISIBLE);

        // 3 trickRight - cardsToPass[2]
        if (cardsToPass[2] != null) {
            int img = imageForCard(cardsToPass[2]);
            playedCardImageList.get(3).setImageResource(cardImages[img]);
            playedCardImageList.get(3).setVisibility(View.VISIBLE);
        }
        else {
            playedCardImageList.get(3).setVisibility(View.INVISIBLE);
        }
    }


    /**
     * A method to show the middle cards during normal play
     */
    public void ShowCardsPlaying(){
        //show played cards next to the player who played it
        //played cards indices
        // 0 trickBottom -  P1 note: P1 isn't necessarily the human player
        switch(playerNum){
            case 0:
                displayPlayedCard(state.getP1CardPlayed(), 0);
                displayPlayedCard(state.getP2CardPlayed(), 1);
                displayPlayedCard(state.getP3CardPlayed(), 2);
                displayPlayedCard(state.getP4CardPlayed(), 3);
                break;
            case 1:
                displayPlayedCard(state.getP1CardPlayed(), 1);
                displayPlayedCard(state.getP2CardPlayed(), 0);
                displayPlayedCard(state.getP3CardPlayed(), 2);
                displayPlayedCard(state.getP4CardPlayed(), 3);
                break;
            case 2:
                displayPlayedCard(state.getP1CardPlayed(), 2);
                displayPlayedCard(state.getP2CardPlayed(), 1);
                displayPlayedCard(state.getP3CardPlayed(), 0);
                displayPlayedCard(state.getP4CardPlayed(), 3);
                break;
            case 3:
                displayPlayedCard(state.getP1CardPlayed(), 3);
                displayPlayedCard(state.getP2CardPlayed(), 1);
                displayPlayedCard(state.getP3CardPlayed(), 2);
                displayPlayedCard(state.getP4CardPlayed(), 0);
                break;
            default:
                return;
        }
    }

    //showindex:
    //0 - bottom
    //1 - left
    //2 - top
    //3 - right
    private void displayPlayedCard(Card card, int showIndex){
        if (card != null) {
            int img = imageForCard(card);
            playedCardImageList.get(showIndex).setImageResource(cardImages[img]);
            playedCardImageList.get(showIndex).setVisibility(View.VISIBLE);
        }
        else {
            playedCardImageList.get(showIndex).setVisibility(View.INVISIBLE);
        }
    }

    /**
     * A method that when given a card returns the corresponding index for the image of the card
     * in the cardImages array. If the card is null, returns transparent
     *
     * @param card
     * @return id to use with the cardImages array
     */
    public int imageForCard(Card card) {
        int id = (13*(card.getCardSuit()-1)) + card.getCardVal() - 1;
        //Log.i("check", "imageForCard: Card with suit " + card.getCardSuit() + " and value " + card.getCardVal() + " has image id of " + id);
        return id;
    }


    /**
     * A method that initializes the entire GUI
     *
     * @param activity
     * 		the activity under which we are running
     */
    public void setAsGui(GameMainActivity activity) {

        myActivity = activity;

        // Load the layout for the Chalice GUI
        activity.setContentView(R.layout.chalice_gui);

        // Initialize the intractable GUI objects
        cardButtonList.add((ImageButton) activity.findViewById(R.id.card0));
        cardButtonList.add((ImageButton) activity.findViewById(R.id.card1));
        cardButtonList.add((ImageButton) activity.findViewById(R.id.card2));
        cardButtonList.add((ImageButton) activity.findViewById(R.id.card3));
        cardButtonList.add((ImageButton) activity.findViewById(R.id.card4));
        cardButtonList.add((ImageButton) activity.findViewById(R.id.card5));
        cardButtonList.add((ImageButton) activity.findViewById(R.id.card6));
        cardButtonList.add((ImageButton) activity.findViewById(R.id.card7));
        cardButtonList.add((ImageButton) activity.findViewById(R.id.card8));
        cardButtonList.add((ImageButton) activity.findViewById(R.id.card9));
        cardButtonList.add((ImageButton) activity.findViewById(R.id.card10));
        cardButtonList.add((ImageButton) activity.findViewById(R.id.card11));
        cardButtonList.add((ImageButton) activity.findViewById(R.id.card12));

        playedCardImageList.add((ImageView) activity.findViewById(R.id.trickBottom));
        playedCardImageList.add((ImageView) activity.findViewById(R.id.trickLeft));
        playedCardImageList.add((ImageView) activity.findViewById(R.id.trickTop));
        playedCardImageList.add((ImageView) activity.findViewById(R.id.trickRight));
        
        playButton = (Button) activity.findViewById(R.id.playButton);
        Button menuButton = (Button) activity.findViewById(R.id.menuButton);

        // Items in the menu
        MenuItem quitButton1 = (MenuItem) activity.findViewById(R.id.quitButton1);

        // define the listeners for all of the interactable objects in our GUI
        for (ImageButton button : cardButtonList) {
            button.setOnClickListener(this);
        }

        playButton.setOnClickListener(this);
        menuButton.setOnClickListener(this);

        //fields to be updated
        this.P1ScoreText = (TextView) activity.findViewById(R.id.p1Score);
        this.P2ScoreText = (TextView) activity.findViewById(R.id.p2Score);
        this.P3ScoreText = (TextView) activity.findViewById(R.id.p3Score);
        this.P4ScoreText = (TextView) activity.findViewById(R.id.p4Score);
        this.GameInfo = (TextView) activity.findViewById(R.id.gameInfo);

        // if we have a game state, "simulate" that we have just received
        // the state from the game so that the GUI values are updated
        if (state != null) {
            receiveInfo(state);
        }
    } // onClick

    public void setAllPlayerNames(String[] playerNames){
        allPlayerNames = playerNames;
    }

    public static int getPlayerNumCurrentPoints(gameStateHearts state, int playerNum){
        switch (playerNum){
            case 0:
                return state.getP1CurrentPoints();
            case 1:
                return state.getP2CurrentPoints();
            case 2:
                return state.getP3CurrentPoints();
            case 3:
                return state.getP4CurrentPoints();
            default:
                return -1;
        }
    }


}// class CounterHumanPlayer