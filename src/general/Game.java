package general;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferStrategy;

import audio.MusicManager;
import audio.SoundManager;
import graphics.Display;
import graphics.SpriteManager;
import scenes.GameScene;
import scenes.Scene;
import scenes.StageSelectScene;
import gameobjects.marx.Marx;
import gameobjects.kingkrool.KingKRool;

public class Game {

	public int width;
	public int height;
	public float scale;
	private String title = "Megaman";
	private Display display;
	private Graphics2D g;
	private BufferStrategy bs;
	
	private KeyManager keyManager;
	private SpriteManager spriteManager;
	public SoundManager soundManager;
	public MusicManager musicManager;
	
	public boolean running = false;
	public static boolean DEBUG = true;
	private int currentFPS = 0;
	
	private Scene currentScene;
	private StageSelectScene stageSelect;
	
	public Game(int width, int height, float scale) {
		this.width = width;
		this.height = height;
		this.scale = scale;
		
		init();
		run();
	}
	
	private void init() {
		running = true;
		
		keyManager = new KeyManager();
		spriteManager = new SpriteManager();
		soundManager = new SoundManager();
		musicManager = new MusicManager();
		
		display = new Display(width, height, 3, title);
		display.jframe.addKeyListener(keyManager);
		
		stageSelect = new StageSelectScene(this);
		currentScene = stageSelect;
		currentScene.onStart();
	}
	
	private void run() {
		final int MAX_FPS = 144;
		final int IDEAL_FPS = 60;
		double timePerUpdate = 1000000000/MAX_FPS;
		double targetTPU = 1000000000/IDEAL_FPS; // physics calibrated to 60 fps
		double deltaTime = 0;
		long currentTime;
		long lastTime = System.nanoTime();
		long timer = 0;
		
		int updates = 0;
	
		long elapsedTime = 0;
		long lastUpdateTime = lastTime;
		
		while (running) {
			
			currentTime = System.nanoTime();
			elapsedTime += currentTime - lastTime;
			deltaTime += (currentTime - lastTime) / timePerUpdate;			
			timer += currentTime - lastTime;
			lastTime = currentTime;

			if (deltaTime >= 1) {
				updates++;
				deltaTime--;
				
				Utility.timeAdjust = (float)((currentTime - lastUpdateTime) / targetTPU);
				update(elapsedTime);
				render();
				
				elapsedTime = 0;
				lastUpdateTime = currentTime;
			}
			
			if (timer >= 1000000000) {
				if (DEBUG) {
					System.out.format("FPS: %d\n", updates);
					currentFPS = updates;
					updates = 0;
				}
				timer = 0;
			}
			
		}
	}
	
	private void update(long elapsedNanoTime) {
		if (currentScene != null) {
			keyManager.update();
			
			if (keyManager.debug)
				DEBUG ^= true;
			
			currentScene._update(elapsedNanoTime);
		}
	}
	
	private void render() {
		bs = display.canvas.getBufferStrategy();
		if (bs == null) {
			display.canvas.createBufferStrategy(2);
			return;
		}
		g = (Graphics2D)bs.getDrawGraphics();
		
		//Clear screen
		g.scale(3, 3);
		g.clearRect(0, 0, (int)((width) * scale), (int)((height) * scale));
		
		//Draw
		if (currentScene != null)
			currentScene.render(g);
		
		if (DEBUG)
			drawFPS();
			
		//End 
		bs.show();
		g.dispose();
	}
	
	private void drawFPS() {
		String s = String.format("FPS: %d", currentFPS);
		Font fpsFont = new Font("Arial", Font.BOLD, (int)(8 * scale));
		g.setColor(Color.BLACK);
		FontRenderContext frc = g.getFontRenderContext();
		GlyphVector gv = fpsFont.createGlyphVector(frc, s);
		g.translate(0, 8 * scale);
		g.draw(gv.getOutline());
		g.translate(0, -8 * scale);
		g.setColor(Color.YELLOW);
		g.setFont(fpsFont);
		g.drawString(s, 0, (int)(8 * scale));
	}
	
	public KeyManager getKeyManager() {
		return keyManager;
	}
	
	public SpriteManager getSpriteManager() {
		return spriteManager;
	}
	
	public Display getDisplay() {
		return display;
	}
	
	public Scene getScene() {
		return currentScene;
	}
	
	public void setScene(Scene s) {
		currentScene.onRemove();
		currentScene = s;
		currentScene.onStart();
	}
	
	public void goBackToStageSelect() {
		setScene(stageSelect);
	}

}
