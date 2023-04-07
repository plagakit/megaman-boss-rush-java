package audio;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {

	String filePath;
	Clip clip;
	
	boolean loops;
	
	public Sound(String filePath, boolean loops) {
		this.filePath = filePath;
		this.loops = loops;
	}

	public void play() {
		URL url = ClassLoader.getSystemClassLoader().getResource(filePath);
	    AudioInputStream audioIn = null;
		try {
			audioIn = AudioSystem.getAudioInputStream(url);
			clip = AudioSystem.getClip();
			clip.open(audioIn);
			
			FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN); 
			volume.setValue(-10.0f);

			if (loops)
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			
			clip.start();
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {e.printStackTrace();}  
	}
	
	public void stop() {
		clip.stop();
	}
}
