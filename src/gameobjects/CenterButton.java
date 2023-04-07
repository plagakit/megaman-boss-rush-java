package gameobjects;

import java.awt.Graphics2D;

import graphics.Sprite;
import scenes.StageSelectScene;

public class CenterButton extends BossButton {

	BossButton[][] grid;
	int centerX, centerY;
	Sprite[] portraits;
	Sprite currentSprite;
	
	public CenterButton(StageSelectScene scene, String name, Vector2 position, BossButton[][] grid) {
		super(scene, name, position, 1, null, null);
		this.grid = grid;
		
		centerX = (int)Math.floor(grid[0].length / 2f);
		centerY = (int)Math.floor(grid.length / 2f);
		
		portraits = new Sprite[] {
			sprites.get("megamanupleft"),
			sprites.get("megamanup"),
			sprites.get("megamanupright"),
			sprites.get("megamanleft"),
			sprites.get("megamancenter"),
			sprites.get("megamanright"),
			sprites.get("megamandownleft"),
			sprites.get("megamandown"),
			sprites.get("megamandownright")
		};
		
		currentSprite = sprites.get("megamancenter");
	}
	
	public void update(int currentPosX, int currentPosY) {		
		if (currentPosX == centerX && currentPosY == centerY)
			currentSprite = sprites.get("megamancenter");
		else if (currentPosX > centerX && currentPosY == centerY)
			currentSprite = sprites.get("megamanright");
		else if (currentPosX < centerX && currentPosY == centerY)
			currentSprite = sprites.get("megamanleft");
		else if (currentPosX == centerX && currentPosY < centerY)
			currentSprite = sprites.get("megamanup");
		else if (currentPosX > centerX && currentPosY < centerY)
			currentSprite = sprites.get("megamanupright");
		else if (currentPosX < centerX && currentPosY < centerY)
			currentSprite = sprites.get("megamanupleft");
		else if (currentPosX == centerX && currentPosY > centerY)
			currentSprite = sprites.get("megamandown");
		else if (currentPosX > centerX && currentPosY > centerY)
			currentSprite = sprites.get("megamandownright");
		else if (currentPosX < centerX && currentPosY > centerY)
			currentSprite = sprites.get("megamandownleft");
		
		currentAnim.update();
	}
	
	@Override
	public void render(Graphics2D g) {
		renderComponent.render(g, this);
		renderComponent.render(g, this, currentSprite);
	}
	
	@Override
	public void press() {}
	
}
