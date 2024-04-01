package engine.utils;

import org.joml.Vector2d;
import org.joml.Vector2i;
import org.joml.Vector3f;

public class Utils {
	public static String vecToString(Vector2i vec) {
		return "(" + vec.x + ", " + vec.y + ")";
	}
	
	public static String vecToString(Vector2d vec) {
		return String.format("(%.3f, %.3f)", vec.x, vec.y);
	}
	
	public static String vecToString(Vector3f vec) {
		return String.format("(r: %.3f, g: %.3f, b: %.3f)", vec.x, vec.y, vec.z);
	}
}
