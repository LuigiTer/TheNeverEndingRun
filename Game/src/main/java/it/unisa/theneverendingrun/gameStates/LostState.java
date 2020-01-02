package it.unisa.theneverendingrun.gameStates;

import com.badlogic.gdx.graphics.Texture;
import it.unisa.theneverendingrun.GameEngine;


public class LostState extends EndedState {

    public LostState(GameEngine game, int finalScore) {
        super(game, finalScore);
    }

    @Override
    protected Texture computeBackground() {
        return new Texture("images/gmork.png");
    }

    @Override
    protected String computeTitle() {
        return "YOU DIED!";
    }
}