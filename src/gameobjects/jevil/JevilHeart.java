package gameobjects.jevil;

import java.awt.Graphics2D;

import components.RenderComponent;
import gameobjects.CollisionType;
import gameobjects.Hitbox;
import gameobjects.Projectile;
import gameobjects.Vector2;
import general.Timer;
import general.Utility;
import graphics.Animation;
import scenes.GameScene;

public class JevilHeart extends Projectile {

	RenderComponent renderComponent;
	
	Vector2 startPos;
	float angle;
	final float CIRCLE_SPEED = 3f;
	final float CIRCLE_RADIUS = 20f;
	
	Timer spawnTimer = new Timer(600);
	Timer despawnTimer = new Timer(7000);
	
	public JevilHeart(GameScene scene, Vector2 position, float angle) {
		super(scene, "JevilHeart", new Vector2(position));
		
		renderComponent = new RenderComponent();
		scale = new Vector2(0.5f, 0.5f);
		
		startPos = position;
		this.angle = angle;
		spawnTimer.restart();
		despawnTimer.restart();
	}

	@Override
	protected void initCollisionData() {
		hitboxes.add(new Hitbox(-4, -4, 8, 8, CollisionType.ENEMY_PROJECTILE, 3));
	}

	@Override
	protected void initSprites() {
		sprites = scene.game.getSpriteManager().getSprites("jevil");
	}

	@Override
	protected void initAnimations() {
		currentAnim = new Animation(sprites.get("heart"));
	}

	float updateNum = 0;
	
	@Override
	public void update() {
		if (spawnTimer.done) {
			updateNum += Utility.timeAdjust;
			float rad = (float)Math.toRadians((updateNum + angle / CIRCLE_SPEED) * CIRCLE_SPEED);
			pos.x = startPos.x + (float)Math.cos(rad) * CIRCLE_RADIUS;
			pos.y = startPos.y + (float)Math.sin(rad) * CIRCLE_RADIUS;
		} 
		else {
			spawnTimer.update();
			float rad = (float)Math.toRadians(angle);
			pos.x = startPos.x + (float)Math.cos(rad) * Utility.lerp(0, CIRCLE_RADIUS, spawnTimer.getProgressed());
			pos.y = startPos.y + (float)Math.sin(rad) * Utility.lerp(0, CIRCLE_RADIUS, spawnTimer.getProgressed());
		}
		
		if (despawnTimer.done)
			despawn();
		else despawnTimer.update();
	}

	@Override
	public void render(Graphics2D g) {
		renderComponent.render(g, this);
	}
	
}
