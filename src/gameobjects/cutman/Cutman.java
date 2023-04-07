package gameobjects.cutman;

import java.awt.Graphics2D;

import components.EntityCollisionComponent;
import components.GravityComponent;
import components.PhysicsComponent;
import components.RenderComponent;
import components.TileCollisionComponent;
import gameobjects.Boss;
import gameobjects.CollisionType;
import gameobjects.Creature;
import gameobjects.Entity;
import gameobjects.Hitbox;
import gameobjects.Vector2;
import general.Timer;
import general.Utility;
import graphics.Animation;
import graphics.Frame;
import scenes.GameScene;

public class Cutman extends Boss {

	RenderComponent renderComponent;
	PhysicsComponent physicsComponent;
	GravityComponent gravityComponent;
	TileCollisionComponent tileCollisionComponent;
	EntityCollisionComponent entityCollisionComponent;
	
	enum Action {
		WALKING,
		JUMPING,
		IDLING
	}
	Action[] actions = Action.values();
	
	Action currentAction;
	
	final float WALK_SPEED = 1.1f;
	final float WALK_SPEED_LOW = 0.5f;
	final float WALK_SPEED_HIGH = 2f;
	final float JUMP_SPEED_LOW = 3.5f;
	final float JUMP_SPEED_HIGH = 6.5f;
	
	final int IDLE_TIME_LOW = 500;
	final int IDLE_TIME_HIGH = 2500;
	Timer idleTimer = new Timer(2000);
	
	boolean hasScissors;
	final float SCISSOR_CHANCE = 0.5f;
	Timer scissorTimer = new Timer(3000);
	boolean throwing;
	Timer throwTimer = new Timer(300);
	Timer throwAnimTimer = new Timer(600);
	final Vector2 SCISSOR_SPAWN = new Vector2(0, -6);
	
	public Cutman(GameScene scene, Vector2 position, Creature player) {
		super(scene, "Cutman", position, player);
		
		renderComponent = new RenderComponent();
		physicsComponent = new PhysicsComponent();
		gravityComponent = new GravityComponent();
		tileCollisionComponent = new TileCollisionComponent(scene);
		entityCollisionComponent = new EntityCollisionComponent();
		lemonDamage = 3;
		
		setMaxHealth(28);
		scene.lbManager.addLifeBar(this, "cutman");
		
		flipped = false;

		currentAction = Action.IDLING;
		
		hasScissors = true;
		throwing = false;
	}

	@Override
	protected void initCollisionData() {
		hitboxes.add(new Hitbox(-10, -10, 20, 26, CollisionType.ENEMY, 2));
	}

	@Override
	protected void initSprites() {
		sprites = scene.game.getSpriteManager().getSprites("cutman");
	}

	@Override
	protected void initAnimations() {
		Frame[] idle = new Frame[] {
				new Frame(sprites.get("cutman_idle1"), 150),
				new Frame(sprites.get("cutman_idle2"), 150)
		};
		
		Frame[] walk = new Frame[] {
				new Frame(sprites.get("cutman_walk1"), 150),
				new Frame(sprites.get("cutman_walk2"), 150),
				new Frame(sprites.get("cutman_walk3"), 150),
				new Frame(sprites.get("cutman_walk2"), 150)
		};
		
		Frame[] altIdle = new Frame[] {
				new Frame(sprites.get("cutman_altidle1"), 150),
				new Frame(sprites.get("cutman_altidle2"), 150)
		};
		
		Frame[] altWalk = new Frame[] {
				new Frame(sprites.get("cutman_altwalk1"), 150),
				new Frame(sprites.get("cutman_altwalk2"), 150),
				new Frame(sprites.get("cutman_altwalk3"), 150),
				new Frame(sprites.get("cutman_altwalk2"), 150)
		};
		
		anims = new Animation[] {
				new Animation(idle),
				new Animation(walk),
				new Animation(sprites.get("cutman_jump")),
				new Animation(sprites.get("cutman_throw1")),
				new Animation(sprites.get("cutman_throw2")),
				new Animation(altIdle),
				new Animation(altWalk),
				new Animation(sprites.get("cutman_altjump"))
		};
		
		currentAnim = anims[0];
	}
	
