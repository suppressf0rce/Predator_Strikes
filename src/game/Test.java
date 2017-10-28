package game;

import engine.GameEngine;
import engine.GameState;
import engine.Renderer;
import engine.gfx.Image;
import engine.gfx.ImageTile;
import engine.sfx.SoundClip;

import java.awt.event.KeyEvent;

@SuppressWarnings("WeakerAccess")
public class Test extends GameState {

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
        GameEngine engine = new GameEngine(new Test());
        engine.start();
    }

    @Override
    public void init() {
    }

    @Override
    public void update(float dt) {
        if (GameEngine.getInput().isKeyDown(KeyEvent.VK_A)) {
            runningSFX.loop();
            renderSprite = true;
            renderAlpha = false;
        }

        if (GameEngine.getInput().isKeyDown(KeyEvent.VK_S)) {
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
    public void render(Renderer r) {
        r.drawImage(image, 30, 30);

        if (renderSprite)
            r.drawImageTile(sprite, GameEngine.getInput().getMouseX() - 62, GameEngine.getInput().getMouseY() - 62, (int) temp, temp2);

        if (renderAlpha)
            r.drawImage(image2, GameEngine.getInput().getMouseX() - 32, GameEngine.getInput().getMouseY() - 32);
    }
}
