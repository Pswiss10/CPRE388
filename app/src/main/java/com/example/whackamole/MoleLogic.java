package com.example.whackamole;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.widget.ImageView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Random;

public class MoleLogic extends ViewModel {

    private int previousHole;
    public int score;
    public static int highScore;
    public int lives;
    public long nextStart;
    public int currentHole;


    public MutableLiveData<Long> totalTime = new MutableLiveData<>();
    public long maxTime;

    public MoleLogic() {
        previousHole = -1;
        score = 0;
        maxTime = 10000L;
        lives = 3;
        currentHole = -1;

    }

    public void nextRandomMole() {
        int nextHole;
        final int numHoles = 12;
        Random holePicker = new Random();
        nextHole = (holePicker.nextInt(12) % numHoles) + 1;

        while (nextHole == previousHole) {
            nextHole = (holePicker.nextInt(12) % numHoles) + 1;
        }

        nextStart = System.currentTimeMillis();
        currentHole = nextHole;
    }

    public boolean checkClick(int holeNum) {
        if (holeNum == currentHole) {
            score += 10;
            previousHole = currentHole;
            return true;
        }
    return false;
    }

    public int setCurrentMole() {
        final int numHoles = 12;
        Random holePicker = new Random();
        currentHole = (holePicker.nextInt(12) % numHoles) + 1;
        return currentHole;
    }


    public int updateHighScore() {
        if (score > highScore) {
            highScore = score;
        }
        return highScore;
    }

    public boolean updateTime() {
        totalTime.setValue(System.currentTimeMillis() - nextStart);
        if (totalTime.getValue() >= maxTime) {
            return true;
        }
        return false;
    }

    public void loseLife() {
        lives--;
        checkGameOver();
    }

    public boolean checkGameOver() {
        if (lives <= 0) {
            updateHighScore();
            return true;
        }
        return false;
    }
}
