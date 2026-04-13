import javax.sound.sampled.*;
import java.io.InputStream;

public class SoundPlayer {
	private Clip clip;
    public void playSound(String resourcePath) {
    	try {
            //stopSound(); // stop previous if any
            InputStream audioSrc = getClass().getResourceAsStream(resourcePath);
            if (audioSrc == null) return;
            InputStream bufferedIn = new java.io.BufferedInputStream(audioSrc);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(bufferedIn);

            clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
