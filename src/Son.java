import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Son {
    private Clip clip;

    public void loadSound(String filePath) throws IOException {
        try {
            File soundFile = new File(filePath);
            if (!soundFile.exists()) {
                throw new IOException("Sound file not found: " + filePath);
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            throw new IOException("Error loading sound: " + e.getMessage());
        }
    }

    public void playSound() {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void setVolume(float volume) {
        if (clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
        }
    }
}
