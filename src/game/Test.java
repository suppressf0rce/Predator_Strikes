package game;

import engine.AbstractGame;
import engine.GameContainer;
import engine.Renderer;
import engine.gfx.Image;
import engine.gfx.ImageTile;
import engine.sfx.SoundClip;

import java.awt.event.KeyEvent;

@SuppressWarnings("WeakerAccess")
public class Test extends AbstractGame {

    private Image image;
    private Image image2;
    private SoundClip runningSFX;
    private ImageTile sprite;

    private boolean renderSprite = true;
    private boolean renderAlpha = false;


    private float temp;
    private int temp2 = 0;

    public Test() {
        sprite = new ImageTile("res/testsprites.png", 125, 125);
        image = new Image("res/test.png");
        image2 = new Image("res/smiley.png");
        runningSFX = new SoundClip("res/bgRunningMusic.wav");
        runningSFX.setVolume(0);
        image2.setAlpha(true);

    }

    public static void main(String[] args) {
        GameContainer container = new GameContainer(new Test());
        container.setWidth(640);
        container.setHeight(480);
        container.setTitle("Test Game");
        container.setCapFps(true);
        container.start();
    }

    @Override
    public void update(GameContainer gc, float dt) {
        if (gc.getInput().isKeyDown(KeyEvent.VK_A)) {
            runningSFX.loop();
            renderSprite = true;
            renderAlpha = false;
        }

        if (gc.getInput().isKeyDown(KeyEvent.VK_S)) {
            runningSFX.stop();
            renderSprite = false;
            renderAlpha = true;
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

    @Override
    public void render(GameContainer gc, Renderer r) {
        r.drawImage(image, 30, 30);

        if (renderSprite)
            r.drawImageTile(sprite, gc.getInput().getMouseX() - 62, gc.getInput().getMouseY() - 62, (int) temp, temp2);

        if (renderAlpha)
            r.drawImage(image2, gc.getInput().getMouseX() - 32, gc.getInput().getMouseY() - 32);
    }
}
