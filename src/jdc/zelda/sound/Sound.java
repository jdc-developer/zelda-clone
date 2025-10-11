package jdc.zelda.sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {

    private Clip clip;

    public final static Sound musicBackground = new Sound("/music.wav");
    public final static Sound hurtSound = new Sound("/hurt.wav");

    private Sound(String path) {
        try {
            clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(path));
            clip.open(inputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void play() {
        try {
            new Thread(() -> {
                clip.setFramePosition(0);
                clip.start();
            }).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void loop() {
        try {
            new Thread(() -> clip.loop(Clip.LOOP_CONTINUOUSLY)).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
