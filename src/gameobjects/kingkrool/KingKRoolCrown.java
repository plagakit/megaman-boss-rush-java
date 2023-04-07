package gameobjects.kingkrool;

import java.awt.Graphics2D;

import components.PhysicsComponent;
import components.RenderComponent;
import gameobjects.CollisionType;
import gameobjects.Hitbox;
import gameobjects.Projectile;
import gameobjects.Vector2;
import graphics.Animation;
import graphics.Frame;
import scenes.GameScene;

public class KingKRoolCrown extends Projectile {

	RenderComponent renderComponent;
	PhysicsComponent physicsComponent;
	
	public KingKRoolCrown(GameScene scene, Vector2 position) {
		super(scene, "KingKRoolCrown", position);
		
		renderComponent = new RenderComponent();
		physicsComponent = new PhysicsComponent();
	}

	@Override
	protected void initCollisionData() {
		hitboxes.add(new Hitbox(-8, -8, 16, 16, CollisionType.ENEMY_PROJECTILE, 3));
	}

	@Override
	protected void initSprites() {
		sprites = scene.game.getSpriteManager().getSprites("kingkrool");
	}

	@Override
	protected void initAnimations() {
		Frame[] spin = new Frame[] {
				new Frame(sprites.get("crown1"), 50),
				new Frame(sprites.get("crown2"), 50),
				new Frame(sprites.get("crown3"), 50),
				new Frame(sprites.get("crown4"), 50),
				new Frame(sprites.get("crown5"), 50),
				new Frame(sprites.get("crown6"), 50),
				new Frame(sprites.get("crown7"), 50),
				new Frame(sprites.get("crown8"), 50),
				new Frame(sprites.get("crown9"), 50),
				new Frame(sprites.get("crown10"), 50),
				new Frame(sprites.get("crown11"), 50),
				new Frame(sprites.get("crown12"), 50)
		};
		
		currentAnim = new Animation(spin);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(Graphics2D g) {
		currentAnim.update();
		renderComponent.render(g, this);
	}

}
