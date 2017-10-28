package engine;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

/**
 * Game window of our engine that will contain and show our game
 */
@SuppressWarnings("WeakerAccess")
public class GameWindow {

    //===>>Variables<<===//
    private JFrame frame;
    private BufferedImage image;
    private Canvas canvas;
    private BufferStrategy buffer;
    private Graphics graphics;

    private String title = "Game Engine";
    private int width = 640;
    private int height = 480;
    private float scale = 2f;

    //===>>Constructor<<===//

    /**
     * Main constructor for {@link GameWindow}
     */
    public GameWindow() {
        //Display Image setup
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        //Canvas Setup
        canvas = new Canvas();
        Dimension s = new Dimension((int) (width * scale), (int) (height * scale));
        canvas.setPreferredSize(s);
        canvas.setMaximumSize(s);
        canvas.setMinimumSize(s);

        //Frame Setup
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(canvas, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        //frame.setResizable(false);
        frame.setVisible(true);

        //Buffer Setup
        canvas.createBufferStrategy(2);
        buffer = canvas.getBufferStrategy();
        graphics = buffer.getDrawGraphics();

        frame.requestFocus();
        canvas.requestFocus();
    }


    //===>Methods<<==//
    void update() {
        graphics.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
        buffer.show();
    }

    public void dispose() {
        image.flush();
        graphics.dispose();
        buffer.dispose();
        frame.dispose();
    }

    //===>>Getters & Setters<<===//
    public BufferedImage getImage() {
        return image;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public JFrame getFrame() {
        return frame;
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public BufferStrategy getBuffer() {
        return buffer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

}
