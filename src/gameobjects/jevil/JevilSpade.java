package gameobjects.jevil;

import java.awt.Graphics2D;

import components.PhysicsComponent;
import components.RenderComponent;
import gameobjects.CollisionType;
import gameobjects.Hitbox;
import gameobjects.Projectile;
import gameobjects.Vector2;
import general.Timer;
import graphics.Animation;
import scenes.GameScene;

public class JevilSpade extends Projectile {

	RenderComponent renderComponent;
	PhysicsComponent physicsComponent;
	
	float angle;
	final float SPEED = 0.9f;
	
	Timer despawnTimer = new Timer(10000);
	
	public JevilSpade(GameScene scene, Vector2 position, float angle) {
		super(scene, "JevilSpade", position);
		
		renderComponent = new RenderComponent();
		physicsComponent = new PhysicsComponent();
		
		scale = new Vector2(0.25f, 0.25f);
		float rad = (float) Math.toRadians(angle);
		Vector2 direction = new Vector2((float)Math.cos(rad), (float)Math.sin(rad));
		vel = Vector2.multiply(direction, SPEED);
		rotation = (int)angle;
		
		despawnTimer.restart();
	}

	@Override
	protected void initCollisionData() {
		hitboxes.add(new Hitbox(-4, -4, 8, 8, CollisionType.ENEMY_PROJECTILE, 2));
	}

	@Override
	protected void initSprites() {
		sprites = scene.game.getSpriteManager().getSprites("jevil");
	}

	@Override
	protected void initAnimations() {
		currentAnim = new Animation(sprites.get("spade"));
	}

	@Override
	public void update() {
		physicsComponent.update(this);
		
		if (despawnTimer.done)
			despawn();
		else despawnTimer.update();
	}

	@Override
	public void render(Graphics2D g) {
		renderComponent.render(g, this);
	}

}
