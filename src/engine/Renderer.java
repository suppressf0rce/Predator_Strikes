package engine;

import java.awt.image.DataBufferInt;

/**
 * This class represents the renderer for our game loop and Game window
 *
 * @see GameWindow
 * @see GameContainer
 */
@SuppressWarnings("WeakerAccess")
public class Renderer {

    //===>>Variables<<==//
    /**
     * Pixel width
     */
    private int pW;

    /**
     * Pixel height
     */
    private int pH;

    /**
     * Pixels array
     */
    private int[] p;

    //===>Constructor<<===//

    /**
     * Default constructor for the game renderer
     *
     * @param gc an {@link GameContainer]} so we can access game loop info
     */
    public Renderer(GameContainer gc) {
        pW = gc.getWidth();
        pH = gc.getHeight();

        //Gets the direct access of pixel data of the image raster pixel array
        p = ((DataBufferInt) gc.getWindow().getImage().getRaster().getDataBuffer()).getData();
    }


    //===>Methods<<===//
    public void clear() {
        for (int i = 0; i < p.length; i++) {
            p[i] = 0xff000000; //Alpha 255, R 0, G 0, B 0
        }
    }
}
