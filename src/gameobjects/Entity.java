package gameobjects;

import java.util.ArrayList;
import java.util.List;

import scenes.GameScene;

public abstract class Entity extends GameObject {
	
	public List<Hitbox> hitboxes;
	public boolean grounded;
	
	public Entity(GameScene scene, String name, Vector2 position) {
		super(scene, name, position);
		
		hitboxes = new ArrayList<Hitbox>();
		initCollisionData();
		grounded = false;
	}
	
	protected abstract void initCollisionData();
}
