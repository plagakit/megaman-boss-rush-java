package general;

import java.util.Random;

public final class Utility {

	public static Random random = new Random();
	
	public static long currentTimeNano = 0;
	public static float timeScale = 1;
	public static float timeAdjust = 1;
	
	public static int randomIntBetween(int low, int high) {
		return random.nextInt(high - low) + low;
	}
	
	public static float randomFloatBetween(float low, float high) {
		return (random.nextFloat() * (high - low)) + low;
	}
	
	public static float lerp(float start, float end, float amount) {
		if (amount > 1) return end;
		else if (amount < 0) return start;
		else return start + ((end - start) * amount);
	}
	
	public static long currentTimeMillis() {
		return (long)(currentTimeNano / 1000000f);
	}
	
	public static void updateTime(long elapsedNanoTime) {;
		currentTimeNano += (elapsedNanoTime * timeScale);
	}

}
