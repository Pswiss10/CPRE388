package com.example.whackamole;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private MoleLogic game = new MoleLogic();
    private Observer<Long> timeObserver;
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
}