package gameobjects.whispy;

import java.awt.Graphics2D;

import components.EntityCollisionComponent;
import components.RenderComponent;
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

public class WhispyWoods extends Boss {
	
	RenderComponent renderComponent;
	EntityCollisionComponent entityCollisionComponent;
	
	int APPLE_TIME_LOW = 1000;
	int APPLE_TIME_HIGH = 5000;
	Timer appleSpawnTimer = new Timer(2000);
	
	boolean blowing = false;
	Vector2 mouthPos;
	int BLOW_TIME_LOW = 2000;
	int BLOW_TIME_HIGH = 7000;
	Timer blowTimer = new Timer(2000);
	Timer blowAnimTimer = new Timer(500);
	boolean spawningGordo = false;
	
	public WhispyWoods(GameScene scene, Vector2 position, Creature player) {
		super(scene, "WhispyWoods", position, player);
		scene.background = new WhispyBackground(scene);
		pos.x += 8;
		pos.y -= 12;
		mouthPos = new Vector2(pos.x - 15, pos.y + 10);
		
		renderComponent = new RenderComponent();
		entityCollisionComponent = new EntityCollisionComponent();
		
		setMaxHealth(56);
		scene.lbManager.addLifeBar(this, "whispy");
	}

	@Override
	protected void initCollisionData() {
		hitboxes.add(new Hitbox(-20, -45, 50, 90, CollisionType.ENEMY, 27));
	}

	@Override
	protected void initSprites() {
		sprites = scene.game.getSpriteManager().getSprites("whispy");
	}

	@Override
	protected void initAnimations() {
		Frame[] idle = new Frame[] {
				new Frame(sprites.get("idle"), 4000),
				new Frame(sprites.get("blink1"), 150),
				new Frame(sprites.get("blink2"), 150),
				new Frame(sprites.get("blink1"), 150)
		};
		
		Frame[] hurt = new Frame[] {
				new Frame(sprites.get("hurt2"), 75),
				new Frame(sprites.get("hurt1"), 75)
		};
		
		Frame[] blow = new Frame[] {
				new Frame(sprites.get("blow1"), 100),
				new Frame(sprites.get("blow2"), 100),
				new Frame(sprites.get("blow3"), 500)
		};
		
		anims = new Animation[] {
				new Animation(idle),
				new Animation(hurt),
				new Animation(blow)
		};
		
		currentAnim = anims[0];
	}
	
	@Override
	public void _update() {
		
		if (appleSpawnTimer.done) {
			new WhispyApple((GameScene)scene, player);
			appleSpawnTimer.restart(Utility.randomIntBetween(APPLE_TIME_LOW, APPLE_TIME_HIGH));
		} else appleSpawnTimer.update();
		
		if (blowTimer.done) {
			blowing = true;
			anims[2].restart();
			scene.game.soundManager.playSound("whispy_puff");
			
			if (spawningGordo) {
				new WhispyGordo((GameScene)scene, mouthPos);
				spawningGordo = false;
			} 
			else {
				new WhispyPuff((GameScene)scene, mouthPos);
				spawningGordo = true;
			}
			
			blowAnimTimer.restart();
			blowTimer.restart(Utility.randomIntBetween(BLOW_TIME_LOW, BLOW_TIME_HIGH));
		} 
		else blowTimer.update();
		
		if (blowing) {
			if (blowAnimTimer.done)
				blowing = false;
			else blowAnimTimer.update();
		}
		
		updateAnimations();
		entityCollisionComponent.update(this);
	}

	@Override
	public void _render(Graphics2D g) {
		renderComponent.render(g, this);	
	}
	
	private void updateAnimations() {
		if (blowing)
			currentAnim = anims[2];
		else if (hurt)
			currentAnim = anims[1];
		else
			currentAnim = anims[0];
		
		currentAnim.update();
	}

}
