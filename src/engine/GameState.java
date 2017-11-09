package engine;

import java.awt.image.BufferedImage;

/**
 * Defines a state of the engine. Example: menu, playing, settings, etc.
 */
public abstract class GameState {

        protected final String name;
        protected final GameHost host;

        public GameState(String name, GameHost host) {
                this.name = name;
                this.host = host;
                host.registerState(this);
        }

        public abstract void init();

        public abstract void update(float dt);

        public abstract void render(Renderer r);

        public abstract void suspendState();

        public abstract void resumeState();

        /**
         * Omotač za renderSnapshot poziv GameHost objekta, iscrtava trenutno stanje u off-screen sliku
         *
         * @param canvas - slika u koju treba da se renderuje
         * @return - referenca na istu sliku
         */
        public BufferedImage renderSnapshot(BufferedImage canvas) {
                return host.renderSnapshot(canvas, this);
        }

        public String getName() {
                return name;
        }
}
