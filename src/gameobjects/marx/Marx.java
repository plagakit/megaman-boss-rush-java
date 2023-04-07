package gameobjects.marx;

import java.awt.Graphics2D;

import components.EntityCollisionComponent;
import components.PhysicsComponent;
import components.RenderComponent;
import gameobjects.Boss;
import gameobjects.CollisionType;
import gameobjects.Creature;
import gameobjects.Hitbox;
import gameobjects.Vector2;
import general.Utility;
import graphics.Animation;
import graphics.Frame;
import scenes.GameScene;

public class Marx extends Boss {

	RenderComponent renderComponent;
	PhysicsComponent physicsComponent;
	EntityCollisionComponent entityCollisionComponent;
	MarxCrystal crystals;
	
	final float HOVER_SPEED = 8.4f;
	final float FAST_HOVER_SPEED = 3.5f;
	
	enum MovementState {
		IDLING,
		LAUGHING,
		FLYING
	}
	MovementState currentMoveState;
	
	public Marx(GameScene scene, Vector2 position, Creature player) {
		super(scene, "Marx", position, player);
		scene.background = new MarxBackground(scene, Vector2.zero());
		renderComponent = new RenderComponent();
		physicsComponent = new PhysicsComponent();
		entityCollisionComponent = new EntityCollisionComponent();
		crystals = new MarxCrystal(scene, pos);
		
		setMaxHealth(28);
		scene.lbManager.addLifeBar(this, "marx");
		
		currentMoveState = MovementState.FLYING;
	}
	
	@Override
	protected void initCollisionData() {
		hitboxes.add(new Hitbox(-15, -20, 30, 40, CollisionType.ENEMY, 4));
	}

	@Override
	protected void initSprites() {
		sprites = scene.game.getSpriteManager().getSprites("marx");
	}

	@Override
	protected void initAnimations() {
		Frame[] idle = new Frame[] {
			new Frame(sprites.get("idle1"), 40),
			new Frame(sprites.get("idle5"), 40),
			
			new Frame(sprites.get("idle2"), 40),
			new Frame(sprites.get("idle6"), 40),
			
			new Frame(sprites.get("idle3"), 40),
			new Frame(sprites.get("idle7"), 40),
			
			new Frame(sprites.get("idle4"), 40),
			new Frame(sprites.get("idle8"), 40),
			new Frame(sprites.get("idle4"), 40),
			new Frame(sprites.get("idle8"), 40),
			new Frame(sprites.get("idle4"), 40),
			new Frame(sprites.get("idle8"), 40),
			
			new Frame(sprites.get("idle3"), 40),
			new Frame(sprites.get("idle7"), 40),
			
			new Frame(sprites.get("idle2"), 40),
			new Frame(sprites.get("idle6"), 40),
			
			new Frame(sprites.get("idle1"), 40),
			new Frame(sprites.get("idle5"), 40)
		};
		
		Frame[] idle2 = new Frame[] {
				new Frame(sprites.get("idle1"), 30),
				new Frame(sprites.get("idle2"), 30),
				new Frame(sprites.get("idle3"), 30),
				new Frame(sprites.get("idle4"), 30),
				new Frame(sprites.get("idle4"), 30),
				new Frame(sprites.get("idle4"), 30),
				new Frame(sprites.get("idle3"), 30),			
				new Frame(sprites.get("idle2"), 30),			
				new Frame(sprites.get("idle1"), 30)
		};
		
		Frame[] flying = new Frame[] {
				new Frame(sprites.get("flying1"), 500),
				new Frame(sprites.get("flying2"), 2000)
		};
		
		anims = new Animation[] {
			new Animation(idle),
			new Animation(idle2),
			new Animation(flying)
		};
		
		currentAnim = anims[0];
	}

	float updateNum = 0;
	
	@Override
	public void _update() {
		physicsComponent.update(this);
		
		// Hover
		updateNum += Utility.timeAdjust;
		if (currentMoveState == MovementState.IDLING)
			vel.y = (float)Math.sin(updateNum / HOVER_SPEED);
		else if (currentMoveState == MovementState.LAUGHING)
			vel.y = (float)Math.sin(updateNum / FAST_HOVER_SPEED);
		else
			vel.y = 0;
		
		
		entityCollisionComponent.update(this);
		
		updateAnimations();
	}

	private void updateAnimations() {
		switch (currentMoveState) {
			case IDLING: currentAnim = anims[0]; break;
			case LAUGHING: currentAnim = anims[1]; break;
			case FLYING: currentAnim = anims[2]; break;
		}
		
		currentAnim.update();
		
		crystals.update();
		if (currentMoveState == MovementState.IDLING || currentMoveState == MovementState.LAUGHING)
			crystals.active = true;
		else crystals.active = false;
	}
	
	@Override
	public void _render(Graphics2D g) {
		if (crystals.active)
			crystals.render(g);
		
		renderComponent.render(g, this);		
	}

}
