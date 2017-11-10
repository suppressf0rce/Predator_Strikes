package game;

import engine.gfx.Image;
import engine.gfx.ScrollableImage;

import java.util.Random;

@SuppressWarnings({"ManualArrayCopy", "WeakerAccess"})
/**
 * This class represents an overlaying mask ( random space particles) which is drawn over the backlaying background image.
 */
public class ScrollingRandomBackground extends ScrollableImage {
    // image over which this mask is drawn
    private Image background;

    // matrix of particles' positions
    private int particle_grid[][];
    // array of random generated particles
    private int particle_row[];


    private Random rand;

    public ScrollingRandomBackground(Image background) {
        super(background);
        this.background = background;

        rand = new Random();


        // initializing particle matrix and array to match the size of the background image
        particle_grid = new int[background.getHeight()][background.getWidth()];
        particle_row = new int[background.getWidth()];

        // filling the matrix with random entries
        for (int y = 0; y < background.getHeight(); y++) {
            scroll();
        }

    }

    /**
     * This method scrolls the overlaying background and particle matrix by a row each time it is called.
     */
    @Override
    public void scroll() {
        int c = rand.nextInt(10);

        // 10% chance to create a new row consisted of random positioned particles
        if (c == 1) {
            // offset at which the particles are drawn
            int offset = 0;

            // filling the new row with random particles
            for (int i = 0; i < 7; i++) {
                // 50% chance that a particle will be generated
                c = rand.nextInt(2);

                // if the particle hasn't been created
                if (c == 0) {
                    offset += 100;
                    continue;
                }

                // getting a particle
                c = rand.nextInt(80);

                // inserting the particle into the row array
                particle_row[c + offset] = 5;
                offset += 200;

            }
            // if we've not created a random row, we're just clearing the particle row to get rid of old entries
        } else {
            // clearing row
            for (int i = 0; i < background.getWidth(); i++) {
                particle_row[i] = 0;
            }
        }
        // shifting the rows in the matrix so that the each lower pixel takes place of the upper pixel
        for (int i = background.getHeight() - 1; i > 0; i--) {
            for (int j = 0; j < background.getWidth(); j++) {
                particle_grid[i][j] = particle_grid[i - 1][j];
            }
        }

        // writing the randomly generated row into the first row of particle grid
        for (int i = 0; i < background.getWidth(); i++) {
            particle_grid[0][i] = particle_row[i];
        }
    }

    public int[][] getParticle_grid() {
        return particle_grid;
    }

}
