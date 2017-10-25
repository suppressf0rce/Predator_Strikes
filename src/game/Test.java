package game;

import engine.AbstractGame;
import engine.GameContainer;
import engine.Renderer;
import engine.gfx.Image;
import engine.sfx.SoundClip;

import java.awt.event.KeyEvent;

public class Test extends AbstractGame {

    private Image image;
    private Image image2;
    private SoundClip runningSFX;

    private float temp;
    private int temp2 = 0;

    public Test() {
        image = new Image("res/test.png");
        image2 = new Image("res/smiley.png");
        runningSFX = new SoundClip("res/bgRunningMusic.wav");
        runningSFX.setVolume(-20);
        image2.setAlpha(true);
    }

    @Override
    public void update(GameContainer gc, float dt) {
        if (gc.getInput().isKeyDown(KeyEvent.VK_A)) {
            runningSFX.loop();
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
        GameContainer container = new GameContainer(new Test());
        container.setWidth(640);
        container.setHeight(480);
        container.setTitle("Test Game");
        container.start();
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        r.drawImage(image2, gc.getInput().getMouseX() - 32, gc.getInput().getMouseY() - 32);
        r.drawImage(image, 30, 30);
    }
}
