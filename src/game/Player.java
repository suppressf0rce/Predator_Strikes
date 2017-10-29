package game;

import engine.GameEngine;
import engine.GameObject;
import engine.GameState;
import engine.Renderer;

import java.awt.event.KeyEvent;

@SuppressWarnings("WeakerAccess")
public class Player extends GameObject {

    //===>>Variables<<===//
    private float speed = 500;
    private float fireSpeed = 8;

    private GameState state;
    private float fireTime = 0;

    //===>>Constructor<<===//
    public Player(GameState state) {
        this.tag = "player";
        this.width = 16;
        this.height = 16;
        this.posX = GameEngine.getWindow().getWidth() / 2 - width / 2;
        this.posY = GameEngine.getWindow().getHeight() - width - 15;

        this.state = state;
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    @Override
    public void update(float dt) {
        //Move left
        if (GameEngine.getInput().isKey(KeyEvent.VK_LEFT)) {
            float newX = posX - dt * speed;

            if (newX > 0)
                posX = newX;
        }

        //Move right
        if (GameEngine.getInput().isKey(KeyEvent.VK_RIGHT)) {
            float newX = posX + dt * speed;

            if (newX + width < GameEngine.getWindow().getWidth())
                posX = newX;
        }

        //Fire bullet
        fireTime += dt * fireSpeed;
        if (fireTime > 1f) {
            if (GameEngine.getInput().isKeyDown(KeyEvent.VK_SPACE)) {
                Bullet bullet = new Bullet(Direction.UP, this);
                bullet.setPosX(posX + width / 2 - bullet.getWidth() / 2);
                bullet.setPosY(posY);
                objects.add(bullet);
                fireTime = 0;
            }
        }


        //Update its objects
        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).update(dt);
        }

    }

    @Override
    public void render(Renderer r) {
        r.drawFillRect((int) posX, (int) posY, width, height, 0xffff0000);

        //Render its objects
        for (GameObject obj : objects) {
            obj.render(r);
        }
    }
}
