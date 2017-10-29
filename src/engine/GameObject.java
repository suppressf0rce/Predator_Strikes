package engine;

import java.util.ArrayList;

@SuppressWarnings("WeakerAccess")
public abstract class GameObject {

    //===>>Variables<<===//
    protected String tag;
    protected float posX;
    protected float posY;
    protected int width, height;
    protected boolean dead;
    protected ArrayList<GameObject> objects = new ArrayList<>();


    //===>>Methods<<===//
    public abstract void update(float dt);

    public abstract void render(Renderer r);


    //===>>Getters & Setters<<===//
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public ArrayList<GameObject> getObjects() {
        return objects;
    }
}
