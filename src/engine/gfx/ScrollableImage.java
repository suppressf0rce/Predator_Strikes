package engine.gfx;

import java.awt.image.BufferedImage;

// This class represents an image which will be visually 'scrolled' on the screen.
public class ScrollableImage extends Image {

    public ScrollableImage(String path) {
        super(path);
    }

    public ScrollableImage(BufferedImage image) {
        super(image);
    }

    public ScrollableImage(int[] pixels, int width, int height) {
        super(pixels, width, height);
    }

    public ScrollableImage(Image image) {
        super(image);
    }


    public void scroll() {
        // dimensions of the background image
        int width = this.getWidth();
        int height = this.getHeight();
        // an array which will contain all pixels of a row of the background
        int row_pixels[] = new int[width];


        for (int y = height - 1; y > 0; y--) {
            for (int x = width - 1; x > 0; x--) {

                // represents rgb value of a pixel
                int pixelValue;

                // if it's the last row of the background image
                if (y == height - 1) {
                    // getting the pixels of the last row and  writing them into the row array
                    pixelValue = this.getPixels()[x + y * width];
                    row_pixels[x] = pixelValue;
                }

                // taking pixels of the lower row
                pixelValue = this.getPixels()[x + (y - 1) * width];
                // writing them on the pixel above
                setPixel(x + y * width, pixelValue);


            }
        }
        // setting pixels of the last row to the first row in the background
        for (int x = 0; x < width; x++) {
            setPixel(x, row_pixels[x]);
        }
    }
}
