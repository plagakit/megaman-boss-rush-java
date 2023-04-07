package graphics;

import java.awt.image.BufferedImage;

public class Frame {
	
	public BufferedImage image;
	public int displayTime;
	public Sprite sprite;
	public boolean empty;
	
	public Frame(BufferedImage image, int displayTime) {
		this.image = image;
		this.displayTime = displayTime;
		empty = false;
	}
	
	public Frame(Sprite sprite, int displayTime) {
		this.sprite = sprite;
		this.displayTime = displayTime;
		empty = false;
	}
	
	public Frame(int displayTime) {
		this.displayTime = displayTime;
		empty = true;
	}
}
