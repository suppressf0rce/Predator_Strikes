package game;

import engine.GameEngine;

public class PredatorStrikes {

    public static void main(String[] args) {
        GameEngine engine = new GameEngine(new PlayState());
        engine.start();
    }

}
