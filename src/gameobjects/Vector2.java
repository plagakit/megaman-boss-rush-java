package gameobjects;

import general.Game;

public class Vector2 {
	public float x;
	public float y;
	
	public static Vector2 zero() { return new Vector2(0, 0); } 
	public static Vector2 up() { return new Vector2(0, -1); } 
	public static Vector2 down() { return new Vector2(0, 1); } 
	public static Vector2 left() { return new Vector2(-1, 0); } 
	public static Vector2 right() { return new Vector2(1, 0); } 
	public static Vector2 upleft() { return new Vector2(-1, -1); } 
	public static Vector2 upright() { return new Vector2(1, -1); } 
	public static Vector2 downleft() { return new Vector2(-1, 1); } 
	public static Vector2 downright() { return new Vector2(1, 1); } 

	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2(Vector2 v2) {
		this.x = v2.x;
		this.y = v2.y;
	}

	public String toString() {;
		return String.format("%f %f", x, y);
	}
	
	public static Vector2 parse(String token) {
		int xindex = token.indexOf('x');
		int yindex = token.indexOf('y');

		int x = Integer.parseInt(token.substring(xindex + 1, yindex));
		int y = Integer.parseInt(token.substring(yindex + 1));
		
		return new Vector2(x, y);
	}
	
	public static float distance(Vector2 v1, Vector2 v2) {
		// c = sqrt((xA - xB)^2 + (yA - yB)^2)
		float distance = (float) Math.sqrt(Math.pow(v1.x - v2.x, 2) + Math.pow(v1.y - v2.y, 2));
		return distance;
	}
	
	public static Vector2 normalize(Vector2 v) {
		float magnitude = (float)Math.sqrt(Math.pow(v.x, 2) + Math.pow(v.y, 2));
		float newX = v.x == 0 ? 0 : v.x / magnitude;
		float newY = v.y == 0 ? 0 : v.y / magnitude;
		
		return new Vector2(newX, newY);
	}
	
	public static Vector2 add(Vector2 v1, Vector2 v2) {
		return new Vector2(v1.x + v2.x, v1.y + v2.y);
	}
	
	public static Vector2 subtract(Vector2 v1, Vector2 v2) {
		return new Vector2(v1.x - v2.x, v1.y - v2.y);
	}
	
	public static Vector2 multiply(Vector2 v1, Vector2 v2) {
		return new Vector2(v1.x * v2.x, v1.y * v2.y);
	}
	
	public static Vector2 multiply(Vector2 v, float a) {
		return new Vector2(v.x * a, v.y * a);
	}
	
	public static Vector2 direction(Vector2 from, Vector2 to) {
		return Vector2.normalize(Vector2.subtract(from, to));
	}
	
	public static Vector2 relocate(Vector2 start, Vector2 direction, float distance) {
		return Vector2.add(start, Vector2.multiply(direction, distance));
	}
	
	public static Vector2 center(Game game) {
		return new Vector2(game.width / 2f, game.height / 2f);
	}
}
