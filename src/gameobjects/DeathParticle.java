package gameobjects;

import java.awt.Graphics2D;

import components.PhysicsComponent;
import components.RenderComponent;
import general.Timer;
import graphics.Animation;
import graphics.Frame;
import scenes.GameScene;

public class DeathParticle extends GameObject {

	final float SPEED = 1f;
	final float DIAGONAL_SPEED = 0.75f;
	Timer despawnTimer = new Timer(10000);
	
	RenderComponent renderComponent;
	PhysicsComponent physicsComponent;
	
	public DeathParticle(GameScene scene, Vector2 position, Vector2 direction, boolean diagonal) {
		super(scene, "DeathParticle", new Vector2(position));
		
		renderComponent = new RenderComponent();
		physicsComponent = new PhysicsComponent();
		
		vel = Vector2.multiply(direction, diagonal ? DIAGONAL_SPEED : SPEED);
	}

	public DeathParticle(GameScene scene, Vector2 position, Vector2 direction) {
		this(scene, position, direction, false);
	}

	@Override
	protected void initSprites() {
		sprites = scene.game.getSpriteManager().getSprites("general");
	}

	@Override
	protected void initAnimations() {
		Frame[] anim = new Frame[] {
				new Frame(sprites.get("death1"), 50),
				new Frame(sprites.get("death2"), 50),
				new Frame(sprites.get("death3"), 50),
				new Frame(sprites.get("death4"), 50),
				new Frame(sprites.get("death5"), 50)
		};
		
		currentAnim = new Animation(anim);
	}

	@Override
	public void update() {
		physicsComponent.update(this);
		currentAnim.update();
		
		if (despawnTimer.done)
			active = false;
		else
			despawnTimer.update();
	}

	@Override
	public void render(Graphics2D g) {
		renderComponent.render(g, this);
	}
	

}
