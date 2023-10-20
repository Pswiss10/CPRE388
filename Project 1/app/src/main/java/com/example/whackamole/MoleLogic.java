package com.example.whackamole;

import android.os.CountDownTimer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Random;

/**
 * Class that contains all the logic to play the wack a mole game
 */
public class MoleLogic extends ViewModel {

    public int previousHole;
    public int score;
    public int highScore;
    public int lives;
    public int currentHole;
    public long maxTime;
    private boolean isRunning = false;
    /**
     * Listener to talk to the activity controller when the player looses a life or the game ends
     */
    private GameOverListener gameOverListener;
    /**
     * CountDownTimer to keep track if the user doesn't click the mole in time
     */
    private CountDownTimer countDownTimer;
    /**
     * MutableLiveData that holds the time used for the CountDownTimer
     */
    public MutableLiveData<Long> totalTime = new MutableLiveData<>();


    /**
     * basic Contructor for the MoleLogic class to initilize variables
     */
    public MoleLogic() {
        previousHole = -1;
        score = 0;
        maxTime = 10000L;
        lives = 3;
        currentHole = -1;

    }

    /**
     * Method starts the CountDownTimer to check for users not clicking the mole
     */
    public void start() {
        startCountdownTimer();
    }

    /**
     * Method is the controller for the coundownTimer, contains onTick() and onFinish()
     */
    private void startCountdownTimer() {
        if (!isRunning) {
            countDownTimer = new CountDownTimer(maxTime, 1000) {
                /**
                 * Updates the timer and posts a new value to totalTime variable
                 * @param millisUntilFinished time until the countdown is finished
                 */
                public void onTick(long millisUntilFinished) {
                    totalTime.postValue(millisUntilFinished);
                }

                /**
                 * if the timer runs out, lose a life and check to see if the game is over.
                 * If not, reset the countdown timer
                 */
                public void onFinish() {
                    loseLife();
                    if (!checkGameOver()){
                        resetCountdownTimer();
                    }
                }
            }.start();
            isRunning = true;
        }
    }

    /**
     * This method generates the next hole that the mole will be at. This method makes sure the mole doesn't
     * Appear in the same hole
     */
    public void nextRandomMole() {
        int nextHole;
        final int numHoles = 12;
        Random holePicker = new Random();
        nextHole = (holePicker.nextInt(12) % numHoles) + 1;

        while (nextHole == previousHole) {
            nextHole = (holePicker.nextInt(12) % numHoles) + 1;
        }

        currentHole = nextHole;
    }

    /**
     * sets the current hole for a mole, only runs once onCreate
     * @return Current hole the mole is located, represented as an int
     */
    public int setCurrentMole()
    {
        final int numHoles = 12;
        Random holePicker = new Random();
        currentHole = (holePicker.nextInt(12) % numHoles) + 1;
        return currentHole;
    }

    /**
     * this method checks to see if the hole the user clicked on contains the mole as well as updating the timing
     * @param holeNum The hole that was clicked
     * @return true if the mole is in that hole, false if not
     */
    public boolean checkClick(int holeNum) {
        if (holeNum == currentHole) {
            score += 1;
            previousHole = currentHole;
            maxTime = (long) ((0.95 * maxTime) + 2);
            resetCountdownTimer();
            return true;
        }
        return false;
    }

    /**
     * resets the countdowntimer when the player loses a life and the game is not over
     */
    private void resetCountdownTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            isRunning = false;
        }
        startCountdownTimer();
    }

    /**
     * Updates the highscore of the user's current game, DOES NOT save to shared prefrences
     * @return the high score of the game, updated or the same value as before
     */
    public int updateHighScore() {
        if (score > highScore) {
            highScore = score;
        }
        return highScore;
    }

    /**
     * loses a life and calls the main Activities listener to change the UI
     */
    public void loseLife() {
        lives--;
        gameOverListener.onLifeLoss();
    }

    /**
     * Checks to see if the game is over, as in the user has 0 lives
     * @return true if the user has 0 lives, false if the user has more than 0 lives
     */
    public boolean checkGameOver() {
        if (lives <= 0) {
           // updateHighScore();
            if (gameOverListener != null) {
                gameOverListener.onGameOver();
            }
            return true;
        }
        return false;
    }

    /**
     * sets the listener to talk to the main activity file
     * @param listener The listerner to set
     */
    public void setGameOverListener(GameOverListener listener) {
        this.gameOverListener = listener;
    }
}
