package gameobjects.jevil;

import java.awt.Graphics2D;

import components.RenderComponent;
import gameobjects.CollisionType;
import gameobjects.Hitbox;
import gameobjects.Projectile;
import gameobjects.Vector2;
import general.Timer;
import general.Utility;
import graphics.Sprite;
import scenes.GameScene;

public class JevilCarousel extends Projectile {

	RenderComponent renderComponent;
	Sprite sprite;
	
	Timer spawnTimer = new Timer(1000);
	boolean despawning = false;
	boolean moving = false;
	
	float startAngle;
	Vector2 startPos;
	final float Y_GAP = 50;
	boolean inBackground = false;
	
	public JevilCarousel(GameScene scene, float startAngle, int yDisplacement) {
		super(scene, "JevilCarousel", Vector2.center(scene.game));
		renderComponent = new RenderComponent();
		transparency = 0;
		
		this.startAngle = startAngle;
		startPos = Vector2.center(scene.game);
		startPos.y += yDisplacement * Y_GAP;
		flipped = true;
		
		float angle = (float)Math.toRadians(startAngle);
		float xMove = (float)(Math.sin(angle / 1f) * 150);
		float yMove = (float)(Math.sin(angle / 2f) * 30);
		float scaleMove = (float)Math.cos(angle / 1f);
		
		pos.x = startPos.x + xMove;
		pos.y = startPos.y + yMove;
		scale.x = scaleMove;
	}

	@Override
	protected void initCollisionData() {
		hitboxes.add(new Hitbox(-10, -8, 24, 20, CollisionType.ENEMY_PROJECTILE, 4));
	}

	@Override
	protected void initSprites() {
		sprites = scene.game.getSpriteManager().getSprites("jevil");
		int choice = Utility.randomIntBetween(0, 3);
		switch (choice) {
		case 0: sprite = sprites.get("carousel1"); break;
		case 1: sprite = sprites.get("carousel2"); break;
		case 2: sprite = sprites.get("carousel3"); break;
		}
	}

	@Override
	protected void initAnimations() {}

	float updateNum = 0;
	
	@Override
	public void update() {		
		if (despawning) {
			if (spawnTimer.done)
				((GameScene)scene).removeGameObject(this);
			else {
				transparency = Utility.lerp(1f, 0f, spawnTimer.getProgressed());
				spawnTimer.update();
				hitboxes.get(0).active = false;
			}
			return;
		}
		
		if (!moving && spawnTimer.done) {
			moving = true;
			hitboxes.get(0).active = true;
		} else if (!spawnTimer.done) {
			transparency = Utility.lerp(0f, 1f, spawnTimer.getProgressed());
			spawnTimer.update();
			hitboxes.get(0).active = false;
			
			if (scale.x < 0 && !inBackground) {
				inBackground = true;
				((GameScene)scene).moveToBackground(this);
			}
		} else {
			updateNum += Utility.timeAdjust;
			move(updateNum + startAngle);
		}
	}
	
	private void move(float originalAngle) {
		float angle = (float)Math.toRadians(originalAngle);
		float xMove = (float)(Math.sin(angle / 1f) * 150);
		float yMove = (float)(Math.sin(angle / 2f) * 30);
		float scaleMove = (float)Math.cos(angle / 1f);
		
		pos.x = startPos.x + xMove;
		pos.y = startPos.y + yMove;
		scale.x = scaleMove;
		
		if (scaleMove < 0 && !inBackground) {
			inBackground = true;
			((GameScene)scene).moveToBackground(this);
		} else if (scaleMove > 0 && inBackground) {
			inBackground = false;
			((GameScene)scene).moveToForeground(this);
		}
	}

	@Override
	public void render(Graphics2D g) {
		renderComponent.render(g, this, sprite);
	}
	
	@Override
	public void despawn() {
		despawning = true;
		moving = false;
		spawnTimer.restart();
	}

}
