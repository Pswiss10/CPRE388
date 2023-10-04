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
 * Class for the start of the gamee as well as when the user looses
 */
public class Menu extends AppCompatActivity {
    private MoleLogic game = new MoleLogic();

    TextView score;

    TextView highScore;
    Button startButton;

    /**
     * creating a string for storing the shared preference
     */
    public static final String MyPREFERENCES = "MyPrefs" ;

    /**
     * instantiating sharedpreferences object for storing high score
     */
    SharedPreferences sharedpreferences;


    /**
     * generates the view of the start menu for the user to see
     * @param savedInstanceState Bundle passed by the system
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        score = findViewById(R.id.score);
        highScore = findViewById(R.id.highscore);
        startButton = findViewById(R.id.startButton);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        highScore.setText("High Score: " + sharedpreferences.getInt(MyPREFERENCES, 0));
        Intent intent = getIntent();
        score.setText("Score: " + intent.getIntExtra("score", 0));
        startButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Changes the view to the actual game
             * @param view This Instances View object
             */
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Menu.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
