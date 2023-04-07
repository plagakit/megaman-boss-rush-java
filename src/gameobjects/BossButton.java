package gameobjects;

import java.awt.Graphics2D;

import components.RenderComponent;
import graphics.Animation;
import graphics.Frame;
import graphics.Sprite;
import scenes.GameScene;
import scenes.StageSelectScene;

public class BossButton extends GameObject {

	
	RenderComponent renderComponent;
	
	// 0 - locked, 1 - unlocked, 2 - cleared
	public int state;
	public boolean selected;
	GameScene gameScene;
	
	Sprite portrait;
	
	public BossButton(StageSelectScene scene, String name, Vector2 position, int state, Sprite portrait, GameScene gameScene) {
		super(scene, name, position);
		this.state = state;
		selected = false;
		this.portrait = portrait;
		this.gameScene = gameScene;
		
		renderComponent = new RenderComponent();
		scale = new Vector2(0.33f, 0.33f);
	}

	@Override
	protected void initSprites() {
		sprites = scene.game.getSpriteManager().getSprites("stageselect");
	}

	@Override
	protected void initAnimations() {
		
		Frame[] flicker = new Frame[] {
				new Frame(sprites.get("border2"), 150),
				new Frame(sprites.get("border1"), 150)
		};
		
		anims = new Animation[] {
				new Animation(sprites.get("border1")),
				new Animation(flicker)
		};
		
		currentAnim = anims[0];
	}

	@Override
	public void update() {		
		currentAnim.update();	
	}

	@Override
	public void render(Graphics2D g) {
		renderComponent.render(g, this);
		
		if (portrait != null)
			renderComponent.render(g, this, portrait);
		
		if (state == 0)
			renderComponent.render(g, this, sprites.get("locked"));
		else if (state == 2)
			renderComponent.render(g, this, sprites.get("cleared"));
	}

	public void press() {
		if (gameScene == null || state == 0) {
			scene.game.soundManager.playSound("error");
			return;
		}
		
		scene.game.setScene(gameScene);
	}
	
	public void select() {
		selected = true;
		currentAnim = anims[1];
		currentAnim.restart();
		scene.game.soundManager.playSound("select");
	}
	
	public void unselect() {
		selected = false;
		currentAnim = anims[0];
	}
	
}
