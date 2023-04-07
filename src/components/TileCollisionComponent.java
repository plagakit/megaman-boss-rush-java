package components;

import gameobjects.CollisionType;
import gameobjects.Entity;
import gameobjects.Hitbox;
import gameobjects.Vector2;
import scenes.GameScene;
import tiles.Tile;

public class TileCollisionComponent {
	
	private GameScene scene;
	private boolean playLandingNoise;
	
	public TileCollisionComponent(GameScene scene) {
		this(scene, false);
	}
	
	public TileCollisionComponent(GameScene scene, boolean playLandingNoise) {
		this.scene = scene;
		this.playLandingNoise = playLandingNoise;
	}
	
	public void update(Entity e) {
		Vector2 vel = e.vel;
		Vector2 pos = e.pos;
		
		for (Hitbox bounds : e.hitboxes) {
			if (!bounds.active || bounds.collisionType == CollisionType.NONE || bounds.collisionType == CollisionType.REFLECT) 
				continue;
			
			if (vel.x >= 0) { // RIGHT
				int tx = (int)(pos.x + vel.x + bounds.x + bounds.width) / Tile.TILE_SIZE;
				int ty1 = (int)(pos.y + bounds.y) / Tile.TILE_SIZE;
				int ty2 = (int)(pos.y + bounds.y + bounds.height - 1) / Tile.TILE_SIZE;
				int ty3 = (int)(pos.y + bounds.y + bounds.height/2) / Tile.TILE_SIZE;
				
				boolean t1 = tileAt(tx, ty1).isSolid();
				boolean t2 = tileAt(tx, ty2).isSolid();
				boolean t3 = tileAt(tx, ty3).isSolid();
				
				if (t1 || t2 || t3)
					pos.x = tx * Tile.TILE_SIZE - bounds.x - bounds.width - 1;
			} 
			
			else if (vel.x <= 0) { // LEFT
				int tx = (int)(pos.x + vel.x + bounds.x) / Tile.TILE_SIZE;
				int ty1 = (int)(pos.y + bounds.y) / Tile.TILE_SIZE;
				int ty2 = (int)(pos.y + bounds.y + bounds.height - 1) / Tile.TILE_SIZE;
				int ty3 = (int)(pos.y + bounds.y + bounds.height/2) / Tile.TILE_SIZE;
				
				boolean t1 = tileAt(tx, ty1).isSolid();
				boolean t2 = tileAt(tx, ty2).isSolid();
				boolean t3 = tileAt(tx, ty3).isSolid();
				
				if (t1 || t2 || t3)
					pos.x = tx * Tile.TILE_SIZE + Tile.TILE_SIZE - bounds.x;
			}

			if (vel.y < 0) { // UP
				int ty = (int)(pos.y + vel.y + bounds.y) / Tile.TILE_SIZE;
				int tx1 = (int)(pos.x + bounds.x) / Tile.TILE_SIZE;
				int tx2 = (int)(pos.x + bounds.x + bounds.width) / Tile.TILE_SIZE;
				int tx3 = (int)(pos.x + bounds.x + bounds.width/2) / Tile.TILE_SIZE;
			
				boolean t1 = tileAt(tx1, ty).isSolid();
				boolean t2 = tileAt(tx2, ty).isSolid();
				boolean t3 = tileAt(tx3, ty).isSolid();
				
				if (t1 || t2 || t3) {
					pos.y = ty * Tile.TILE_SIZE + Tile.TILE_SIZE - bounds.y;
					vel.y = 0;
				}
			} 
			
			if (vel.y >= 0) { // DOWN
				int ty = (int)(pos.y + vel.y + bounds.y + bounds.height) / Tile.TILE_SIZE;
				int tx1 = (int)(pos.x + bounds.x) / Tile.TILE_SIZE;
				int tx2 = (int)(pos.x + bounds.x + bounds.width) / Tile.TILE_SIZE;
				int tx3 = (int)(pos.x + bounds.x + bounds.width/2) / Tile.TILE_SIZE;
				
				boolean t1 = tileAt(tx1, ty).isSolid() || tileAt(tx1, ty).isPlatform();
				boolean t2 = tileAt(tx2, ty).isSolid() || tileAt(tx2, ty).isPlatform();
				boolean t3 = tileAt(tx3, ty).isSolid() || tileAt(tx3, ty).isPlatform();
				
				if (t1 || t2 || t3) {
					pos.y = ty * Tile.TILE_SIZE - bounds.y - bounds.height;
					vel.y = 0;
					if (playLandingNoise && !e.grounded)
						scene.game.soundManager.playSound("megamanland");
					e.grounded = true;
				} else
					e.grounded = false;
			}
			
		}
	}
	
	private Tile tileAt(int x, int y) {
		return scene.stage.getTile(x, y);
	}
}
