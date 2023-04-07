package scenes;

import java.awt.Graphics2D;

import general.Game;
import general.Utility;

public abstract class Scene {

	public Game game;
	
	public abstract void update();
	
	public void _update(long elapsedNanoTime) {
		Utility.updateTime(elapsedNanoTime);
		update();
	}
	
	public abstract void render(Graphics2D g);
	
	public abstract void onStart();
	
	public abstract void onRemove();
	
	public Scene(Game game) {
		this.game = game;
	}
	
	/**
	 * For testing different FPS settings, drops FPS considerably.
	 * Call in update() to start lag.
	 */
	protected void lagMachine() {
		long counter = 0;
		for (int i = 0; i < 100000; i++) {
			for (int j = 0; j < 100000; j++) {
				counter++;
			}
		}
		System.out.println(counter);
	}
}
