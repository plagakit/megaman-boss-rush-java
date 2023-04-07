package general;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener {

	private final int KEY_SIZE = 256;
	private boolean[] keys, justPressed, cantPress;
	public boolean up, left, right, down, upPressed, leftPressed, rightPressed, downPressed,
	shoot, jumpPressed, jumpHeld, slide, start, debug;

	public KeyManager() {
		keys = new boolean[KEY_SIZE];
		justPressed = new boolean[KEY_SIZE];
		cantPress = new boolean[KEY_SIZE];
	}
	
	public void update() {
		
		for (int i = 0; i < KEY_SIZE; i++) {
			if (cantPress[i] && !keys[i])
				cantPress[i] = false;
			else if (justPressed[i]) {
				cantPress[i] = true;
				justPressed[i] = false;
			}
			if (!cantPress[i] && keys[i])
				justPressed[i] = true;
		}
		
		left = keys[KeyEvent.VK_A] || keys[KeyEvent.VK_LEFT];
		right = keys[KeyEvent.VK_D] || keys[KeyEvent.VK_RIGHT];
		up = keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP];
		down = keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN];
		
		leftPressed = justPressed[KeyEvent.VK_A] || justPressed[KeyEvent.VK_LEFT];
		rightPressed = justPressed[KeyEvent.VK_D] || justPressed[KeyEvent.VK_RIGHT];
		upPressed = justPressed[KeyEvent.VK_W] || justPressed[KeyEvent.VK_UP];
		downPressed = justPressed[KeyEvent.VK_S] || justPressed[KeyEvent.VK_DOWN];
		
		jumpPressed = justPressed[KeyEvent.VK_SPACE] || justPressed[KeyEvent.VK_Z];
		jumpHeld = keys[KeyEvent.VK_SPACE] || keys[KeyEvent.VK_Z];
		shoot = justPressed[KeyEvent.VK_O] || justPressed[KeyEvent.VK_X];
		slide = justPressed[KeyEvent.VK_P] || justPressed[KeyEvent.VK_C];
		start = justPressed[KeyEvent.VK_ENTER] || justPressed[KeyEvent.VK_ESCAPE];
		debug = justPressed[KeyEvent.VK_F1];
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int num = e.getKeyCode();
		if(num < 0 || num >= KEY_SIZE)
			return;
		keys[num] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int num = e.getKeyCode();
		if(num < 0 || num >= KEY_SIZE)
			return;
		keys[num] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}

}
