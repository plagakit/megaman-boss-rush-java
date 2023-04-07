package general;

public class Timer {
	
	public long goalTime;
	public long lastTime;
	public long timer;
	public boolean done;
	
	public Timer(long timeInMS) {
		restart(timeInMS);
	}
	
	public void update() {
		if (done) 
			return;
		
		timer += Utility.currentTimeMillis() - lastTime;
		lastTime = Utility.currentTimeMillis();
		
		if (timer >= goalTime)
			done = true;
	}
	
	public void restart() {
		restart(goalTime);
	}
	
	public void restart(long timeInMS) {
		goalTime = timeInMS;
		lastTime = Utility.currentTimeMillis();
		timer = 0;
		done = false;
	}
	
	public float getProgressed() {
		return (float)timer / (float)goalTime;
	}
}


