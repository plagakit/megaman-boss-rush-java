package components;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import gameobjects.Entity;
import gameobjects.GameObject;
import gameobjects.Hitbox;
import general.Game;
import graphics.Frame;
import graphics.Sprite;

public class RenderComponent {
	
	AlphaComposite opaque = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
	
	public void render(Graphics2D g, GameObject o) {
		if (!o.active)
			return;
		
		Frame frame =  o.currentAnim.getCurrentFrame();
		Sprite sprite = frame.sprite;
		
		if (frame.empty)
			return;
		
		render(g, o, sprite);
	}
	
	public void render(Graphics2D g, GameObject o, Sprite s) {
		if (!o.active)
			return;
		
		float gameScale = o.scene.game.scale;
		Sprite sprite = s;
		
		int x, width;
		if (!o.flipped) {
			x = (int)((o.pos.x + sprite.anchorPoint.x - (sprite.width/2 * o.scale.x)) * gameScale);
			width = (int)(sprite.width * o.scale.x * gameScale);
		}
		else {
			//System.out.format("%s: %f\n", o.name, sprite.anchorPoint.x);
			x = (int)((o.pos.x - sprite.anchorPoint.x + (sprite.width/2 * o.scale.x)) * gameScale);
			width = (int)(sprite.width * o.scale.x * gameScale * -1);
		}
		
		int y = (int)((o.pos.y + sprite.anchorPoint.y - (sprite.height/2 * o.scale.y)) * gameScale);
		int height = (int)(sprite.height * o.scale.y * gameScale);
		
		if (o.transparency != 1) {
			AlphaComposite transparency = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, o.transparency);
			g.setComposite(transparency);
		}
		
		float midx = x + (width / 2.0f);
		float midy = y + (height / 2.0f);
		
		if (o.rotation != 0) 
			g.rotate(Math.toRadians(o.rotation), midx, midy);
		
		g.drawImage(sprite.image, x, y, width, height, null);
		
		if (o.rotation != 0) 
			g.rotate(Math.toRadians(-o.rotation), midx, midy);
		
		if (o.transparency != 1)
			g.setComposite(opaque);
		
		if (Game.DEBUG) {
			
			// Hitbox
			if (o instanceof Entity) {
				Entity e = (Entity)o;
				
				g.setColor(Color.magenta);
				g.setStroke(new BasicStroke(0.5f + (0.5f * gameScale)));
				for (Hitbox bounds : e.hitboxes) {
					if (!bounds.active) continue;
					g.drawRect(
							(int)((e.pos.x + bounds.x) * gameScale), 
							(int)((e.pos.y + bounds.y) * gameScale), 
							(int)(bounds.width * gameScale), 
							(int)(bounds.height * gameScale));
				}
			}	
			
			// Position
			g.setColor(Color.blue);
			g.setStroke(new BasicStroke(10));
			g.drawLine((int)(o.pos.x * gameScale), (int)(o.pos.y * gameScale), (int)(o.pos.x * gameScale), (int)(o.pos.y * gameScale)); 
			
			// Anchor point
			g.setColor(Color.magenta);
			g.drawLine(
					(int)((o.pos.x + sprite.anchorPoint.x) * gameScale), 
					(int)((o.pos.y + sprite.anchorPoint.y) * gameScale), 
					(int)((o.pos.x + sprite.anchorPoint.x) * gameScale), 
					(int)((o.pos.y + sprite.anchorPoint.y) * gameScale)); 
			
			g.setStroke(new BasicStroke(1));
			
			/* // Tile collision moving down
			int ty = (int)(o.pos.y + o.vel.y + o.bounds.y + o.bounds.height);
			int tx1 = (int)(o.pos.x + o.bounds.x);
			int tx2 = (int)(o.pos.x + o.bounds.x + o.bounds.width);
			int tx3 = (int)(o.pos.x + o.bounds.x + o.bounds.width/2);
			
			g.setColor(Color.green);
			g.setStroke(new BasicStroke(10));
			g.drawLine((int)(tx1 * o.scale), (int)(ty * o.scale), (int)(tx1 * o.scale), (int)(ty * o.scale));
			g.drawLine((int)(tx2 * o.scale), (int)(ty * o.scale), (int)(tx2 * o.scale), (int)(ty * o.scale));
			g.drawLine((int)(tx3 * o.scale), (int)(ty * o.scale), (int)(tx3 * o.scale), (int)(ty * o.scale));
			g.setStroke(new BasicStroke(1));
			*/
		}
	}
}
