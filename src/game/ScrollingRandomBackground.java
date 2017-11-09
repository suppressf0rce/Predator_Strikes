package game;

import engine.gfx.Image;
import engine.gfx.ScrollableImage;

import java.util.Random;

@SuppressWarnings({"ManualArrayCopy", "WeakerAccess"})
public class ScrollingRandomBackground extends ScrollableImage {
    private Image background;

    private int particle_grid[][];
    private int particle_row[];

    private Random rand;

    public ScrollingRandomBackground(Image background) {
        super(background);
        this.background = background;

        particle_grid = new int[background.getHeight()][background.getWidth()];
        particle_row = new int[background.getWidth()];

        rand = new Random();
    }

    @Override
    public void scroll() {

        int c = rand.nextInt(4);
        int counter = 0;
        // 50% chance to generate a new row, exiting if the chance failed
        if (c == 1) {

            // filling the new row
            for (int i = 0; i < 7; i++) {
                // 50% chance that a particle will be generated
                c = rand.nextInt(2);

                if (c == 0) {
                    counter += 100;
                    continue;
                }
                // getting a particle
                c = rand.nextInt(80);

                particle_row[c + counter] = 5;
                counter += 200;

            }
        } else {
            // clearing row
            for (int i = 0; i < background.getWidth(); i++) {
                particle_row[i] = 0;
            }
        }
        // shifting the rows
        for (int i = background.getHeight() - 1; i > 0; i--) {
            for (int j = 0; j < background.getWidth(); j++) {
                particle_grid[i][j] = particle_grid[i - 1][j];
            }
        }

        // writing the randomly generated row into the particle grid
        for (int i = 0; i < background.getWidth(); i++) {
            particle_grid[0][i] = particle_row[i];
        }
    }

    public int[][] getParticle_grid() {
        return particle_grid;
    }

    public void printMatrix() {
        System.out.println("STARTED");
        for (int i = 0; i < background.getHeight(); i++) {
            for (int j = 0; j < background.getWidth(); j++) {
                System.out.print(particle_grid[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("ENDED");
    }
}
