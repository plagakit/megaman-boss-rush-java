package gameobjects.jevil;

import java.awt.Graphics2D;

import components.RenderComponent;
import gameobjects.Creature;
import gameobjects.Projectile;
import gameobjects.Vector2;
import general.Timer;
import general.Utility;
import graphics.Sprite;
import scenes.GameScene;

public class JevilAttack1 extends Projectile {

	RenderComponent renderComponent;
	Sprite currentSprite;
	Sprite teleport1;
	Sprite teleport2;
	
	Creature player;
	
	enum Action { TELEPORTINGIN, ATTACKING1, ATTACKING2, TELEPORTINGOUT };
	Action currentAction = Action.TELEPORTINGIN;
	Timer teleportAnimTimer = new Timer(150);
	Timer attackTimer = new Timer(500);
	
	JevilSpade[] spades = new JevilSpade[5];
	final float ANGLE_SPREAD = 90;
	
	final int BOUNDS = 90;
	
	public JevilAttack1(GameScene scene, Creature player) {
		super(scene, "JevilAttack1", Vector2.center(scene.game));
		
		renderComponent = new RenderComponent();
		currentSprite = teleport1;
		this.player = player;
		
		pos = generateRandomPos();
	}

	@Override
	protected void initCollisionData() {}

	@Override
	protected void initSprites() {
		sprites = scene.game.getSpriteManager().getSprites("jevil");
		teleport1 = sprites.get("teleport1");
		teleport2 = sprites.get("teleport2");
	}

	@Override
	protected void initAnimations() {}

	@Override
	public void update() {
		
		if (currentAction == Action.TELEPORTINGIN) {
			if (teleportAnimTimer.done) {
				currentAction = Action.ATTACKING1;
				scale.x = 1;
				scale.y = 1;
			} else {
				teleportAnimTimer.update();
				scale.x = Utility.lerp(0, 1, teleportAnimTimer.getProgressed());
				scale.y = Utility.lerp(1.2f, 1, teleportAnimTimer.getProgressed());
			}
		}
		
		else if (currentAction == Action.ATTACKING1) {
			if (attackTimer.done) {
				currentAction = Action.ATTACKING2;
				
				currentSprite = teleport2;
				scene.game.soundManager.playSound("jevil_oah");
				
				Vector2 centerDir = Vector2.direction(player.pos, pos);
				float centerAngle = (float)Math.toDegrees(Math.atan(centerDir.y / centerDir.x));
				if (centerDir.x < 0) centerAngle += 180;
				
				float startAngle = centerAngle - (ANGLE_SPREAD / 2f);
				float increment = ANGLE_SPREAD / (spades.length - 1);
				
				for (int i = 0; i < spades.length; i++) {
					float angle = startAngle + (increment * i);
					spades[i] = new JevilSpade((GameScene)scene, new Vector2(pos), angle);
				}
				
				attackTimer.restart();
			} else attackTimer.update();
		}
		
		else if (currentAction == Action.ATTACKING2) {
			if (attackTimer.done) {
				currentAction = Action.TELEPORTINGOUT;
				teleportAnimTimer.restart();
			} else attackTimer.update();
		}
		
		else if (currentAction == Action.TELEPORTINGOUT) {
			if (teleportAnimTimer.done) {
				scale.x = 0;
				scale.y = 0;
				despawn();
			} else {
				teleportAnimTimer.update();
				scale.x = Utility.lerp(1, 0, teleportAnimTimer.getProgressed());
				scale.y = Utility.lerp(1, 1.2f, teleportAnimTimer.getProgressed());
			}
		}
	}

	private Vector2 generateRandomPos() {
		int x;
		while (true) {
			x = Utility.randomIntBetween((int)(0 + (teleport2.width/2f)), 
					(int)(scene.game.width - (teleport2.width/2f)));
			if (x > (scene.game.width - BOUNDS) || x < BOUNDS)
				break;
		}
		int y = Utility.randomIntBetween(0 + teleport2.height, scene.game.height - teleport2.height);
		return new Vector2(x, y);
	}
	
	@Override
	public void render(Graphics2D g) {
		renderComponent.render(g, this, currentSprite);
	}

}
