package game;

import engine.GameEngine;
import engine.GameHost;
import engine.GameState;
import engine.Renderer;
import engine.gfx.Font;
import engine.gfx.Image;
import engine.gfx.ImageTile;
import engine.gfx.Transition;
import engine.gfx.Transition.TransitionType;
import engine.sfx.SoundClip;

import java.awt.event.KeyEvent;

@SuppressWarnings("WeakerAccess")
public class PauseState extends GameState {

    private Image bg;
    private Font font;
    private Font fontSelected;
    private Font activeFont = null;

    private ImageTile planet = new ImageTile("res/planet.png", 72, 72);
    private int planetX = GameEngine.getWindow().getWidth() / 2 - 150;
    private int planetY = 250;
    private float planetTile = 0;
    private int planetTile1 = 0;
    private int planetAnimationSpeed = 15;

    private String selectedItem = null;

    private SoundClip backgroundMusic;
    private SoundClip menuEffect;

    public PauseState(GameHost host) {
        super("pause", host);

        bg = new Image("res/menu-bg.png");
        font = new Font("res/russo_one_50pt.png", 300, 2);
        fontSelected = new Font("res/russo_one_50pt_red.png", 300, 2);

        backgroundMusic = new SoundClip("res/Pendulum - Granite (8 bit remix).wav");
        backgroundMusic.setVolume(-5);

        menuEffect = new SoundClip("res/menu-sfx.wav");
    }

    @Override
    public void update(float dt) {

        if (GameEngine.getInput().isKeyDown(KeyEvent.VK_DOWN) && selectedItem.equals("play")) {
            selectedItem = "highscores";
            planetY = 325;
            planetX -= 120;
            menuEffect.play();
        } else if (GameEngine.getInput().isKeyDown(KeyEvent.VK_DOWN) && selectedItem.equals("highscores")) {
            selectedItem = "quit";
            planetY = 400;
            planetX += 120;
            menuEffect.play();
        } else if (GameEngine.getInput().isKeyDown(KeyEvent.VK_UP) && selectedItem.equals("quit")) {
            selectedItem = "highscores";
            planetY = 325;
            planetX -= 120;
            menuEffect.play();
        } else if (GameEngine.getInput().isKeyDown(KeyEvent.VK_UP) && selectedItem.equals("highscores")) {
            selectedItem = "play";
            planetY = 250;
            planetX += 120;
            menuEffect.play();
        }


        if (GameEngine.getInput().isKeyDown(KeyEvent.VK_ENTER)) {
            if (selectedItem.equals("quit"))
                System.exit(0);

            if (selectedItem.equals("play")) {
                TransitionType transType = TransitionType.values()[(int) (Math.random() * TransitionType.values().length)];
                Transition.transitionTo("play", transType, 0.5f);
            }

            if (selectedItem.equals("highscores")) {
                TransitionType transType = TransitionType.values()[(int) (Math.random() * TransitionType.values().length)];
                Transition.transitionTo("highscore", transType, 0.5f);
            }
        }

        planetTile += dt * planetAnimationSpeed;

        if ((int) planetTile > 4) {
            planetTile = 0;
            planetTile1++;
        }

        if (planetTile1 == 3 && (int) planetTile == 3) {
            planetTile1 = 0;
            planetTile = 0;
        }
    }

    @Override
    public void render(Renderer r) {

        activeFont = font;

        r.drawImage(bg, 0, 0);

        if (selectedItem.equals("play")) {
            activeFont = fontSelected;
        } else {
            activeFont = font;
        }
        r.drawString("Play", GameEngine.getWindow().getWidth() / 2 - 60, 250, activeFont);


        if (selectedItem.equals("highscores")) {
            activeFont = fontSelected;
        } else {
            activeFont = font;
        }
        r.drawString("High scores", GameEngine.getWindow().getWidth() / 2 - 180, 325, activeFont);

        if (selectedItem.equals("quit")) {
            activeFont = fontSelected;
        } else {
            activeFont = font;
        }
        r.drawString("Quit", GameEngine.getWindow().getWidth() / 2 - 60, 400, activeFont);

        r.drawImageTile(planet, planetX, planetY, (int) planetTile, planetTile1);
    }

    @Override
    public void suspendState() {
        backgroundMusic.stop();
    }

    @Override
    public void resumeState() {
        backgroundMusic.loop();
        selectedItem = "play";
        planetX = GameEngine.getWindow().getWidth() / 2 - 150;
        planetY = 250;
        activeFont = fontSelected;
    }
}
