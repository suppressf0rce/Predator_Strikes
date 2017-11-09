package game;

import engine.*;
import engine.gfx.Image;
import engine.gfx.Transition;
import engine.gfx.Transition.TransitionType;
import engine.sfx.SoundClip;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class PlayState extends GameState {

    //===>>Variables<<===//
    private Image background;
    private ArrayList<GameObject> objects = new ArrayList<>();
    private SoundClip backgroundMusic;

    public PlayState(GameHost host) {
        super("play", host);


        GameEngine.setDebug(true);

        //Initialization of the background
        background = new Image("res/bgd.png");

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

        if (GameEngine.getInput().isKeyDown(KeyEvent.VK_ESCAPE)) {
            TransitionType transType = TransitionType.values()[(int) (Math.random() * TransitionType.values().length)];

            Transition.transitionTo("pause", transType, 0.5f);
        }
    }

    @Override
    public void render(Renderer r) {
        //r.drawImage(background, 0, 0);
        r.drawBackground(background);
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
