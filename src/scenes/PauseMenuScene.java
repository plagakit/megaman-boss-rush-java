package scenes;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

import general.Game;

public class PauseMenuScene extends Scene {

	public PauseMenuScene(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(Graphics2D g) {		
		// Draw transparent "blur" over game scene
		AlphaComposite translucent = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
		AlphaComposite opaque = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1);
		g.setComposite(translucent);
		g.setColor(Color.black);
		g.fillRect(0, 0, (int)(game.width * game.scale), (int)(game.height * game.scale));
		g.setComposite(opaque);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRemove() {
		// TODO Auto-generated method stub

	}

}
