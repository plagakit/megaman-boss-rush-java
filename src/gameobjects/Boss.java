package gameobjects;

import scenes.GameScene;

public abstract class Boss extends Creature {

	protected Creature player;
	
	public Boss(GameScene scene, String name, Vector2 position, Creature player) {
		super(scene, name, position);
		
		this.player = player;
	}

}
