package engine;


import engine.gfx.Font;
import engine.gfx.Image;
import engine.gfx.ImageTile;

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
    /**
     * This method clears the screen (Paints all pixels black)
     */
    public void clear() {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = 0xff000000; //Alpha 255, R 0, G 0, B 0
        }
    }

    /**
     * This method sets the specific pixel of the screen
     *
     * @param x     position of the pixel
     * @param y     position of the pixel
     * @param value value of the pixel in format 0xXXXXXXXX (Alpha,Red,Green,Blue)
     */
    public void setPixel(int x, int y, int value) {

        //We are using 0xFFFF00FF as our invisible color so we don't want to render it
        //it is A: 255, R: 255, G: 0, B: 255
        if ((x < 0 || x >= pW || y < 0 || y >= pH) || ((value >> 24) & 0xff) == 0x00) //Shifting for 24 bits to the right and checking if the alpha is 00
            return;

        pixels[x + y * pW] = value;
    }

    /**
     * This method draws the image on the screen
     *
     * @param image   an instance of {@link Image} that will be drawn
     * @param offsetX an offset x position where it will be drawn
     * @param offsetY an offset y position where it will be drawn
     */
    public void drawImage(Image image, int offsetX, int offsetY) {
        //Don't render
        if (offsetX < -image.getWidth()) {
            return;
        }
        if (offsetY < -image.getHeight()) {
            return;
        }
        if (offsetX >= pW) {
            return;
        }
        if (offsetY >= pH) {
            return;
        }

        int newX = 0;
        int newY = 0;
        int newWidth = image.getWidth();
        int newHeight = image.getHeight();

        //Render optimization

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

    /**
     * This method draws the single tile / sprite from {@link ImageTile}
     *
     * @param image   an instance of {@link ImageTile}
     * @param offsetX an offset x position where it will be drawn
     * @param offsetY an offset y position where it will be drawn
     * @param tileX   an tile X position from tile image matrix (indexing begins at 0)
     * @param tileY   and tile Y posit from tile image matrix (indexing begins at 0)
     */
    public void drawImageTile(ImageTile image, int offsetX, int offsetY, int tileX, int tileY) {
        //Don't render
        if (offsetX < -image.getTileWidth()) {
            return;
        }
        if (offsetY < -image.getTileHeight()) {
            return;
        }
        if (offsetX >= pW) {
            return;
        }
        if (offsetY >= pH) {
            return;
        }

        int newX = 0;
        int newY = 0;
        int newWidth = image.getTileWidth();
        int newHeight = image.getTileHeight();

        //Render optimization

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
                int pixelValue = image.getPixels()[(x + tileX * image.getTileWidth()) + (y + tileY * image.getTileHeight()) * image.getWidth()];

                //Calling set pixel method
                setPixel(x + offsetX, y + offsetY, pixelValue);
            }
        }
    }

    public void drawText(String text, int offsetX, int offsetY, Font font) {

        if (font == null)
            font = Font.STANDARD_YELLOW;

        int offset = 0;

        for (int i = 0; i < text.length(); i++) {
            int unicode = text.codePointAt(i) - 32;

            for (int y = 0; y < font.getFontImage().getHeight(); y++) {
                for (int x = 0; x < font.getWidths()[unicode]; x++) {

                    //if(font.getFontImage().getPixels()[(x + font.getOffsets()[unicode]) + y * font.getFontImage().getWidth()] == 0xff000000)
                    setPixel(x + offsetX + offset, y + offsetY, font.getFontImage().getPixels()[(x + font.getOffsets()[unicode]) + y * font.getFontImage().getWidth()]);
                }
            }

            offset += font.getWidths()[unicode];
        }
    }

    public void drawRect(int offsetX, int offsetY, int width, int height, int color) {
        for (int y = 0; y <= height; y++) {
            setPixel(offsetX, y + offsetY, color);
            setPixel(offsetX + width, y + offsetY, color);
        }

        for (int x = 0; x <= width; x++) {
            setPixel(x + offsetX, offsetY, color);
            setPixel(x + offsetX, height + offsetY, color);
        }
    }

    public void drawFillRect(int offsetX, int offsetY, int width, int height, int color) {
        //Don't render
        if (offsetX < -width) {
            return;
        }
        if (offsetY < -height) {
            return;
        }
        if (offsetX >= pW) {
            return;
        }
        if (offsetY >= pH) {
            return;
        }

        int newX = 0;
        int newY = 0;
        int newWidth = width;
        int newHeight = height;

        //Render optimization

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
        for (int y = newY; y <= newHeight; y++) {
            for (int x = newX; x <= newWidth; x++) {
                setPixel(x + offsetX, y + offsetY, color);
            }
        }
    }


    //===>>Getters & Setters<<===//
}
