package gameobjects.whispy;

import java.awt.Graphics2D;

import components.RenderComponent;
import gameobjects.GameObject;
import gameobjects.Vector2;
import graphics.Sprite;
import scenes.Scene;

public class WhispyBackground extends GameObject {

	RenderComponent renderComponent;
	Sprite bg;
	
	public WhispyBackground(Scene scene) {
		super(scene, "WhispyBackground", Vector2.zero());
		
		pos = Vector2.center(scene.game);
		
		renderComponent = new RenderComponent();
	}

	@Override
	protected void initSprites() {
		bg = scene.game.getSpriteManager().getSprites("whispy").get("background");
	}

	@Override
	protected void initAnimations() {}

	@Override
	public void update() {}

	@Override
	public void render(Graphics2D g) {
		renderComponent.render(g, this, bg);
	}

}
