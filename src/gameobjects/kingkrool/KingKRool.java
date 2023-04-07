package gameobjects.kingkrool;

import java.awt.Graphics2D;

import components.EntityCollisionComponent;
import components.GravityComponent;
import components.PhysicsComponent;
import components.RenderComponent;
import components.TileCollisionComponent;
import gameobjects.Boss;
import gameobjects.CollisionType;
import gameobjects.Creature;
import gameobjects.Hitbox;
import gameobjects.Vector2;
import general.Timer;
import general.Utility;
import graphics.Animation;
import graphics.Frame;
import scenes.GameScene;

public class KingKRool extends Boss {

	RenderComponent renderComponent;
	PhysicsComponent physicsComponent;
	GravityComponent gravityComponent;
	TileCollisionComponent tileCollisionComponent;
	EntityCollisionComponent entityCollisionComponent;
	Hitbox leftShield;
	Hitbox rightShield;
	
	enum Action {
		IDLING,
		WALKING,
		JUMPING,
		ATTACKING;
		
		public static Action randomAction() {
			return Action.values()[Utility.random.nextInt(Action.values().length)];
		}
	}
	Action currentAction;
	
	final int IDLE_TIME_LOW = 500;
	final int IDLE_TIME_HIGH = 2000;
	Timer idleTimer = new Timer(2000);
	
	final float WALK_SPEED = 2.3f;
	float walkToXPos;
	final float TOO_CLOSE_RANGE = 75;
	
	final float JUMP_SPEED = -6.5f;
	boolean jumped = false;
	
	boolean spawningCannonballs = false;
	Timer cannonballTimer = new Timer(750);
	final int CANNONBALL_COUNT = 6;
	int cannonballIndex = 0;
	
	public KingKRool(GameScene scene, Vector2 position, Creature player) {
		super(scene, "KingKRool", position, player);
		scene.background = new KingKRoolBackground(scene);
		
		renderComponent = new RenderComponent();
		physicsComponent = new PhysicsComponent();
		gravityComponent = new GravityComponent();
		tileCollisionComponent = new TileCollisionComponent(scene);
		entityCollisionComponent = new EntityCollisionComponent();
		
		setMaxHealth(28);
		scene.lbManager.addLifeBar(this, "kingkrool");
		
		flipped = true;
		currentAction = Action.IDLING;
		
		new KingKRoolCrown(scene, Vector2.center(scene.game));
	}
	
	@Override
	protected void initCollisionData() {
		hitboxes.add(new Hitbox(-12, -30, 24, 70, CollisionType.ENEMY, 7));
		leftShield = new Hitbox(-20, -12, 4, 50, CollisionType.REFLECT);
		rightShield = new Hitbox(16, -12, 4, 50, CollisionType.REFLECT);
	
		hitboxes.add(leftShield);
		hitboxes.add(rightShield);
		rightShield.active = false;
	}

	@Override
	protected void initSprites() {
		sprites = scene.game.getSpriteManager().getSprites("kingkrool");
	}

	@Override
	protected void initAnimations() {
		Frame[] idle = new Frame[] {
				new Frame(sprites.get("idle1"), 1000),
				new Frame(sprites.get("idle2"), 100),
				new Frame(sprites.get("idle3"), 100),
				new Frame(sprites.get("idle4"), 100),
				new Frame(sprites.get("idle2"), 100),
				new Frame(sprites.get("idle3"), 100),
				new Frame(sprites.get("idle4"), 100),
				new Frame(sprites.get("idle2"), 100),
				new Frame(sprites.get("idle3"), 100),
				new Frame(sprites.get("idle4"), 100)
		};
		
		Frame[] walk = new Frame[] {
				new Frame(sprites.get("walk1"), 75),
				new Frame(sprites.get("walk2"), 75),
				new Frame(sprites.get("walk3"), 75),
				new Frame(sprites.get("walk4"), 75),
				new Frame(sprites.get("walk5"), 75),
				new Frame(sprites.get("walk6"), 75),
				new Frame(sprites.get("walk7"), 75)
		};
		
		Frame[] jump = new Frame[] {
				new Frame(sprites.get("jump1"), 50),
				new Frame(sprites.get("jump2"), 50),
				new Frame(sprites.get("jump3"), 50),
				new Frame(sprites.get("jump4"), 50),
				new Frame(sprites.get("jump5"), 50),
				new Frame(sprites.get("falling"), 5000)
		};
		
		Frame[] throwCrown = new Frame[] {
				new Frame(sprites.get("throw1"), 75),
				new Frame(sprites.get("throw2"), 75),
				new Frame(sprites.get("throw3"), 75),
				new Frame(sprites.get("throw4"), 75),
				new Frame(sprites.get("throw5"), 1000)
		};
		
		Frame[] catchCrown = new Frame[] {
				new Frame(sprites.get("throw5"), 75),
				new Frame(sprites.get("throw4"), 75),
				new Frame(sprites.get("throw3"), 75),
				new Frame(sprites.get("throw2"), 75),
				new Frame(sprites.get("throw1"), 75)
		};
		
		anims = new Animation[] {
				new Animation(idle),
				new Animation(walk),
				new Animation(sprites.get("falling")),
				new Animation(jump),
				new Animation(throwCrown),
				new Animation(sprites.get("throw5")),
				new Animation(catchCrown)
		};
		
		currentAnim = anims[0];
	}

