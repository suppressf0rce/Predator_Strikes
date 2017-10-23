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

    private int width, height;
    private float scale;
    private String title;

    /**
     * Game window of our game
     *
     * @see GameWindow
     */
    private GameWindow window;

    //===>>Constructor<<===//

    /**
     * Global constructor for {@link GameContainer}
     *
     * @param refreshRate the actual refresh rate of the game
     */
    public GameContainer(int refreshRate, int width, int height, float scale, String title) {
        UPDATE_CAP = 1.0 / refreshRate;
        this.width = width;
        this.height = height;
        this.title = title;
        this.scale = scale;
    }


    //===>>Methods<<===//
    public void start() {

        //Initialize our window
        window = new GameWindow(this);

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
        int fps;

        while (running) {
            render = false;

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

                if (frameTime >= 1.0) {
                    frameTime = 0;
                    fps = frames;
                    frames = 0;

                    System.out.println("FPS: " + fps);
                }
                //TODO: Update the game
            }

            //Some game engine optimization
            if (render) {
                //TODO: Render game
                frames++;
                window.update();

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
}
