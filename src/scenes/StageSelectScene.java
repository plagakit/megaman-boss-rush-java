package scenes;

import java.awt.Graphics2D;
import java.util.HashMap;

import gameobjects.BossButton;
import gameobjects.CenterButton;
import gameobjects.Vector2;
import gameobjects.cutman.Cutman;
import gameobjects.jevil.Jevil;
import gameobjects.whispy.WhispyWoods;
import gameobjects.marx.Marx;
import general.Game;
import general.KeyManager;
import graphics.Sprite;

public class StageSelectScene extends Scene {

	BossButton[][] buttons;
	final int WIDTH = 3;
	final int HEIGHT = 3;
	final float BUTTON_GAP = 80;
	HashMap<String, Sprite> sprites;
	
	int currentPosX = 1;
	int currentPosY = 1;
	
	KeyManager keyManager;
	
	public StageSelectScene(Game game) {
		super(game);
		
		sprites = game.getSpriteManager().getSprites("stageselect");
		buttons = new BossButton[3][3];
		initButtons();
		
		buttons[currentPosX][currentPosY].select();
		
		keyManager = game.getKeyManager();
	}
	
	private void initButtons() {
		Vector2 centerPos = new Vector2(game.width / 2, game.height / 2);
		
		buttons[0][0] = new BossButton(this, "JevilButton", Vector2.relocate(centerPos, Vector2.upleft(), BUTTON_GAP), 
				1, sprites.get("jevil"), new GameScene(game, "jevil", Jevil.class));
		
		buttons[0][1] = new BossButton(this, "CutmanButton", Vector2.relocate(centerPos,Vector2.up(), BUTTON_GAP), 	
				1, sprites.get("cutman"), new GameScene(game, "cutman", Cutman.class));
		
		buttons[0][2] = new BossButton(this, "Button3", Vector2.relocate(centerPos,Vector2.upright(), BUTTON_GAP), 	
				1, sprites.get("whispy"), new GameScene(game, "whispy", WhispyWoods.class));
		
		buttons[1][0] = new BossButton(this, "Button4", Vector2.relocate(centerPos,Vector2.left(), BUTTON_GAP), 		
				1, sprites.get("marx"), new GameScene(game, "marx", Marx.class));
		
		buttons[1][1] = new CenterButton(this, "CenterButton", centerPos, buttons);
		
		buttons[1][2] = new BossButton(this, "Button6", Vector2.relocate(centerPos,Vector2.right(), BUTTON_GAP), 		
				0, null, null);
		
		buttons[2][0] = new BossButton(this, "Button7", Vector2.relocate(centerPos,Vector2.downleft(), BUTTON_GAP), 	
				0, null, null);
		
		buttons[2][1] = new BossButton(this, "Button8", Vector2.relocate(centerPos,Vector2.down(), BUTTON_GAP), 		
				0, null, null);
		
		buttons[2][2] = new BossButton(this, "Button8", Vector2.relocate(centerPos,Vector2.downright(), BUTTON_GAP),	
				0, null, null);
	}

	@Override
	public void update() {
		
		handleInput();
		
		for (int i = 0; i < buttons.length; i++)
			for (int j = 0; j < buttons[i].length; j++)
				if (buttons[i][j] != null) {
					if (buttons[i][j] instanceof CenterButton)
						((CenterButton)buttons[i][j]).update(currentPosX, currentPosY);
					else buttons[i][j].update();
				}
	}

	@Override
	public void render(Graphics2D g) {
		for (int i = 0; i < buttons.length; i++)
			for (int j = 0; j < buttons[i].length; j++)
				if (buttons[i][j] != null)
					buttons[i][j].render(g);
	}
	
	private void handleInput() {
		if (keyManager.upPressed && currentPosY > 0) {
			buttons[currentPosY][currentPosX].unselect();
			currentPosY--;
			buttons[currentPosY][currentPosX].select();
		} 
		else if (keyManager.downPressed && currentPosY < HEIGHT - 1) {
			buttons[currentPosY][currentPosX].unselect();
			currentPosY++;
			buttons[currentPosY][currentPosX].select();
		}
		else if (keyManager.leftPressed && currentPosX > 0) {
			buttons[currentPosY][currentPosX].unselect();
			currentPosX--;
			buttons[currentPosY][currentPosX].select();
		} 
		else if (keyManager.rightPressed && currentPosX < WIDTH - 1) {
			buttons[currentPosY][currentPosX].unselect();
			currentPosX++;
			buttons[currentPosY][currentPosX].select();
		}
		
		if (keyManager.start)
			buttons[currentPosY][currentPosX].press();
	}

	@Override
	public void onStart() {
		
	}
	
	@Override
	public void onRemove() {
		
	}
	
}
