package engine;

import java.awt.event.KeyEvent;

/**
 * This class contains our game
 * <p>
 * The way this engine is going to be designed is separate from the game
 * This engine can be used multiple times. Its going to be self independent
 * and not meshed with the game.
 *
 * This class implements
 *
 * @see Runnable
 * @version 0.1
 * @author suppressf0rce
 */
@SuppressWarnings("WeakerAccess")
public class GameEngine implements Runnable {


    //===>>Variables<<===//
    private static GameWindow window;
    private static Renderer renderer;
    private static Input input;
    private static GameState state;

    private static volatile boolean running = false; //Control the games main loop
    private static double FRAME_CAP = 1.0 / 60.0;    //Time frame for 60 frames every one second.
    private static boolean debug = false;



    //===>>Constructor<<===//
    /**
     * Global constructor for {@link GameEngine}
     * @param state an instance of {@link GameState} that tells on which state game is
     */
    public GameEngine(GameState state) {
        GameEngine.state = state;
    }

    //===>>Getters & Setters<<====//
    public static GameWindow getWindow() {
        return window;
    }

    public static boolean isRunning() {
        return running;
    }

    public static Renderer getRenderer() {
        return renderer;
    }

    public static Input getInput() {
        return input;
    }

    public static GameState getState() {
        return state;
    }

    public static void setDebug(boolean debug) {
        GameEngine.debug = debug;
    }

    //===>>Methods<<===//
    public void start() {

        window = new GameWindow();
        renderer = new Renderer();
        input = new Input();

        Thread thread = new Thread(this);
        thread.run();
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        running = true;

        boolean render;

        double firstTime;
        double lastTime = System.nanoTime() / 1000000000.0;
        double passedTime;
        double unprocessedTime = 0;
        double frameTime = 0;
        int frames = 0;
        int fps = 0;

        state.init();

        while (running) {
            render = false;
            firstTime = System.nanoTime() / 1000000000.0;
            passedTime = firstTime - lastTime;
            lastTime = firstTime;

            unprocessedTime += passedTime;
            frameTime += passedTime;

            while (unprocessedTime >= FRAME_CAP) {
                unprocessedTime -= FRAME_CAP;
                render = true;

                if (input.isKeyUp(KeyEvent.VK_F1)) {
                    debug = !debug;
                }

                state.update((float) FRAME_CAP);
                input.update();

                if (frameTime >= 1.0) {
                    frameTime = 0;
                    fps = frames;
                    frames = 0;
                }
            }

            if (render) {
                renderer.clear();
                state.render(renderer);
                if (debug)
                    renderer.drawString("FPS: " + fps, 0xffffffff, 0, null);

                window.update();
                frames++;

            } else {
                try {
                    Thread.sleep(1);
                } catch (Exception ignored) {
                }
            }
        }

        cleanUp();
    }

    public void cleanUp() {
        window.dispose();
    }
}
