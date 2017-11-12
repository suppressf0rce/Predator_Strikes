package game;

import engine.GameEngine;
import engine.GameHost;
import engine.gfx.Transition;

public class PredatorStrikes {

    public static void main(String[] args) {
        Utils.readHighScoreEntries();

        GameHost host = new GameHost();
        GameEngine engine = new GameEngine(host);

        new PlayState(host);
        new Transition(host);
        new PauseState(host);
        new HighscoreState(host);
        new GameOverState(host);

        host.setState("pause");
        GameEngine.setDebug(false);

        engine.start();

    }

}
