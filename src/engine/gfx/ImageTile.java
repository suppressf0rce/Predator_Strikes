package engine.gfx;

/**
 * This class represents an TileImage (Sprite Sheet) which contains
 * multiple images on 1 big image
 */
public class ImageTile extends Image {

    //===>>Variables<<===//
    private int tileWidth;
    private int tileHeight;


    //===>>Constructor<<===//

    /**
     * Default constructor for {@link ImageTile}
     *
     * @param path       to the image location
     * @param tileWidth  an width of the single tile / sprite
     * @param tileHeight an height of the single tile / sprite
     */
    public ImageTile(String path, int tileWidth, int tileHeight) {
        super(path);
        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;
    }


    //===>>Getters & Setters<<===//
    public int getTileWidth() {
        return tileWidth;
    }

    public void setTileWidth(int tileWidth) {
        this.tileWidth = tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public void setTileHeight(int tileHeight) {
        this.tileHeight = tileHeight;
    }
}
