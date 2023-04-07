package gameobjects;

import java.awt.Graphics2D;

import components.RenderComponent;
import graphics.Animation;
import graphics.Frame;
import scenes.GameScene;

public class DamageFlash extends GameObject {

	RenderComponent renderComponent;
	
	public DamageFlash(GameScene scene, String name, Vector2 position) {
		super(scene, name, position);
		
		renderComponent = new RenderComponent();
		
		active = false;
	}
	
	@Override
	protected void initSprites() {
		sprites = scene.game.getSpriteManager().getSprites("general");
	}

	@Override
	protected void initAnimations() {
		Frame[] flash = new Frame[] {
				new Frame(sprites.get("damageflash"), 50),
				new Frame(50)
		};
		currentAnim = new Animation(flash);
	}

	@Override
	public void update() {
		currentAnim.update();
	}

	@Override
	public void render(Graphics2D g) {
		renderComponent.render(g, this);
	}

}
