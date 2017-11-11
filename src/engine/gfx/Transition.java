package engine.gfx;

import engine.GameEngine;
import engine.GameHost;
import engine.GameState;
import engine.Renderer;

import java.awt.*;
import java.awt.image.BufferedImage;

// Posebno stanje za animirane tranzicije
public class Transition extends GameState {
    // Static instance this class needs to be singleton
    private static Transition instance;
    // Image that will be rendered on screen
    BufferedImage imgToRender;
    // Two pictures in which we keep snapshot of the states, current and next
    private BufferedImage startImage = null;
    private BufferedImage endImage = null;
    // Animation status, from 0 to 1
    private float position = 0.0f;
    private float speed = 0.02f;
    ;
    // Currently selected transition effect
    private TransitionType type = TransitionType.Crossfade;

    // State that needs to be switched to after the transition effect
    private GameState nextState = null;

    public Transition(GameHost host) {
        super("transition", host);

        // We are instancing pictures earlier because they will always be same dimensions and we won't
        // resize them, we are using RGB w/o Alpha channel
        startImage = new BufferedImage(GameEngine.getWindow().getWidth(), GameEngine.getWindow().getHeight(), BufferedImage.TYPE_INT_RGB);
        endImage = new BufferedImage(GameEngine.getWindow().getWidth(), GameEngine.getWindow().getHeight(), BufferedImage.TYPE_INT_RGB);
        imgToRender = new BufferedImage(GameEngine.getWindow().getWidth(), GameEngine.getWindow().getHeight(), BufferedImage.TYPE_INT_RGB);

        instance = this;
    }

    // Static call, because of easier usability, it is gonna start process of transitions animations
    public static void transitionTo(String nextStateName, TransitionType type, float seconds) {
        // Searching state by the name
        instance.nextState = instance.host.getState(nextStateName);

        // If there is no next state, we don't have to do anything
        if (instance.nextState == null) return;

        // Position for transition at the beginning is 0
        instance.position = 0.0f;

        // Writing the type of transition
        instance.type = type;

        //Calculating what increment needs to be, so we could assign the number of
        // secs and current fps to come from 0 to 1
        instance.speed = 1.0f / (seconds * GameEngine.getFPS());

        // Asking for current state to be drawn into start image
        instance.startImage = instance.host.getCurrentState().renderSnapshot(instance.startImage);

        // Asking next state to be drawn into end image
        instance.endImage = instance.nextState.renderSnapshot(instance.endImage);

        // And now we are switching to the transition phase which means that it updates itself
        // And drawing is being made only in this state, until we pass the
        // control to other state (which happens when the position hits the 1)
        instance.host.setState(instance);
    }

    @Override
    public String getName() {
        return "transition";
    }

    @Override
    public void resumeState() {
        // Some of the transitions will remove the background so we are setting it to black
        GameEngine.getRenderer().setClearBackground(true);
        GameEngine.getHost().setClearBackBuffer(true);
    }

    @Override
    public void suspendState() {
        GameEngine.getRenderer().setClearBackground(false);
        GameEngine.getHost().setClearBackBuffer(false);
    }

    public void renderAnimation(Graphics2D g) {
        switch (type) {
            case Crossfade:
                // Starting picture is being drawn totally visible
                g.drawImage(startImage, 0, 0, null);

                // Another picture is being drawn with alpha transparency
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, position));
                g.drawImage(endImage, 0, 0, null);
                break;

            case SwipeLeft:
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, GameEngine.getWindow().getWidth(), GameEngine.getWindow().getHeight());
                // Starting picture is becoming smaller and starts to move left getting out of the screen
                g.drawImage(startImage,
                        (int) (0 - position * (GameEngine.getWindow().getWidth())),
                        (int) (position * GameEngine.getWindow().getHeight() * 0.25f),
                        (int) (GameEngine.getWindow().getWidth() * (1.0f - position * 0.5f)),
                        (int) (GameEngine.getWindow().getHeight() * (1.0f - position * 0.5f)),
                        null);

                // Krajnja slika se povecava i pomijera lijevo, ulazeci u ekran
                g.drawImage(endImage,
                        (int) (GameEngine.getWindow().getWidth() - position * (GameEngine.getWindow().getWidth())),
                        (int) ((1.0f - position) * GameEngine.getWindow().getHeight() * 0.25f),
                        (int) (GameEngine.getWindow().getWidth() * (0.5f + position * 0.5f)),
                        (int) (GameEngine.getWindow().getHeight() * (0.5f + position * 0.5f)),
                        null);
                break;

            case SwipeRight:
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, GameEngine.getWindow().getWidth(), GameEngine.getWindow().getHeight());
                g.drawImage(startImage,
                        (int) (0 + position * (GameEngine.getWindow().getWidth())),
                        (int) (position * GameEngine.getWindow().getHeight() * 0.25f),
                        (int) (GameEngine.getWindow().getWidth() * (1.0f - position * 0.5f)),
                        (int) (GameEngine.getWindow().getHeight() * (1.0f - position * 0.5f)),
                        null);
                g.drawImage(endImage,
                        (int) (-(1.0f - position) * (GameEngine.getWindow().getWidth())),
                        (int) ((1.0f - position) * GameEngine.getWindow().getHeight() * 0.25f),
                        (int) (GameEngine.getWindow().getWidth() * (0.5f + position * 0.5f)),
                        (int) (GameEngine.getWindow().getHeight() * (0.5f + position * 0.5f)),
                        null);
                break;

            case ZoomIn:
                // First picture is drawing 1:1 absolutely visible
                g.drawImage(startImage, 0, 0, null);

                // Second picture is starting to become visible and becoming bigger from the center of screen
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, position));
                g.drawImage(endImage,
                        (int) ((1.0f - position) * (GameEngine.getWindow().getWidth() / 2)),
                        (int) ((1.0f - position) * (GameEngine.getWindow().getHeight() / 2)),
                        (int) (position * GameEngine.getWindow().getWidth()),
                        (int) (position * GameEngine.getWindow().getHeight()),
                        null);
                break;

            case ZoomOut:
                // Other picture is 1:1 background
                g.drawImage(endImage, 0, 0, null);

                // Over it we are drawing picture which is scaling a little bit every time and begins to be transparent
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f - position));
                g.drawImage(startImage,
                        (int) (position * (GameEngine.getWindow().getWidth() / 2)),
                        (int) (position * (GameEngine.getWindow().getHeight() / 2)),
                        (int) ((1.0f - position) * GameEngine.getWindow().getWidth()),
                        (int) ((1.0f - position) * GameEngine.getWindow().getHeight()),
                        null);
                break;

            case None:
                break;
        }

    }

    @Override
    public void update(float dt) {
        // In every step incrementing position for the speed value
        position += speed;

        // If we happened to come to 1, we need to change the state
        if (position >= 1.0f) {
            //  Alpha bigger than 1 creates exceptions so we are handling it
            position = 1.0f;

            // Changing into the next state
            host.setState(nextState);
        }
    }

    @Override
    public void render(Renderer r) {
        Graphics2D g = (Graphics2D) imgToRender.getGraphics();
        renderAnimation(g);

        r.drawImage(new Image(imgToRender), 0, 0);

    }

    // Currently implemented versions of the effects
    public static enum TransitionType {
        Crossfade,
        ZoomIn,
        ZoomOut,
        SwipeLeft,
        SwipeRight,
        None
    }

}
