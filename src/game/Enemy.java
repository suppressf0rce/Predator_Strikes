package game;

import engine.GameEngine;
import engine.GameObject;
import engine.GameState;
import engine.Renderer;
import engine.gfx.Image;

import java.awt.event.KeyEvent;
import java.util.Random;

@SuppressWarnings({"WeakerAccess", "ForLoopReplaceableByForEach"})
public class Enemy extends GameObject {
    // offset on which images spawn
    public static int offset = 0;


    // image that represents the enemies
    private Image image;

    // movement speed
    private float speed = 2;
    // threshold for shooting
    private float fireSpeed = 1;
    // state in which the Enemy is
    private GameState state;
    // how long ago the enemy has fired
    private float fireTime = 0;

    private Random random;

    public Enemy() {
        tag = "enemy";

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

        //Fire bullet
        fireTime += dt * fireSpeed;
        if (fireTime > 1f) {
            int fireChance = random.nextInt(100);
            if (fireChance < 20) {
                Bullet bullet = new Bullet(Direction.DOWN, this);
                bullet.setPosX(posX + width / 2 + bullet.getWidth());
                bullet.setPosY(posY + height + 15);
                objects.add(bullet);

            }
            fireTime = 0;
        }

        //Update its objects
        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).update(dt);
        }
    }

    @Override
    public void render(Renderer r) {
        // drawing enemy image on the screen
        r.drawImage(image, (int) this.posX, (int) this.posY);

        //Render its objects
        for (GameObject obj : objects) {
            obj.render(r);
        }
    }
}
