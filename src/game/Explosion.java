package game;

import engine.GameEngine;
import engine.GameObject;
import engine.Renderer;

import java.awt.*;

public class Explosion extends GameObject {
    private static final int MAX_PARTICLES = 1000;
    private Particle[] particles = new Particle[MAX_PARTICLES];

    private int starting_posX;
    private int starting_posY;


    protected Explosion(int posX, int posY) {
        this.setTag("explosion");

        this.starting_posX = posX;
        this.starting_posY = posY;

        for (int i = 0; i < MAX_PARTICLES; i++) {
            particles[i] = new Particle();
        }

        genEx(starting_posX, starting_posY, 3.0f, 50, 150);
    }

    @Override
    public void update(float dt) {

        for (Particle p : particles) {
            if (p.life <= 0) {
                continue;
            }

            p.life--;
            p.posX += p.dX;
            p.posY += p.dY;
            p.dX *= 0.99f;
            p.dY *= 0.99f;
            p.dY += 0.1f;

            if (p.posX < 0) {
                p.posX = 0.01f;
                p.dX = Math.abs(p.dX) * (float) Math.random() * 0.6f;
            }

            if (p.posY < 0) {
                p.posY = 0.01f;
                p.dY = Math.abs(p.dY) * (float) Math.random() * 0.6f;
            }

        }
    }

    @Override
    public void render(Renderer r) {
        for (Particle p : particles) {
            if (p.life <= 0)
                continue;

            GameEngine.getRenderer().drawLine((int) (p.posX - p.dX), (int) (p.posY - p.dY), (int) p.posX, (int) p.posY, Color.green);
        }
    }

    private void genEx(float cX, float cY, float radius, int life, int count) {
        for (Particle p : particles) {
            if (p.life <= 0) {
                p.life = (int) (Math.random() * life * 0.5) + life / 2;
                p.posX = cX;
                p.posY = cY;

                double angle = Math.random() * Math.PI * 2.0;
                double speed = Math.random() * radius;

                p.dX = (float) (Math.cos(angle) * speed);
                p.dY = (float) (Math.sin(angle) * speed);

                count--;

                if (count <= 0) {
                    return;
                }
            }
        }
    }
}
