import javax.sound.sampled.*;
import java.io.InputStream;

public class SoundPlayer {
    public void playSound(String resourcePath) {
        try {
            InputStream audioSrc = getClass().getResourceAsStream(resourcePath);
            InputStream bufferedIn = new java.io.BufferedInputStream(audioSrc);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(bufferedIn);

            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
