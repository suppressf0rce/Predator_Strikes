package engine.gfx;

import java.awt.image.BufferedImage;

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
        // dimensions of the background
        int width = this.getWidth();
        int height = this.getHeight();
        // all pixels of the last row
        int row_pixels[] = new int[width];

        for (int y = height - 1; y > 0; y--) {
            for (int x = width - 1; x > 0; x--) {

                // rgb value of the pixel
                int pixelValue;
                // if it's the last row
                if (y == height - 1) {

                    pixelValue = this.getPixels()[x + y * width];
                    row_pixels[x] = pixelValue;
                }
                // taking pixels of the lesser row
                pixelValue = this.getPixels()[x + (y - 1) * width];

                setPixel(x + y * width, pixelValue);


            }
        }

        for (int x = 0; x < width; x++) {
            setPixel(x, row_pixels[x]);
        }
    }
}
