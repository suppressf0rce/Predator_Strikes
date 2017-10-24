package engine.gfx;

public class Font {

    //===>>Variables<<===//
    public static final Font STANDARD_WHITE = new Font("res/inconsolata_14pt_white.png", 95);
    public static final Font STANDARD_YELLOW = new Font("res/inconsolata_14pt_yellow.png", 95);

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
    public Font(String path, int numOfLetters) {
        fontImage = new Image(path);

        offsets = new int[numOfLetters];
        widths = new int[numOfLetters];

        int unicode = 0;

        for (int i = 0; i < fontImage.getWidth(); i++) {

            //Search for blue pixel that means beginning of letter
            if (fontImage.getPixels()[i] == 0xff0000ff)
                offsets[unicode] = i + 1;

            //Search for yellow pixel that means end of the letter
            if (fontImage.getPixels()[i] == 0xffffff00) {
                widths[unicode] = i - offsets[unicode];
                unicode++;
            }
        }
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
