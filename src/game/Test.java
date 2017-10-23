package game;

import engine.GameContainer;

public class Test {

    public static void main(String[] args) {
        GameContainer testGame = new GameContainer(60, 640, 480, 1f, "Test game");
        testGame.start();
    }
}
