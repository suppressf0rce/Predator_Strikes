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
    private BufferStrategy bs;
    private Graphics g;

    //===>>Constructor<<===//

    /**
     * Main constructor for {@link GameWindow}
     *
     * @param gc an {@link GameContainer} so we can access the info from game loop
     */
    public GameWindow(GameContainer gc) {
        image = new BufferedImage(gc.getWidth(), gc.getHeight(), BufferedImage.TYPE_INT_ARGB);
        canvas = new Canvas();
        Dimension s = new Dimension((int) (gc.getWidth() * gc.getScale()), (int) (gc.getHeight() * gc.getScale()));

        //Keep canvas the same size
        canvas.setPreferredSize(s);
        canvas.setMaximumSize(s);
        canvas.setMinimumSize(s);

        //Setting up JFrame
        frame = new JFrame(gc.getTitle());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(canvas, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null); //Putting JFrame at the center of screen
        SwingUtilities.invokeLater(() -> frame.setResizable(false));
        frame.setVisible(true);

        //Setting up buffer strategy
        canvas.createBufferStrategy(2);
        bs = canvas.getBufferStrategy();
        g = bs.getDrawGraphics();
    }


    //===>Methods<<==//
    void update() {
        g.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
        bs.show();
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
}
