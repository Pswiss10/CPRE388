package com.example.whackamole;

/**
 * interface for Listener objects used in MoleLogic class
 */
public interface GameOverListener {
    /**
     * Method for when the game is over
     */
    void onGameOver();

    /**
     * Method for when a live is lost by the user
     */
    void onLifeLoss();
}
