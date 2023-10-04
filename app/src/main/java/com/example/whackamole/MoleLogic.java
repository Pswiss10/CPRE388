package com.example.whackamole;

import android.os.CountDownTimer;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
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

    private GameOverListener gameOverListener;
    private CountDownTimer countDownTimer;
    private boolean isRunning = false;
    public MutableLiveData<Long> totalTime = new MutableLiveData<>();
    public long maxTime;


    public MoleLogic() {
        previousHole = -1;
        score = 0;
        maxTime = 10000L;
        lives = 3;
        currentHole = -1;

    }

//    private long calculateElapsedTime() {
//        return System.currentTimeMillis() - nextStart;
//    }

    public void start() {
        nextStart = System.currentTimeMillis();
        startCountdownTimer();
    }

    private void startCountdownTimer() {
        if (!isRunning) {
            countDownTimer = new CountDownTimer(maxTime, 1000) {
                public void onTick(long millisUntilFinished) {
                    totalTime.postValue(millisUntilFinished);
                }

                public void onFinish() {
                    loseLife();
                    if (!checkGameOver()){
                        nextRandomMole();
                        startCountdownTimer();
                    }
                }
            }.start();
            isRunning = true;
        }
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
            score += 1;
            previousHole = currentHole;
            maxTime = (long) ((0.9999 * maxTime) + 2);
            resetCountdownTimer();
            return true;
        }
        return false;
    }

    private void resetCountdownTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            isRunning = false;
        }
        startCountdownTimer();
    }

    public int setCurrentMole()
    {
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

    public void loseLife() {
        lives--;
        gameOverListener.onLifeLoss();
        nextStart = System.currentTimeMillis();
    }

    public boolean checkGameOver() {
        if (lives <= 0) {
            updateHighScore();
            if (gameOverListener != null) {
                gameOverListener.onGameOver();
            }
            return true;
        }
        return false;
    }

    public MutableLiveData<Long> getCurrentDuration() {
        return totalTime;
    }


    public void setGameOverListener(GameOverListener listener) {
        this.gameOverListener = listener;
    }
}