	@Override
	public void _update() {
		physicsComponent.update(this);
		gravityComponent.update(this);
		
		// AI
		 
		if (currentAction == null) {
			
			currentAction = Action.WALKING;
			
			if (currentAction == Action.IDLING) {
				idleTimer.restart(Utility.randomIntBetween(IDLE_TIME_LOW, IDLE_TIME_HIGH));
			}
			else if (currentAction == Action.WALKING) {
				
				if (Math.abs(player.pos.x - pos.x) < TOO_CLOSE_RANGE) {
					System.out.format("WITHIN %f RANGE\n", TOO_CLOSE_RANGE);
				} else {
					walkToXPos = player.pos.x;
				}
				
				vel.x = Math.signum(walkToXPos - pos.x) * WALK_SPEED;
				scene.game.soundManager.playSound("kingkrool_walk");
			}
			else if (currentAction == Action.JUMPING) {
				jump();
			}
			else if (currentAction == Action.ATTACKING) {
				
			}
		}
		
		//System.out.println(currentAction.toString());
		
		if (currentAction == Action.IDLING) {
			if (idleTimer.done)
				currentAction = null;
			else idleTimer.update();
		}	
		else if (currentAction == Action.WALKING) {
			if (Math.abs(walkToXPos - pos.x) <= 10) {
				currentAction = Action.IDLING;
				idleTimer.restart(Utility.randomIntBetween(IDLE_TIME_LOW, IDLE_TIME_HIGH));
				vel.x = 0;
				scene.game.soundManager.stopSound("kingkrool_walk");
			}
		}
		else if (currentAction == Action.JUMPING) {
			if (grounded) {
				if (jumped) {
					jumped = false;
					currentAction = Action.IDLING;
					idleTimer.restart(Utility.randomIntBetween(IDLE_TIME_LOW, IDLE_TIME_HIGH));
					vel.x = 0;
					
					if (spawningCannonballs == false) {
						spawningCannonballs = true;
						cannonballIndex = 0;
					}
					
				} else jump();
			}
		}
		
		if (spawningCannonballs) {
			if (cannonballTimer.done) {
				float xPos = ((float)(scene.game.width) / CANNONBALL_COUNT) * (cannonballIndex);
				new Cannonball((GameScene)scene, new Vector2(xPos, -16));
				
				cannonballTimer.restart();
				cannonballIndex++;
				
				if (cannonballIndex > CANNONBALL_COUNT)
					spawningCannonballs = false;
			} else cannonballTimer.update();
		}
		
		// AI
		
		tileCollisionComponent.update(this);
		entityCollisionComponent.update(this);
	}
	
	private void jump() {
		anims[3].restart();
		grounded = false;
		jumped = true;
		
		walkToXPos = player.pos.x;
		vel.x = Math.signum(walkToXPos - pos.x) * WALK_SPEED;
		vel.y += JUMP_SPEED;
		
		scene.game.soundManager.playSound("kingkrool_jump");
	}
	
	private void updateAnimations() {
		if (currentAction == Action.JUMPING && vel.y < 0)
			currentAnim = anims[3];
		else if (currentAction == Action.JUMPING && vel.y >= 0)
			currentAnim = anims[2];
		else if (currentAction == Action.WALKING)
			currentAnim = anims[1];
		else
			currentAnim = anims[0];
		
		if (currentAction != Action.WALKING && currentAction != Action.JUMPING) {
			if (!flipped && pos.x - player.pos.x > 0) {
				flipped = true;
				leftShield.active = true;
				rightShield.active = false;
			} else if (flipped && pos.x - player.pos.x < 0) {
				flipped = false;
				leftShield.active = false;
				rightShield.active = true;
			}
		}
		
		currentAnim.update();
	}
	
	@Override
	protected void damageNoise() {
		scene.game.soundManager.playSound("enemydamage");
		scene.game.soundManager.playSound("kingkrool_hurt");
	}

	@Override
	public void _render(Graphics2D g) {
		updateAnimations();
		renderComponent.render(g, this);
	}

}
