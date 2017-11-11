package game;

import engine.*;
import engine.gfx.Image;
import engine.gfx.ScrollableImage;
import engine.gfx.Transition;
import engine.gfx.Transition.TransitionType;
import engine.sfx.SoundClip;

import java.awt.*;
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

    public static int playerScore = 0;

    Player player;
    private float deathConter;

    public PlayState(GameHost host) {
        super("play", host);

        //Initialization of the background
        background = new ScrollableImage("res/bgd.png");
        ufo = new Image("res/ufo_enemy.png");
        starMask = new ScrollingRandomBackground(background);

        //Initialization of the player
        player = new Player(this);
        objects.add(player);

        backgroundMusic = new SoundClip("res/Inverse Phase - Propane NESmares (8-bit remix).wav");
        backgroundMusic.setVolume(-10);
    }


    //===>>Methods<<===//

    @SuppressWarnings("Duplicates")
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

        if (player.isDead()) {
            deathConter += dt;
        }

        if (deathConter > 1) {
            GameOverState go = (GameOverState) GameEngine.getHost().getState("gameover");
            go.setBackground(new Image(GameEngine.getHost().renderSnapshot(null, this)));
            Transition.transitionTo("gameover", TransitionType.Crossfade, .3f);
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
        r.drawString("SCORE: " + playerScore, 930, 0, null);
    }

    @SuppressWarnings("Duplicates")
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
        float p_x1, p_x2, p_y1;
        float eb_x1, eb_x2, eb_y1, eb_y2;
        for (int i = 0; i < objects.size(); i++) {
            GameObject go = objects.get(i);

            //Checking for player bullet collision
            if (go.getTag().equals("enemy")) {

                if (player != null) {
                    for (GameObject bullet : player.getObjects()) {


                        b_x1 = bullet.getPosX();
                        b_x2 = b_x1 + bullet.getWidth();

                        b_y1 = bullet.getPosY();

                        e_x1 = go.getPosX();
                        e_x2 = e_x1 + go.getWidth();

                        e_y1 = go.getPosY();
                        e_y2 = e_y1 + go.getHeight();


                        //Player bullet collided with the enemy
                        if (e_y2 > b_y1 && e_y1 < b_y1 && e_x1 < b_x2 && e_x2 > b_x1) {
                            objects.addAll(go.getObjects());
                            playerScore += 10;
                            objects.add(new Explosion((int) (go.getPosX() + go.getWidth() / 2), (int) (go.getPosY() + go.getHeight() / 2), Color.green));
                            go.setDead(true);
                            bullet.setDead(true);

                            //TODO: Enemy died or lost life
                        }

                        for (GameObject e_bullet : go.getObjects()) {

                            eb_x1 = e_bullet.getPosX();
                            eb_x2 = eb_x1 + e_bullet.getWidth();

                            eb_y1 = e_bullet.getPosY();
                            eb_y2 = eb_y1 + e_bullet.getHeight();


                            //Player bullet collided with the enemy bullet
                            if (eb_y2 > b_y1 && eb_y1 < b_y1 && eb_x1 < b_x2 && eb_x2 > b_x1) {
                                e_bullet.setDead(true);
                                bullet.setDead(true);
                                //TODO: Bullets collided do something
                            }

                        }
                    }
                }
            }


            //Checking for enemy bullet collision
            if (go.getTag().equals("player")) {

                for (int j = 0; j < objects.size(); j++) {
                    GameObject enemy = objects.get(j);
                    if (enemy.getTag().equals("enemy")) {

                        e_x1 = enemy.getPosX();
                        e_x2 = e_x1 + enemy.getWidth();

                        e_y1 = enemy.getPosY();
                        e_y2 = e_y1 + enemy.getHeight();

                        p_x1 = go.getPosX();
                        p_x2 = p_x1 + go.getWidth();

                        p_y1 = go.getPosY();


                        //Collision of enemy bullet with player
                        for (GameObject bullet : enemy.getObjects()) {

                            b_x1 = bullet.getPosX();
                            b_x2 = b_x1 + bullet.getWidth();

                            b_y1 = bullet.getPosY();
                            b_y2 = b_y1 + bullet.getHeight();

                            if (b_y2 > p_y1 && b_y1 < p_y1 && b_x1 < p_x2 && b_x2 > p_x1) {
                                objects.addAll(go.getObjects());
                                go.setDead(true);
                                objects.addAll(bullet.getObjects());
                                bullet.setDead(true);

                                objects.add(new Explosion((int) (go.getPosX() + go.getWidth() / 2), (int) (go.getPosY() + go.getHeight() / 2), Color.white));
                                //GameEngine.getHost().setState("gameover");

                            }
                        }

                        //Collision of enemy with player
                        if (e_y2 > p_y1 && e_y1 < p_y1 && e_x1 < p_x2 && e_x2 > p_x1) {
                            objects.addAll(go.getObjects());
                            go.setDead(true);
                            objects.addAll(enemy.getObjects());
                            enemy.setDead(true);


                            objects.add(new Explosion((int) (go.getPosX() + go.getWidth() / 2), (int) (go.getPosY() + go.getHeight() / 2), Color.white));

                        }

                    }


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
        backgroundMusic.loop();
    }


    //===>>Getters & Setters<<===//
    public ArrayList<GameObject> getObjects() {
        return objects;
    }
}
