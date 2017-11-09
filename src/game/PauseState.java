package game;

import engine.GameEngine;
import engine.GameHost;
import engine.GameState;
import engine.Renderer;
import engine.gfx.Image;
import engine.gfx.Transition;
import engine.gfx.Transition.TransitionType;

import java.awt.event.KeyEvent;

public class PauseState extends GameState {

    Image bg = new Image("res/bgd.png");

    public PauseState(GameHost host) {
        super("pause", host);
    }

    @Override
    public void init() {

    }

    @Override
    public void update(float dt) {

        if (GameEngine.getInput().isKey(KeyEvent.VK_ESCAPE)) {
            TransitionType transType = TransitionType.values()[(int) (Math.random() * TransitionType.values().length)];

            Transition.transitionTo("play", transType, 0.5f);
        }
    }

    @Override
    public void render(Renderer r) {
        r.drawImage(bg, 0, 0);
    }

    @Override
    public void suspendState() {

    }

    @Override
    public void resumeState() {

    }
}
