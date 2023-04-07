package gameobjects.marx;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import components.RenderComponent;
import gameobjects.GameObject;
import gameobjects.Vector2;
import graphics.Animation;
import graphics.Frame;
import graphics.Sprite;
import scenes.Scene;

public class MarxCrystal extends GameObject {

	RenderComponent renderComponent;
	
	Color[][] palettes;
	float currentPalette = 0;
	
	public MarxCrystal(Scene scene, Vector2 position) {
		super(scene, "MarxCrystal", position);
		renderComponent = new RenderComponent();
		initPalettes();
	}
	
	private void initPalettes() {
		palettes = new Color[6][7];
		
		palettes[0] = new Color[] {
			new Color(136, 64, 144),
			new Color(248, 0, 144),
			new Color(120, 0, 232),
			new Color(168, 0, 224),
			new Color(248, 248, 248),
			new Color(0, 0, 224),
			new Color(152, 152, 144)
		};
		
		palettes[1] = new Color[] {
				new Color(120, 0, 232),
				new Color(168, 0, 224),
				new Color(248, 248, 248),
				new Color(248, 248, 248),
				new Color(0, 0, 96),
				new Color(0, 248, 144),
				new Color(136, 64, 144),
			};
		
		palettes[2] = new Color[] {
				new Color(248, 248, 248),
				new Color(248, 248, 248),
				new Color(0, 0, 96),
				new Color(0, 0, 224),
				new Color(0, 120, 144),
				new Color(248, 248, 144),
				new Color(120, 0, 144)
			};
		
		palettes[3] = new Color[] {
				new Color(0, 0, 96),
				new Color(0, 0, 224),
				new Color(0, 120, 144),
				new Color(0, 248, 144),
				new Color(152, 152, 144),
				new Color(248, 128, 144),
				new Color(120, 0, 232)
			};
		
		palettes[4] = new Color[] {
				new Color(152, 152, 144),
				new Color(248, 248, 144),
				new Color(136, 64, 144),
				new Color(248, 128, 144),
				new Color(120, 0, 144),
				new Color(168, 0, 224),
				new Color(0, 0, 96)
			};
		
		palettes[5] = new Color[] {
				new Color(136, 64, 144),
				new Color(248, 128, 144),
				new Color(120, 0, 144),
				new Color(248, 0, 144),
				new Color(120, 0, 232),
				new Color(248, 248, 248),
				new Color(0, 120, 144)
			};
	}

	@Override
	protected void initSprites() {
		sprites = scene.game.getSpriteManager().getSprites("marx");
	}

	@Override
	protected void initAnimations() {
		
		Frame[] crystal = new Frame[] {
			new Frame(sprites.get("crystal1"), 20),
			new Frame(sprites.get("crystal2"), 20),
			new Frame(sprites.get("crystal3"), 20),
			new Frame(sprites.get("crystal4"), 20),
			new Frame(sprites.get("crystal5"), 20),
			new Frame(sprites.get("crystal6"), 20)
		};
		
		currentAnim = new Animation(crystal);
	}

	@Override
	public void update() {
		
		currentPalette += 0.2;
		if (currentPalette > 5)
			currentPalette = 0;
		
		currentAnim.update();
	}

	@Override
	public void render(Graphics2D g) {
		if (currentPalette == 0) {
			renderComponent.render(g, this);
			return;
		}
		
		Sprite currentSprite = currentAnim.getCurrentFrame().sprite;
		Sprite updated = updateCrystalsInSprite(currentSprite);
		renderComponent.render(g, this, updated);
	}
	
	public Sprite updateCrystalsInSprite(Sprite currentSprite) {
		BufferedImage bi = currentSprite.image;
		BufferedImage edit = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());
		
		for (int y = 0; y < edit.getHeight(); y++) {
			for (int x = 0; x < edit.getWidth(); x++) {
				int clr = bi.getRGB(x, y);
				int red = (clr & 0x00ff0000) >> 16;
				int green = (clr & 0x0000ff00) >> 8;
				int blue = clr & 0x000000ff;
				
				if (red == 255 && green == 255 && blue == 255)
					continue;
			
				for (int c = 0; c < palettes[0].length; c++) {
					if (palettes[0][c].getRGB() == clr) {
						//System.out.format("%d %d %d matches %d\n", palettes[0][c].getRed(), palettes[0][c].getGreen(), palettes[0][c].getBlue(), clr);
						int index = (int)Math.floor(currentPalette);
						edit.setRGB(x, y, palettes[index][c].getRGB());
						break;
					}
				}
			}
		}
		
		return new Sprite("crystal", edit, currentSprite.anchorPoint);
	}

}
