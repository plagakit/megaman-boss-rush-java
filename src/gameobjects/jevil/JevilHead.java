package gameobjects.jevil;

import java.awt.Graphics2D;

import components.RenderComponent;
import gameobjects.GameObject;
import gameobjects.Vector2;
import general.Utility;
import graphics.Animation;
import graphics.Sprite;
import scenes.Scene;

public class JevilHead extends GameObject {

	Jevil jevil;
	RenderComponent renderComponent;
	
	Sprite chainSprite;
	Vector2 chain1Pos;
	Vector2 chain2Pos;
	
	public JevilHead(Scene scene, Vector2 position, Jevil jevil) {
		super(scene, "JevilHead", position);
		this.jevil = jevil;
		renderComponent = new RenderComponent();
		chain1Pos = new Vector2(position);
		chain2Pos = new Vector2(position);
		
		active = false;
	}

	@Override
	protected void initSprites() {
		sprites = scene.game.getSpriteManager().getSprites("jevil");
		chainSprite = sprites.get("chain");
	}

	@Override
	protected void initAnimations() {
		anims = new Animation[] {
				new Animation(sprites.get("head1")),
				new Animation(sprites.get("head2")),
				new Animation(sprites.get("head3")),
				new Animation(sprites.get("head4"))
		};
		currentAnim = anims[0];
	}

	float updateNum = 0;
	
	@Override
	public void update() {
		updateNum += Utility.timeAdjust;
		
		flipped = jevil.flipped;
		float hurtProgressed = jevil.hurtTimer.getProgressed();
		
		float headMoveX = (float)Math.sin(updateNum / 3.0f) * (Utility.lerp(45, 0, hurtProgressed));
		float headMoveY = Math.abs((float)Math.sin(updateNum / 3.0f) * (Utility.lerp(40, 10, hurtProgressed)));
		
		float chain1MoveX = (float)Math.sin(updateNum / 3.0f) * (Utility.lerp(30, 0, hurtProgressed));
		float chain1MoveY = Math.abs((float)Math.sin(updateNum / 3.0f) * (Utility.lerp(27, 10, hurtProgressed)));
		float chain2MoveX = (float)Math.sin(updateNum / 3.0f) * (Utility.lerp(15, 0, hurtProgressed));
		float chain2MoveY = Math.abs((float)Math.sin(updateNum / 3.0f) * (Utility.lerp(14, 10, hurtProgressed)));
		
		pos.x = jevil.pos.x + headMoveX;
		pos.y = jevil.pos.y - headMoveY;
		
		chain1Pos.x = jevil.pos.x + chain1MoveX;
		chain1Pos.y = jevil.pos.y - chain1MoveY;
		chain2Pos.x = jevil.pos.x + chain2MoveX;
		chain2Pos.y = jevil.pos.y - chain2MoveY;
		
		if (headMoveX > 5) // Moving right
			currentAnim = flipped ? anims[2] : anims[1];
		else if (headMoveX < -5) // Moving left
			currentAnim = flipped ? anims[1] : anims[2];
		else
			currentAnim = anims[0];
	}

	@Override
	public void render(Graphics2D g) {
		
		if (!active)
			return;
		
		// Render chains
		float gameScale = scene.game.scale;
		Sprite sprite = sprites.get("chain");
		
		int x1 = (int)(chain1Pos.x * scale.x * gameScale);
		int y1 = (int)(chain1Pos.y * scale.y * gameScale);
		int x2 = (int)(chain2Pos.x * scale.x * gameScale);
		int y2 = (int)(chain2Pos.y * scale.y * gameScale);
		int width = (int)(sprite.width * scale.x * gameScale);	
		int height = (int)(sprite.height * scale.y * gameScale);
		
		g.drawImage(sprite.image, x1, y1, width, height, null);
		g.drawImage(sprite.image, x2, y2, width, height, null);
		
		// Render the head
				renderComponent.render(g, this);
	}

}
