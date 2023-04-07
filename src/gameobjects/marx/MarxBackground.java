package gameobjects.marx;

import java.awt.Graphics2D;

import gameobjects.GameObject;
import gameobjects.Vector2;
import general.Utility;
import graphics.Sprite;
import scenes.Scene;

public class MarxBackground extends GameObject {

	Sprite[] bgs;
	float[] xPos;
	final float SCROLL_SPEED = 0.6f;
	final float SCROLL_INCREMENT = 0.5f;
	
	public MarxBackground(Scene scene, Vector2 position) {
		super(scene, "MarxBackground", position);
	}

	@Override
	protected void initSprites() {
		sprites = scene.game.getSpriteManager().getSprites("marx");
		bgs = new Sprite[] {
			sprites.get("background1"),
			sprites.get("background2"),
			sprites.get("background3"),
			sprites.get("background4"),
			sprites.get("background5"),
			sprites.get("background6")
		};
		xPos = new float[bgs.length];
		for (int i = 0; i < xPos.length; i++) xPos[i] = 0;
	}

	@Override
	protected void initAnimations() {}

	@Override
	public void update() {
		for (int i = 0; i < bgs.length; i++) {
			xPos[i] += (SCROLL_SPEED + (i * SCROLL_INCREMENT)) * Utility.timeAdjust;
			
			if (xPos[i] >= bgs[i].width)
				xPos[i] = 0;
		}	
	}

	@Override
	public void render(Graphics2D g) {
		float gameScale = scene.game.scale;
		
		int mainX = (int)(xPos[0] * scale.x * gameScale);
		int mainY = 0;
		int mainWidth = (int)(bgs[0].width * scale.x * gameScale);
		int mainHeight = (int)(bgs[0].height * scale.y * gameScale);
		g.drawImage(bgs[0].image, mainX, mainY, mainWidth, mainHeight, null);
		g.drawImage(bgs[0].image, mainX + mainWidth, mainY, mainWidth, mainHeight, null);
		g.drawImage(bgs[0].image, mainX - mainWidth, mainY, mainWidth, mainHeight, null);
		
		int x, y, width, height;
		int yPos = scene.game.height;
		
		for (int i = bgs.length - 1; i > 0; i--) {
			Sprite bg = bgs[i];
			
			yPos -= bg.height;
			x = (int)(xPos[i] * scale.x * gameScale);
			y = (int)(yPos * scale.y * gameScale);
			width = (int)(bg.width * scale.x * gameScale);
			height = (int)(bg.height * scale.y * gameScale);

			g.drawImage(bg.image, x, y, width, height, null);
			g.drawImage(bg.image, x + width, y, width, height, null);
			g.drawImage(bg.image, x - width, y, width, height, null);
		}		
	}

}
