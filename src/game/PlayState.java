package game;

import engine.*;
import engine.gfx.ScrollableImage;
import engine.gfx.Transition;
import engine.gfx.Transition.TransitionType;
import engine.sfx.SoundClip;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

@SuppressWarnings("WeakerAccess")
public class PlayState extends GameState {

    //===>>Variables<<===//
    private ScrollableImage background;
    private ScrollingRandomBackground starMask;
    private ArrayList<GameObject> objects = new ArrayList<>();
    private SoundClip backgroundMusic;

    private float tmp = 0;

    public PlayState(GameHost host) {
        super("play", host);

        //Initialization of the background
        background = new ScrollableImage("res/bgd.png");
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
