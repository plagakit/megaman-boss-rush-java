package gameobjects;

import java.awt.Graphics2D;
import java.util.HashMap;

import graphics.Animation;
import graphics.Sprite;
import scenes.Scene;

public abstract class GameObject {

	public Scene scene;
	
	public String name;
	public boolean active;
	
	public Vector2 pos;
	public Vector2 vel;
	public float rotation;
	public Vector2 scale;
	public boolean flipped;
	
	public HashMap<String, Sprite> sprites;
	public float transparency;
	public Animation currentAnim;
	public Animation[] anims;
	
	public GameObject(Scene scene, String name, Vector2 position) {
		this.name = name;
		this.scene = scene;
		this.pos = position;
		vel = new Vector2(0, 0);
		scale = new Vector2(1f, 1f);
		rotation = 0;
		flipped = false;
		transparency = 1f;
		
		active = true;
		
		initSprites();
		initAnimations();
	}

	protected abstract void initSprites();
	
	protected abstract void initAnimations();
	
	public abstract void update();
	
	public abstract void render(Graphics2D g);

}
