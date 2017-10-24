package engine;

import engine.gfx.Image;

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
    private int[] pixels;

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
        pixels = ((DataBufferInt) gc.getWindow().getImage().getRaster().getDataBuffer()).getData();
    }


    //===>Methods<<===//
    public void clear() {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = 0xff000000; //Alpha 255, R 0, G 0, B 0
        }
    }

    public void setPixel(int x, int y, int value) {

        //We are using 0xFFFF00FF as our invisible color so we don't want to render it
        //it is A: 255, R: 255, G: 0, B: 255
        if ((x < 0 || x >= pW || y < 0 || y >= pH) || ((value >> 24) == 0x00)) //Shifting for 24 bits to the right and checking if the alpha is 00
            return;

        pixels[x + y * pW] = value;
    }

    public void drawImage(Image image, int offsetX, int offsetY) {
        int newX = 0;
        int newY = 0;
        int newWidth = image.getWidth();
        int newHeight = image.getHeight();

        //Render optimization

        //Don't render
        if (offsetX < -newWidth) {
            return;
        }
        if (offsetY < -newHeight) {
            return;
        }
        if (offsetX >= pW) {
            return;
        }
        if (offsetY >= pH) {
            return;
        }

        //Clipping code so we render o nly visible part of the image
        if (offsetX < 0) {
            newX -= offsetX;
        }
        if (offsetY < 0) {
            newY -= offsetY;
        }
        if (newWidth + offsetX > pW) {
            newWidth -= newWidth + offsetX - pW;
        }
        if (newHeight + offsetY > pH) {
            newHeight -= newHeight + offsetY - pH;
        }

        for (int y = newY; y < newHeight; y++) {
            for (int x = newX; x < newWidth; x++) {

                //Getting pixel from the image at x and y Position
                int pixelValue = image.getPixels()[x + y * image.getWidth()];

                //Calling set pixel method
                setPixel(x + offsetX, y + offsetY, pixelValue);
            }
        }
    }
}
