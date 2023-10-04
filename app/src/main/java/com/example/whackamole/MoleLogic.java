package com.example.whackamole;

import android.widget.ImageView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Random;
import android.os.Handler;
import java.util.logging.LogRecord;

public class MoleLogic extends ViewModel {

    public int previousHole;
    public int score;
    public static int highScore;
    public int lives;
    public long nextStart;

    public int currentHole;



    public MutableLiveData<Long> totalTime = new MutableLiveData<>();
    private Runnable timerRunnable;
    private Handler timerHandler = new Handler();
    public long maxTime;


    public MoleLogic() {
        previousHole = -1;
        score = 0;
        maxTime = 10000L;
        lives = 3;
        currentHole = -1;
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                totalTime.postValue(calculateElapsedTime());
                timerHandler.postDelayed(this, 10);
            }
        };
    }

    private long calculateElapsedTime() {
        return System.currentTimeMillis() - nextStart;
    }

    public void start() {
        nextStart = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 10);
    }

    public void nextRandomMole() {
        int nextHole;
        final int numHoles = 12;
        Random holePicker = new Random();
        nextHole = (holePicker.nextInt(12) % numHoles) + 1;

        while (nextHole == previousHole){
            nextHole = (holePicker.nextInt(12) % numHoles) + 1;
        }

        nextStart = System.currentTimeMillis();
        currentHole = nextHole;
    }

    public void checkClick(int holeNum){
        if (holeNum == currentHole) {
            score += 10;
            previousHole = currentHole;
        }

    }

    public int setCurrentMole()
    {
        final int numHoles = 12;
        Random holePicker = new Random();
        currentHole = (holePicker.nextInt(12) % numHoles) + 1;
        return currentHole;
    }


    public void updateHighScore(){
        if (score > highScore) {
            highScore = score;
        }
        score = 0;
    }

    public boolean updateTime() {
        totalTime.setValue(System.currentTimeMillis() - nextStart);
        if (totalTime.getValue() >= maxTime) {
            maxTime *= .95;
            return true;
        }
        return false;
    }

    public void loseLife() {
        lives--;
        nextStart = System.currentTimeMillis();
    }

    public boolean checkGameOver() {
        if (lives <= 0) {
            updateHighScore();
            timerHandler.removeCallbacks(timerRunnable);
            return true;
        }
        return false;
    }

    public MutableLiveData<Long> getCurrentDuration() {
        return totalTime;
    }

    public void stop() {
    }
}
