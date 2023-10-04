package com.example.whackamole;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Main activity for the app.
 */
public class MainActivity extends AppCompatActivity implements GameOverListener{
    ImageView mole1;
    ImageView mole2;
    ImageView mole3;
    ImageView mole4;
    ImageView mole5;
    ImageView mole6;
    ImageView mole7;
    ImageView mole8;
    ImageView mole9;
    ImageView mole10;
    ImageView mole11;
    ImageView mole12;
    ImageView life1;
    ImageView life2;
    ImageView life3;

    MediaPlayer jump;
    MediaPlayer bonk;
    TextView score;
    TextView highScore;
    TextView gameOver;
    Button startButton;

    private boolean newGame = false;
    /**
     * Instantiating a MoleLogic object
     */
    private MoleLogic game;

    /**
     * creating a string for storing the shared preference
     */
    public static final String MyPREFERENCES = "MyPrefs" ;


    /**
     * instantiating sharedpreferences object for storing high score
     */
    SharedPreferences sharedpreferences;

    /**
     * Creates the layout that corresponds with the activity
     * @param savedInstanceState An instance of the UI to be reloaded
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /**
         * MediaPLayer objects for sound effects
         */
         jump = MediaPlayer.create(MainActivity.this, R.raw.jump);
         bonk = MediaPlayer.create(MainActivity.this, R.raw.clang);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mole1 = findViewById(R.id.mole1);
        mole2 = findViewById(R.id.mole2);
        mole3 = findViewById(R.id.mole3);
        mole4 = findViewById(R.id.mole4);
        mole5 = findViewById(R.id.mole5);
        mole6 = findViewById(R.id.mole6);
        mole7 = findViewById(R.id.mole7);
        mole8 = findViewById(R.id.mole8);
        mole9 = findViewById(R.id.mole9);
        mole10 = findViewById(R.id.mole10);
        mole11 = findViewById(R.id.mole11);
        mole12 = findViewById(R.id.mole12);

        life1 = findViewById(R.id.life1);
        life2 = findViewById(R.id.life2);
        life3 = findViewById(R.id.life3);

        score = findViewById(R.id.score);
        highScore = findViewById(R.id.highscore);

        gameOver = findViewById(R.id.gameOverTxt);
        startButton = findViewById(R.id.startButton);

