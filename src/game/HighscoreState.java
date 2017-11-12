package game;

import engine.GameEngine;
import engine.GameHost;
import engine.GameState;
import engine.Renderer;
import engine.gfx.Font;
import engine.gfx.Image;
import engine.gfx.Transition;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

@SuppressWarnings("WeakerAccess")
public class HighscoreState extends GameState {

    private static final int STAR_MAX = 1000;
    private static final float MAX_Z = 2000.0f;
    BufferedImage image;
    private Star[] stars = new Star[STAR_MAX];
    private Image background;
    private Color[] grayscale = new Color[256];
    private float speed = 10.0f;
    private Font font;



    public HighscoreState(GameHost host) {
        super("highscore", host);
        font = new Font("res/russo_one_36pt.png", 300, 2);

        for (int i = 0; i < STAR_MAX; ++i) {
            stars[i] = new Star();
            stars[i].posX = (float) (Math.random() * 2000.0) - 1000.0f;
            stars[i].posY = (float) (Math.random() * 2000.0) - 1000.0f;
            stars[i].posZ = (float) (Math.random() * MAX_Z);
        }

        for (int i = 0; i < 256; ++i)
            grayscale[i] = new Color(i, i, i);



        background = new Image("res/black.jpg");
        image = new BufferedImage(GameEngine.getWindow().getWidth(), GameEngine.getWindow().getHeight(), BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    public void update(float dt) {

        if (GameEngine.getInput().isKeyDown(KeyEvent.VK_ESCAPE)) {
            Transition.TransitionType transType = Transition.TransitionType.values()[(int) (Math.random() * Transition.TransitionType.values().length)];
            Transition.transitionTo("pause", transType, 0.5f);
        }

        for (Star s : stars) {
            s.posZ -= speed;
            if (s.posZ < 1.0) {
                s.posZ += MAX_Z;
                s.posX = (float) (Math.random() * 2000.0) - 1000.0f;
                s.posY = (float) (Math.random() * 2000.0) - 1000.0f;
            }
        }

        image = new BufferedImage(GameEngine.getWindow().getWidth(), GameEngine.getWindow().getHeight(), BufferedImage.TYPE_INT_ARGB);

    }


    public void renderBackground(Renderer r) {


        Graphics2D g = (Graphics2D) image.getGraphics();


        r.drawImage(new Image(image), 0, 0);

    }

    @Override
    public void render(Renderer r) {
        r.drawImage(background, 0, 0);

        int sw = GameEngine.getWindow().getWidth();
        int sh = GameEngine.getWindow().getHeight();
        for (Star s : stars) {
            float sX1 = sw / 2 + s.posX * (400.0f / s.posZ);
            float sY1 = sh / 2 + s.posY * (400.0f / s.posZ);

            float sX2 = sw / 2 + s.posX * (400.0f / (s.posZ + speed));
            float sY2 = sh / 2 + s.posY * (400.0f / (s.posZ + speed));

            int brightness = (int) (255 - (s.posZ / MAX_Z) * 255.0f);

            r.drawLine((int) sX1, (int) sY1, (int) sX2, (int) sY2, grayscale[brightness]);
        }


        r.drawString("Pos", 20, 20, font);
        r.drawString("Name", 150, 20, font);
        r.drawString("Score", 750, 20, font);

        for (int i = 0; i < 10; i++) {

            r.drawString("" + (i + 1) + ".", 40, 50 + i * 55, font);
            r.drawString(Utils.highscore_entries.get(i).playerName, 150, 50 + i * 55, font);
            r.drawString(String.valueOf(Utils.highscore_entries.get(i).playerScore), 750, 50 + i * 55, font);
        }
    }

    @Override
    public void suspendState() {
    }

    @Override
    public void resumeState() {


    }


    private static class Star {
        public float posX;
        public float posY;
        public float posZ;
    }
}
