package game;

import engine.GameEngine;
import engine.GameState;
import engine.Renderer;
import engine.gfx.Image;

public class PlayState extends GameState {

    //===>>Variables<<===//
    private Image background;

    @Override
    public void init() {
        GameEngine.setDebug(true);

        background = new Image("res/background.jpg");
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(Renderer r) {
        r.drawImage(background, 0, 0);
    }
}
