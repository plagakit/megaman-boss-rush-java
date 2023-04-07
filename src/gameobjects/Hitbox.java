package gameobjects;

public class Hitbox {
	
	public boolean active;
	public int x, y, width, height;
	public CollisionType collisionType;
	public int contactDamage;
	
	public Hitbox(int x, int y, int width, int height, CollisionType collisionType, int contactDamage) {
		active = true;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.collisionType = collisionType;
		this.contactDamage = contactDamage;
	}
	
	public Hitbox(int x, int y, int width, int height, CollisionType collisionType) {
		this(x, y, width, height, collisionType, 0);
	}
	
}
