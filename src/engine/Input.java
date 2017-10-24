package engine;

import java.awt.event.*;

/**
 * An input class that manages keyboard strokes, mouse buttons, mouse motion etc...
 */
@SuppressWarnings("WeakerAccess")
public class Input implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {


    private final int NUM_KEYS = 256;
    private final int NUM_BUTTONS = 5;
    //===>>Variables<<===//
    private GameContainer gc;
    private boolean[] keys = new boolean[NUM_KEYS];
    private boolean[] keysLast = new boolean[NUM_KEYS];
    private boolean[] buttons = new boolean[NUM_BUTTONS];
    private boolean[] buttonsLast = new boolean[NUM_BUTTONS];

    private int mouseX, mouseY;
    private int scroll;

    //===>>Constructor<<===//

    /**
     * Default constructor for the input listener
     *
     * @param gc an instance of the {@link GameContainer} so we can access game info
     */
    public Input(GameContainer gc) {
        this.gc = gc;
        mouseX = 0;
        mouseY = 0;
        scroll = 0;

        //Add listeners to canvas
        gc.getWindow().getCanvas().addKeyListener(this);
        gc.getWindow().getCanvas().addMouseMotionListener(this);
        gc.getWindow().getCanvas().addMouseListener(this);
        gc.getWindow().getCanvas().addMouseWheelListener(this);
    }

    //===>>Methods<<===//
    public void update() {

        scroll = 0;

        //Save last keys of the keyboard
        System.arraycopy(keys, 0, keysLast, 0, NUM_KEYS);

        //Save last buttons of the mouse
        System.arraycopy(buttons, 0, buttonsLast, 0, NUM_BUTTONS);
    }

    public boolean isKey(int keyCode) {
        return keys[keyCode];
    }

    public boolean isKeyUp(int keyCode) {
        return !keys[keyCode] && keysLast[keyCode];
    }

    public boolean isKeyDown(int keyCode) {
        return keys[keyCode] && !keysLast[keyCode];
    }


    public boolean isButton(int button) {
        return buttons[button];
    }

    public boolean isButtonUp(int button) {
        return !buttons[button] && buttonsLast[button];
    }

    public boolean isButtonDown(int button) {
        return buttons[button] && !buttonsLast[button];
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        buttons[e.getButton()] = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        buttons[e.getButton()] = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = (int) (e.getX() / gc.getScale());
        mouseY = (int) (e.getY() / gc.getScale());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = (int) (e.getX() / gc.getScale());
        mouseY = (int) (e.getY() / gc.getScale());
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        scroll = e.getWheelRotation();
    }

    //===>>Getters & Setters<<===//
    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public int getScroll() {
        return scroll;
    }
}
