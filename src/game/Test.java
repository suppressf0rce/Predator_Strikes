package game;

import engine.AbstractGame;
import engine.GameContainer;
import engine.Renderer;
import engine.gfx.ImageTile;

import java.awt.event.KeyEvent;

public class Test extends AbstractGame {

    private ImageTile image;
    private float temp;
    private int temp2 = 0;

    public Test() {
        image = new ImageTile("res/testsprites.png", 125, 125);
    }

    @Override
    public void update(GameContainer gc, float dt) {
        if (gc.getInput().isKeyDown(KeyEvent.VK_A)) {
            System.out.println("A was pressed");
        }

        temp += dt * 15;

        if (temp > 3) {
            temp = 0;
            temp2++;
        }

        if (temp2 > 3) {
            temp2 = 0;
        }
    }

    public static void main(String[] args) {
        GameContainer container = new GameContainer(new Test(), 60, 640, 480, 1f, "Igra");
        container.start();
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        r.drawImageTile(image, gc.getInput().getMouseX() - 62, gc.getInput().getMouseY() - 62, (int) temp, temp2);
    }
}
