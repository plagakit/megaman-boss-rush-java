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

public class JevilScythe extends Projectile {

	RenderComponent renderComponent;
	
	boolean moving;
	Timer spawnTimer = new Timer(1000);
	boolean despawning = false;
	
	Vector2 startPos;
	float updateNum = 0;
	int startRot;
	final float ROT_SPEED = 5;
	final float CIRCLE_SPEED = 1;
	final float CIRCLE_RADIUS = 110;
	final float CONVERGE_SPEED = 35;
	
	public JevilScythe(GameScene scene, Vector2 position, int rotation) {
		super(scene, "JevilScythe", position);
		
		renderComponent = new RenderComponent();
		
		startPos = new Vector2(position);
		this.rotation = rotation;
		startRot = rotation;
		
		moving = false;
		spawnTimer.restart();
		
		scene.addGameObject(this);
	}

	@Override
	protected void initCollisionData() {
		hitboxes.add(new Hitbox(-15, -20, 30, 35, CollisionType.NONE, 2));
	}

	@Override
	protected void initSprites() {
		sprites = scene.game.getSpriteManager().getSprites("jevil");
	}

	@Override
	protected void initAnimations() {
		currentAnim = new Animation(sprites.get("scythe"));
	}
	
	@Override
	public void update() {
		
		if (despawning) {
			if (spawnTimer.done)
				((GameScene)scene).removeGameObject(this);
			else {
				transparency = Utility.lerp(1f, 0f, spawnTimer.getProgressed());
				spawnTimer.update();
				hitboxes.get(0).collisionType = CollisionType.NONE;
			}
			return;
		}
		
		if (!moving && spawnTimer.done) {
			moving = true;
			transparency = 1f;
			hitboxes.get(0).collisionType = CollisionType.ENEMY_PROJECTILE;
		} else if (!spawnTimer.done) {
			transparency = Utility.lerp(0f, 1f, spawnTimer.getProgressed());
			spawnTimer.update();
		} else {
			rotation -= ROT_SPEED * Utility.timeAdjust;
			updateNum += Utility.timeAdjust;
		}
		
		float rad = (float)Math.toRadians(updateNum + startRot);
		float offsetx = (float)Math.sin(rad / CIRCLE_SPEED) * CIRCLE_RADIUS;
		float offsety = (float)Math.cos(rad / CIRCLE_SPEED) * CIRCLE_RADIUS;
		pos.x = startPos.x + offsetx * (float)Math.abs(Math.cos(updateNum / CONVERGE_SPEED));
		pos.y = startPos.y + offsety * (float)Math.abs(Math.cos(updateNum / CONVERGE_SPEED));
	}

	@Override
	public void render(Graphics2D g) {
		renderComponent.render(g, this);
	}
	
	@Override
	public void despawn() {
		despawning = true;
		moving = false;
		spawnTimer.restart();
	}

}
