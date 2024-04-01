package engine.display;

import engine.controller.Keyboard;
import engine.controller.Mouse;
import engine.states.GameState;
import engine.states.RayCasterScene;
import engine.states.Scene;
import engine.utils.Time;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.tinylog.Logger;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
	private long window;
	private static Window instance = null;
	public final int SCREEN_WIDTH = 1024 << 1;
	public final int SCREEN_HEIGHT = 512 << 1;
	private String title;
	
	private Window() {
		title = "Doom in Java! FPS: 0";
	}
	
	public static Window get() {
		if (instance == null) {
			instance = new Window();
		}
		return instance;
	}
	
	public void init() {
		Logger.info("Running LWJGL v" + Version.getVersion());
		GLFWErrorCallback.createPrint(System.err).set();
		
		if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW.");
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);
		
		window = glfwCreateWindow(SCREEN_WIDTH, SCREEN_HEIGHT, title, NULL, NULL);
		if (window == NULL) throw new IllegalStateException("Failed to create window.");
		
		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		
		glfwShowWindow(window);
		
		GL.createCapabilities();
		
		glfwSetCursorPosCallback(window, Mouse::mousePosCallback);
		glfwSetMouseButtonCallback(window, Mouse::mouseButtonCallback);
		glfwSetScrollCallback(window, Mouse::mouseScrollCallback);
		glfwSetKeyCallback(window, Keyboard::keyCallback);
		
		glClearColor(0.3f, 0.3f, 0.3f, 0.0f);
		glOrtho(0, SCREEN_WIDTH >> 1, SCREEN_HEIGHT >> 1, 0, 0, 1);
	}
	
	public void update() {
		glfwSwapBuffers(window);
		glfwPollEvents();
	}
	
	public void cleanup() {
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	public boolean shouldClose() {return glfwWindowShouldClose(window);}
	
	public void setTitle(String title) {
		glfwSetWindowTitle(window, title);
	}
	
	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT);
	}
}
