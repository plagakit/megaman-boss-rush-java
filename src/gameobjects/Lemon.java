package gameobjects;

import java.awt.Graphics2D;

import components.PhysicsComponent;
import components.RenderComponent;
import general.Timer;
import graphics.Animation;
import scenes.GameScene;

public class Lemon extends Projectile {

	final float SPEED = 4f;
	
	RenderComponent renderComponent;
	PhysicsComponent physicsComponent;
	Timer despawnTimer;
	
	public Lemon(GameScene scene, Vector2 position, boolean direction) {
		super(scene, "Lemon", position);
		
		vel.x = direction ? SPEED : SPEED * -1;
		
		renderComponent = new RenderComponent();
		physicsComponent = new PhysicsComponent();
		despawnTimer = new Timer(5000);
	}

	@Override
	protected void initCollisionData() {
		hitboxes.add(new Hitbox(-4, -3, 8, 6, CollisionType.PLAYER_PROJECTILE, 1));
	}

	@Override
	protected void initSprites() {
		sprites = scene.game.getSpriteManager().getSprites("megaman");
	}

	@Override
	protected void initAnimations() {
		currentAnim = new Animation(sprites.get("lemon"));
	}

	@Override
	public void update() {
		physicsComponent.update(this);
		
		if (despawnTimer.done)
			despawn();
		else 
			despawnTimer.update();
	}

	@Override
	public void render(Graphics2D g) {
		renderComponent.render(g, this);
	}

}
