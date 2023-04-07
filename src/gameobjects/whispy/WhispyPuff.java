package gameobjects.whispy;

import java.awt.Graphics2D;

import components.PhysicsComponent;
import components.RenderComponent;
import gameobjects.CollisionType;
import gameobjects.Hitbox;
import gameobjects.Projectile;
import gameobjects.Vector2;
import general.Timer;
import general.Utility;
import graphics.Animation;
import scenes.GameScene;

public class WhispyPuff extends Projectile {

	RenderComponent renderComponent;
	PhysicsComponent physicsComponent;
	
	final float X_SPEED_LOW = -1.5f;
	final float X_SPEED_HIGH = -3f;
	final float Y_SPEED_LOW = 0f;
	final float Y_SPEED_HIGH = 0.4f;
	float xSpeed, ySpeed;
	Timer despawnTimer = new Timer(2000);
	
	public WhispyPuff(GameScene scene, Vector2 position) {
		super(scene, "WhispyPuff", new Vector2(position));
		
		renderComponent = new RenderComponent();
		physicsComponent = new PhysicsComponent();
		
		xSpeed = Utility.randomFloatBetween(X_SPEED_LOW, X_SPEED_HIGH);
		ySpeed = Utility.randomFloatBetween(Y_SPEED_LOW, Y_SPEED_HIGH);
		
		boolean direction = Utility.random.nextBoolean();
		ySpeed *= direction ? -1 : 1;
		
		vel = new Vector2(xSpeed, ySpeed);
		
		despawnTimer.restart();
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
		currentAnim = new Animation(sprites.get("gust"));
	}

	@Override
	public void update() {
		physicsComponent.update(this);

		if (despawnTimer.done)
			despawn();
		else {
			despawnTimer.update();
			vel.x = Utility.lerp(xSpeed, 0, despawnTimer.getProgressed());
			vel.y = Utility.lerp(ySpeed, 0, despawnTimer.getProgressed());
		}
	}

	@Override
	public void render(Graphics2D g) {
		renderComponent.render(g, this);
	}

}
