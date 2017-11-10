package game;

import engine.*;
import engine.gfx.Image;
import engine.gfx.ScrollableImage;
import engine.gfx.Transition;
import engine.gfx.Transition.TransitionType;
import engine.sfx.SoundClip;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

@SuppressWarnings("WeakerAccess")
public class PlayState extends GameState {

    //===>>Variables<<===//
    private ScrollableImage background;
    private ScrollingRandomBackground starMask;
    private ArrayList<GameObject> objects = new ArrayList<>();
    private SoundClip backgroundMusic;
    private Image ufo;

    private float tmp = 0;

    public PlayState(GameHost host) {
        super("play", host);

        //Initialization of the background
        background = new ScrollableImage("res/bgd.png");
        ufo = new Image("res/ufo_enemy.png");
        starMask = new ScrollingRandomBackground(background);

        //Initialization of the player
        Player player = new Player(this);
        objects.add(player);

        backgroundMusic = new SoundClip("res/Inverse Phase - Propane NESmares (8-bit remix).wav");
        backgroundMusic.setVolume(-10);
    }


    //===>>Methods<<===//

    @Override
    public void update(float dt) {
        checkForCollisions();

        // spawning enemies
        Random rand = new Random();
        int r = rand.nextInt(100);

        // reset offset if it's out of screen boundaries
        if (Enemy.offset > 600) {
            Enemy.offset = 0;
        }
        if (r == 1) {
            objects.add(new Enemy());
        }
        Enemy.offset += 100;




        //loop through objects, and if it is dead remove it from list
        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).update(dt);
            if (objects.get(i).isDead()) {
                objects.remove(i);
                i--;
            }
        }

        tmp += dt;
        if (tmp > 0.05) {
            tmp = 0;
            background.scroll();
        }
        starMask.scroll();

        if (GameEngine.getInput().isKeyDown(KeyEvent.VK_ESCAPE)) {
            TransitionType transType = TransitionType.values()[(int) (Math.random() * TransitionType.values().length)];

            Transition.transitionTo("pause", transType, 0.5f);
        }
    }

    @Override
    public void render(Renderer r) {
        r.drawImage(background, 0, 0);
        // drawing the space particles from the star mask
        r.drawParticles(starMask, starMask.getParticle_grid());
        for (GameObject obj : objects) {
            obj.render(r);
        }
    }

    public void checkForCollisions() {
        GameObject player = null;
        // looking for the player object
        for (GameObject go : objects) {
            if (go.getTag().equals("player")) {
                player = go;

            }
        }

        float b_x1, b_x2, b_y1, b_y2;
        float e_x1, e_x2, e_y1, e_y2;
        // checking for bullet collision
        for (GameObject go : objects) {
            if (go.getTag().equals("enemy")) {

                for (GameObject bullet : player.getObjects()) {


                    b_x1 = bullet.getPosX();
                    b_x2 = b_x1 + bullet.getWidth();

                    b_y1 = bullet.getPosY();
                    b_y2 = b_y1 + bullet.getHeight();

                    e_x1 = go.getPosX();
                    e_x2 = e_x1 + go.getWidth();

                    e_y1 = go.getPosY();
                    e_y2 = e_y1 + go.getHeight();


                    if (e_x1 > b_x2 && e_x2 < b_x1 && e_y1 < b_y2 && e_y2 > b_y1) {
                        System.out.println("HAPPENED");
                        go.setDead(true);
                        bullet.setDead(true);
                    } else
                        continue;
                }
            }
        }
    }
    @Override
    public void suspendState() {
        backgroundMusic.stop();
    }

    @Override
    public void resumeState() {
        //backgroundMusic.loop();
    }


    //===>>Getters & Setters<<===//
    public ArrayList<GameObject> getObjects() {
        return objects;
    }
}
