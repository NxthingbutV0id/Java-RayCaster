package engine.controller;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector2fc;

import static org.lwjgl.glfw.GLFW.*;

public class Mouse {
	private static Mouse instance = null;
	private Vector2d scroll;
	private Vector2d currentPos, lastPos;
	private boolean[] mouseButtonPressed = new boolean[3];
	private boolean isDragging;
	
	private Mouse() {
		scroll = new Vector2d();
		currentPos = new Vector2d();
		lastPos = new Vector2d();
		
	}
	
	public static Mouse get() {
		if (instance == null) {
			instance = new Mouse();
		}
		return instance;
	}
	
	public static void mousePosCallback(long window, double xpos, double ypos) {
		get().lastPos = new Vector2d(get().currentPos);
		get().currentPos = new Vector2d(xpos, ypos);
		get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2];
	}
	
	public static void mouseButtonCallback(long window, int button, int action, int mods) {
		if (action == GLFW_PRESS) {
			if (button < get().mouseButtonPressed.length) {
				get().mouseButtonPressed[button] = true;
			}
		} else if (action == GLFW_RELEASE) {
			if (button < get().mouseButtonPressed.length) {
				get().mouseButtonPressed[button] = false;
				get().isDragging = false;
			}
		}
	}
	
	public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
		get().scroll = new Vector2d(xOffset, yOffset);
	}
	
	public static void endFrame() {
		get().scroll = new Vector2d();
		get().lastPos = new Vector2d(get().currentPos);
	}
	
	public static Vector2f getPosition() {
		return new Vector2f((float)get().currentPos.x, (float)get().currentPos.y);
	}
	
	public static Vector2f getDelta() {
		Vector2f result = new Vector2f((float)get().lastPos.x, (float)get().lastPos.y);
		return result.sub(new Vector2f((float)get().currentPos.x, (float)get().currentPos.y));
	}
	
	public static Vector2f getScroll() {
		return new Vector2f((float)get().scroll.x, (float)get().scroll.y);
	}
	
	public static boolean isDragging() {
		return get().isDragging;
	}
	
	public static boolean isButtonDown(int button) {
		return button < get().mouseButtonPressed.length && get().mouseButtonPressed[button];
	}
}
