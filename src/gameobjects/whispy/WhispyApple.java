package gameobjects.whispy;

import java.awt.Graphics2D;

import components.GravityComponent;
import components.PhysicsComponent;
import components.RenderComponent;
import components.TileCollisionComponent;
import gameobjects.CollisionType;
import gameobjects.Creature;
import gameobjects.Hitbox;
import gameobjects.Projectile;
import gameobjects.Vector2;
import general.Timer;
import general.Utility;
import graphics.Animation;
import graphics.Sprite;
import scenes.GameScene;

public class WhispyApple extends Projectile {

	RenderComponent renderComponent;
	PhysicsComponent physicsComponent;
	GravityComponent gravityComponent;
	TileCollisionComponent tileCollisionComponent;
	Creature player;
	
	boolean spawning;
	Timer spawnTimer = new Timer(2000);
	Timer flashTimer = new Timer(200);
	final int BOUNDS_X = 300;
	final int BOUNDS_Y = 100; 
	
	boolean moving;
	final float MOVE_SPEED = 0.75f;
	final float BOUNCE_SPEED = -2.5f;
	final float GRAVITY = 0.075f;
	Timer rotateTimer = new Timer(250);
	
	Timer despawnTimer = new Timer(5000);
	
	public WhispyApple(GameScene scene, Creature player) {
		super(scene, "WhispyApple", Vector2.zero());
		this.player = player;
		
		renderComponent = new RenderComponent();
		physicsComponent = new PhysicsComponent();
		gravityComponent = new GravityComponent();
		tileCollisionComponent = new TileCollisionComponent(scene);
		
		pos = randomPos();
		spawning = true;
		spawnTimer.restart();
		flashTimer.restart();
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
		currentAnim = new Animation(sprites.get("apple"));
	}

	@Override
	public void update() {
		
		if (spawning) {
			if (spawnTimer.done) {
				spawning = false;
				transparency = 1;
				despawnTimer.restart();
			} 
			else {
				spawnTimer.update();
				
				if (flashTimer.done) {
					transparency = transparency == 1 ? 0 : 1;
					flashTimer.restart();
				} else flashTimer.update();
			}
		} else {
			gravityComponent.update(this, GRAVITY);
			physicsComponent.update(this);
			
			if (grounded) {
				grounded = false;
				vel.y = BOUNCE_SPEED;
				
				if (!moving) {
					moving = true;
					float direction = Math.signum(player.pos.x - pos.x);
					vel.x = MOVE_SPEED * direction;
				}
			}
			
			if (moving) {
				if (rotateTimer.done) {
					rotation -= 90;
					rotateTimer.restart();
				} else rotateTimer.update();
			}
			
			if (despawnTimer.done)
				despawn();
			else despawnTimer.update();
		}
		
		
		tileCollisionComponent.update(this);
	}
	
	private Vector2 randomPos() {
		int x, y;
		Sprite sprite = currentAnim.getCurrentFrame().sprite;
		while (true) {
			x = Utility.randomIntBetween((int)(0 + (sprite.width/2f)), (int)(scene.game.width - (sprite.width/2f)));
			if (x > (scene.game.width - BOUNDS_X) && x < BOUNDS_X)
				break;
		}
		
		while (true) {
			y = Utility.randomIntBetween((int)(0 + (sprite.height/2f)), 
					(int)(scene.game.width - (sprite.height/2f)));
			if (y < BOUNDS_Y)
				break;
		}
		
		return new Vector2(x, y);
	}

	@Override
	public void render(Graphics2D g) {
		renderComponent.render(g, this);
	}

}
