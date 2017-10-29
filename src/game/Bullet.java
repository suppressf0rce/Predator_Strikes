package game;

import engine.GameEngine;
import engine.GameObject;
import engine.Renderer;

@SuppressWarnings("WeakerAccess")
public class Bullet extends GameObject {

    //===>>Variables<<===//
    private float speed = 800;
    private Direction direction;
    private GameObject containedIn;

    //===>>Constructor<<===//
    public Bullet(Direction direction, GameObject containedIn) {
        this.direction = direction;
        this.containedIn = containedIn;

        this.width = 8;
        this.height = 12;
    }


    //===>>Methods<<===//
    @Override
    public void update(float dt) {

        switch (direction) {
            case UP: {
                posY -= speed * dt;
                if (posY < 0) {
                    containedIn.getObjects().remove(this);
                }
                break;
            }
            case DOWN: {
                posY += speed * dt;
                if (posY > GameEngine.getWindow().getHeight()) {
                    containedIn.getObjects().remove(this);
                }
                break;
            }
        }

    }

    @Override
    public void render(Renderer r) {
        r.drawFillRect((int) posX, (int) posY, width, height, 0xff00ff00);
    }
}
