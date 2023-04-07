package gameobjects.jevil;

import java.awt.Graphics2D;

import components.EntityCollisionComponent;
import components.PhysicsComponent;
import components.RenderComponent;
import gameobjects.Boss;
import gameobjects.CollisionType;
import gameobjects.Creature;
import gameobjects.Hitbox;
import gameobjects.Vector2;
import general.Game;
import general.Timer;
import general.Utility;
import graphics.Animation;
import graphics.Frame;
import scenes.GameScene;

public class Jevil extends Boss {
	
	RenderComponent renderComponent;
	PhysicsComponent physicsComponent;
	EntityCollisionComponent entityCollisionComponent;
	
	JevilHead jevilHead;
	boolean startHurt;
	
	int byebyeTimerState = 0;
	Timer byebyeTimer = new Timer(2000);
	
	enum MovementState {
		WAITING,
		TELEPORTINGIN,
		TELEPORTINGOUT,
		STATIC
	}
	final int TP_TIME_LOW = 500;
	final int TP_TIME_HIGH = 6000;
	Timer tpWaitTimer = new Timer(1500);
	Timer teleportAnimTimer = new Timer(150);

	enum Action {
		IDLING,
		ATTACK1,
		ATTACK2,
		ATTACK3,
		ATTACK4,
		ATTACK5;

		public static Action randomAction() {
			return Action.values()[Utility.random.nextInt(Action.values().length)];
		}
		
		public static Action randomActionThatIsNot(Action action) {
			while (true) {
				Action a = randomAction();
				if (a != action)
					return a;
			}
		}
	}
	Action currentAction;
	Action previousAttack = null;
	MovementState currentMoveState;
	boolean startingAttack = false;
	Timer attackIntroTimer = new Timer(1000);
	
	final int IDLE_TIME_LOW = 3000;
	final int IDLE_TIME_HIGH = 10000;
	Timer idleTimer = new Timer(2000);
	
	int attack1Counter = 0;
	final int ATTACK1_MAX = 10;
	Timer attack1Timer = new Timer(750);
	
	int attack2Counter = 0;
	final int ATTACK2_MAX = 80;
	Timer attack2Timer = new Timer(150);
	
	int attack3Counter = 0;
	final int ATTACK3_MAX = 15;
	Timer attack3Timer = new Timer(500);
	
	JevilScythe[] attack4 = new JevilScythe[4];
	Timer attack4Timer = new Timer(10000);
	
	JevilCarousel[][] attack5 = new JevilCarousel[5][4];
	Timer attack5Timer = new Timer(14000);
	
	public Jevil(GameScene scene, Vector2 position, Creature player) {
		super(scene, "Jevil", position, player);
		scene.background = new JevilBackground(scene, Vector2.zero());
		
		renderComponent = new RenderComponent();
		physicsComponent = new PhysicsComponent();
		entityCollisionComponent = new EntityCollisionComponent();
		lemonDamage = 1;
		
		setMaxHealth(56);
		scene.lbManager.addLifeBar(this, "jevil");
		
		jevilHead = new JevilHead(scene, new Vector2(pos), this);
		
		startIdleAction();
		currentMoveState = MovementState.WAITING;
	}

	@Override
	protected void initCollisionData() {
		hitboxes.add(new Hitbox(-16, -16, 32, 32, CollisionType.ENEMY, 6));
	}
	
	@Override
	protected void initSprites() {
		sprites = scene.game.getSpriteManager().getSprites("jevil");
	}

	@Override
	protected void initAnimations() {
		Frame[] laugh = new Frame[] {
				new Frame(sprites.get("idle"), 75),
				new Frame(sprites.get("laugh"), 75),
		};
		
		Frame[] dance1 = new Frame[] {
				new Frame(sprites.get("dance1"), 75),
				new Frame(sprites.get("dance2"), 75),
				new Frame(sprites.get("dance3"), 75),
				new Frame(sprites.get("dance4"), 75),
				new Frame(sprites.get("dance5"), 75),
				new Frame(sprites.get("dance6"), 75),
				new Frame(sprites.get("dance7"), 75),
				new Frame(sprites.get("dance8"), 75)
		};
		
		Frame[] dance2 = new Frame[] {
				new Frame(sprites.get("dance1"), 75),
				new Frame(sprites.get("dance2"), 75),
				new Frame(sprites.get("dance3"), 75),
				new Frame(sprites.get("dance4"), 75),
				new Frame(sprites.get("dance5"), 75),
				new Frame(sprites.get("dance6"), 75),
				new Frame(sprites.get("dance7"), 75),
				new Frame(sprites.get("dance8"), 75),
				new Frame(sprites.get("dance9"), 75),
				new Frame(sprites.get("dance10"), 75),
				new Frame(sprites.get("dance11"), 75),
				new Frame(sprites.get("dance12"), 75),
				new Frame(sprites.get("dance13"), 75),
				new Frame(sprites.get("dance14"), 75),
				new Frame(sprites.get("dance15"), 75),
				new Frame(sprites.get("dance16"), 75)
		};
		
		anims = new Animation[] {
				new Animation(sprites.get("idle")),
				new Animation(laugh),
				new Animation(dance1),
				new Animation(dance2),
				new Animation(sprites.get("headless"))
		};
		
		currentAnim = anims[0];
	}
	
