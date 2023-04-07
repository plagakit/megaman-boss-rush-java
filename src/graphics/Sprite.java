package graphics;

import java.awt.image.BufferedImage;

import gameobjects.Vector2;

public class Sprite {

	public String name;
	public BufferedImage image;
	public int width;
	public int height;
	public Vector2 anchorPoint;
	
	public Sprite(String name, BufferedImage image, Vector2 anchorPoint) {
		this.name = name;
		this.image = image;
		width = image.getWidth();
		height = image.getHeight();
		this.anchorPoint = anchorPoint;
	}

}
