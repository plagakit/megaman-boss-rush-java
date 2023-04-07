package gameobjects.whispy;

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
import general.Utility;
import graphics.Animation;
import graphics.Frame;
import scenes.GameScene;

public class WhispyGordo extends Projectile {

	RenderComponent renderComponent;
	PhysicsComponent physicsComponent;
	GravityComponent gravityComponent;
	TileCollisionComponent tileCollisionComponent;
	
	Timer despawnTimer = new Timer(3000);
	final float MOVE_SPEED = -1.5f;
	final float BOUNCE_SPEED = -1.9f;
	final float GRAVITY = 0.075f;
	final float ROT_SPEED = -2.5f;
	
	public WhispyGordo(GameScene scene, Vector2 position) {
		super(scene, "WhispyGordo", new Vector2(position));
		
		renderComponent = new RenderComponent();
		physicsComponent = new PhysicsComponent();
		gravityComponent = new GravityComponent();
		tileCollisionComponent = new TileCollisionComponent(scene);
		
		vel.x = MOVE_SPEED;
	}

	@Override
	protected void initCollisionData() {
		hitboxes.add(new Hitbox(-6, -6, 12, 12, CollisionType.ENEMY_PROJECTILE, 4));
	}

	@Override
	protected void initSprites() {
		sprites = scene.game.getSpriteManager().getSprites("whispy");
	}

	@Override
	protected void initAnimations() {
		Frame[] idle = new Frame[] {
				new Frame(sprites.get("gordo1"), 250),
				new Frame(sprites.get("gordo2"), 250)
		};
		
		currentAnim = new Animation(idle);
	}

	@Override
	public void update() {
		gravityComponent.update(this, GRAVITY);
		physicsComponent.update(this);

		if (grounded) {
			vel.y = BOUNCE_SPEED;
			grounded = false;
		}
		
		rotation += ROT_SPEED * Utility.timeAdjust;
		
		if (despawnTimer.done)
			despawn();
		else despawnTimer.update();
		
		tileCollisionComponent.update(this);
		currentAnim.update();
	}

	@Override
	public void render(Graphics2D g) {
		renderComponent.render(g, this);
	}

}
