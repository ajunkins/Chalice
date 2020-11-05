package edu.up.cs301.chalice;


import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.Normalizer;

//import static edu.up.cs301.chalice.R.id.runTestButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //declare variables
    private EditText editText;
    private Button testButton;
    private String secondInstanceToString;
    private String fourthInstanceToString;
    private Card testCard= new Card(10,1);

    @Override
    public void onClick (View v){
        editText.setText("", TextView.BufferType.NORMAL);
        heartsLocalGame firstInstance = new heartsLocalGame();
        //firstInstance.Randomize();
        editText.append("First Instance: \n" + firstInstance.toString());
        heartsLocalGame secondInstance = new heartsLocalGame(firstInstance);

        firstInstance.selectCard(testCard);
        editText.append("Player selected card.\n");

       /* firstInstance.collectTrick();
        editText.append("Player collected cards.\n");

        */

        firstInstance.passCard();
        editText.append("Player passed cards.\n");

        firstInstance.playCard();
        editText.append("Player played card.\n");

        firstInstance.quit();
        editText.append("Player quit game.\n");


        heartsLocalGame thirdInstance = new heartsLocalGame();
        heartsLocalGame fourthInstance = new heartsLocalGame(thirdInstance);

        editText.append("Called toString on second and fourth Instances.\n");

        secondInstanceToString = secondInstance.toString();
        fourthInstanceToString = fourthInstance.toString();
        if (secondInstanceToString.equals(fourthInstanceToString)) {
            editText.append("Instances are equal.\n \n");
        } else {
            editText.append("Instances are not equal.\n");
        }

        editText.append("Second Instance: \n" + secondInstanceToString);
        editText.append("Fourth Instance: \n" + fourthInstanceToString);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //testButton = (Button) findViewById(R.id.runTestButton);
        //testButton.setOnClickListener(this);
        //editText = findViewById(R.id.editTextMulti);
    }

}