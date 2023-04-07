package graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import general.Game;

public class TileSpriteSheet {

	private String path;
	private BufferedImage sheet;
	private int numImages;
	
	public int spriteWidth;
	public int spriteHeight;
	
	public final static int DEFAULT_CROP_SIZE = 32;
	
	private BufferedImage[] images;
	
	public TileSpriteSheet(String name) {
		this.path = "sprites/" + name;
		
		if (Game.DEBUG)
			System.out.format("Attempting to retrive sprite %s at path %s\n", name, path);

		try {
			sheet = ImageIO.read(ClassLoader.getSystemClassLoader().getResourceAsStream(path));
			initSprites();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void initSprites() {
		if (sheet == null) return;
		
		spriteWidth = sheet.getHeight();
		spriteHeight = sheet.getHeight();
		
		numImages = sheet.getWidth() / spriteWidth;
		images = new BufferedImage[numImages];
		//System.out.format("Num images: %d\n", numImages);
		
		for (int i = 0; i < sheet.getWidth() / spriteWidth; i++) {
			images[i] = sheet.getSubimage(i * spriteWidth, 0, spriteWidth, spriteHeight);
		}
		
		if (Game.DEBUG) 
			System.out.format("Sprite %s succesfully loaded\n", path);
	}
	
	public BufferedImage getImage(int index) {
		return images[index];
	}

}
