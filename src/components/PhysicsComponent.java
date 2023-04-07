package components;

import gameobjects.GameObject;
import general.Utility;

public class PhysicsComponent {

	public void update(GameObject o) {
		o.pos.x += o.vel.x * Utility.timeAdjust;
		o.pos.y += o.vel.y * Utility.timeAdjust;
	}
	
}
