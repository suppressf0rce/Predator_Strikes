package engine.gfx;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * This class represents the graphical images drawn in the game
 */
@SuppressWarnings("WeakerAccess")
public class Image {

    //===>>Variables<<===//
    private int width, height;
    private int[] pixels;
    private boolean alpha = false;

    //===>>Constructor<<===//

    /**
     * Constructor for the image that loads image from the path
     *
     * @param path to the image to load
     */
    public Image(String path) {
        BufferedImage image;

        //Try loading image from the path
        try {
            File imageFile = new File(path);
            image = ImageIO.read(imageFile);

            width = image.getWidth();
            height = image.getHeight();
            pixels = image.getRGB(0, 0, width, height, null, 0, width);

            image.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Image(int[] pixels, int width, int height) {
        this.pixels = pixels;
        this.width = width;
        this.height = height;
    }

    //===>>Getters & Setters<<===//
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

    public int[] getPixels() {
        return pixels;
    }

    public void setPixels(int[] pixels) {
        this.pixels = pixels;
    }

    public boolean isAlpha() {
        return alpha;
    }

    public void setAlpha(boolean alpha) {
        this.alpha = alpha;
    }
}
