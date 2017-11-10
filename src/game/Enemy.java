package game;

import engine.GameObject;
import engine.GameState;
import engine.Renderer;
import engine.gfx.Image;

import java.util.Random;

@SuppressWarnings("WeakerAccess")
public class Enemy extends GameObject {
    // offset on which images spawn
    public static int offset = 0;


    // image that represents the enemies
    private Image image;

    // movement speed
    private float speed = 2;
    // threshold for shooting
    private float fireSpeed = 3;
    // state in which the Enemy is
    private GameState state;
    // how long ago the enemy has fired
    private float fireTime = 0;

    private Random random;

    public Enemy() {
        image = new Image("res/ufo_enemy.png");
        random = new Random();

        // starting position
        this.posX = random.nextInt(50) + offset;
        this.posY = -200;
    }


    @Override
    public void update(float dt) {
        // increasing the vertical positon
        this.posY += speed;
    }

    @Override
    public void render(Renderer r) {
        // drawing enemy image on the screen
        r.drawImage(image, (int) this.posX, (int) this.posY);
    }
}
