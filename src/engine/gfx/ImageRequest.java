package engine.gfx;

public class ImageRequest {

    //===>>Variables<<===//
    public Image image;
    public int zDepth;
    public int offsetX, offsetY;

    //===>>Constructor<<===//
    public ImageRequest(Image image, int zDepth, int offsetX, int offsetY) {
        this.image = image;
        this.zDepth = zDepth;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }
}
