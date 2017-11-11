package game;

import engine.GameEngine;
import engine.GameHost;
import engine.GameState;
import engine.Renderer;
import engine.gfx.Font;
import engine.gfx.Image;
import engine.gfx.Transition;
import engine.sfx.SoundClip;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.security.Key;

public class GameOverState extends GameState {
    private Image background;
    private Font font;
    private Font selectedFont;
    private Font activeFont;

    private String selectedItem = null;

    private SoundClip backgroundMusic;

    public GameOverState(GameHost host) {
        super("gameover", host);

        background = null;
        font = new Font("res/russo_one_50pt.png", 300, 2);
        selectedFont = new Font("res/russo_one_50pt_red.png", 300, 2);

        backgroundMusic = new SoundClip("res/davy_jones_8bit.wav");
        backgroundMusic.setVolume(-5);
    }

    @Override
    public void update(float dt) {

        if (GameEngine.getInput().isKeyDown(KeyEvent.VK_ENTER) && selectedItem == null) {
            selectedItem = "play";
        }

        if (GameEngine.getInput().isKeyDown(KeyEvent.VK_ENTER)) {
            if (selectedItem.equals("play")) {
                Transition.TransitionType transType = Transition.TransitionType.values()[(int) (Math.random() * Transition.TransitionType.values().length)];
                Transition.transitionTo("play", transType, 0.5f);

                ((PlayState) GameEngine.getHost().getState("play")).getObjects().clear();
                PlayState.playerScore = 0;
                Player p = new Player(((PlayState) GameEngine.getHost().getState("play")));
                ((PlayState) GameEngine.getHost().getState("play")).getObjects().add(p);
                backgroundMusic.stop();
            }
        }
    }

    @Override
    public void render(Renderer r) {
        activeFont = font;
        if (background != null) {
            effectGrayScale();
            r.drawImage(background, 0, 0);
        }

        r.drawString("Play again", GameEngine.getWindow().getWidth() / 2 - 180, GameEngine.getWindow().getHeight() / 2 - 50, activeFont);
    }

    private void effectGrayScale() {
        if (background == null)
            return;

        for (int y = 0; y < background.getHeight(); y++) {
            for (int x = 0; x < background.getWidth(); x++) {
                int pixel = background.getPixels()[x + y * background.getWidth()];

                int red = ((pixel >> 16) & 0xff);
                int green = ((pixel >> 8) & 0xff);
                int blue = (pixel & 0xff);


                int i = (red + green + blue) / 3;
                i = (255 << 24 | i << 16 | i << 8 | i);
                System.out.println(red + " " + green + " " + blue);
                background.setPixel(x + y * background.getWidth(), i);


            }
        }
    }
    @Override
    public void suspendState() {

    }

    @Override
    public void resumeState() {
        this.background = new Image(GameEngine.getHost().renderSnapshot(null, this));
        backgroundMusic.play();
    }

    public void setBackground(Image background) {
        this.background = background;
    }
}

