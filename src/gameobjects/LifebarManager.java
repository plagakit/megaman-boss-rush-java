package gameobjects;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import graphics.Sprite;
import scenes.GameScene;

public class LifebarManager extends GameObject {

	class Lifebar {
		Creature creature;
		Sprite lifebarSprite;
		
		public Lifebar(Creature creature, Sprite lifebarSprite) {
			this.creature = creature;
			this.lifebarSprite = lifebarSprite;
		}
	}
	
	final static Vector2 START_POS = new Vector2(10, 10);
	List<Lifebar> lifebars = new ArrayList<Lifebar>();
	final int MAX_LB_HEIGHT = 28;
	
	public LifebarManager(GameScene scene) {
		super(scene, "Lifebar", START_POS);
	}

	@Override
	protected void initSprites() {
		sprites = scene.game.getSpriteManager().getSprites("lifebars");
	}

	@Override
	public void render(Graphics2D g) {

		float gameScale = scene.game.scale;
		Sprite emptySprite = sprites.get("empty");
		float xPos = 0;
		
		for (int i = 0; i < lifebars.size(); i++) {
			
			Lifebar lb = lifebars.get(i);
			
			int maxHealth = lb.creature.maxHealth;
			int currentHealth = lb.creature.currentHealth;
			int emptyHealth = maxHealth - currentHealth;
			int counter = 0;
			int extraColumns = 0;
			
			Sprite sprite = lb.lifebarSprite;
			
			// Empty health render
			for (int j = 0; j < emptyHealth; j++) {
				int x = (int)((pos.x + xPos + (extraColumns * sprite.width)) * scale.x * gameScale);
				int width = (int)(sprite.width * scale.x * gameScale);
				int y = (int)((pos.y + (counter * sprite.height)) * scale.y * gameScale);
				int height = (int)(sprite.height * scale.y * gameScale);
				
				g.drawImage(emptySprite.image, x, y, width, height, null);
				counter++;
				if (counter >= MAX_LB_HEIGHT) {
					extraColumns++;
					counter = 0;
				}
			}
			
			// Filled health render
			for (int j = 0; j < currentHealth; j++) {
				int x = (int)((pos.x + xPos + (extraColumns * sprite.width)) * scale.x * gameScale);
				int width = (int)(sprite.width * scale.x * gameScale);
				int y = (int)((pos.y + (counter * sprite.height)) * scale.y * gameScale);
				int height = (int)(sprite.height * scale.y * gameScale);
				
				g.drawImage(sprite.image, x, y, width, height, null);
				counter++;
				if (counter >= MAX_LB_HEIGHT) {
					extraColumns++;
					counter = 0;
				}
			}
			
			// Gap between lifebars
			xPos += sprite.width * (1 + extraColumns);
		}
		
	}

	public void addLifeBar(Creature c, String lifebarName) {
		Sprite s = sprites.get(lifebarName);
		if (s == null) {
			s = sprites.get("default");
			System.out.format("\nWARNING LIFEBAR SPRITE %s NOT FOUND\n\n", lifebarName);
		}
		
		Lifebar lb = new Lifebar(c, s);
		lifebars.add(lb);
	}
	
	
	@Override
	protected void initAnimations() {}
	@Override
	public void update() {}
}
