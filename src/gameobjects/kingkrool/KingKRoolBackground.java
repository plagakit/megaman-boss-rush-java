package gameobjects.kingkrool;

import java.awt.Graphics2D;

import gameobjects.GameObject;
import gameobjects.Vector2;
import general.Utility;
import graphics.Sprite;
import scenes.Scene;

public class KingKRoolBackground extends GameObject {

	Sprite bg1, bg2;
	
	public KingKRoolBackground(Scene scene) {
		super(scene, "KingKRoolBackground", Vector2.zero());
		
		pos = new Vector2(-100, -125);
	}

	@Override
	protected void initSprites() {
		sprites = scene.game.getSpriteManager().getSprites("kingkrool");
		bg1 = sprites.get("bg1");
		bg2 = sprites.get("bg2");
	}

	@Override
	protected void initAnimations() {}

	float updateNum = 0;
	
	@Override
	public void update() {
		updateNum += Utility.timeAdjust;
		pos.y = (float)Math.sin(updateNum / 125f) * 10f - 50;
	}

	@Override
	public void render(Graphics2D g) {
		float gameScale = scene.game.scale;
		
		int width1 = (int)(bg1.width * scale.x * gameScale);
		int height1 = (int)(bg1.height * scale.y * gameScale);
		
		int x = (int)(pos.x * scale.x * gameScale);
		int y = (int)(pos.y * scale.y * gameScale);
		int width2 = (int)(bg2.width * scale.x * gameScale 		* 2);
		int height2 = (int)(bg2.height * scale.y * gameScale 	* 2);

		g.drawImage(bg2.image, x, y, width2, height2, null);
		g.drawImage(bg1.image, 0, 0, width1, height1, null);
	}

}
