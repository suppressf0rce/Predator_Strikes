package engine;

import java.awt.event.*;

/**
 * An input class that manages keyboard strokes, mouse buttons, mouse motion etc...
 */
@SuppressWarnings("WeakerAccess")
public class Input implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    //===>>Variables<<===//

    private final int NUM_KEYS = 256;
    private final int NUM_BUTTONS = 5;

    private boolean[] keys = new boolean[NUM_KEYS];
    private boolean[] keysLast = new boolean[NUM_KEYS];
    private boolean[] buttons = new boolean[NUM_BUTTONS];
    private boolean[] buttonsLast = new boolean[NUM_BUTTONS];

    private int mouseX, mouseY;
    private int prevMouseX, prevMouseY;
    private int mouseDx, mouseDy;

    private int wheel;

    private char keyTyped;

    //===>>Constructor<<===//

    /**
     * Default constructor for the input listener
     */
    public Input() {

        GameEngine.getWindow().getCanvas().addKeyListener(this);
        GameEngine.getWindow().getCanvas().addMouseListener(this);
        GameEngine.getWindow().getCanvas().addMouseMotionListener(this);
        GameEngine.getWindow().getCanvas().addMouseWheelListener(this);

        mouseX = 0;
        mouseY = 0;
        prevMouseX = 0;
        prevMouseY = 0;
        mouseDx = 0;
        mouseDy = 0;
    }

    //===>>Methods<<===//
    public void update() {

        System.arraycopy(keys, 0, keysLast, 0, NUM_KEYS);
        System.arraycopy(buttons, 0, buttonsLast, 0, NUM_BUTTONS);

        mouseDx = prevMouseX - mouseX;
        mouseDy = prevMouseY - mouseY;

        prevMouseX = mouseX;
        prevMouseY = mouseY;

        keyTyped = 0;
        wheel = 0;
    }

    public boolean isKey(int keyCode) {
        return keys[keyCode];
    }

    public boolean isKeyDown(int keyCode) {
        return keys[keyCode] && !keysLast[keyCode];
    }

    public boolean isKeyUp(int keyCode) {
        return !keys[keyCode] && keysLast[keyCode];
    }

    public boolean isButton(int button) {
        return buttons[button];
    }

    public boolean isButtonDown(int button) {
        return buttons[button] && !buttonsLast[button];
    }

    public boolean isButtonUp(int button) {
        return !buttons[button] && buttonsLast[button];
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public int getMouseDx() {
        return mouseDx;
    }

    public int getMouseDy() {
        return mouseDy;
    }

    public char getKeyTyped() {
        return keyTyped;
    }

    public int getWheel() {
        return wheel;
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        keyTyped = ke.getKeyChar();
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        if (ke.getKeyCode() >= NUM_KEYS)
            return;
        keys[ke.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        if (ke.getKeyCode() >= NUM_KEYS)
            return;
        keys[ke.getKeyCode()] = false;
    }

    @Override
    public void mouseClicked(MouseEvent me) {
    }

    @Override
    public void mousePressed(MouseEvent me) {
        if (me.getButton() >= NUM_BUTTONS)
            return;
        buttons[me.getButton()] = true;
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        if (me.getButton() >= NUM_BUTTONS)
            return;
        buttons[me.getButton()] = false;
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        mouseX = (int) (me.getX() / GameEngine.getWindow().getScale());
        mouseY = (int) (me.getY() / GameEngine.getWindow().getScale());
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        mouseX = (int) (me.getX() / GameEngine.getWindow().getScale());
        mouseY = (int) (me.getY() / GameEngine.getWindow().getScale());
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent mwe) {
        wheel = mwe.getWheelRotation();
    }

}