	float updateNum = 0;
	
	@Override
	public void _update() {
		physicsComponent.update(this);

		// Head move anim
		if (hurt) {
			if (!startHurt) {
				startHurt = true;
				int rand = Utility.randomIntBetween(0, 3);
				switch (rand) {
				case 0: scene.game.soundManager.playSound("jevil_laugh1"); break;
				case 1: scene.game.soundManager.playSound("jevil_laugh2"); break;
				case 2: scene.game.soundManager.playSound("jevil_laugh3"); break;
				}
			}
			jevilHead.active = true;
			jevilHead.update();
		} 
		else if (hurtTimer.done) {
			startHurt = false;
			jevilHead.active = false;
		}
			
		// Hover
		updateNum += Utility.timeAdjust;
		vel.y = (float)Math.sin(updateNum / 20.0f) / 2f;
			
		// Bye bye
		if (!player.alive) {
			if (byebyeTimerState == 0) {
				byebyeTimerState = 1;
				byebyeTimer.restart();
			}
					
			if (byebyeTimer.done && byebyeTimerState == 1) {
				scene.game.soundManager.playSound("jevil_byebye");
				byebyeTimerState = 2;
			} else byebyeTimer.update();
		}
		
		
		// AI
		
		// START ACTION
		if (currentAction == null) {
			
			currentAction = Action.randomActionThatIsNot(previousAttack);
			if (Game.DEBUG) System.out.println(currentAction.toString());
			
			if (currentAction == Action.IDLING)
				startIdleAction();
			else {	
				if (currentAction == Action.ATTACK1) {
					scene.game.soundManager.playSound("jevil_voice1");
					attack1Counter = 0;
					attack1Timer.restart();
				}
				else if (currentAction == Action.ATTACK2) {
					scene.game.soundManager.playSound("jevil_crying");
					attack2Counter = 0;
					attack2Timer.restart();
				}
				else if (currentAction == Action.ATTACK3) {
					scene.game.soundManager.playSound("jevil_voice2");
					attack3Counter = 0;
					attack3Timer.restart();
				}
				else if (currentAction == Action.ATTACK4) {
					scene.game.soundManager.playSound("jevil_voice3");
					scene.game.soundManager.playSound("jevil_womp");
					for (int i = 0; i < attack4.length; i++) {
						int angle = (int)(i * (360f / attack4.length));
						attack4[i] = new JevilScythe((GameScene)scene, Vector2.center(scene.game), angle);
					}
					attack4Timer.restart();
				}
				else if (currentAction == Action.ATTACK5) {
					scene.game.soundManager.playSound("jevil_voice1");
					scene.game.soundManager.playSound("jevil_womp");
					for (int i = -2; i < 3; i++) {
						int startAngle = i % 2 == 0 ? 0 : 45;
						for (int j = 0; j < 4; j++) {
							int angle = startAngle + (j * 90);
							attack5[i+2][j] = new JevilCarousel((GameScene)scene, angle, i);
						}
					}
					attack5Timer.restart();
				}
				
				startingAttack = true;
				attackIntroTimer.restart();
				if (currentMoveState == MovementState.WAITING)
					currentMoveState = MovementState.STATIC;
			}	
		}
		// START ACTION
	
		
		// ACTION EXECUTION
		// MOVEMENT
		if (currentMoveState == MovementState.WAITING) {
			if (tpWaitTimer.done) {
				currentMoveState = MovementState.TELEPORTINGIN;
				teleportAnimTimer.restart();
			}	
			else tpWaitTimer.update();
		}
		else if (currentMoveState == MovementState.TELEPORTINGIN) { 
			if (teleportAnimTimer.done) {
				scale.x = 0;
				scale.y = 1.2f;
				teleport();
			} else {
				teleportAnimTimer.update();
				scale.x = Utility.lerp(1, 0, teleportAnimTimer.getProgressed());
				scale.y = Utility.lerp(1, 1.2f, teleportAnimTimer.getProgressed());
			}
		} 
		else if (currentMoveState == MovementState.TELEPORTINGOUT) {
			if (teleportAnimTimer.done)
				startWaitMoveState();
			else {
				teleportAnimTimer.update();
				scale.x = Utility.lerp(0, 1, teleportAnimTimer.getProgressed());
				scale.y = Utility.lerp(1.2f, 1, teleportAnimTimer.getProgressed());
			}
		}
		// MOVEMENT
		
		// ATTACKING
		if (startingAttack) {
			if (attackIntroTimer.done) {
				startingAttack = false;
				currentMoveState = MovementState.TELEPORTINGIN;
				teleportAnimTimer.restart();
			} else attackIntroTimer.update();
		}
		
		if (currentAction == Action.IDLING) {
			if (idleTimer.done)
				currentAction = null;
			else idleTimer.update();
		}
		else if (currentAction == Action.ATTACK1) {
			if (attack1Timer.done) {
				if (attack1Counter == ATTACK1_MAX)
					startIdleAction();
				else {
					new JevilAttack1((GameScene)scene, player);
					attack1Counter++;
					attack1Timer.restart();
				}
			} else attack1Timer.update();
		}
		else if (currentAction == Action.ATTACK2) {
			if (attack2Timer.done) {
				if (attack2Counter == ATTACK2_MAX)
					startIdleAction();
				else {
					new JevilDiamond((GameScene)scene);
					attack2Counter++;
					attack2Timer.restart();
				}
			} else attack2Timer.update();
		}
		else if (currentAction == Action.ATTACK3) {
			if (attack3Timer.done) {
				if (attack3Counter == ATTACK3_MAX)
					startIdleAction();
				else {
					new JevilAttack3((GameScene)scene, player);
					attack3Counter++;
					attack3Timer.restart();
				}
			} else attack3Timer.update();
		}
		else if (currentAction == Action.ATTACK4) {
			if (attack4Timer.done) {
				scene.game.soundManager.playSound("jevil_womp");
				for (JevilScythe j : attack4)
					j.despawn();
				startIdleAction();
			} else attack4Timer.update();
		}
		else if (currentAction == Action.ATTACK5) {
			if (attack5Timer.done) {
				scene.game.soundManager.playSound("jevil_womp");
				
				for (int i = 0; i < attack5.length; i++)
					for (int j = 0; j < attack5[i].length; j++)
						attack5[i][j].despawn();
				
				startIdleAction();
			} else attack5Timer.update();
		}
		// ATTACKING
		// ACTION EXECUTION
		
		// AI
		
		entityCollisionComponent.update(this);
		updateAnimations();
	}
	
