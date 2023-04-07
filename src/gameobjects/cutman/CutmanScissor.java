package gameobjects.cutman;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import components.PhysicsComponent;
import components.RenderComponent;
import gameobjects.CollisionType;
import gameobjects.Creature;
import gameobjects.Hitbox;
import gameobjects.Projectile;
import gameobjects.Vector2;
import general.Game;
import general.Utility;
import graphics.Animation;
import scenes.GameScene;

public class CutmanScissor extends Projectile {

	RenderComponent renderComponent;
	PhysicsComponent physicsComponent;
	
	Creature cutman;
	Creature player;
	
	boolean returning;
	Vector2 startPos;
	final float EXTRA_DISTANCE = 30f; // 30 pixels extra
	Vector2 goalPos;
	Vector2 returningStartPos;
	
	Vector2 toDirection;
	Vector2 returnDirection = pos;
	
	final float FLY_SPEED = 2f;
	final float ROT_SPEED = 15;
	
	public CutmanScissor(GameScene scene, Vector2 position, Creature cutman, Creature player) {
		super(scene, "CutmanScissor", position);

		this.cutman = cutman;
		this.player = player;
		
		renderComponent = new RenderComponent();
		physicsComponent = new PhysicsComponent();
		
		returning = false;
		startPos = new Vector2(position);
		Vector2 playerPos = player.pos;
		Vector2 extraDistance = Vector2.multiply(Vector2.direction(playerPos, startPos), EXTRA_DISTANCE);
		goalPos = Vector2.add(playerPos, extraDistance);
		
		toDirection = Vector2.direction(goalPos, startPos);
		vel = Vector2.multiply(toDirection, FLY_SPEED);
		
		scene.game.soundManager.playSound("cutmanscissor");
	}

	@Override
	protected void initCollisionData() {
		hitboxes.add(new Hitbox(-6, -6, 12, 12, CollisionType.ENEMY_PROJECTILE, 4));
	}

	@Override
	protected void initSprites() {
		sprites = scene.game.getSpriteManager().getSprites("cutman");
	}

	@Override
	protected void initAnimations() {
		currentAnim = new Animation(sprites.get("cutman_scissor1"));
	}

	@Override
	public void update() {
		physicsComponent.update(this);
		
		rotation += ROT_SPEED * Utility.timeAdjust;
		
		if (!returning) {
			if (Vector2.distance(goalPos, pos) < 5) {
				returningStartPos = new Vector2(pos);
				returning = true;
			}
		} else {
			returnDirection = Vector2.normalize(Vector2.subtract(cutman.pos, pos));
			vel = Vector2.multiply(returnDirection, FLY_SPEED);
		}
	}

	@Override
	public void render(Graphics2D g) {
		renderComponent.render(g, this);
		
		if (Game.DEBUG) {
			float gameScale = scene.game.scale;
			g.setColor(Color.magenta);
			g.setStroke(new BasicStroke(2));
			g.drawLine(
					(int)(pos.x * gameScale), 
					(int)(pos.y * gameScale), 
					(int)((returning ? cutman.pos.x : goalPos.x) * gameScale), 
					(int)((returning ? cutman.pos.y : goalPos.y) * gameScale));
			g.setStroke(new BasicStroke(1));
		}
	}

}
