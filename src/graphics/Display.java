package graphics;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Display {

	public int width;
	public int height;
	public String title;
	
	public JFrame jframe;
	public Canvas canvas;
	
	public Display (int width, int height, float scale, String title) {
		this.width = (int)(width * scale);
		this.height = (int)(height * scale);
		this.title = title;
		
		createDisplay();
	}
	
	private void createDisplay() {
		jframe = new JFrame(title);
		jframe.setSize(width, height);
		jframe.setResizable(false);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setIconImage(getIcon());
		jframe.setVisible(true);
		
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(width, height));
		canvas.setMaximumSize(new Dimension(width, height));
		canvas.setMinimumSize(new Dimension(width, height));
		canvas.setFocusable(false);
		
		jframe.add(canvas);
		jframe.pack();
		jframe.setLocationRelativeTo(null);
	}
	
	private BufferedImage getIcon() {
		String path = "icon.png";
		InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(path);
		BufferedImage image = null;
		try { image = ImageIO.read(is); } catch (IOException e) { e.printStackTrace();}
		return image;
	}
	
}
