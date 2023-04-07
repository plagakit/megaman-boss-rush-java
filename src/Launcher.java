import general.Game;

public class Launcher {

	public static void main(String[] args) {
		
		Game g = new Game(400, 240, 1f);

		System.out.format("%s closed\n", g.getClass());
		
	}

}
