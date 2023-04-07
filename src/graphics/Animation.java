package graphics;

import java.awt.image.BufferedImage;

import general.Utility;

public class Animation {

	private boolean isStatic;
	private int index;
	private Frame[] frames;
	
	private long lastTime;
	private long timer;
	
	public Animation(Frame[] frames) {
		this.isStatic = false;
		this.frames = frames;
		index = 0;
		timer = 0;
		lastTime = Utility.currentTimeMillis();
	}
	
	public Animation(Sprite sprite) {
		this.isStatic = true;
		this.frames = new Frame[1];
		frames[0] = new Frame(sprite, 0);
		index = 0;
		
		timer = 0;
		lastTime = Utility.currentTimeMillis();
	}
	
	public Animation(BufferedImage image) {
		this.isStatic = true;
		this.frames = new Frame[1];
		frames[0] = new Frame(image, 0);
		index = 0;
		
		timer = 0;
		lastTime = Utility.currentTimeMillis();
	}
	
	public void restart() {
		index = 0;
		timer = 0;
		lastTime = Utility.currentTimeMillis();
	}
	
	public void update() {
		if (isStatic) { return; }
		
		timer += Utility.currentTimeMillis() - lastTime;
		lastTime = Utility.currentTimeMillis();
		
		if (timer > frames[index].displayTime) {
			index++;
			timer = 0;
			if (index >= frames.length) { index = 0; }
		}
	}

	public Frame getCurrentFrame() {
		return frames[index];
	}
	
	public int getLengthInMS() {
		int ms = 0;
		for (Frame f : frames)
			ms += f.displayTime;
		return ms;
	}
	
}
