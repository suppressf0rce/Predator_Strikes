package engine.sfx;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * This class represents sound clips that game engine can play
 * <p>
 * The way the sound works is load the file, decode and re encode to the format that java understands
 */
@SuppressWarnings("WeakerAccess")
public class SoundClip {

    //===>>Variables<<===//
    private Clip clip;
    private FloatControl gainControl;

    //===>>Constructor<<===//

    /**
     * This class loads audio file and decodes it to format that java can understand
     * <p>
     * *Note this class only read 16 bitrate wav files
     *
     * @param path an path to the audio file
     * @see SoundClip
     */
    public SoundClip(String path) {
        try {
            //Loading audio from source file into ram
            File soundFile = new File(path);
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);

            //Decoding audio format settings
            AudioFormat baseFormat = ais.getFormat();
            AudioFormat decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
                    baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);

            //Decode Auto format
            AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);

            //Storing audio stream into clip
            clip = AudioSystem.getClip();
            clip.open(dais);

            //Setting clip volume control from decoded audio stream
            gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);


        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }


    //===>>Methods<<===//

    /**
     * Plays sound clip from the beginning
     */
    public void play() {
        if (clip == null) {
            return;
        }

        //We want to stop clip if its already running
        stop();

        //Set clip play position to beginning
        clip.setFramePosition(0);

        //Keep starting the clip because sometimes it doesn't start at first time
        while (!clip.isRunning()) {
            clip.start();
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void play(boolean loop) {
        if (clip == null) {
            return;
        }

        //We want to stop clip if its already running
        stop();

        //Set clip play position to beginning
        clip.setFramePosition(0);

        //Keep starting the clip because sometimes it doesn't start at first time
        while (!clip.isRunning()) {
            if (loop)
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            else
                clip.start();
        }
    }

    /**
     * Stops sound clip from the playing
     */
    public void stop() {
        if (clip.isRunning())
            clip.stop();

        clip.setFramePosition(0);
    }

    /**
     * Removes clip stream from ram. And closes the audio clip
     */
    public void close() {
        stop();

        //This command will empty the stream
        clip.drain();
        clip.close();
    }

    /**
     * Plays sound clip but in the forever loop
     */
    public void loop() {
        play(true);
    }


    //===>>Getters & Setters<<===//

    /**
     * Sets the volume of the sound clip
     */
    public void setVolume(float value) {
        gainControl.setValue(value);
    }

    public boolean isRunning() {
        return clip.isRunning();
    }

}


