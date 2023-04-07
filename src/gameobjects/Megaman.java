package gameobjects;

import java.awt.Graphics2D;

import components.EntityCollisionComponent;
import components.GravityComponent;
import components.PhysicsComponent;
import components.RenderComponent;
import components.TileCollisionComponent;
import general.KeyManager;
import general.Timer;
import graphics.Animation;
import graphics.Frame;
import scenes.GameScene;

public class Megaman extends Creature {

	final String SPRITE_PATH = "gameobjects/megaman.png";
	final int ANCHOR_POINT = -18;
	
	KeyManager keyManager;
	
	RenderComponent renderComponent;
	PhysicsComponent physicsComponent;
	GravityComponent gravityComponent;
	TileCollisionComponent tileCollisionComponent;
	EntityCollisionComponent entityCollisionComponent;
	
	Hitbox normalHitbox;
	Hitbox slidingHitbox;
	
	final float GRAVITY = 0.2f;
	final float WALK_SPEED = 1.2f;
	final float JUMP_SPEED = -5f;
	final float HURT_SLIDE_SPEED = 0.5f;
	final float TELEPORT_SPEED = -6f;
	
	final int EXTENDED_IFRAME_TIME = 1000;
	
	boolean sliding;
	final float SLIDE_SPEED = 2.2f;
	final int SLIDING_TIME = 500;
	Timer slidingTimer;
	
	boolean shooting;
	boolean shootAnimPlaying;
	final int SHOOTING_TIME = 100;
	final int SHOOTING_ANIMATION_TIME = 300;
	Timer shootingTimer;
	Timer shootingAnimationTimer;
	final Vector2 FIRE_POINT = new Vector2(12, 0);
	
	public boolean teleporting;
	boolean teleportStarted;
	Timer teleportAnimTimer = new Timer(270); // 270 ms for animation
	
	public Megaman(GameScene scene, Vector2 position) {
		super(scene, "Megaman", position);

		keyManager = scene.game.getKeyManager();
		
		renderComponent = new RenderComponent();
		physicsComponent = new PhysicsComponent();
		gravityComponent = new GravityComponent();
		tileCollisionComponent = new TileCollisionComponent(scene, true);
		entityCollisionComponent = new EntityCollisionComponent();
		
		setMaxHealth(28);
		scene.lbManager.addLifeBar(this, "megaman");
		
		iframeTime = HURT_TIME + EXTENDED_IFRAME_TIME;
		iframeTimer = new Timer(iframeTime);
		
		sliding = false;
		slidingTimer = new Timer(SLIDING_TIME);
		
		shooting = false;
		shootAnimPlaying = false;
		shootingTimer = new Timer(SHOOTING_TIME);
		shootingAnimationTimer = new Timer(SHOOTING_ANIMATION_TIME);
		
		teleporting = false;
		teleportStarted = false;
	}
	
	@Override 
	protected void initCollisionData() {
		normalHitbox = new Hitbox(-6, -12, 14, 24, CollisionType.PLAYER);
		slidingHitbox = new Hitbox(-10, -4, 22, 16, CollisionType.PLAYER);
		hitboxes.add(normalHitbox);
		hitboxes.add(slidingHitbox);
		slidingHitbox.active = false;
	}
	
	@Override
	protected void initSprites() {
		sprites = scene.game.getSpriteManager().getSprites("megaman");
	}
	
	@Override
	protected void initAnimations() {
		Frame[] idle = new Frame[] { 
				new Frame(sprites.get("megaman_idle"), 3000), 
				new Frame(sprites.get("megaman_blink"), 250)
		};
		
		Frame[] walk = new Frame[] { 
				new Frame(sprites.get("megaman_walk1"), 125), 
				new Frame(sprites.get("megaman_walk2"), 125),  
				new Frame(sprites.get("megaman_walk3"), 125),  
				new Frame(sprites.get("megaman_walk2"), 125) 
		};
		
		Frame[] walkShoot = new Frame[] { 
				new Frame(sprites.get("megaman_shootwalk1"), 125), 
				new Frame(sprites.get("megaman_shootwalk2"), 125),  
				new Frame(sprites.get("megaman_shootwalk3"), 125),  
				new Frame(sprites.get("megaman_shootwalk2"), 125)
		};
		
		Frame[] teleport = new Frame[] {
				new Frame(sprites.get("megaman_teleport_08"), 30),
				new Frame(sprites.get("megaman_teleport_07"), 30),
				new Frame(sprites.get("megaman_teleport_06"), 30),
				new Frame(sprites.get("megaman_teleport_05"), 30),
				new Frame(sprites.get("megaman_teleport_04"), 50),
				new Frame(sprites.get("megaman_teleport_03"), 50),
				new Frame(sprites.get("megaman_teleport_02"), 50),
				new Frame(sprites.get("megaman_teleport_01"), 5000)
		};
		
		anims = new Animation[] {
				new Animation(idle),
				new Animation(walk),
				new Animation(sprites.get("megaman_jump")),
				new Animation(sprites.get("megaman_shoot")),
				new Animation(walkShoot),
				new Animation(sprites.get("megaman_shootjump")),
				new Animation(sprites.get("megaman_slide")),
				new Animation(sprites.get("megaman_hurt")),
				new Animation(teleport)
		};
		
		currentAnim = anims[0];
	}
	
