package graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import javax.imageio.ImageIO;

import gameobjects.Vector2;

public class SpriteManager {

	private HashMap<String, HashMap<String, Sprite>> spriteList;
	
	public SpriteManager() {
		spriteList = new HashMap<>();
		loadSprites();
	}

	private void loadSprites() {
		String dataPath = "sprites/gameobjects/data.txt";
		Scanner sc = new Scanner(ClassLoader.getSystemClassLoader().getResourceAsStream(dataPath));
		
		while (sc.hasNextLine()) {
			
			String name = sc.nextLine();
			
			if (name.toCharArray()[0] == '#')
				continue;
			
			int numSprites = Integer.parseInt(sc.nextLine());
			
			HashMap<String, Sprite> sprites = new HashMap<>();
			
			for (int i = 0; i < numSprites; i++) {
				String line = sc.nextLine();
				String[] tokens = line.split(":");
				
				String imagePath = "sprites/gameobjects/" + name + "/" + tokens[0];
				String spriteName = tokens[0].replace(".png", "");
				
				//System.out.println("Loading " + imagePath);
				BufferedImage image = null;
				try { image = ImageIO.read(ClassLoader.getSystemClassLoader().getResourceAsStream(imagePath)); } catch (IOException e) {e.printStackTrace(); }

				Vector2 anchorPoint = Vector2.parse(tokens[1]);
				
				Sprite s = new Sprite(spriteName, image, anchorPoint);
				sprites.put(spriteName, s);
			}
			
			spriteList.put(name, sprites);
		}
		
		sc.close();
	}
	
	public HashMap<String, Sprite> getSprites(String name) {
		return spriteList.get(name);
	}
	
}
