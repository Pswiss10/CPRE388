package com.example.whackamole;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

/**
 * This class represents the start menu of the game and the screen displayed when the user loses.
 */
public class Menu extends AppCompatActivity {
    private MoleLogic game = new MoleLogic();

    TextView score;
    TextView highScore;
    Button startButton;

    /**
     * The name for storing shared preferences.
     */
    public static final String MyPREFERENCES = "MyPrefs";

    /**
     * The SharedPreferences object for storing the high score.
     */
    SharedPreferences sharedpreferences;

    /**
     * Called when the activity is first created.
     * @param savedInstanceState The saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        score = findViewById(R.id.score);
        highScore = findViewById(R.id.highscore);
        startButton = findViewById(R.id.startButton);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        // Set the high score from shared preferences
        highScore.setText("High Score: " + sharedpreferences.getInt(MyPREFERENCES, 0));

        // Get the score passed from another activity and display it
        Intent intent = getIntent();
        score.setText("Score: " + intent.getIntExtra("score", 0));

        // Set an onClickListener for the startButton
        startButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when the startButton is clicked. Changes the view to the actual game.
             *
             * @param view The View object that was clicked.
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
