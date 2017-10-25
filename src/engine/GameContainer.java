package engine;

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
public class GameContainer implements Runnable {


    //===>>Variables<<===//
    private Thread gameThread;

    private boolean running = false;
    private final double UPDATE_CAP;

    private int width = 320, height = 240;
    private float scale = 1f;
    private String title = "Simple 2D Engine";
    private int refreshRate = 60;
    private boolean capFps = false;

    /**
     * Game window of our game
     *
     * @see GameWindow
     */
    private GameWindow window;

    /**
     * Game renderer
     *
     * @see Renderer
     */
    private Renderer renderer;

    /**
     * Game input
     */
    private Input input;

    private AbstractGame game;

    //===>>Constructor<<===//

    /**
     * Global constructor for {@link GameContainer}
     * @param game an instance of {@link AbstractGame} that will be contained
     */
    public GameContainer(AbstractGame game) {
        this.game = game;

        UPDATE_CAP = 1.0 / refreshRate;
    }


    //===>>Methods<<===//
    public void start() {

        //Initialize our window
        window = new GameWindow(this);

        //Initialize game renderer
        renderer = new Renderer(this);

        //Initialize input
        input = new Input(this);

        //Initialize game thread
        gameThread = new Thread(this);
        gameThread.run();
    }

    public void stop() {

    }

    @Override
    public void run() {
        running = true;

        boolean render;

        //Variables for the system running time calculations
        double firstTime;
        double lastTime = System.nanoTime() / 1000000000.0; //Converts nano seconds into milli seconds
        double passedTime;
        double unprocessedTime = 0;

        //Variables for fps calculations
        double frameTime = 0;
        int frames = 0;
        int fps = 0;

        while (running) {
            render = !capFps;

            firstTime = System.nanoTime() / 1000000000.0;
            passedTime = firstTime - lastTime;
            lastTime = firstTime;

            unprocessedTime += passedTime;
            frameTime += passedTime;

            /* If our thread freezes for some reason and our frame rate drops bellow certain point
             * We still want to update all missed update. So for example thread freezes for the twice of the time
             * of the UPDATE_CAP, we still want to update twice, because we missed those updates, and we wanna make sure
             * we updated those
             */
            while (unprocessedTime >= UPDATE_CAP) {
                unprocessedTime -= UPDATE_CAP;
                render = true;

                game.update(this, (float) UPDATE_CAP);

                input.update();

                if (frameTime >= 1.0) {
                    frameTime = 0;
                    fps = frames;
                    frames = 0;
                }
            }

            //Some game engine optimization
            if (render) {
                renderer.clear();
                game.render(this, renderer);
                renderer.drawText("FPS: " + fps, 0, 0, null);
                window.update();
                frames++;

            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


        //Dispose the engine if not running
        dispose();
    }

    private void dispose() {

    }


    //===>>Getters & Setters<<====//
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

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public GameWindow getWindow() {
        return window;
    }

    public Input getInput() {
        return input;
    }

    public int getRefreshRate() {
        return refreshRate;
    }

    public void setRefreshRate(int refreshRate) {
        this.refreshRate = refreshRate;
    }
}
