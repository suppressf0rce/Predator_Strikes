package engine;

import java.awt.*;
import java.awt.image.*;
import java.util.HashMap;
import java.util.Properties;

/**
 * Main object for complex application, which will have more different states.
 * Needs to be usually instanced once, this object is owner of all states.
 * It can freely change the active states, which will then user for the drawing.
 *
 * @author Aleksandar
 */
public class GameHost {

    static {
        System.setProperty("sun.java2d.transaccel", "True");
        //   System.setProperty("sun.java2d.opengl", "True");
        System.setProperty("sun.java2d.ddforcevram", "True");
    }

    private Color backColor = Color.BLACK;
    private boolean clearBackBuffer = true;

    private GameState currentState = null;
    private GameState nextState = null;

    private HashMap<String, GameState> states = new HashMap<String, GameState>();


    /**
     * Returns registered states by the name
     *
     * @param name name which asked state returns with GameState.getName() function
     * @return reference to state if its found null if its not
     */
    public GameState getState(String name) {
        return states.get(name);
    }

    /**
     * Changing to the next state by the reference
     *
     * @param nextState reference to the GameState object, which needs to be registered
     */
    public void setState(GameState nextState) {
        if (nextState == null) return;
        if (currentState == nextState) return;

        this.nextState = nextState;
    }

    /**
     * Changing the next state by the name
     *
     * @param stateName name of the state
     */
    public void setState(String stateName) {
        setState(getStateByName(stateName));
    }

    /**
     * Reference to the current active state
     *
     * @return reference to the {@link GameState} object
     */
    public GameState getCurrentState() {
        return currentState;
    }

    /**
     * Searching for reference to any of the registered states
     *
     * @param name name of the state, how it returns his getName() method
     * @return reference to the object, or null if its not found
     */
    public GameState getStateByName(String name) {
        return states.get(name);
    }

    /**
     * Registering state to this host (* shouldn't be done manually *)
     * Registering is being done in constructor of the state
     *
     * @param state reference to the state which is being registered
     */
    public void registerState(GameState state) {
        if (state == null)
            return;

        if (!states.containsValue(state))
            states.put(state.getName(), state);
    }

    public void tick(float dt) {
        if (nextState != null) {
            if (currentState != null) currentState.suspendState();

            currentState = nextState;
            nextState = null;

            currentState.resumeState();
        }

        if (currentState != null) currentState.update(dt);
    }

    public void render(Renderer r) {
        if (currentState == null)
            return;
        else
            currentState.render(r);
    }


    public void initCurrentState() {


        if (nextState != null) {
            if (currentState != null) currentState.suspendState();

            currentState = nextState;
            nextState = null;

            currentState.resumeState();
        }

    }


    /**
     * Method which will draw current state, but in the off-screen image, instead of the screen.
     * Notice: try not to call this method from render() of the same state as it will cause infinite loop

     *
     * @param canvas Object of the picture that will be drawn on the screen, can be null
     * @param state  state that needs to be drawn, his render method will be called
     * @return returns the reference to the newly created buffered image
     */
    public BufferedImage renderSnapshot(BufferedImage canvas, GameState state) {
        if (canvas == null)
            canvas = new BufferedImage(GameEngine.getWindow().getWidth(), GameEngine.getWindow().getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = (Graphics2D) canvas.getGraphics();

        if (clearBackBuffer) {
            g.setColor(backColor);
            g.fillRect(0, 0, GameEngine.getWindow().getWidth(), GameEngine.getWindow().getHeight());
        }


        Renderer r = new Renderer();
        r.setPixels(canvas.getRGB(0, 0, GameEngine.getWindow().getWidth(), GameEngine.getWindow().getHeight(), null, 0, GameEngine.getWindow().getWidth()));

        if (state != null)
            state.render(r);


        return getBufferedImage(r.getPixels(), GameEngine.getWindow().getWidth(), GameEngine.getWindow().getHeight(), true);
    }

    private BufferedImage getBufferedImage(int data[], int width, int height, boolean hasAlpha) {
        ColorModel colorModel;
        WritableRaster raster;
        DataBufferInt buffer = new DataBufferInt(data, width * height);
        if (hasAlpha) {
            colorModel = new DirectColorModel(32, 0x00ff0000, 0x0000ff00, 0x000000ff, 0xff000000);
            raster = WritableRaster.createPackedRaster(buffer, width, height, width, new int[]{0x00ff0000, 0x0000ff00, 0x000000ff, 0xff000000}, null);
        } else {
            colorModel = new DirectColorModel(24, 0x00ff0000, 0x0000ff00, 0x000000ff);
            raster = WritableRaster.createPackedRaster(buffer, width, height, width, new int[]{0x00ff0000, 0x0000ff00, 0x000000ff}, null);
        }
        return new BufferedImage(colorModel, raster, colorModel.isAlphaPremultiplied(), new Properties());
    }

    public void setClearBackBuffer(boolean clearBackBuffer) {
        this.clearBackBuffer = clearBackBuffer;
    }
}