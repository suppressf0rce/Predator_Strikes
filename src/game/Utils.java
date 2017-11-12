package game;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Utils {
    public static ArrayList<HighscoreEntry> highscore_entries = new ArrayList<HighscoreEntry>();

    public static void readHighScoreEntries() {
        File f = new File("res/highscores.txt");
        try {
            Scanner scanner = new Scanner(new FileInputStream(f));
            for (int i = 0; i < 10; i++) {
                String line[] = scanner.nextLine().split(" -> ");

                highscore_entries.add(new HighscoreEntry(line[0], Integer.valueOf(line[1])));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void writeHighscoreEntries() {
        File f = new File("res/highscores.txt");

        try {
            PrintWriter printWriter = new PrintWriter(new FileOutputStream(f));

            for (HighscoreEntry hse : highscore_entries) {
                printWriter.println(hse.playerName + " -> " + hse.playerScore);
            }
            printWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sortHighScoreEntries() {
        Collections.sort(highscore_entries);
        highscore_entries = new ArrayList<HighscoreEntry>(highscore_entries.subList(0, 11));

    }

}