        game = new ViewModelProvider(this).get(MoleLogic.class);
        game.setGameOverListener(this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        highScore.setText("High Score: " + sharedpreferences.getInt(MyPREFERENCES, 0));
        game.highScore = sharedpreferences.getInt(MyPREFERENCES, 0);

        startButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Method that detects if the start button is clicked
             * @param view The view object
             */
            @Override
            public void onClick(View view) {
                game.start();
                changeTransparency(game.setCurrentMole());
                startButton.setVisibility(View.INVISIBLE);
                if (newGame) {
                    gameOver.setVisibility(View.INVISIBLE);
                }
            }
        });
        mole1.setOnClickListener(new View.OnClickListener() {
            /**
             * Method that detects if the first mole is clicked
             * @param v The view object
             */
            public void onClick(View v) {

                game.checkClick(1);
                if(game.checkClick(1) == true)
                {
                    bonk.start();
                }
                score.setText("Score: " + game.score);
                mole1.setVisibility(View.INVISIBLE);
                game.nextRandomMole();
                changeTransparency(game.currentHole);
            }
        });

        mole2.setOnClickListener(new View.OnClickListener() {
            /**
             * Method that detects if the second mole is clicked
             * @param v The view object
             */
            public void onClick(View v) {

                if(game.checkClick(2) == true)
                {
                    bonk.start();
                }
                mole2.setVisibility(View.INVISIBLE);
                score.setText("Score: " + game.score);
                game.nextRandomMole();
                changeTransparency(game.currentHole);
            }
        });
        mole3.setOnClickListener(new View.OnClickListener() {
            /**
             * Method that detects if the third mole is clicked
             * @param v The view object
             */
            public void onClick(View v) {

                if(game.checkClick(3) == true)
                {
                    bonk.start();
                }
                mole3.setVisibility(View.INVISIBLE);
                score.setText("Score: " + game.score);
                game.nextRandomMole();
                changeTransparency(game.currentHole);
            }
        });
        mole4.setOnClickListener(new View.OnClickListener() {
            /**
             * Method that detects if the fourth mole is clicked
             * @param v The view object
             */
            public void onClick(View v) {

                if(game.checkClick(4) == true)
                {
                    bonk.start();
                }
                mole4.setVisibility(View.INVISIBLE);
                score.setText("Score: " + game.score);
                game.nextRandomMole();
                changeTransparency(game.currentHole);
            }
        });

        mole5.setOnClickListener(new View.OnClickListener() {
            /**
             * Method that detects if the fifth mole is clicked
             * @param v The view object
             */
            public void onClick(View v) {

                if(game.checkClick(5) == true)
                {
                    bonk.start();
                }
                mole5.setVisibility(View.INVISIBLE);
                score.setText("Score: " + game.score);
                game.nextRandomMole();
                changeTransparency(game.currentHole);
            }
        });
        mole6.setOnClickListener(new View.OnClickListener() {
            /**
             * Method that detects if the sixth mole is clicked
             * @param v The view object
             */
            public void onClick(View v) {

                if(game.checkClick(6) == true)
                {
                    bonk.start();
                }
                mole6.setVisibility(View.INVISIBLE);
                score.setText("Score: " + game.score);
                game.nextRandomMole();
                changeTransparency(game.currentHole);
            }
        });
        mole7.setOnClickListener(new View.OnClickListener() {
            /**
             * Method that detects if the seventh mole is clicked
             * @param v The view object
             */
            public void onClick(View v) {

                if(game.checkClick(7) == true)
                {
                    bonk.start();
                }
                mole7.setVisibility(View.INVISIBLE);
                score.setText("Score: " + game.score);
                game.nextRandomMole();
                changeTransparency(game.currentHole);
            }
        });
        mole8.setOnClickListener(new View.OnClickListener() {
            /**
             * Method that detects if the eighth mole is clicked
             * @param v The view object
             */
            public void onClick(View v) {

                if(game.checkClick(8) == true)
                {
                    bonk.start();
                }
                mole8.setVisibility(View.INVISIBLE);
                score.setText("Score: " + game.score);
                game.nextRandomMole();
                changeTransparency(game.currentHole);
            }
        });
        mole9.setOnClickListener(new View.OnClickListener() {
            /**
             * Method that detects if the ninth mole is clicked
             * @param v The view object
             */
            public void onClick(View v) {

                if(game.checkClick(9) == true)
                {
                    bonk.start();
                }
                mole9.setVisibility(View.INVISIBLE);
                score.setText("Score: " + game.score);
                game.nextRandomMole();
                changeTransparency(game.currentHole);
            }
        });
        mole10.setOnClickListener(new View.OnClickListener() {
            /**
             * Method that detects if the tenth mole is clicked
             * @param v The view object
             */
            public void onClick(View v) {
                if(game.checkClick(10) == true)
                {
                    bonk.start();
                }
                mole10.setVisibility(View.INVISIBLE);
                score.setText("Score: " + game.score);
                game.nextRandomMole();
                changeTransparency(game.currentHole);
            }
        });
        mole11.setOnClickListener(new View.OnClickListener() {
            /**
             * Method that detects if the eleventh mole is clicked
             * @param v The view object
             */
            public void onClick(View v) {
                if(game.checkClick(11) == true)
                {
                    bonk.start();
                }
                mole11.setVisibility(View.INVISIBLE);
                score.setText("Score: " + game.score);
                game.nextRandomMole();
                changeTransparency(game.currentHole);
            }
        });
        mole12.setOnClickListener(new View.OnClickListener() {
            /**
             * Method that detects if the twelfth mole is clicked
             * @param v The view object
             */
            public void onClick(View v) {
                if(game.checkClick(12) == true)
                {
                    bonk.start();
                }
                mole12.setVisibility(View.INVISIBLE);
                score.setText("Score: " + game.score);
                game.nextRandomMole();
                changeTransparency(game.currentHole);

            }
        });

    }

    /**
     * Turns on the visibility of whatever hole is passed to it and plays a sound
     * @param mole what mole to make visible
     */
    public void changeTransparency(int mole)
    {
        if (mole == 1) { mole1.setVisibility(View.VISIBLE); jump.start(); }
        else if (mole == 2) { mole2.setVisibility(View.VISIBLE); jump.start(); }
        else if (mole == 3) { mole3.setVisibility(View.VISIBLE); jump.start(); }
        else if (mole == 4) { mole4.setVisibility(View.VISIBLE); jump.start();}
        else if (mole == 5) { mole5.setVisibility(View.VISIBLE); jump.start();}
        else if (mole == 6) { mole6.setVisibility(View.VISIBLE); jump.start();}
        else if (mole == 7) { mole7.setVisibility(View.VISIBLE); jump.start();}
        else if (mole == 8) { mole8.setVisibility(View.VISIBLE); jump.start();}
        else if (mole == 9) { mole9.setVisibility(View.VISIBLE); jump.start();}
        else if (mole == 10) { mole10.setVisibility(View.VISIBLE); jump.start();}
        else if (mole == 11) { mole11.setVisibility(View.VISIBLE); jump.start();}
        else if (mole == 12) { mole12.setVisibility(View.VISIBLE); jump.start();}
    }

    /**
     * Turns off the visibility of whatever hole is passed to it and plays a sound
     * @param mole what mole to make invisible
     */
    public void turnOffTransparency(int mole)
    {
        if (mole == 1) { mole1.setVisibility(View.INVISIBLE); }
        else if (mole == 2) { mole2.setVisibility(View.INVISIBLE); }
        else if (mole == 3) { mole3.setVisibility(View.INVISIBLE); }
        else if (mole == 4) { mole4.setVisibility(View.INVISIBLE); }
        else if (mole == 5) { mole5.setVisibility(View.INVISIBLE); }
        else if (mole == 6) { mole6.setVisibility(View.INVISIBLE); }
        else if (mole == 7) { mole7.setVisibility(View.INVISIBLE); }
        else if (mole == 8) { mole8.setVisibility(View.INVISIBLE); }
        else if (mole == 9) { mole9.setVisibility(View.INVISIBLE); }
        else if (mole == 10) { mole10.setVisibility(View.INVISIBLE); }
        else if (mole == 11) { mole11.setVisibility(View.INVISIBLE); }
        else if (mole == 12) { mole12.setVisibility(View.INVISIBLE); }
    }

    /**
     * Methods created by the interface GameOverListener
     * onGameOver is called when moleLogic's GameOverListener detects the games is over
     */
    @Override
    public void onGameOver() {
        runOnUiThread(new Runnable() {
            /**
             * runs when the game is over
             */
            @Override
            public void run() {
                if (game.score > game.highScore) {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putInt(MyPREFERENCES, game.updateHighScore());
                    editor.commit();
                }
                newGame = true;
                Intent intent=new Intent(MainActivity.this,Menu.class);
                intent.putExtra("score", game.score);
                startActivity(intent);
            }


        });
    }

    /**
     * runs when the gameOverListener in moleLogic detects the user lost a life
     */
    @Override
    public void onLifeLoss() {
        if (game.lives == 3) {
            life1.setVisibility(View.VISIBLE);
            life2.setVisibility(View.VISIBLE);
            life3.setVisibility(View.VISIBLE);
        } else if (game.lives == 2) {
            life1.setVisibility(View.INVISIBLE);
            life2.setVisibility(View.VISIBLE);
            life3.setVisibility(View.VISIBLE);
        } else if (game.lives == 1) {
            life1.setVisibility(View.INVISIBLE);
            life2.setVisibility(View.INVISIBLE);
            life3.setVisibility(View.VISIBLE);
        }
        turnOffTransparency(game.currentHole);
        game.nextRandomMole();
        changeTransparency(game.currentHole);
    }
}