package gameobjects.jevil;

import java.awt.Graphics2D;

import components.PhysicsComponent;
import gameobjects.GameObject;
import gameobjects.Vector2;
import graphics.Sprite;
import scenes.Scene;

public class JevilBackground extends GameObject {

	PhysicsComponent physicsComponent;
	Sprite bg;
	
	public JevilBackground(Scene scene, Vector2 position) {
		super(scene, "JevilBackground", position);
		
		physicsComponent = new PhysicsComponent();
		
		vel = new Vector2(-3, 1);
		scale = new Vector2(2f, 2f);
	}

	@Override
	protected void initSprites() {
		bg = scene.game.getSpriteManager().getSprites("jevil").get("background");
	}

	@Override
	protected void initAnimations() {}

	
	@Override
	public void update() {
		physicsComponent.update(this);
		
		if (pos.y > bg.height)
			pos.y = 0;
		
		if (pos.x + bg.width < 0)
			pos.x = 0;
	}

	@Override
	public void render(Graphics2D g) {
		
		float gameScale = scene.game.scale;
		
		int x = (int)(pos.x * scale.x * gameScale);
		int width = (int)(bg.width * scale.x * gameScale);
		int y = (int)(pos.y * scale.y * gameScale);
		int height = (int)(bg.height * scale.y * gameScale);
		
		g.drawImage(bg.image, x, y, width, height, null);
		g.drawImage(bg.image, x + width, y, width, height, null);
		g.drawImage(bg.image, x, y - height, width, height, null);
		g.drawImage(bg.image, x + width, y - height, width, height, null);
			
	}

}
