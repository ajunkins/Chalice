package edu.up.cs301.chalice;


import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.Normalizer;

import edu.up.cs301.game.GameFramework.GameMainActivity;
import edu.up.cs301.game.GameFramework.LocalGame;
import edu.up.cs301.game.GameFramework.gameConfiguration.GameConfig;


public class MainActivity extends GameMainActivity implements View.OnClickListener {

    //declare variables


    @Override
    public void onClick (View v){

    }

    @Override
    public GameConfig createDefaultConfig() {
        return null;
    }

    @Override
    public LocalGame createLocalGame() {
        return null;
    }

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

}