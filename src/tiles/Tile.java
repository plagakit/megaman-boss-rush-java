package tiles;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import general.Game;
import scenes.GameScene;

public class Tile {

	public static final int TILE_SIZE = 16;
	
	private GameScene scene;
	private int id;
	private BufferedImage image;
	private int behaviourID;
	
	public Tile(GameScene scene, int id, BufferedImage image, int behaviourID) {
		this.scene = scene;
		this.id = id;
		this.image = image;
		this.behaviourID = behaviourID;
		
	}
	
	public void update() {
		
	}
	
	public void render(Graphics2D g, int x, int y) {
		
		float scale = scene.game.scale;
		
		/*
		if (Game.DEBUG) {
			g.setColor(Color.red);
			g.drawRect((int)(x * scale), 
					(int)(y * scale), 
					(int)(TILE_SIZE * scale), 
					(int)(TILE_SIZE * scale));
		}
		*/
		
		if (behaviourID == 0)
			return;
		
		g.drawImage(image, 
				(int)(x * scale), 
				(int)(y * scale), 
				(int)(TILE_SIZE * scale), 
				(int)(TILE_SIZE * scale), 
				null);
		
		if (Game.DEBUG) {
			g.setColor(Color.red);
			g.drawRect((int)(x * scale), 
					(int)(y * scale), 
					(int)(TILE_SIZE * scale), 
					(int)(TILE_SIZE * scale));
		}
	}
	
	public int getID() {
		return id;
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public int getBehaviourID() {
		return behaviourID;
	}
	
	/* 0 - solid no render
	 * 1 - background
	 * 2 - solid render
	 * 3 - platform
	 * 4 - instadeath
	 * 5 - foreground nonsolid
	 * 6 - foreground solid
	 */
	
	public boolean isSolid() {
		return behaviourID == 2 || behaviourID == 0 || behaviourID == 6;
	}
	
	public boolean isPlatform() {
		return behaviourID == 3;
	}
	
	public boolean isInstaDeath() {
		return behaviourID == 4;
	}
	
}
