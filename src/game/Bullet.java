package game;

import engine.GameEngine;
import engine.GameObject;
import engine.Renderer;
import engine.gfx.ImageTile;

@SuppressWarnings("WeakerAccess")
public class Bullet extends GameObject {

    //===>>Variables<<===//
    private float speed = 600;
    private Direction direction;
    private GameObject containedIn;
    float animationSpeed = 200;
    float animationTime;
    private ImageTile image;

    //===>>Constructor<<===//
    public Bullet(Direction direction, GameObject containedIn) {
        this.direction = direction;
        this.containedIn = containedIn;

        if (direction == Direction.DOWN)
            image = new ImageTile("res/bullet_down.png", 25, 89);
        else
            image = new ImageTile("res/bullet_up.png", 25, 89);

        this.width = image.getTileWidth();
        this.height = image.getTileHeight();
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

        animationTime += dt * animationSpeed;
        if (animationTime > 3) {
            animationTime = 0;
        }

    }

    @Override
    public void render(Renderer r) {
        r.drawImageTile(image, (int) posX, (int) posY, (int) animationTime, 0);
    }
}
