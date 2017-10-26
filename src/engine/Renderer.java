package engine;


import engine.gfx.Font;
import engine.gfx.Image;
import engine.gfx.ImageRequest;
import engine.gfx.ImageTile;

import java.awt.image.DataBufferInt;
import java.util.ArrayList;

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
    /**
     * Z axis buffer (image stacking on top of each other)
     */
    private int[] zBuffer;
    private int zDepth = 0;
    private boolean processing = false;
    private ArrayList<ImageRequest> imageRequest = new ArrayList<>();

    /**
     * A Light map of our lighting pixels
     */
    private int[] lightMap;

    /**
     * An int array which tells if pixel blocks the lighting
     */
    private int[] lightBlock;

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

        zBuffer = new int[pixels.length];
        lightMap = new int[pixels.length];
        lightBlock = new int[pixels.length];
    }


    //===>Methods<<===//
    /**
     * This method clears the screen (Paints all pixels black)
     */
    public void clear() {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = 0xff000000; //Alpha 255, R 0, G 0, B 0
            zBuffer[i] = 0;
        }
    }

    /**
     * Processes which object will be rendered first and which will be rendered last
     */
    public void process() {
        processing = true;

        imageRequest.sort((i0, i1) -> {
            if (i0.zDepth < i1.zDepth) {
                return -1;
            } else if (i0.zDepth > i1.zDepth) {
                return 1;
            }
            return 0;
        });

        for (ImageRequest ir : imageRequest) {
            setzDepth(ir.zDepth);
            drawImage(ir.image, ir.offsetX, ir.offsetY);
        }
        imageRequest.clear();
        processing = false;
    }

    /**
     * This method sets the specific pixel of the screen
     *
     * @param x     position of the pixel
     * @param y     position of the pixel
     * @param value value of the pixel in format 0xXXXXXXXX (Alpha,Red,Green,Blue)
     */
    public void setPixel(int x, int y, int value) {

        int alpha = ((value >> 24) & 0xff);
        //We are using 0xFFFF00FF as our invisible color so we don't want to render it
        //it is A: 255, R: 255, G: 0, B: 255
        if ((x < 0 || x >= pW || y < 0 || y >= pH) || alpha == 0) //Shifting for 24 bits to the right and checking if the alpha is 00
            return;

        int index = x + y * pW;

        if (zBuffer[index] > zDepth)
            return;

        zBuffer[index] = zDepth;

        if (alpha == 255) {
            pixels[index] = value;
        } else {
            //Alpha is not 255 and we have transparent pixel

            //Some mambo jambo jimble jumble jet bullshit goin on here. #Urke approves
            int pixelColor = pixels[index];

            //Blending colors
            //Will comment out later what does this math quotation means
            int newRed = ((pixelColor >> 16) & 0xff) - (int) ((((pixelColor >> 16) & 0xff) - ((value >> 16) & 0xff)) * (alpha / 255f));
            int newGreen = ((pixelColor >> 8) & 0xff) - (int) ((((pixelColor >> 8) & 0xff) - ((value >> 8) & 0xff)) * (alpha / 255f));
            int newBlue = (pixelColor & 0xff) - (int) (((pixelColor & 0xff) - (value & 0xff)) * (alpha / 255f));


            //noinspection NumericOverflow
            pixels[index] = (255 << 24 | newRed << 16 | newGreen << 8 | newBlue);
        }
    }

    /**
     * This method draws the image on the screen
     *
     * @param image   an instance of {@link Image} that will be drawn
     * @param offsetX an offset x position where it will be drawn
     * @param offsetY an offset y position where it will be drawn
     */
    public void drawImage(Image image, int offsetX, int offsetY) {
        if (image.isAlpha() && !processing) {
            imageRequest.add(new ImageRequest(image, zDepth, offsetX, offsetY));
            return;
        }

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
        if (image.isAlpha() && !processing) {
            imageRequest.add(new ImageRequest(image.getTileImage(tileX, tileY), zDepth, offsetX, offsetY));
            return;
        }

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

    /**
     * Draws text to the screen
     *
     * @param text    an String of the text that will be drawn
     * @param offsetX x position on the screen where drawing will start
     * @param offsetY y position on the screen where drawing will start
     * @param font    an instance of {@link Font} which will be used for drawing
     */
    public void drawText(String text, int offsetX, int offsetY, Font font) {

        if (font == null)
            font = Font.STANDARD;

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

    /**
     * This method draws empty rectangle on the screen
     *
     * @param offsetX an x position where rectangle will begin its drawing
     * @param offsetY an y position where rectangle will begin its drawing
     * @param width   an width of the rectangle
     * @param height  an height of the rectangle
     * @param color   represents the color for rect lines
     */
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

    /**
     * This method draws an filled rectangle on the screen
     *
     * @param offsetX an x position where rectangle will begin its drawing
     * @param offsetY an y position where rectangle will begin its drawing
     * @param width   an width of the rectangle
     * @param height  an height of the rectangle
     * @param color   represents the color for rectangle
     */
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
        for (int y = newY; y < newHeight; y++) {
            for (int x = newX; x < newWidth; x++) {
                setPixel(x + offsetX, y + offsetY, color);
            }
        }
    }


    //===>>Getters & Setters<<===//
    public int getzDepth() {
        return zDepth;
    }

    public void setzDepth(int zDepth) {
        this.zDepth = zDepth;
    }
}
