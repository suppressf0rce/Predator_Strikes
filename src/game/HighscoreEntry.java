package game;

import java.util.Comparator;

public class HighscoreEntry implements Comparable {
    public String playerName;
    public int playerScore;

    public HighscoreEntry(String playerName, int playerScore) {
        this.playerName = playerName;
        this.playerScore = playerScore;
    }


    @Override
    public int compareTo(Object o) {
        HighscoreEntry hse = (HighscoreEntry) o;

        if (hse.playerScore < this.playerScore)
            return -1;
        if (hse.playerScore == this.playerScore)
            return 0;

        return 1;
    }
}
