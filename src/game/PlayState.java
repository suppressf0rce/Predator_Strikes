package game;

import engine.GameEngine;
import engine.GameObject;
import engine.GameState;
import engine.Renderer;
import engine.gfx.Image;
import engine.sfx.SoundClip;

import java.util.ArrayList;

public class PlayState extends GameState {

    //===>>Variables<<===//
    private Image background;
    private ScrollingRandomBackground srb;
    private ArrayList<GameObject> objects = new ArrayList<>();
    private SoundClip backgroundMusic;

    private float tmp = 0;

    //===>>Methods<<===//
    @Override
    public void init() {
        GameEngine.setDebug(true);

        //Initialization of the background
        background = new Image("res/bgd.png");
        srb = new ScrollingRandomBackground(background);

        //Initialization of the player
        Player player = new Player(this);
        objects.add(player);

        backgroundMusic = new SoundClip("res/Inverse Phase - Propane NESmares (8-bit remix).wav");
        backgroundMusic.setVolume(-20);
        //backgroundMusic.loop();
    }

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
    }

    @Override
    public void render(Renderer r) {
        if (tmp > 0.05) {
            r.drawBackground(background);
            tmp = 0;
        }

        srb.scroll();
        //r.drawImage(background,0,0);
        for (GameObject obj : objects) {
            obj.render(r);
        }
    }


    //===>>Getters & Setters<<===//
    public ArrayList<GameObject> getObjects() {
        return objects;
    }
}
