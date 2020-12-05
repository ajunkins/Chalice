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
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import edu.up.cs301.game.GameFramework.GameComputerPlayer;
import edu.up.cs301.game.GameFramework.GameHumanPlayer;
import edu.up.cs301.game.GameFramework.GameMainActivity;
import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;
import edu.up.cs301.game.GameFramework.infoMessage.GameInfo;
import edu.up.cs301.game.GameFramework.infoMessage.IllegalMoveInfo;

public class PlayerHuman extends GameHumanPlayer implements
        View.OnClickListener {

    /* instance variables */


    private TextView P1ScoreText;
    private TextView P2ScoreText;
    private TextView P3ScoreText;
    private TextView P4ScoreText;
    private TextView P2Chat;
    private TextView P3Chat;
    private TextView P4Chat;
    private String currentP2Chat = "";
    private String currentP3Chat = "";
    private String currentP4Chat = "";
    private TextView GameInfo;
    private Button playButton;
    private Card[] cardsToPass;
    private final String TAG = "PlayerHuman";

    // the android activity that we are running
    private GameMainActivity myActivity;

    //the control variable to enable acting when the human player
    //receives 
    private static boolean enableSpeech = false;

    public int[] getCardImages() {
        return cardImages;
    }

    // (speechType, personalityType, option)
    public int[][][] sayings = {
            //Greeting = 0
            {{R.string.helloText, R.string.heyText},
                    {R.string.goodLuckText, R.string.haveFunText},
                    {R.string.braceText, R.string.braceText},
                    {R.string.howdyPText, R.string.howdyText}},
            //HAPPY = 1
            {{R.string.closeCallText, R.string.safeText},
                    {R.string.eatThisText, R.string.closeOneText},
                    {R.string.outsmartText, R.string.notCloseText},
                    {R.string.chewText, R.string.citySlickerText}},
            //SAD = 2
            {{R.string.awText, R.string.darnText},
                    {R.string.ahText, R.string.ohNoText},
                    {R.string.getYouText, R.string.setBackText},
                    {R.string.partnerText, R.string.whoaNellyText}},
            //ANGRY = 3
            {{R.string.ohNoText, R.string.awDarnText},
                    {R.string.hurtsText, R.string.ouchText},
                    {R.string.growlText, R.string.ughText},
                    {R.string.nabbitText, R.string.whoaThereText}},
            //SURPRISE = 4
            {{R.string.huhText, R.string.happenedText},
                    {R.string.uhOhText, R.string.whatText},
                    {R.string.whatText,R.string.howText},
                    {R.string.tarnationText,R.string.tarnationText}},
            //MYSTERIOUS = 5
            {{-1, -1}, {R.string.hopeWorksText, R.string.hopeWorksText},
                    {R.string.desperateText, R.string.desperateText},
                    {R.string.holdHatText, R.string.yeehawText}}
    };


    /**
     * External Citation
     *   Date:     11 November 2020
     *   Problem:  Could not reference images of cards.
     *   Resource:
     *      https://developer.android.com/reference/android/R.drawable
     *   Solution: Used R.drawable.image .
     */
    private int[] cardImages = {
        R.drawable.cupsa, R.drawable.cups2, R.drawable.cups3, R.drawable.cups4,
            R.drawable.cups5, R.drawable.cups6, R.drawable.cups7,
            R.drawable.cups8, R.drawable.cups9, R.drawable.cups10,
            R.drawable.cupsj, R.drawable.cupsq, R.drawable.cupsk,

        R.drawable.swordsa, R.drawable.swords2, R.drawable.swords3,
            R.drawable.swords4, R.drawable.swords5, R.drawable.swords6,
            R.drawable.swords7, R.drawable.swords8, R.drawable.swords9,
            R.drawable.swords10, R.drawable.swordsj, R.drawable.swordsq,
            R.drawable.swordsk,

        R.drawable.coinsa, R.drawable.coins2, R.drawable.coins3,
            R.drawable.coins4, R.drawable.coins5, R.drawable.coins6,
            R.drawable.coins7, R.drawable.coins8, R.drawable.coins9,
            R.drawable.coins10, R.drawable.coinsj, R.drawable.coinsq,
            R.drawable.coinsk,

        R.drawable.wandsa, R.drawable.wands2, R.drawable.wands3,
            R.drawable.wands4, R.drawable.wands5, R.drawable.wands6,
            R.drawable.wands7, R.drawable.wands8, R.drawable.wands9,
            R.drawable.wands10, R.drawable.wandsj, R.drawable.wandsq,
            R.drawable.wandsk};

    ArrayList <ImageButton> cardButtonList = new ArrayList<>(13);
    ArrayList <ImageView> playedCardImageList = new ArrayList<>(4);

    // the most recent game state, as given to us by the chaliceLocalGame
    private ChaliceGameState state;


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
     * Does not return positive if
     * the button does not correspond with a card in  the player's hand
     *
     * @param button    button to be checked
     * @return  id if the button is one of the GUI's occupied cardButtons,
     * -1 otherwise
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
     * Method to respond to the player pressing buttons
     * @param button
     * 		the button that was clicked
     */
    public void onClick(View button) {
        // if we are not yet connected to a game, ignore
        if (game == null) return;
        GameAction action = null;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, 255);
        int index = isCardButton(button);
        if (index >= 0){
            onClickSelectingCard(index, params);
        }
        else if (button.getId() == R.id.playButton) {
            if (!state.getPassingCards()){
                action = playerPlayCard();
            } else {
                action = playerPassCard();
            }
        }
        else if (button.getId() == R.id.menuButton) {
            onClickMenu();
        } else {
            // something else was pressed: ignore
            return;
        }
        if (action != null){
            Log.i("action: ", "was sent");
            game.sendAction(action); // send action to the game
        }
    }// onClick

    /**
     * A helper method to handle GUI response when selecting cards
     * @param index     the index of the selected card button
     * @param params    mystery variable - sp000ky!
     */
    private void onClickSelectingCard(int index, LinearLayout.LayoutParams params){
        //check that we don't have 3 cards selected to pass
        if (cardsToPass[2] == null){
            //set as the selected card
            ArrayList<Card> myHand =
                    PlayerComputerSimple.getMyHand(state, playerNum);
            state.setSelectedCard(myHand.get(index));
            //make gui element a little larger, set others to normal scale.
            for (ImageButton cardButton : cardButtonList){
                if (cardButton.getId() ==
                        cardButtonList.get(index).getId()) {
                    cardButton.setScaleX(1.2f);
                    cardButton.setScaleY(1.2f);
                    if (cardButtonList.indexOf(cardButton) != 12) {
                        params.setMargins(0, 0, 0, 0);
                    }
                } else {
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

    /**
     * A helper method to handle GUI response when pressing the menu button
     */
    private void onClickMenu(){
        /**
         * External Citation
         *   Date:     24 November 2020
         *   Problem:  Could not figure out how to set up a popup menu.
         *   Resource:
         *      https://www.tutlane.com/tutorial/
         *      android/android-popup-menu-with-examples
         *   Solution: Used the code as an example to help
         *   figure out how to create a popup menu. Proved to be not very
         *   helpful but could not find much good info on this.
         */
        final GameAction quitAction = new ActionQuit(this);
        // a popup menu shows on the screen when the menu button is pressed
        final PopupMenu popup = new PopupMenu(myActivity,
                myActivity.findViewById(R.id.menuButton));
        final MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.game_main, popup.getMenu());

        // an alert dialog that tells the user what the rules are
        final AlertDialog.Builder dialogBuilder =
                new AlertDialog.Builder(myActivity);
        // set the listener for the popup menu
        popup.setOnMenuItemClickListener(
                new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        // handles the behavior of the
                        // button items in the popup menu
                        switch (menuItem.getItemId()) {
                            case R.id.rules:
                                dialogBuilder.setTitle("Rules");
                                dialogBuilder.setView(R.layout.rules_layout);
                                dialogBuilder.setCancelable(true);
                                dialogBuilder.show();
                                return true;
                            case R.id.quitButton:
                                game.sendAction(quitAction);
                                return true;
                            case R.id.new_game:
                                myActivity.recreate();
                            default:
                                return false;
                        }
                    }
                });
        popup.show();
    }


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
                    //add our selected card to the first null space in
                    // cardsToPass, remove it from selectedCard and hand
                    ArrayList<Card> myHand = PlayerComputerSimple.getMyHand(
                            state, playerNum);
                    myHand.remove(state.getSelectedCard());
                    addCardToPassArray(state.getSelectedCard());
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
                action = new ActionPassCards(
                        this, this.playerNum, actionArray);

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
        Log.e("assignment error",
                "addCardToPassArray: array was full.");
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
                action = new ActionPlayCard(this,
                        this.playerNum, state.getSelectedCard());
                //temporary holder for p1Hand
                ArrayList<Card> tempHand = state.getP1Hand();

                /**
                 * External Citation
                 *   Date:     11 November 2020
                 *   Problem:  Could not remember how to loop through arrayList
                 *   Resource:
                 *      https://stackoverflow.com/questions/
                 *      25538511/iterate-through-arraylistt-java
                 *   Solution: I used the first suggestion on this post.
                 */
                //remove the selected card from the hand
                for (Card currentCard : tempHand) {
                    if (currentCard.equals(state.getSelectedCard())) {
                        tempHand.remove(currentCard);
                        //set temporary hand as the P1Hand
                        state.setP1Hand(tempHand);
                        state.setP1CardPlayed(currentCard);
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
        if (!(info instanceof ChaliceGameState)) {
            if (info instanceof InfoDisplaySpeech){
                DisplaySpeech((InfoDisplaySpeech)info);
            }
            if (info instanceof IllegalMoveInfo) {
                GameInfo.setText(R.string.illegalMoveText);
                updateDisplay();
                flash(Color.RED, 10);
            }
            return;
        }
        if (((ChaliceGameState) info).getWhoTurn() == playerNum){
            Log.i("Turn update", "receiveInfo: " +
                    "It is the human player's turn");
            String illegalText = ""+ R.string.illegalMoveText;
            if (GameInfo.getText() != illegalText){
                GameInfo.setText(R.string.yourTurnText);
            }

            if (((ChaliceGameState) info).getPassingCards()) {
                GameInfo.setText(R.string.pick3Text);
            }
        }
        else {
            if (((ChaliceGameState) info).getPassingCards()) {
                GameInfo.setText(R.string.otherPassText);
            } else {
                GameInfo.setText(R.string.notYouText);
            }
        }
        if(((ChaliceGameState) info).getTricksPlayed() == 0 &&
                !((ChaliceGameState) info).getPassingCards()) {
            GameInfo.setText(R.string.newHandText);
        }
        if(((ChaliceGameState) info).getTrickCardsPlayed().size() == 4
                && state.getWhoTurn() == this.playerNum) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // update our state; then update the display
        this.state = (ChaliceGameState) info;
        updateDisplay();
    }

    public void DisplaySpeech(InfoDisplaySpeech info){
        if (!enableSpeech) { return; }
        //remember to account for different player positions
        //both null are code to set the display to empty
        if(info.getPlayer() instanceof PlayerHuman) {
            return;
        }
        PlayerComputerSimple pRef = (PlayerComputerSimple)info.getPlayer();
        int playerNum = pRef.getPlayerNum();
        if (info.getPersonality() == null && info.getSpeech() == null){
            setAISpeechText(playerNum, "");
            updateDisplay();
            return;
        } else if (info.getSpeech() == null) {
            Log.e(TAG, "DisplaySpeech: encountered a null speech type");
        }
        //decoding the 3D array of text options
        //(speechType, personalityType, option)
        int speechCoord = -1;
        switch(info.getSpeech()){
            case GREETING:
                speechCoord = 0;
                break;
            case HAPPY:
                speechCoord = 1;
                break;
            case SAD:
                speechCoord = 2;
                break;
            case ANGRY:
                speechCoord = 3;
                break;
            case SURPRISE:
                speechCoord = 4;
                break;
            case MYSTERIOUS:
                speechCoord = 5;

        }
        int personalityCoord = -1;
        if(info.getPersonality() == null) {
            personalityCoord = 0;
        }
        else {
            switch (info.getPersonality()) {
                case DFLT:
                    personalityCoord = 1;
                    break;
                case VOIDER:
                    personalityCoord = 2;
                    break;
                case SHERIFF:
                    personalityCoord = 3;
                    break;
                case LOAF:
                    personalityCoord = 4;
                    break;
                default:
                    personalityCoord = 0;
            }
        }
        Random r = new Random();
        int optionCoord = r.nextInt(sayings[0][0].length);
        //get the string and update the chat bubble
        String text = "";
        if (personalityCoord == 4){
            text = "...";
        } else {
            int stringID = sayings[speechCoord][personalityCoord][optionCoord];
            text =  myActivity.getString(stringID);
        }
        setAISpeechText(playerNum, text);
        updateDisplay();
    }

    /**
     * A method to display set the string for an AI's speech,
     * accounting for variable player pNum
     * @param aiPlayerNumber    the ai's playerNum
     * @param text              the text to be set
     */
    private void setAISpeechText(int aiPlayerNumber, String text){
        synchronized (this){
            switch (playerNum){
                case 0:
                    switch (aiPlayerNumber){
                        case 1:
                            currentP2Chat = text;
                            break;
                        case 2:
                            currentP3Chat = text;
                            break;
                        case 3:
                            currentP4Chat = text;
                            break;
                    }
                    break;
                case 1:
                    switch (aiPlayerNumber){
                        case 0:
                            currentP2Chat = text;
                            break;
                        case 2:
                            currentP3Chat = text;
                            break;
                        case 3:
                            currentP4Chat = text;
                            break;
                    }
                    break;
                case 2:
                    switch (aiPlayerNumber){
                        case 1:
                            currentP2Chat = text;
                            break;
                        case 0:
                            currentP3Chat = text;
                            break;
                        case 3:
                            currentP4Chat = text;
                            break;
                    }
                    break;
                case 3:
                    switch (aiPlayerNumber){
                        case 1:
                            currentP2Chat = text;
                            break;
                        case 2:
                            currentP3Chat = text;
                            break;
                        case 0:
                            currentP4Chat = text;
                            break;
                    }
                    break;
            }
        }
    }

    /**
         External Citation
         Date: 11 November 2020
         Problem: Could not figure out how to programmatically hide/draw buttons
         Resource:
         https://stackoverflow.com/questions/
         11169360/android-remove-button-dynamically
         Solution: button.setVisibility(View.GONE) && ...Visibility(View.VISIBLE)
         View.GONE makes it so the button doesn't take up space anymore
     */
    /**
     * sets the counter value in the text view
     */
    protected void updateDisplay() {
        //score updates:
        //(chaliceLocalGame)game.GetPlayers();
        updateScores();
        updateChats();
        //change play button if we are passing cards
        if(state.getPassingCards()){
            if (cardsToPass[2] != null){
                playButton.setText(R.string.passText);
            } else {
                playButton.setText(R.string.pickText);
            }
        } else {
            playButton.setText(R.string.playText);
        }
        //card image updates
        //cards in hand
        int i;
        ArrayList<Card> myHand =
                PlayerComputerSimple.getMyHand(state, playerNum);
        assert myHand != null;
        for (i = 0; i < myHand.size(); i++){
            cardButtonList.get(i).setVisibility(View.VISIBLE);
            cardButtonList.get(i).setImageResource(
                    cardImages[imageForCard(myHand.get(i))]);
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

        Log.i("updateDisplay: ", "finished updating display");
    }


    /**
     * A method to update the scores
     * human player can be any playerNum 0-3
     */
    private void updateScores(){ 
        String scoreText = myActivity.getString(R.string.possessiveScoreText);
        String str0 = allPlayerNames[0] + scoreText +
                state.getP1CurrentPoints();
        String str1 = allPlayerNames[1] + scoreText +
                state.getP2CurrentPoints();
        String str2 = allPlayerNames[2] + scoreText+
                state.getP3CurrentPoints();
        String str3 = allPlayerNames[3] + scoreText +
                state.getP4CurrentPoints();
        String strPlayer = allPlayerNames[playerNum] + scoreText +
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
                break;
        }
    }

    /**
     * method to update the chats of computer players
     */
    public void updateChats(){
        P2Chat.setText(currentP2Chat);
        P3Chat.setText(currentP3Chat);
        P4Chat.setText(currentP4Chat);
    }

    /**
     * A method to show the middle cards while
     * the player is picking cards to pass
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
                break;
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

            // Checks to see if the card is the highest card in the trick
            int highVal = 0;
            Card highCard = new Card(0, state.getSuitLed());
            for (Card cardTemp : state.getTrickCardsPlayed()) {
                if (cardTemp.getCardSuit() == state.getSuitLed()) {
                    if (cardTemp.getCardVal() == 1){
                        highVal = 13;
                        highCard = cardTemp;
                    } else if (highVal < cardTemp.getCardVal()) {
                        highVal = cardTemp.getCardVal();
                        highCard = cardTemp;
                    }
                }
            }

            /**
                External Citation
                Date: 2 December 2020
                Problem: Wanted to darken an ImageView programmatically.
                Resource:
                    https://stackoverflow.com/questions/6581808/
                    programmatically-darken-a-view-android
                Solution: setColorFilter(color, PorterDuff.Mode.MULTIPLY
             */
            if(card == highCard) {
                // if the card is the highest, darken the image
                playedCardImageList.get(showIndex).setColorFilter(
                    Color.rgb(200,200,200), PorterDuff.Mode.MULTIPLY);
            } else {
                // if not, reset the color filter of the image back to null
                playedCardImageList.get(showIndex).setColorFilter(null);
            }
        }
        else {
            playedCardImageList.get(showIndex).setVisibility(View.INVISIBLE);
        }
    }


    /**
     * A method that when given a card returns the corresponding index
     * for the image of the card
     * in the cardImages array. If the card is null, returns transparent
     *
     * @param card Card value of the corresponding image
     * @return id to use with the cardImages array
     */
    public int imageForCard(Card card) {
        return (13*(card.getCardSuit()-1)) + card.getCardVal() - 1;
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

        // Initialize the trick card images in an ArrayList
        playedCardImageList.add(
                (ImageView) activity.findViewById(R.id.trickBottom));
        playedCardImageList.add(
                (ImageView) activity.findViewById(R.id.trickLeft));
        playedCardImageList.add(
                (ImageView) activity.findViewById(R.id.trickTop));
        playedCardImageList.add(
                (ImageView) activity.findViewById(R.id.trickRight));
        playButton = (Button) activity.findViewById(R.id.playButton);
        Button menuButton = (Button) activity.findViewById(R.id.menuButton);

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
        this.P2Chat = (TextView) activity.findViewById(R.id.chatp2);
        this.P3Chat = (TextView) activity.findViewById(R.id.chatp3);
        this.P4Chat = (TextView) activity.findViewById(R.id.chatp4);
        this.GameInfo = (TextView) activity.findViewById(R.id.gameInfo);
        if (state != null) { receiveInfo(state); }
    } // setAsGui

    public static int getPlayerNumCurrentPoints(
            ChaliceGameState state, int playerNum){
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