	@Override
	public void _update() {
		updateInputMovement();
		
		physicsComponent.update(this);
		gravityComponent.update(this, GRAVITY);
		
		if (shooting) {
			if (shootingTimer.done)
				shooting = false;
			else
				shootingTimer.update();				
		}
		
		if (shootAnimPlaying) {
			if (shootingAnimationTimer.done)
				shootAnimPlaying = false;
			else
				shootingAnimationTimer.update();
		}
		
		if (sliding) {
			slidingHitbox.active = true;
			normalHitbox.active = false;
			if (slidingTimer.done || !grounded)
				sliding = false;
			else
				slidingTimer.update();
		} else {
			slidingHitbox.active = false;
			normalHitbox.active = true;
		}
		
		if (teleporting) {
			if (!teleportStarted) {
				teleportAnimTimer.restart();
				teleportStarted = true;
				scene.game.soundManager.playSound("megamanexit");
			}
			
			if (teleportAnimTimer.done) {
				vel.y = TELEPORT_SPEED;
			} else {
				teleportAnimTimer.update();
				vel.x = 0;
			}
		} 
		
		if (!teleportAnimTimer.done) {
			tileCollisionComponent.update(this);
			entityCollisionComponent.update(this);
		}
		
		updateAnimation();
	}
	
	@Override
	public void _render(Graphics2D g) {
		renderComponent.render(g, this);
	}
	
	private void updateInputMovement() {
		if (teleporting)
			return;
		
		if (!hurt) {
			
			if (!sliding) {
				if (keyManager.left && !keyManager.right) {
					vel.x = -1 * WALK_SPEED; 
					flipped = true;
				} 
				else if (!keyManager.left && keyManager.right) {
					vel.x = WALK_SPEED; 
					flipped = false; 
				} 
				else 
					vel.x = 0;
				
				if (keyManager.shoot && !shooting)
					shoot();
			}
				
			if (keyManager.jumpPressed && grounded) {
				if (keyManager.down && !sliding) {
					slide();
				} else {
					vel.y += JUMP_SPEED;
					grounded = false;
				}
			}
			else if (!keyManager.jumpHeld && vel.y < 0)
				vel.y = 0;
			
			if (keyManager.slide && grounded && !sliding)
				slide();
		} 
		else {
			vel.x = flipped ? HURT_SLIDE_SPEED : HURT_SLIDE_SPEED * -1;
		}
	}
	
	private void slide() {
		sliding = true;
		vel.x = flipped ? SLIDE_SPEED * -1 : SLIDE_SPEED;
		slidingTimer.restart();
	}

	private void shoot() {
		shooting = true;
		shootAnimPlaying = true;
		shootingTimer.restart();
		shootingAnimationTimer.restart();
		
		scene.game.soundManager.playSound("megamanshoot");
		
		Vector2 lemonPos;
		if (flipped)
			lemonPos = new Vector2(pos.x - FIRE_POINT.x, pos.y - FIRE_POINT.y);
		else
			lemonPos = new Vector2(pos.x + FIRE_POINT.x, pos.y + FIRE_POINT.y);
		
		new Lemon(((GameScene)scene), new Vector2(lemonPos), !flipped);
	}
	
	private void updateAnimation() {
		
		if (teleporting)
			currentAnim = anims[8];
		else if (hurt)
			currentAnim = anims[7];
		else if (!grounded)
			currentAnim = shootAnimPlaying ? anims[5] : anims[2];
		else if (sliding)
			currentAnim = anims[6];
		else if (vel.x != 0) {
			currentAnim = shootAnimPlaying ? anims[4] : anims[1];
			(shootAnimPlaying ? anims[1] : anims[4]).update();
		} else
			currentAnim = shootAnimPlaying ? anims[3] : anims[0];

		currentAnim.update();
	}
	
	@Override
	protected void damageNoise() {
		scene.game.soundManager.playSound("megamandamage");
	}
}
