package tiles;

import java.awt.Graphics2D;
import java.io.InputStream;
import java.util.Scanner;

import gameobjects.Vector2;
import general.Game;
import graphics.TileSpriteSheet;
import scenes.GameScene;

public class Stage {
	
	private GameScene scene;
	
	private String name;
	private String tileSetPath;
	private String levelDataPath;
	
	private int[][] map;
	
	private int tileSetSize;
	private Tile[] tiles;
	private TileSpriteSheet tileSet;
	
	public Tile test;
	public Tile nullTile;
	
	private int stageWidth;
	private int stageHeight;
	private Vector2 playerSpawn;
	private Vector2 bossSpawn;
	
	public Stage(GameScene scene, String name) {
		this.scene = scene;
		this.name = name;
		tileSetPath = name + ".png";
		levelDataPath = name + ".txt";
		
		nullTile = new Tile(scene, -1, null, 1);
		
		loadWorld();
	}
	
	public void render(Graphics2D g) {
		for (int y = 0; y < stageHeight; y++) {
			for (int x = 0; x < stageWidth; x++) {
				tiles[map[y][x]].render(g, 
						(x * Tile.TILE_SIZE), 
						(y * Tile.TILE_SIZE));
			}
		}
	}
	
	public void renderForeground(Graphics2D g) {
		for (int y = 0; y < stageHeight; y++) {
			for (int x = 0; x < stageWidth; x++) {
				Tile t = tiles[map[y][x]];
				if (t.getBehaviourID() == 5 || t.getBehaviourID() == 6) {
					tiles[map[y][x]].render(g, 
							(x * Tile.TILE_SIZE), 
							(y * Tile.TILE_SIZE));
				}
			}
		}
	}
	
	public Tile getTile(int x, int y) {
		if (y >= map.length || y < 0 || x >= map[0].length || x < 0)
			return nullTile;
	
		return tiles[map[y][x]];
	}
	
	private void loadWorld() {
		tileSet = new TileSpriteSheet(tileSetPath);
		test = new Tile(scene, 0, tileSet.getImage(0), 0);
		
		String path = "sprites/" + levelDataPath;
		Scanner sc;
		try {
			InputStream levelData = ClassLoader.getSystemClassLoader().getResourceAsStream(path);
			sc = new Scanner(levelData);
		} catch (Exception e) {
			System.out.println("Warning: file" + levelDataPath + " not found.");
			return; 
		}
		
		String name = sc.nextLine();
		if (Game.DEBUG)
			System.out.println("Loading " + name);
		
		stageWidth = sc.nextInt();
		stageHeight = sc.nextInt();
		playerSpawn = new Vector2(sc.nextInt(), sc.nextInt());
		bossSpawn = new Vector2(sc.nextInt(), sc.nextInt());
		
		//System.out.format("%d %d %f %f\n", stageWidth, stageHeight, playerSpawn.x, playerSpawn.y);
		
		tileSetSize = sc.nextInt();
		tiles = new Tile[tileSetSize];
		
		for (int i = 0; i < tileSetSize; i++) {
			if (i == sc.nextInt()) {
				tiles[i] = new Tile(scene, i, tileSet.getImage(i), sc.nextInt());
				if (Game.DEBUG)
					System.out.println("Loaded tile #" + i);
			}
		}
		
		map = new int[stageHeight][stageWidth];
		for (int y = 0; y < stageHeight; y++) {
			for (int x = 0; x < stageWidth; x++) {
				map[y][x] = sc.nextInt();
			}
		}
		
		sc.close();
	}

	public String getName() {
		return name;
	}

	public int[][] getMap() {
		return map;
	}

	public int getTileSetSize() {
		return tileSetSize;
	}

	public Tile[] getTiles() {
		return tiles;
	}

	public TileSpriteSheet getTileSet() {
		return tileSet;
	}

	public int getStageWidth() {
		return stageWidth;
	}

	public int getStageHeight() {
		return stageHeight;
	}

	public Vector2 getAbsolutePlayerSpawn() {
		return new Vector2(playerSpawn.x * Tile.TILE_SIZE, playerSpawn.y * Tile.TILE_SIZE);
	}
	
	public Vector2 getAbsoluteBossSpawn() {
		return new Vector2(bossSpawn.x * Tile.TILE_SIZE, bossSpawn.y * Tile.TILE_SIZE);
	}
	
	public Vector2 getTilePlayerSpawn() {
		return playerSpawn;
	}	
}
