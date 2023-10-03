package com.example.whackamole;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Random;

public class MoleLogic extends ViewModel {

    private int previousHole;
    public int score;
    public static int highScore;
    public int lives;
    public long nextStart;


    public MutableLiveData<Long> totalTime = new MutableLiveData<>();
    public long maxTime;


    public MoleLogic() {
        previousHole = -1;
        score = 0;
        maxTime = 10L;
        lives = 3;
    }

    public int nextRandomMole() {
        int nextHole = -1;
        final int numHoles = 12;
        Random holePicker = new Random();
        nextHole = holePicker.nextInt() % numHoles;

        while (nextHole == previousHole){
            nextHole = holePicker.nextInt();
        }

        nextStart = System.currentTimeMillis();
        return nextHole;
    }

    public void updateHighScore(){
        if (score > highScore) {
            highScore = score;
        }
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

    public void checkGameOver() {
        if (lives <= 0) {
            //TODO
        }
    }
}
