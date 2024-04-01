package engine.utils;

public class Time {
	private static final long timeStarted = System.nanoTime();
	public static double getTime() {
		return (System.nanoTime() - timeStarted)/1e9;
	}
}
