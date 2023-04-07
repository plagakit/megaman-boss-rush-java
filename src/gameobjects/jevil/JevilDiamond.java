package gameobjects.jevil;

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

public class JevilDiamond extends Projectile {

	RenderComponent renderComponent;
	PhysicsComponent physicsComponent;
	
	Timer spawnTimer = new Timer(600);
	float acceleration = 0.01f;
	final float ACCELERATION_INCREMENT = 0.00015f;
	
	Timer despawnTimer = new Timer(5000);
	
	public JevilDiamond(GameScene scene) {
		super(scene, "JevilDiamond", Vector2.center(scene.game));
		
		renderComponent = new RenderComponent();
		physicsComponent = new PhysicsComponent();
		
		scale = new Vector2(0.5f, 0.5f);
		spawnTimer.restart();

		pos = new Vector2(Utility.randomFloatBetween(100, 300), 230);
	}

	@Override
	protected void initCollisionData() {
		hitboxes.add(new Hitbox(-3, -5, 6, 10, CollisionType.ENEMY_PROJECTILE, 4));
	}

	@Override
	protected void initSprites() {
		sprites = scene.game.getSpriteManager().getSprites("jevil");
	}

	@Override
	protected void initAnimations() {
		currentAnim = new Animation(sprites.get("diamond"));
	}

	@Override
	public void update() {
		physicsComponent.update(this);
		
		if (spawnTimer.done) {
			transparency = 1f;
			scale = new Vector2(0.75f, 0.75f);
			vel.y -= acceleration;
			acceleration += ACCELERATION_INCREMENT * Utility.timeAdjust;
			
			if (despawnTimer.done)
				despawn();
			else despawnTimer.update();
			
		} else {
			spawnTimer.update();
			transparency = Utility.lerp(0, 1, spawnTimer.getProgressed());
			scale.x = Utility.lerp(2, 0.75f, spawnTimer.getProgressed());
			scale.y = Utility.lerp(2, 0.75f, spawnTimer.getProgressed());
		}
	}

	@Override
	public void render(Graphics2D g) {
		renderComponent.render(g, this);
	}

}
