package engine;


import engine.gfx.Font;
import engine.gfx.Image;
import engine.gfx.ImageRequest;
import engine.gfx.ImageTile;

import java.awt.*;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

/**
 * This class represents the renderer for our game loop and Game window
 *
 * @see GameWindow
 * @see GameEngine
 */
@SuppressWarnings({"WeakerAccess", "NumericOverflow"})
public class Renderer {

    boolean processing = true;

    private int[] pixels;
    //===>>Variables<<==//
    private Font font = Font.STANDARD;
    private int[] depthBuffer;
    private int width, height, depth;

    private ArrayList<ImageRequest> imageRequest = new ArrayList<>();
    private int limX, limY, limW, limH;

    private int[] lightMap;

    private boolean clearBackground = false;
    //===>Constructor<<===//

    /**
     * Default constructor for the game renderer
     */
    public Renderer() {
        width = GameEngine.getWindow().getWidth();
        height = GameEngine.getWindow().getHeight();
        pixels = ((DataBufferInt) GameEngine.getWindow().getImage().getRaster().getDataBuffer()).getData();

        depthBuffer = new int[pixels.length];
        lightMap = new int[pixels.length];

        limX = 0;
        limY = 0;
        limW = width;
        limH = height;
    }


    //===>Methods<<===//

    /**
     * This method clears the buffer
     */
    public void clear() {
        for (int i = 0; i < pixels.length; i++) {
            //pixels[i] = 0xff000000; //Alpha 255, R 0, G 0, B 0
            depthBuffer[i] = 0;
        }
    }

    /**
     * This method clears the buffer
     */
    public void clearBackground() {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = 0xff000000; //Alpha 255, R 0, G 0, B 0
            depthBuffer[i] = 0;
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
            setDepth(ir.zDepth);
            drawImage(ir.image, ir.offsetX, ir.offsetY);
        }

//        for (int i = 0; i < pixels.length; i++) {
//            float r = ((lightMap[i] >> 16) & 0xff) / 255f;
//            float g = ((lightMap[i] >> 8) & 0xff) / 255f;
//            float b = (lightMap[i] & 0xff) / 255f;
//
//            //System.out.println((lightMap[i] & 0xff) / 255f);
//            //System.out.println(String.format("%08x", lightMap[i]));
//            //System.out.println(pixels[i]);
//            //System.out.println("R:"+r+"G:"+g+"B:"+b);
//
//            pixels[i] = ((int) (((pixels[i] >> 16) & 0xff) * r) << 16 | (int) (((pixels[i] >> 8) & 0xff) * g) << 8 | (int) ((pixels[i] & 0xff) * b));
//
//            //System.out.println(pixels[i]);
//        }

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
        if ((x < limX || x >= limX + limW || y < limY || y >= limY + limH) || alpha == 0) //Shifting for 24 bits to the right and checking if the alpha is 00
            return;

        int index = x + y * width;

        if (depthBuffer[index] > depth)
            return;

        depthBuffer[index] = depth;

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

    public void setLightMap(int x, int y, int value) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return;
        }

        int baseColor = lightMap[x + y * width];

        int maxRed = Math.max((baseColor >> 16) & 0xff, (value >> 16) & 0xff);
        int maxGreen = Math.max((baseColor >> 8) & 0xff, (value >> 8) & 0xff);
        int maxBlue = Math.max(baseColor & 0xff, (value & 0xff));

        lightMap[x + y * width] = (maxRed << 16 | maxGreen << 8 | maxBlue);

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
            imageRequest.add(new ImageRequest(image, depth, offsetX, offsetY));
            return;
        }

        //Don't render
        if (offsetX < -image.getWidth()) {
            return;
        }
        if (offsetY < -image.getHeight()) {
            return;
        }
        if (offsetX >= width) {
            return;
        }
        if (offsetY >= height) {
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
        if (newWidth + offsetX > width) {
            newWidth -= newWidth + offsetX - width;
        }
        if (newHeight + offsetY > height) {
            newHeight -= newHeight + offsetY - height;
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
            imageRequest.add(new ImageRequest(image.getTileImage(tileX, tileY), depth, offsetX, offsetY));
            return;
        }

        //Don't render
        if (offsetX < -image.getTileWidth()) {
            return;
        }
        if (offsetY < -image.getTileHeight()) {
            return;
        }
        if (offsetX >= width) {
            return;
        }
        if (offsetY >= height) {
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
        if (newWidth + offsetX > width) {
            newWidth -= newWidth + offsetX - width;
        }
        if (newHeight + offsetY > height) {
            newHeight -= newHeight + offsetY - height;
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
    public void drawString(String text, int offsetX, int offsetY, Font font) {

        if (font == null)
            font = this.font;

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

    public void drawParticles(Image background, int particle_grid[][]) {
        // going through each entry in the matix, and whenever '5' ( particle entry) has been found, a white pixel
        // is being written in the pixels of the renderer

        for (int y = 0; y < background.getHeight(); y++) {
            for (int x = 0; x < background.getWidth(); x++) {
                if (particle_grid[y][x] == 5) {
                    Color c = new Color(255, 255, 255);
                    int rgb = c.getRGB();
                    GameEngine.getRenderer().setPixel(x, y, rgb);
                }
            }
        }


    }

    /**
     * This method draws a line from x, y position to x1,y2 position
     *
     * @param x1    an x position from which drawing line will start
     * @param y1    an y position from which drawing line will start
     * @param x2    an x position to which line will be drawn
     * @param y2    an y position to which line will be drawn
     * @param color an color of the line
     */
    public void drawLine(int x1, int y1, int x2, int y2, Color color) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;

        int err = dx - dy;

        while (true) {
            setPixel(x1, y1, color.getRGB());

            if (x1 == x2 && y1 == y2) {
                break;
            }

            int e2 = 2 * err;

            if (e2 > -dy) {
                err = err - dy;
                x1 = x1 + sx;
            }

            if (e2 < dx) {
                err = err + dx;
                y1 = y1 + sy;
            }
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
        if (offsetX < -this.width) {
            return;
        }
        if (offsetY < -this.height) {
            return;
        }
        if (offsetX >= this.width) {
            return;
        }
        if (offsetY >= this.height) {
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
        if (newWidth + offsetX > this.width) {
            newWidth -= newWidth + offsetX - this.width;
        }
        if (newHeight + offsetY > this.height) {
            newHeight -= newHeight + offsetY - this.height;
        }
        for (int y = newY; y < newHeight; y++) {
            for (int x = newX; x < newWidth; x++) {
                setPixel(x + offsetX, y + offsetY, color);
            }
        }
    }


    //===>>Getters & Setters<<===//
    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void setLimits(int limX, int limY, int limW, int limH) {
        this.limX = limX;
        this.limY = limY;
        this.limW = limW;
        this.limH = limH;
    }

    public void resetLimit() {
        limX = 0;
        limY = 0;
        limW = width;
        limH = height;
    }

    public boolean isClearBackground() {
        return clearBackground;
    }

    public void setClearBackground(boolean clearBackground) {
        this.clearBackground = clearBackground;
    }

    public int[] getPixels() {
        return pixels;
    }

    public void setPixels(int[] pixels) {
        this.pixels = pixels;
    }
}
