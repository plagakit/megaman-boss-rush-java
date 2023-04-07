package components;

import java.util.List;

import gameobjects.CollisionType;
import gameobjects.Creature;
import gameobjects.Entity;
import gameobjects.Hitbox;
import scenes.GameScene;

public class EntityCollisionComponent {

	public void update(Creature e1) {
		GameScene gs = (GameScene)(e1.scene);
		List<Entity> entities = gs.getEntities();
		
		for (Entity e2 : entities) {
			if (!e2.active || e2.equals(e1)) 
				continue;
			
			boolean hit = false;
			
			for (Hitbox e1Hitbox : e1.hitboxes) {
				if (!e1Hitbox.active || e1Hitbox.collisionType == CollisionType.NONE)
					continue;
				
				for (Hitbox e2Hitbox : e2.hitboxes) {
					if (!e2Hitbox.active || e2Hitbox.collisionType == CollisionType.NONE)
						continue;
					
					float ax1 = e1.pos.x + e1Hitbox.x; // leftmost corner
					float ax2 = ax1 + e1Hitbox.width; // rightmost corner
					float ay1 = e1.pos.y + e1Hitbox.y; // topmost corner
					float ay2 = ay1 + e1Hitbox.height; // bottommost corner
					
					float bx1 = e2.pos.x + e2Hitbox.x; // leftmost corner
					float bx2 = bx1 + e2Hitbox.width; // rightmost corner
					float by1 = e2.pos.y + e2Hitbox.y; // topmost corner
					float by2 = by1 + e2Hitbox.height; // bottommost corner
					
					boolean inbetweenX = (ax1 < bx2) && (ax2 > bx1);
					boolean inbetweenY = (ay1 < by2) && (ay2 > by1);
					
					if (!hit && inbetweenX && inbetweenY) { // collision
						e1.handleCollision(e1Hitbox, e2, e2Hitbox);
						hit = true;
					}
				}
			}
			
		}
	}

}
