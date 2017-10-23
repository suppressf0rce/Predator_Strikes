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
public class GameContainer implements Runnable {

    private final double UPDATE_CAP;
    //===>>Variables<<===//
    private Thread gameThread;
    private boolean running = false;

    //===>>Constructor<<===//

    /**
     * Global constructor for {@link GameContainer}
     *
     * @param refreshRate the actual refresh rate of the game
     */
    public GameContainer(int refreshRate) {
        UPDATE_CAP = 1.0 / refreshRate;
    }


    //===>>Methods<<===//
    public void start() {
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

                frames++;
                //TODO: Render game

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
}
