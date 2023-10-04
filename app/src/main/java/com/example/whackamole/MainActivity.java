package com.example.whackamole;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
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

    TextView score;
    TextView highScore;
    TextView gameOver;
    Button startButton;

    private boolean newGame = false;

    private MoleLogic game = new MoleLogic();
    private Observer<Long> timeObserver;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

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

        timeObserver = new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                boolean outOfTime = game.updateTime();
                if (outOfTime) {
                    game.loseLife();
                    if (game.checkGameOver()) {
                        game.stop();
                        gameOver.setVisibility(View.VISIBLE);
                        startButton.setVisibility(View.VISIBLE);
                        newGame = true;
                    }

                    turnOffTransparency(game.currentHole);
                    game.previousHole = game.currentHole;

                    game.nextRandomMole();
                    changeTransparency(game.currentHole);

                }
            }
        };

        game.getCurrentDuration().observe(this, timeObserver);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.start();
                startButton.setVisibility(View.INVISIBLE);
                if (newGame) {
                    gameOver.setVisibility(View.INVISIBLE);
                }
            }
        });

        changeTransparency(game.setCurrentMole());
        mole1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                game.checkClick(1); mole1.setVisibility(View.INVISIBLE);
                score.setText("Score: " + game.score);

                game.checkClick(1);
                mole1.setVisibility(View.INVISIBLE);
                game.nextRandomMole();
                changeTransparency(game.currentHole);
            }
        });
        mole2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                game.checkClick(2); mole2.setVisibility(View.INVISIBLE);
                score.setText("Score: " + game.score);
                game.nextRandomMole();
                changeTransparency(game.currentHole);
            }
        });
        mole3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                game.checkClick(3); mole3.setVisibility(View.INVISIBLE);
                score.setText("Score: " + game.score);
                game.nextRandomMole();
                changeTransparency(game.currentHole);
            }
        });
        mole4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                game.checkClick(4); mole4.setVisibility(View.INVISIBLE);
                score.setText("Score: " + game.score);
                game.nextRandomMole();
                changeTransparency(game.currentHole);
            }
        });
        mole5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                game.checkClick(5); mole5.setVisibility(View.INVISIBLE);
                score.setText("Score: " + game.score);
                game.nextRandomMole();
                changeTransparency(game.currentHole);
            }
        });
        mole6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                game.checkClick(6); mole6.setVisibility(View.INVISIBLE);
                score.setText("Score: " + game.score);
                game.nextRandomMole();
                changeTransparency(game.currentHole);
            }
        });
        mole7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                game.checkClick(7); mole7.setVisibility(View.INVISIBLE);
                score.setText("Score: " + game.score);
                game.nextRandomMole();
                changeTransparency(game.currentHole);
            }
        });
        mole8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                game.checkClick(8); mole8.setVisibility(View.INVISIBLE);
                score.setText("Score: " + game.score);
                game.nextRandomMole();
                changeTransparency(game.currentHole);
            }
        });
        mole9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                game.checkClick(9); mole9.setVisibility(View.INVISIBLE);
                score.setText("Score: " + game.score);
                game.nextRandomMole();
                changeTransparency(game.currentHole);
            }
        });
        mole10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                game.checkClick(10); mole10.setVisibility(View.INVISIBLE);
                score.setText("Score: " + game.score);
                game.nextRandomMole();
                changeTransparency(game.currentHole);
            }
        });
        mole11.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                game.checkClick(11); mole11.setVisibility(View.INVISIBLE);
                score.setText("Score: " + game.score);
                game.nextRandomMole();
                changeTransparency(game.currentHole);
            }
        });
        mole12.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                game.checkClick(12); mole12.setVisibility(View.INVISIBLE);
                score.setText("Score: " + game.score);
                game.nextRandomMole();
                changeTransparency(game.currentHole);

            }
        });
    }

    public void changeTransparency(int hole)
    {
        if (hole == 1) { mole1.setVisibility(View.VISIBLE); }
        else if (hole == 2) { mole2.setVisibility(View.VISIBLE); }
        else if (hole == 3) { mole3.setVisibility(View.VISIBLE); }
        else if (hole == 4) { mole4.setVisibility(View.VISIBLE); }
        else if (hole == 5) { mole5.setVisibility(View.VISIBLE); }
        else if (hole == 6) { mole6.setVisibility(View.VISIBLE); }
        else if (hole == 7) { mole7.setVisibility(View.VISIBLE); }
        else if (hole == 8) { mole8.setVisibility(View.VISIBLE); }
        else if (hole == 9) { mole9.setVisibility(View.VISIBLE); }
        else if (hole == 10) { mole10.setVisibility(View.VISIBLE); }
        else if (hole == 11) { mole11.setVisibility(View.VISIBLE); }
        else if (hole == 12) { mole12.setVisibility(View.VISIBLE); }
    }

    public void turnOffTransparency(int hole)
    {
        if (hole == 1) { mole1.setVisibility(View.INVISIBLE); }
        else if (hole == 2) { mole2.setVisibility(View.INVISIBLE); }
        else if (hole == 3) { mole3.setVisibility(View.INVISIBLE); }
        else if (hole == 4) { mole4.setVisibility(View.INVISIBLE); }
        else if (hole == 5) { mole5.setVisibility(View.INVISIBLE); }
        else if (hole == 6) { mole6.setVisibility(View.INVISIBLE); }
        else if (hole == 7) { mole7.setVisibility(View.INVISIBLE); }
        else if (hole == 8) { mole8.setVisibility(View.INVISIBLE); }
        else if (hole == 9) { mole9.setVisibility(View.INVISIBLE); }
        else if (hole == 10) { mole10.setVisibility(View.INVISIBLE); }
        else if (hole == 11) { mole11.setVisibility(View.INVISIBLE); }
        else if (hole == 12) { mole12.setVisibility(View.INVISIBLE); }
    }
}