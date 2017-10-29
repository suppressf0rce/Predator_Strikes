package game;

import engine.GameEngine;
import engine.GameObject;
import engine.GameState;
import engine.Renderer;
import engine.gfx.Image;

import java.util.ArrayList;

public class PlayState extends GameState {

    //===>>Variables<<===//
    private Image background;
    private ArrayList<GameObject> objects = new ArrayList<>();


    //===>>Methods<<===//
    @Override
    public void init() {
        GameEngine.setDebug(true);

        //Initialization of the background
        background = new Image("res/background.jpg");

        //Initialization of the player
        Player player = new Player(this);
        objects.add(player);

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
    }

    @Override
    public void render(Renderer r) {
        r.drawImage(background, 0, 0);

        for (GameObject obj : objects) {
            obj.render(r);
        }
    }


    //===>>Getters & Setters<<===//
    public ArrayList<GameObject> getObjects() {
        return objects;
    }
}
