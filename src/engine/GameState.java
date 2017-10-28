package engine;

/**
 * Defines a state of the engine. Example: menu, playing, settings, etc.
 */
public abstract class GameState {


        public abstract void init();

        public abstract void update(float dt);

        public abstract void render(Renderer r);

}
