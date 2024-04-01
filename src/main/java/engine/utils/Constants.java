package engine.utils;

import org.joml.Vector3f;

public class Constants {
	public static final String TITLE = "Java RayCaster | FPS: ";
	public static final double DEGREE = Math.PI / 180.0;
	public static final Vector3f SKY_BLUE = new Vector3f(66.0f/255.0f, 135.0f/255.0f, 245.0f/255.0f);
	public static final Vector3f GRASS_GREEN = new Vector3f(29.0f/255.0f, 102.0f/255.0f, 42.0f/255.0f);
	public static final Vector3f RED = new Vector3f(1.0f, 0.0f, 0.0f);
	public static final Vector3f GREEN = new Vector3f(0.0f, 1.0f, 0.0f);
	public static final Vector3f BLUE = new Vector3f(0.0f, 0.0f, 1.0f);
	public static final Vector3f WHITE = new Vector3f(1.0f, 1.0f, 1.0f);
	public static final Vector3f BLACK = new Vector3f(0.0f, 0.0f, 0.0f);
	
	public static int NULLS = 0;
	public static int TILES = 1;
	public static int BRICK = 2;
	public static int WOODS = 3;
	public static int DOORS = 4;
	public static int W_DEV = 5;
	public static int F_DEV = 6;
	public static int SKY_1 = 7;
}
