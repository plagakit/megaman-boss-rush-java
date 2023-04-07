package components;

import gameobjects.Entity;
import general.Utility;

public class GravityComponent {

	public final float DEFAULT_GRAVITY = 0.2f;
	
	public void update(Entity e, float gravity) {
		if (!e.grounded)
			e.vel.y += gravity * Utility.timeAdjust;
	}
	
	public void update(Entity c) {
		update(c, DEFAULT_GRAVITY);
	}
	
}
