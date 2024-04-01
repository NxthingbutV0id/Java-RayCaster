package engine;

import engine.controller.Keyboard;
import engine.controller.Mouse;
import engine.display.Window;
import engine.states.GameState;
import engine.states.RayCasterScene;
import engine.states.Scene;
import engine.utils.Time;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.tinylog.Logger;

import static engine.utils.Constants.TITLE;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.system.MemoryUtil.NULL;

public class EngineManager {
	private int fps;
	private boolean isRunning;
	private static Scene currentScene;
	private Window window;
	
	private void init() throws Exception {
		glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err).set());
		window = Window.get();
		window.init();
		changeScene(GameState.MAIN);
	}
	
	public static void changeScene(GameState states) throws Exception {
		switch (states) {
			case MAIN -> {
				currentScene = new RayCasterScene();
				currentScene.init();
			}
			case RAY -> {
				assert false : "State not implemented";
			}
			case MAP -> {
			}
		}
	}
	
	public void start() {
		try {
			init();
			if (isRunning) return;
			run();
		} catch (Exception e) {
			Logger.error(e);
		} finally {
			cleanup();
		}
	}
	
	private void run() throws Exception {
		isRunning = true;
		int frames = 0;
		double elapsedTime = 0;
		double lastTime = Time.getTime();
		double startTime;
		double passedTime = 0;
		
		while (isRunning) {
			elapsedTime += passedTime;
			
			if (window.shouldClose()) stop();
			
			if (elapsedTime >= 1) {
				fps = frames;
				window.setTitle(TITLE + fps);
				frames = 0;
				elapsedTime = 0;
			}
			
			update();
			render();
			frames++;
			
			startTime = Time.getTime();
			passedTime = startTime - lastTime;
			lastTime = startTime;
		}
	}
	
	private void update() {if (1.0/fps > 0) currentScene.update(1.0/fps);}
	
	private void render() throws Exception {
		window.clear();
		currentScene.render();
		window.update();
	}
	
	private void cleanup() {
		window.cleanup();
	}
	
	private void stop() {
		if (!isRunning) return;
		isRunning = false;
	}
}