	private void startIdleAction() {
		currentAction = Action.IDLING;
		idleTimer.restart(Utility.randomIntBetween(IDLE_TIME_LOW, IDLE_TIME_HIGH));
	}
	
	private void startWaitMoveState() {
		currentMoveState = MovementState.WAITING;
		tpWaitTimer.restart(Utility.randomIntBetween(TP_TIME_LOW, TP_TIME_HIGH));
		scale.x = 1;
		scale.y = 1;
	}
	
	private void teleport() {		
		int x, y;
		while (true) {
			int x1 = hitboxes.get(0).width;
			int x2 = scene.game.width - x1;
			int y1 = hitboxes.get(0).height;
			int y2 = scene.game.height - y1;

			x = Utility.randomIntBetween(x1, x2);
			y = Utility.randomIntBetween(y1, y2); 
			
			float mx1 = player.pos.x - (player.hitboxes.get(0).width * 3f);
			float mx2 = player.pos.x + (player.hitboxes.get(0).width * 3f);
			float my1 = player.pos.y - (player.hitboxes.get(0).height * 3f);
			float my2 = player.pos.y + (player.hitboxes.get(0).height * 3f);
			
			if (x > mx1 && x < mx2 && y > my1 && y < my2)
				continue;
			else break;
		}
		
		pos.x = x;
		pos.y = y;
		
		float posDiff = pos.x - player.pos.x;
		if (posDiff < 1)
			flipped = true;
		else flipped = false;
		
		currentMoveState = MovementState.TELEPORTINGOUT;
		teleportAnimTimer.restart();
	}
	
	@Override
	public void _render(Graphics2D g) {
		renderComponent.render(g, this);
		jevilHead.render(g);
	}
	
	private void updateAnimations() {
		float percentHP = (float)currentHealth / (float)maxHealth;

		if (hurt)
			currentAnim = anims[4];
		else if (startingAttack)
			currentAnim = anims[1];
		else if (percentHP < 0.3)
			currentAnim = anims[3];
		else if (percentHP < 0.6)
			currentAnim = anims[2];
		else
			currentAnim = anims[0];
					
		currentAnim.update();
	}


}
