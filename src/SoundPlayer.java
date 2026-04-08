import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundPlayer {
    public void playSound(String soundFilePath) {
        try {
            // Locate file relative to the project directory
            File soundFile = new File(soundFilePath);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            
            // Get sound clip resource
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            
            // Start playing
            clip.start();
            
            // Optional: loop the music
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
