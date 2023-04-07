package audio;

import java.util.HashMap;
import java.util.Scanner;

public class SoundManager {

	public HashMap<String, Sound> sounds;
	
	public SoundManager() {
		sounds = new HashMap<String, Sound>();
		loadSounds();
	}
	
	private void loadSounds() {
		String dataPath = "audio/data.txt";
		Scanner sc = new Scanner(ClassLoader.getSystemClassLoader().getResourceAsStream(dataPath));
		while (sc.hasNextLine()) {
			String path = sc.nextLine();
			
			Sound s;
			String name;
			if (path.contains(":")) {
				String actualPath = path.split(":")[0];
				boolean loops = Boolean.parseBoolean(path.split(":")[1]);
				s = new Sound("audio/" + actualPath, loops);
				name = actualPath.replace(".wav", "");
				sounds.put(name, s);
			} 
			else {
				s = new Sound("audio/" + path, false);
				name = path.replace(".wav", "");
				sounds.put(name, s);
			}
		}
		
		sc.close();
	}
	
	public void playSound(String name) {
		Sound s = sounds.get(name);
		if (s != null)
			sounds.get(name).play();
	}
	
	public void stopSound(String name) {
		Sound s = sounds.get(name);
		if (s != null)
			s.stop();
	}

}
