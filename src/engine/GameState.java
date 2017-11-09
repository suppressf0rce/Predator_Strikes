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

        public abstract void update(float dt);

        public abstract void render(Renderer r);

        public abstract void suspendState();

        public abstract void resumeState();

        /**
         * Cover for the renderSnapshot call of {@link GameHost} object, draws current state in offscreen image
         *
         * @param canvas - Image that needs to be drawn in renderer
         * @return - reference to the newly picture that will be drawn
         */
        public BufferedImage renderSnapshot(BufferedImage canvas) {
                return host.renderSnapshot(canvas, this);
        }

        public String getName() {
                return name;
        }
}
