package scenes;

import java.awt.Graphics2D;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import gameobjects.Boss;
import gameobjects.Creature;
import gameobjects.Entity;
import gameobjects.GameObject;
import gameobjects.LifebarManager;
import gameobjects.Megaman;
import gameobjects.Projectile;
import gameobjects.Vector2;
import general.Game;
import general.Timer;
import general.Utility;
import tiles.Stage;

public class GameScene extends Scene {
	
	enum GameState {
		BATTLING,
		WON,
		LOST
	}
	
	public GameState gameState;
	
	boolean canPause;
	boolean paused;
	PauseMenuScene pauseMenu;
	
	Timer resetTimer = new Timer(4000); // 4 seconds
	Timer winTimer = new Timer(12000); // 12 seconds
	boolean playedVictoryFanfare = false;
	
	public Stage stage;
	String stageName;
	public GameObject background;
	
	private Megaman player;
	Class<? extends Boss> bossClass;
	Constructor<? extends Boss> bossConstructor;
	private Boss boss;
	
	private List<GameObject> gameObjects;
	private List<GameObject> addQueue;
	private List<GameObject> removeQueue;
	
	private List<GameObject> bgGameObjects;
	private List<GameObject> moveToBgQueue;
	private List<GameObject> moveToFgQueue;
	
	public LifebarManager lbManager;
	
	public GameScene(Game game, String stageName, Class<? extends Boss> boss) {
		super(game);
		this.stageName = stageName;
		bossClass = boss;
		
		canPause = true;
		paused = false;
		pauseMenu = new PauseMenuScene(game);
		
		stage = new Stage(this, "stages/" + stageName);
		
		try {
			
			bossConstructor = bossClass.getConstructor(
					new Class[]{GameScene.class, Vector2.class, Creature.class});
		
		} catch (NoSuchMethodException | SecurityException e) { e.printStackTrace(); }
		
	}
	
	private void reset() {
		gameObjects = new ArrayList<GameObject>();
		addQueue = new ArrayList<GameObject>();
		removeQueue = new ArrayList<GameObject>();
		
		bgGameObjects = new ArrayList<GameObject>();
		moveToBgQueue = new ArrayList<GameObject>();
		moveToFgQueue = new ArrayList<GameObject>();
		
		lbManager = new LifebarManager(this);
		
		player = new Megaman(this, stage.getAbsolutePlayerSpawn());
		
		try {
			
			boss = bossConstructor.newInstance(this, stage.getAbsoluteBossSpawn(), player);
		
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) { e.printStackTrace(); }
		
		gameObjects.add(boss);
		gameObjects.add(player);
		
		gameState = GameState.BATTLING;
		playedVictoryFanfare = false;
		
		game.musicManager.playMusic(stageName);
	}
	
	@Override
	public void update() {		
		
		if (game.getKeyManager().start && canPause) {			
			paused = !paused;	
			game.soundManager.playSound("pause");
			
			Utility.timeScale = paused ? 0f : 1f;
		}
			
		if (paused) {
			pauseMenu.update();
			return;
		}
		
		if (background != null)
			background.update();
		
		// Update
		for (GameObject object : bgGameObjects)
			if (object.active)
				object.update();
		
		for (GameObject object : gameObjects)
			if (object.active)
				object.update();
		
		// Check if player is alive
		if (!player.alive) {
			
			if (gameState == GameState.BATTLING) {
				gameState = GameState.LOST;
				game.musicManager.stopMusic(stageName);
				resetTimer.restart();
			} 
			else if (gameState == GameState.LOST) {
				if (resetTimer.done) reset();
				else resetTimer.update();
			}
		}
		
		// Check if boss is alive
		if (!boss.alive) {
			if (gameState == GameState.BATTLING) {
				gameState = GameState.WON;
	
				canPause = false;
				removeAllProjectiles();
				game.musicManager.stopMusic(stageName);
				
				winTimer.restart();
			} 
			else if (gameState == GameState.WON) {
				if (winTimer.done) {
					game.goBackToStageSelect();;
				} else winTimer.update();
				
				// 30% of 12 sec = 3.6 seconds
				if (winTimer.getProgressed() > 0.3 && !playedVictoryFanfare) {
					game.soundManager.playSound("victoryfanfare");
					playedVictoryFanfare = true;
				}
				
				// 80% of 12 sec = 9.6 seconds
				if (winTimer.getProgressed() > 0.8 && !player.teleporting) {
					player.teleporting = true;
				}
			}
		}
		
		bgQueue();
		fgQueue();
		removeQueue();
		addQueue();
	}

	@Override
	public void render(Graphics2D g) {
		if (background != null)
			background.render(g);
		
		for (GameObject object : bgGameObjects)
			if (object.active)
				object.render(g);
		
		stage.render(g);
		
		for (GameObject object : gameObjects)
			if (object.active)
				object.render(g);
		
		stage.renderForeground(g);
		
		lbManager.render(g);
		
		if (paused)
			pauseMenu.render(g);
	}
	
	@Override
	public void onStart() {
		reset();
	}
	
	@Override
	public void onRemove() {
		
	}
	
	public void addGameObject(GameObject object) {
		if (!addQueue.contains(object))
			addQueue.add(object);
	}
	
	public void removeGameObject(GameObject object) {
		if (!removeQueue.contains(object))
			removeQueue.add(object);
	}
	
	public void moveToBackground(GameObject object) {
		if (!moveToBgQueue.contains(object))
			moveToBgQueue.add(object);
	}
	
	public void moveToForeground(GameObject object) {
		if (!moveToFgQueue.contains(object))
			moveToFgQueue.add(object);
	}
	
	private void addQueue() {
		if (addQueue.size() > 0) {
			for (GameObject object : addQueue)
				gameObjects.add(object);
			addQueue.clear();
		}
	}
	
	private void removeQueue() {
		if (removeQueue.size() > 0) {
			for (GameObject object : removeQueue) {
				if (gameObjects.contains(object))
					gameObjects.remove(object);
				else if (bgGameObjects.contains(object))
					bgGameObjects.remove(object);
			}
			removeQueue.clear();
		}
	}
	
	private void bgQueue() {
		if (moveToBgQueue.size() > 0) {
			for (GameObject object : moveToBgQueue) {
				gameObjects.remove(object);
				bgGameObjects.add(object);
			}
			moveToBgQueue.clear();
		}
	}
	
	private void fgQueue() {
		if (moveToFgQueue.size() > 0) {
			for (GameObject object : moveToFgQueue) {
				bgGameObjects.remove(object);
				gameObjects.add(object);
			}
			moveToFgQueue.clear();
		}
	}
	
	private void removeAllProjectiles() {
		for (GameObject go : bgGameObjects) {
			if (go instanceof Projectile)
				removeGameObject(go);
		}
		for (GameObject go : gameObjects) {
			if (go instanceof Projectile)
				removeGameObject(go);
		}
	}
	
	public List<GameObject> getGameObjects() {
		return gameObjects;
	}
	
	public List<Entity> getEntities() {
		List<Entity> list = new ArrayList<Entity>();
		for (GameObject o : gameObjects)
			if (o instanceof Entity)
				list.add((Entity)o);
		
		return list;
	}

}
