package gameobjects.kingkrool;

import java.awt.Graphics2D;

import components.GravityComponent;
import components.PhysicsComponent;
import components.RenderComponent;
import components.TileCollisionComponent;
import gameobjects.CollisionType;
import gameobjects.Hitbox;
import gameobjects.Projectile;
import gameobjects.Vector2;
import general.Timer;
import graphics.Animation;
import scenes.GameScene;

public class Cannonball extends Projectile {

	RenderComponent renderComponent;
	PhysicsComponent physicsComponent;
	GravityComponent gravityComponent;
	TileCollisionComponent tileCollisionComponent;
	
	Timer despawnTimer = new Timer(5000);
	final float GRAVITY = 0.22f;
	final float BOUNCE_SPEED = -2.1f;
	boolean bounced = false;
	
	public Cannonball(GameScene scene, Vector2 position) {
		super(scene, "Cannonball", position);
		
		renderComponent = new RenderComponent();
		physicsComponent = new PhysicsComponent();
		gravityComponent = new GravityComponent();
		tileCollisionComponent = new TileCollisionComponent(scene);
		
		scene.game.soundManager.playSound("kingkrool_cannon");
	}

	@Override
	protected void initCollisionData() {
		hitboxes.add(new Hitbox(-16, -16, 32, 32, CollisionType.ENEMY_PROJECTILE, 5));
	}

	@Override
	protected void initSprites() {
		sprites = scene.game.getSpriteManager().getSprites("kingkrool");
	}

	@Override
	protected void initAnimations() {
		currentAnim = new Animation(sprites.get("cannonball"));
	}

	@Override
	public void update() {
		physicsComponent.update(this);
		gravityComponent.update(this, GRAVITY);
		
		if (grounded) {
			vel.y = BOUNCE_SPEED;
			grounded = false;
			bounced = true;
		}
			
		if (!bounced)
			tileCollisionComponent.update(this);
		
		if (despawnTimer.done)
			despawn();
		else despawnTimer.update();
	}

	@Override
	public void render(Graphics2D g) {
		currentAnim.update();
		renderComponent.render(g, this);
	}

}
