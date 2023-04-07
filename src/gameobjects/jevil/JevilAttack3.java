package gameobjects.jevil;

import java.awt.Graphics2D;

import components.PhysicsComponent;
import components.RenderComponent;
import gameobjects.Creature;
import gameobjects.Projectile;
import gameobjects.Vector2;
import general.Timer;
import general.Utility;
import graphics.Animation;
import graphics.Frame;
import scenes.GameScene;

public class JevilAttack3 extends Projectile {

	RenderComponent renderComponent;
	PhysicsComponent physicsComponent;
	
	final int BOUNDS = 90;
	int spriteWidth, spriteHeight;
	
	int startY;
	Vector2 gotoPos;
	final float DOWN_SPEED = 2f;
	
	boolean moving = false;
	final float MOVE_SPEED = 1.3f;
	Timer moveAnimTimer = new Timer(600);
	Creature player;
	JevilHeart[] hearts = new JevilHeart[4];
	boolean startedMove = false;
	
	public JevilAttack3(GameScene scene, Creature player) {
		super(scene, "JevilHeartbox", Vector2.center(scene.game));
		renderComponent = new RenderComponent();
		physicsComponent = new PhysicsComponent();
		
		gotoPos = generateRandomPos();
		this.player = player;
		
		if (gotoPos.y > scene.game.height / 2f) {
			startY = 0 - spriteHeight;
			vel.y = DOWN_SPEED;
		} else {
			startY = scene.game.height + spriteHeight;
			vel.y = DOWN_SPEED * -1;
		}
			
		pos = new Vector2(gotoPos.x, startY);
	}

	@Override
	protected void initCollisionData() {}

	@Override
	protected void initSprites() {
		sprites = scene.game.getSpriteManager().getSprites("jevil");
		spriteWidth = sprites.get("heartbox1").width;
		spriteHeight = sprites.get("heartbox1").height;
	}

	@Override
	protected void initAnimations() {
		Frame[] flash = new Frame[] {
				new Frame(sprites.get("heartbox1"), 100),
				new Frame(sprites.get("heartbox2"), 100)
		};
		
		anims = new Animation[] {
				new Animation(flash),
				new Animation(sprites.get("circle"))
		};
		
		currentAnim = anims[0];
	}

	@Override
	public void update() {
		physicsComponent.update(this);
		
		if (!moving && Math.abs(gotoPos.y - pos.y) < 5) {
			moving = true;
			vel = Vector2.zero();
			moveAnimTimer.restart();
			currentAnim = anims[1];
			scene.game.soundManager.playSound("jevil_explosion");
			new JevilHeart((GameScene) scene, pos, 0);
			new JevilHeart((GameScene) scene, pos, 90);
			new JevilHeart((GameScene) scene, pos, 180);
			new JevilHeart((GameScene) scene, pos, 270);
		}
		
		if (moving) {
			if (moveAnimTimer.done) {
				if (!startedMove) {
					vel = Vector2.multiply(Vector2.direction(player.pos, pos), MOVE_SPEED);
					transparency = 0;
					startedMove = true;
				}
			} else {
				moveAnimTimer.update();
				transparency = Utility.lerp(1, 0, moveAnimTimer.getProgressed());
				float value = Utility.lerp(0, 5, moveAnimTimer.getProgressed());
				scale = new Vector2(value, value);	
			}
		}
		
		currentAnim.update();
	}

	@Override
	public void render(Graphics2D g) {
		renderComponent.render(g, this);
	}
	
	private Vector2 generateRandomPos() {
		int x;
		while (true) {
			x = Utility.randomIntBetween((int)(0 + (spriteWidth/2f)), 
					(int)(scene.game.width - (spriteWidth/2f)));
			if (x > (scene.game.width - BOUNDS) || x < BOUNDS)
				break;
		}
		int y = Utility.randomIntBetween(0 + (spriteHeight*2), scene.game.height - (spriteHeight*2));
		return new Vector2(x, y);
	}

}