	private void updateAnimation() {		
		if (vel.x > 0)
			flipped = true;
		else if (vel.x < 0)
			flipped = false;
		
		if (throwing)
			currentAnim = hasScissors ? anims[3] : anims[4];
		else if (!grounded)
			currentAnim = hasScissors ? anims[2] : anims[7];
		else if (vel.x != 0)
			currentAnim = hasScissors ? anims[1] : anims[6];
			else
			currentAnim = hasScissors ? anims[0] : anims[5];
		
		currentAnim.update();
	}
	
	@Override
	public void _update() {
		physicsComponent.update(this);
		gravityComponent.update(this);
		
		// AI START
		
		if (currentAction == null) {
			currentAction = getRandomAction();
			
			if (currentAction == Action.IDLING) {
				int idleTime = Utility.randomIntBetween(IDLE_TIME_LOW, IDLE_TIME_HIGH);
				idleTimer.restart(idleTime);
				scene.game.soundManager.playSound("cutmansnip");
			} 
			else if (currentAction == Action.JUMPING) {
				int direction = (int)Math.signum(player.pos.x - pos.x);
				vel.x = direction * Utility.randomFloatBetween(WALK_SPEED_LOW, WALK_SPEED_HIGH);
				vel.y += -1 * Utility.randomFloatBetween(JUMP_SPEED_LOW, JUMP_SPEED_HIGH);
				grounded = false;
			}
		}
		
		if (currentAction == Action.IDLING) {
			if (idleTimer.done) {
				currentAction = null;
			} else {
				idleTimer.update();
				vel.x = 0;
			}
		}
		else if (currentAction == Action.WALKING) {
			float distance = player.pos.x - pos.x;
			int direction = (int)Math.signum(distance);
			if (Math.abs(distance) > 16f)
				vel.x = WALK_SPEED * direction;
			else {
				vel.x = 0;
				currentAction = null;
			}
		}
		else if (currentAction == Action.JUMPING) {
			if (grounded) {
				currentAction = null;
				vel.x = 0;
			}
		}
			
			
		if (scissorTimer.done) {
			throwScissors();
		} else if (!throwing && hasScissors)
			scissorTimer.update();
		
		if (throwing) {
			if (throwTimer.done && hasScissors) {
				Vector2 scissorPos = new Vector2(pos.x + SCISSOR_SPAWN.x, pos.y + SCISSOR_SPAWN.y);
				new CutmanScissor(((GameScene)scene), scissorPos, this, player);

				hasScissors = false;
			} 
			else if (hasScissors)
				throwTimer.update();
			
			if (throwAnimTimer.done) {
				throwing = false;
			} else
				throwAnimTimer.update();
		}
		
		// AI END
		
		tileCollisionComponent.update(this);
		entityCollisionComponent.update(this);
		
		updateAnimation();
	}
	
	private void throwScissors() {
		throwing = true;
		throwTimer.restart();
		
		throwAnimTimer.restart();
		anims[6].restart();
		
		scissorTimer.restart();
	}
	
	@Override
	public void handleCollision(Hitbox self, Entity col, Hitbox hit) {
		super.handleCollision(self, col, hit);
		if (hit.collisionType == CollisionType.ENEMY_PROJECTILE && col.name == "CutmanScissor") {
			if (((CutmanScissor)col).returning) {
				((CutmanScissor)col).despawn();
				scene.game.soundManager.stopSound("cutmanscissor");
				hasScissors = true;
			}
		}
	}

	@Override
	public void _render(Graphics2D g) {
		renderComponent.render(g, this);	
	}
	
	private Action getRandomAction() {
		int pick = Utility.random.nextInt(7);
		switch (pick) {
		case 0: return Action.IDLING;
		case 1: return Action.WALKING;
		case 2: return Action.WALKING;
		case 3: return Action.JUMPING;
		case 4: return Action.JUMPING;
		case 5: return Action.JUMPING;
		case 6: return Action.JUMPING;
		default: return null;
		}
	}

}
