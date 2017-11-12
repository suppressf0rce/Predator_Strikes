package game;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class HighscoreWriteListener implements WindowListener {
    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        Utils.writeHighscoreEntries();
        System.out.println("SS");
    }

    @Override
    public void windowClosed(WindowEvent e) {
        Utils.writeHighscoreEntries();

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
