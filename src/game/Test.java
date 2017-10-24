package game;

import engine.AbstractGame;
import engine.GameContainer;
import engine.Renderer;

import java.awt.event.KeyEvent;

public class Test extends AbstractGame {

    public static void main(String[] args) {
        GameContainer container = new GameContainer(new Test(), 60, 640, 480, 1f, "Igra");
        container.start();
    }

    @Override
    public void update(GameContainer gc, float dt) {
        if (gc.getInput().isKeyDown(KeyEvent.VK_A)) {
            System.out.println("A was pressed");
        }
    }

    @Override
    public void render(GameContainer gc, Renderer r) {

    }
}
