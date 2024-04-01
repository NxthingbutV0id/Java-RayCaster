package engine.controller;

import static org.lwjgl.glfw.GLFW.*;

public class Keyboard {
	private static Keyboard instance = null;
	private boolean[] keyPressed = new boolean[350];
	private boolean[] keyPressedShift = new boolean[350];
	
	private Keyboard() {}
	
	public static Keyboard get() {
		if (instance == null) {
			instance = new Keyboard();
		}
		return instance;
	}
	
	public static void keyCallback(long window, int key, int scancode, int action, int mods) {
		if (mods == GLFW_MOD_SHIFT) {
			if (action == GLFW_PRESS) {
				get().keyPressedShift[key] = true;
			} else if (action == GLFW_RELEASE) {
				get().keyPressedShift[key] = false;
			}
		} else {
			if (action == GLFW_PRESS) {
				get().keyPressed[key] = true;
			} else if (action == GLFW_RELEASE) {
				get().keyPressed[key] = false;
			}
		}
	}
	
	public static boolean isKeyPressed(int keyCode) {
		return get().keyPressed[keyCode];
	}
	
	public static boolean isKeyPlusShiftPressed(int keyCode) {
		return get().keyPressedShift[keyCode];
	}
}
