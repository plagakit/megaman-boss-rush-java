package gameobjects;

import scenes.GameScene;

public abstract class Projectile extends Entity {

	boolean reflecting;
	final float REFLECT_SPEED = -1;
	
	public Projectile(GameScene scene, String name, Vector2 position) {
		super(scene, name, position);
		
		reflecting = false;
		scene.addGameObject(this);
	}

	public void despawn() {
		active = false;
		((GameScene)scene).removeGameObject(this);
	}
	
	public void reflect() {
		reflecting = true;
		vel.x *= REFLECT_SPEED;
		vel.y = vel.x;
		
		for (Hitbox h : hitboxes) h.active = false;
		
		scene.game.soundManager.playSound("lemondink");
	}
}
