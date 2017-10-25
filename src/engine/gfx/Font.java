package engine.gfx;

public class Font {

    //===>>Variables<<===//
    public static final Font STANDARD = new Font("res/inconsolata_14pt.png", 1000, 6);

    private Image fontImage;
    private int[] offsets;
    private int[] widths;


    //===>>Constructor<<===//

    /**
     * This class loads font sheet from image files and reads the letters from it,
     * and uses part of the font sheet to render on the screen
     * <p>
     * Each letter in font sheet starts with the color 0xff0000ff (Blue) and
     * ends with color 0xffffff00 (Yellow).
     * The indicating color must be in the first row of the font sheet and the letters have to be colored black
     *
     * @param path to the font sheet
     */
    public Font(String path, int numOfLetters, int offsetY) {
        fontImage = new Image(path);

        offsets = new int[numOfLetters];
        widths = new int[numOfLetters];

        boolean firstTime = true;

        int unicode = 0;

        for (int i = 0; i < fontImage.getWidth(); i++) {

            int alpha = ((fontImage.getPixels()[i + offsetY * fontImage.getWidth()] >> 24) & 0xff);
            //Search for blue pixel that means beginning of letter
            if (alpha == 255) {

                if (!firstTime || i == fontImage.getWidth() - 1) {
                    widths[unicode] = i - offsets[unicode];
                    unicode++;
                }

                offsets[unicode] = i + 1;
                firstTime = false;
            }
        }
        System.out.println(unicode);
    }


    //===>>Getters & Setters<<===//
    public Image getFontImage() {
        return fontImage;
    }

    public void setFontImage(Image fontImage) {
        this.fontImage = fontImage;
    }

    public int[] getOffsets() {
        return offsets;
    }

    public void setOffsets(int[] offsets) {
        this.offsets = offsets;
    }

    public int[] getWidths() {
        return widths;
    }

    public void setWidths(int[] widths) {
        this.widths = widths;
    }
}
