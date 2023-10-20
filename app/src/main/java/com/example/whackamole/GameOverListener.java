package com.example.whackamole;

/**
 * An interface for listener objects used in the MoleLogic class to handle game events.
 */
public interface GameOverListener {
    /**
     * Called when the game is over.
     */
    void onGameOver();

    /**
     * Called when a life is lost by the user during the game.
     */
    void onLifeLoss();
}
