import javax.sound.sampled.*;
import java.io.File;

public class SoundPlayer {
	
    private Clip   bgmClip;
    private String currentTrack = "";

    public void playSound(String filename) {
        try {
            stopSound();
            File f = new File(filename);
            if (!f.exists()) {
                System.err.println("BGM file not found: " + f.getAbsolutePath());
                return;
            }
            AudioInputStream raw = AudioSystem.getAudioInputStream(f);
            AudioFormat baseFormat = raw.getFormat();
            AudioFormat targetFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                baseFormat.getSampleRate(),
                16,
                baseFormat.getChannels(),
                baseFormat.getChannels() * 2,
                baseFormat.getSampleRate(),
                false
            );
            AudioInputStream audioIn = AudioSystem.isConversionSupported(targetFormat, baseFormat)
                ? AudioSystem.getAudioInputStream(targetFormat, raw)
                : raw;
            bgmClip = AudioSystem.getClip();
            bgmClip.open(audioIn);
            bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
            bgmClip.start();
            currentTrack = filename;
        } catch (Exception e) {
            System.err.println("BGM error (" + filename + "): " + e.getMessage());
        }
    }

    public void stopSound() {
        try {
            if (bgmClip != null) {
                bgmClip.stop();
                bgmClip.close();
                bgmClip = null;
            }
            currentTrack = "";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isPlaying() {
        return bgmClip != null && bgmClip.isRunning();
    }

    public boolean isPlayingTrack(String filename) {
        return isPlaying() && currentTrack.equals(filename);
    }


    public void playSfx(String filename) {
        try {
            File f = new File(filename);
            if (!f.exists()) {
                System.err.println("SFX file not found: " + f.getAbsolutePath());
                return;
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(f);

            AudioFormat baseFormat = audioIn.getFormat();
            AudioFormat targetFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                baseFormat.getSampleRate(),
                16,
                baseFormat.getChannels(),
                baseFormat.getChannels() * 2,
                baseFormat.getSampleRate(),
                false
            );
            if (!AudioSystem.isConversionSupported(targetFormat, baseFormat)) {
                targetFormat = baseFormat;
            }
            AudioInputStream converted = AudioSystem.getAudioInputStream(targetFormat, audioIn);

            Clip sfx = AudioSystem.getClip();
            sfx.open(converted);
            sfx.start();

            sfx.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    sfx.close();
                }
            });
        } catch (Exception e) {
            System.err.println("SFX error (" + filename + "): " + e.getMessage());
        }
    }
}