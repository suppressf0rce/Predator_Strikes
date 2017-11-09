package game;

import engine.GameEngine;
import engine.GameObject;
import engine.GameState;
import engine.Renderer;
import engine.gfx.ImageTile;

import java.awt.event.KeyEvent;

@SuppressWarnings("WeakerAccess")
public class Player extends GameObject {

    //===>>Variables<<===//
    private ImageTile image;
    private int numberOfTileX;
    private int numberOfTileY;

    private float speed = 300;
    private float fireSpeed = 6;

    private GameState state;
    private float fireTime = 0;

    private float animX, animY;
    private float animationSpeed = 20;
    private int idleAnimX = 1;
    private int idleAnimY = 1;
    private int endAnimX = 2;
    private int endAnimY = 2;

    //===>>Constructor<<===//
    public Player(GameState state) {
        this.tag = "player";

        image = new ImageTile("res/spaceship.png", 63, 100);
        numberOfTileX = 3;
        numberOfTileY = 2;

        this.width = image.getTileWidth();
        this.height = image.getTileHeight();
        this.posX = GameEngine.getWindow().getWidth() / 2 - width / 2;
        this.posY = GameEngine.getWindow().getHeight() - height - 15;

        this.state = state;

        animX = 1;
        animY = 1;
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    @Override
    public void update(float dt) {
        //Move left
        if (GameEngine.getInput().isKey(KeyEvent.VK_LEFT)) {
            //Update position
            float newX = posX - dt * speed;
            if (newX > 0)
                posX = newX;

            //Update animation
            animX -= dt * animationSpeed;
            if ((int) animX < 0 && animY > 0) {
                animX = numberOfTileX;
                animY--;
            }

            if ((int) animX < 0) {
                animX = 0;
            }
        }

        //Move right
        else if (GameEngine.getInput().isKey(KeyEvent.VK_RIGHT)) {
            //Update position
            float newX = posX + dt * speed;
            if (newX + width < GameEngine.getWindow().getWidth())
                posX = newX;

            //Update animation
            animX += dt * animationSpeed;
            if ((int) animX > numberOfTileX && animY < numberOfTileY) {
                animX = 0;
                animY++;
            }
            if (animY == endAnimY && (int) animX >= endAnimX) {
                animX = endAnimX;
            }

        } else {
            //Animate space ship to idle if its not moving
            if (animY < idleAnimY) {
                idleFromLeft(dt);
            } else if (animY > idleAnimY) {
                idleFromRight(dt);
            } else {
                if (animX < idleAnimX) {
                    idleFromLeft(dt);
                } else if (animX > idleAnimX) {
                    idleFromRight(dt);
                }
            }
        }

        //Fire bullet
        fireTime += dt * fireSpeed;
        if (fireTime > 1f) {
            if (GameEngine.getInput().isKey(KeyEvent.VK_SPACE)) {
                Bullet bullet = new Bullet(Direction.UP, this);
                bullet.setPosX(posX + width / 2 - bullet.getWidth() / 2);
                bullet.setPosY(posY - bullet.getHeight() + 15);
                objects.add(bullet);
                fireTime = 0;
            }
        }


        //Update its objects
        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).update(dt);
        }


        //Crash proof animation array
        if ((int) animY < 0)
            animY = 0;
        if ((int) animX < 0)
            animX = 0;
        if ((int) animX > numberOfTileX)
            animX = numberOfTileX;
        if ((int) animY > numberOfTileY)
            animY = numberOfTileY;

        //System.out.println("Tile X:"+(int)animX+ " Tile Y:"+(int)animY);
    }

    @Override
    public void render(Renderer r) {
        r.drawImageTile(image, (int) posX, (int) posY, (int) animX, (int) animY);

        //Render its objects
        for (GameObject obj : objects) {
            obj.render(r);
        }
    }


    private void idleFromRight(float dt) {
        animX -= dt * animationSpeed;
        if ((int) animX < 0 && animY > idleAnimY) {
            animX = 3;
            animY--;
        }

        if ((int) animX < idleAnimX && animY == idleAnimY) {
            animX = idleAnimX;
        }
    }

    private void idleFromLeft(float dt) {
        animX += dt * animationSpeed;
        if ((int) animX > numberOfTileX && animY < idleAnimY) {
            animX = 0;
            animY++;
        }
        if (animY == idleAnimY && (int) animX >= idleAnimX) {
            animX = idleAnimX;
        }
    }
}
