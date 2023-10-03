package com.example.whackamole;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private MoleLogic game = new MoleLogic();
    private Observer<Long> timeObserver;

    ImageView mole1 = findViewById(R.id.mole1);
    ImageView mole2 = findViewById(R.id.mole2);
    ImageView mole3 = findViewById(R.id.mole3);
    ImageView mole4 = findViewById(R.id.mole4);
    ImageView mole5 = findViewById(R.id.mole5);
    ImageView mole6 = findViewById(R.id.mole6);
    ImageView mole7 = findViewById(R.id.mole7);
    ImageView mole8 = findViewById(R.id.mole8);
    ImageView mole9 = findViewById(R.id.mole9);
    ImageView mole10 = findViewById(R.id.mole10);
    ImageView mole11 = findViewById(R.id.mole11);
    ImageView mole12 = findViewById(R.id.mole12);

    ImageView life1 = findViewById(R.id.mole1);
    ImageView life2 = findViewById(R.id.mole1);
    ImageView life3 = findViewById(R.id.mole1);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        game = new ViewModelProvider(this).get(MoleLogic.class);

        timeObserver = new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                boolean outOfTime = game.updateTime();
                if (outOfTime) {
                    game.loseLife();
                }
            }
        };

    }

    public void changeTransparency()
    {
        int hole = game.setCurrentMole();
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
        else  { mole12.setVisibility(View.VISIBLE); }
    }

}