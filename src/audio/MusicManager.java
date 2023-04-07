package audio;

import java.util.HashMap;
import java.util.Scanner;

public class MusicManager {

	public HashMap<String, Sound> music;
	
	public MusicManager() {
		music = new HashMap<String, Sound>();
		loadMusic();
	}
	
	private void loadMusic() {
		String dataPath = "audio/music/data.txt";
		Scanner sc = new Scanner(ClassLoader.getSystemClassLoader().getResourceAsStream(dataPath));
		
		while (sc.hasNextLine()) {
			String path = sc.nextLine();
			Sound s = new Sound("audio/music/" + path, true);
			String name = path.replace(".wav", "");
			music.put(name, s);
		}
		
		sc.close();
	}
	
	public void playMusic(String name) {
		Sound m = music.get(name);
		if (m != null)
			music.get(name).play();
	}
	
	public void stopMusic(String name) {
		Sound m = music.get(name);
		if (m != null)
			m.stop();
	}

}
