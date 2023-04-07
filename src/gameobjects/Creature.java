package gameobjects;

import java.awt.Graphics2D;

import general.Timer;
import scenes.GameScene;

public abstract class Creature extends Entity {
	
	public boolean alive;
	public static final int DEFAULT_HEALTH = 10;
	public int currentHealth;
	public int maxHealth;
	DeathParticle[] deathParticles;
	
	public int lemonDamage = 1;

	public boolean hurt; // state of having damage flash
	public boolean invulnerable; // state of being invulnerable to damage
	public DamageFlash damageFlash;
	public final int HURT_TIME = 1000; // 1 second of damage flash
	public int iframeTime = HURT_TIME; // 1 second of invulnerability (ends when damage flash ends)
	public Timer hurtTimer;
	public Timer iframeTimer;
	public Timer iframeAnimationTimer;
	
	public Creature(GameScene scene, String name, Vector2 position) {
		super(scene, name, position);
		
		maxHealth = DEFAULT_HEALTH;
		currentHealth = maxHealth;
		damageFlash = new DamageFlash(scene, "DamageFlash", pos);
		
		alive = true;
		hurt = false;
		invulnerable = false;
		damageFlash.active = false;
		hurtTimer = new Timer(HURT_TIME);
		iframeTimer = new Timer(iframeTime);
		iframeAnimationTimer = new Timer(50);
	}
	
	@Override
	public void update() {
		if (alive)
			_update();
		else {
			for (DeathParticle dp : deathParticles)
				dp.update();
			return;
		}
			
		if (hurtTimer.done) {
			damageFlash.active = false;
			hurt = false;
		} else if (hurt) {
			hurtTimer.update();
			damageFlash.update();
		}
		
		if (iframeTimer.done) {
			invulnerable = false;
			transparency = 1;
		} else if (invulnerable) {
			
			iframeTimer.update();
			
			if (!hurt) {
				if (iframeAnimationTimer.done) {
					iframeAnimationTimer.restart();
					transparency = transparency == 1 ? 0 : 1;
				} else
					iframeAnimationTimer.update();
			}
		}	
	}
	
	@Override
	public void render(Graphics2D g) {
		if (alive)
			_render(g);
		else {
			for (DeathParticle dp : deathParticles)
				dp.render(g);
			return;
		}
		
		if (hurt)
			damageFlash.render(g);
	}
	
	public abstract void _update();
	
	public abstract void _render(Graphics2D g);
	
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
		currentHealth = maxHealth;
	}
	
	public void handleCollision(Hitbox self, Entity col, Hitbox hit) {
		if (self.collisionType == CollisionType.PLAYER) {
			if (hit.collisionType == CollisionType.ENEMY || hit.collisionType == CollisionType.ENEMY_PROJECTILE) {
				takeDamage(hit.contactDamage);
			}
		}
		else if (hit.collisionType == CollisionType.PLAYER_PROJECTILE) {
			if (self.collisionType == CollisionType.ENEMY) {
				boolean destroyLemon = takeLemonDamage();
				if (destroyLemon)
					col.active = false;
			}
			else if (self.collisionType == CollisionType.REFLECT)
				((Projectile)col).reflect();
		}
	}
	
	private boolean takeDamage(int damage) {
		if (!hurt && !invulnerable) {
			currentHealth -= damage;
			
			if (currentHealth <= 0) {
				die();
				return true;
			}
			
			vel.y = 0;
			
			hurt = true;
			invulnerable = true;
			damageFlash.active = true;
			
			hurtTimer.restart();
			iframeTimer.restart();
			
			damageNoise();
			
			return true;
		} else return false;
	}
	
	private boolean takeLemonDamage() {
		return takeDamage(lemonDamage);
	}
	
	protected void damageNoise() {
		scene.game.soundManager.playSound("enemydamage");
	}
	
	private void die() {
		alive = false;
		currentHealth = 0;
		for (Hitbox h : hitboxes) h.collisionType = CollisionType.NONE;
		
		deathParticles = new DeathParticle[8];
		deathParticles[0] = new DeathParticle(((GameScene)scene), pos, Vector2.up());
		deathParticles[1] = new DeathParticle(((GameScene)scene), pos, Vector2.down());
		deathParticles[2] = new DeathParticle(((GameScene)scene), pos, Vector2.left());
		deathParticles[3] = new DeathParticle(((GameScene)scene), pos, Vector2.right());
		deathParticles[4] = new DeathParticle(((GameScene)scene), pos, Vector2.upleft(), true);
		deathParticles[5] = new DeathParticle(((GameScene)scene), pos, Vector2.upright(), true);
		deathParticles[6] = new DeathParticle(((GameScene)scene), pos, Vector2.downleft(), true);
		deathParticles[7] = new DeathParticle(((GameScene)scene), pos, Vector2.downright(), true);
	
		scene.game.soundManager.playSound("death");
	}
	
}
