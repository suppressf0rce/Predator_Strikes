package engine;

import java.awt.*;
import java.awt.image.*;
import java.util.HashMap;
import java.util.Properties;

/**
 * Glavni objekat za složenije aplikacije, koje će imati više odvojenih stanja. Treba ga
 * instancirati samo jednom, ovaj objekat je vlasnik prozora i preko njega se postavlja
 * trenutno aktivno stanje, koje će onda da bude pozivano za ažuriranje i iscrtavanje, te
 * će samo ono dobijati input događaje.
 *
 * @author Aleksandar
 */
public class GameHost {

    static {
//        System.setProperty("sun.java2d.transaccel", "True");
//        System.setProperty("sun.java2d.opengl", "true");
//        System.setProperty("sun.java2d.ddforcevram", "True");
    }

    private Color backColor = Color.blue;
    private boolean clearBackBuffer = true;

    private GameState currentState = null;
    private GameState nextState = null;

    private HashMap<String, GameState> states = new HashMap<String, GameState>();


    /**
     * Vraća prijavljene GameState objekte po nazivu
     *
     * @param name naziv koji traženi GameState daje u getName() pozivu
     * @return referenca na GameState ako je nađen, ili null ako nije
     */
    public GameState getState(String name) {
        return states.get(name);
    }

    /**
     * Prelazak na novo stanje, po referenci
     *
     * @param nextState referenca na GameState objekat, trebao bi biti jedan od prijavljenih
     */
    public void setState(GameState nextState) {
        if (nextState == null) return;
        if (currentState == nextState) return;

        this.nextState = nextState;
    }

    /**
     * Prelazak na novo stanje, po nazivu
     *
     * @param stateName naziv stanja, kako ga vraća njegova getName() metoda
     */
    public void setState(String stateName) {
        setState(getStateByName(stateName));
    }

    /**
     * Referenca na trenutno aktivno stanje
     *
     * @return referenca na GameState objekat
     */
    public GameState getCurrentState() {
        return currentState;
    }

    /**
     * Traženje reference na bilo koje od trneutno prijavljenih stanja
     *
     * @param name naziv stanja, kako ga vraća njegova getName() metoda
     * @return referenca ako je stanje pronađeno, null ako nije
     */
    public GameState getStateByName(String name) {
        return states.get(name);
    }

    /**
     * Prijavljuje stanje na ovaj Host, ovo *ne bi trebalo ručno raditi*, jer
     * se prijavljivanje radi u konstruktoru stanja.
     *
     * @param state referenca na stanje koje se registruje
     */
    public void registerState(GameState state) {
        if (state == null)
            return;

        if (!states.containsValue(state))
            states.put(state.getName(), state);
    }

    public void tick(float dt) {
        if (nextState != null) {
            if (currentState != null) currentState.suspendState();

            currentState = nextState;
            nextState = null;

            currentState.resumeState();
        }

        if (currentState != null) currentState.update(dt);
    }

    public void render(Renderer r) {
        if (currentState == null)
            return;
        else
            currentState.render(r);
    }


    public void initCurrentState() {


        if (nextState != null) {
            if (currentState != null) currentState.suspendState();

            currentState = nextState;
            nextState = null;

            currentState.init();
            currentState.resumeState();
        }

    }


    /**
     * Metod koji će da iscrta zadato stanje, ali u off-screen sliku, umjesto na ekran.
     * Obratiti pažnju da se ovo ne pozove iz render() metode istog stanja, jer bi to
     * izazvalo beskonačnu rekurziju.
     *
     * @param canvas objekat slike u koju će se crtati, može biti null, pa će nova slika biti alocirana;
     *               ako se ovo radi često, bolje je imati jednu unaprijed alociranu sliku koja će se reciklirati.
     * @param state  stanje koje se treba iscrtati, biće pozvan njegov render() metod
     * @return vraća referencu na proslijeđenu ili novokonstruisanu sliku, u koju je iscrtano stanje
     */
    public BufferedImage renderSnapshot(BufferedImage canvas, GameState state) {
        if (canvas == null)
            canvas = new BufferedImage(GameEngine.getWindow().getWidth(), GameEngine.getWindow().getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = (Graphics2D) canvas.getGraphics();

        if (clearBackBuffer) {
            g.setColor(backColor);
            g.fillRect(0, 0, GameEngine.getWindow().getWidth(), GameEngine.getWindow().getHeight());
        }


        Renderer r = new Renderer();
        r.setPixels(((DataBufferInt) canvas.getRaster().getDataBuffer()).getData());

        if (state != null)
            state.render(r);


        return getBufferedImage(r.getPixels(), GameEngine.getWindow().getWidth(), GameEngine.getWindow().getHeight(), true);
    }

    private BufferedImage getBufferedImage(int data[], int width, int height, boolean hasAlpha) {
        ColorModel colorModel;
        WritableRaster raster;
        DataBufferInt buffer = new DataBufferInt(data, width * height);
        if (hasAlpha) {
            colorModel = new DirectColorModel(32, 0x00ff0000, 0x0000ff00, 0x000000ff, 0xff000000);
            raster = WritableRaster.createPackedRaster(buffer, width, height, width, new int[]{0x00ff0000, 0x0000ff00, 0x000000ff, 0xff000000}, null);
        } else {
            colorModel = new DirectColorModel(24, 0x00ff0000, 0x0000ff00, 0x000000ff);
            raster = WritableRaster.createPackedRaster(buffer, width, height, width, new int[]{0x00ff0000, 0x0000ff00, 0x000000ff}, null);
        }
        return new BufferedImage(colorModel, raster, colorModel.isAlphaPremultiplied(), new Properties());
    }

}