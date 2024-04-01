package engine.states;

import engine.controller.Keyboard;
import engine.display.Window;
import engine.objects.Level;
import engine.objects.Player;
import engine.textures.Texture;
import engine.textures.TextureManager;
import engine.utils.Pair;
import org.joml.Matrix2d;
import org.joml.Vector2d;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.tinylog.Logger;

import static engine.utils.Constants.*;
import static java.lang.Math.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;

public class RayCasterScene implements Scene {
	private Player player;
	private Level level;
	private final int INTERACTION_DISTANCE = 20;
	private final int BOUNDING_BOX = 10;
	private double deltaT;
	private double timeElapsed = 0;
	private final double P2 = PI/2;
	private final double P3 = 3 * PI / 2;
	private final double FOV = 60.0;
	private final double RENDER_SCALE = 2.0;
	private final int RENDER_WIDTH = 320;
	private final int RENDER_HEIGHT = 200;
	//private final Vector2d RENDER_OFFSET = new Vector2d(530.0, 0);
	private final Vector2d RENDER_OFFSET = new Vector2d(
			(Window.get().SCREEN_WIDTH >> 2) - (RENDER_SCALE * RENDER_WIDTH * 0.5),
			(Window.get().SCREEN_HEIGHT >> 2) - (RENDER_SCALE * RENDER_HEIGHT * 0.5)
	);
	
	private boolean spacePressed = false;
	private double testDelta1 = 0;
	private double testDelta2 = 0;
	
	@Override
	public void init() {
		Logger.info("GameState \"RayCaster\" initialized.");
		//player = new Player(73.930, 73.815, 29.636);
		player = new Player(8 * 64, 8 * 64, 0);
		level = new Level();
	}
	
	@Override
	public void update(double deltaT) {
		this.deltaT = deltaT;
		timeElapsed += deltaT;
		if (timeElapsed >= 1) {
			timeElapsed = 0;
			spacePressed = false;
		}
		boolean forward, backward, strafeLeft, strafeRight, interact, turnLeft, turnRight, turnLeftSlow, turnRightSlow;
		forward = Keyboard.isKeyPressed(GLFW_KEY_W);
		backward = Keyboard.isKeyPressed(GLFW_KEY_S);
		strafeLeft = Keyboard.isKeyPressed(GLFW_KEY_D);
		strafeRight = Keyboard.isKeyPressed(GLFW_KEY_A);
		interact = Keyboard.isKeyPressed(GLFW_KEY_E);
		turnLeft = Keyboard.isKeyPressed(GLFW_KEY_LEFT);
		turnRight = Keyboard.isKeyPressed(GLFW_KEY_RIGHT);
		turnLeftSlow = Keyboard.isKeyPlusShiftPressed(GLFW_KEY_LEFT);
		turnRightSlow = Keyboard.isKeyPlusShiftPressed(GLFW_KEY_RIGHT);
		double turnAmount = player.ROTATION_RATE * deltaT;
		
		if (Double.isFinite(turnAmount)) {
			if (turnLeft) player.updateAngle(-turnAmount);
			if (turnRight) player.updateAngle(turnAmount);
			if (turnLeftSlow) player.updateAngle(-turnAmount * 0.1);
			if (turnRightSlow) player.updateAngle(turnAmount * 0.1);
		}
		if (Keyboard.isKeyPressed(GLFW_KEY_SPACE) && !spacePressed) {
			player.logPlayer();
			System.out.printf("Test Delta 1: %.3f\nTest Delta 2: %.3f\n", testDelta1, testDelta2);
			spacePressed = true;
		}
		if (Keyboard.isKeyPressed(GLFW_KEY_UP)) testDelta1 += 20 * deltaT;
		if (Keyboard.isKeyPressed(GLFW_KEY_DOWN)) testDelta1 -= 20 * deltaT;
		if (Keyboard.isKeyPlusShiftPressed(GLFW_KEY_UP)) testDelta2 += 20 * deltaT;
		if (Keyboard.isKeyPlusShiftPressed(GLFW_KEY_DOWN)) testDelta2 -= 20 * deltaT;
		if (Keyboard.isKeyPressed(GLFW_KEY_R)) player.setPosition(new Vector2d(256, 256));
		
		Vector2d movementDirection = new Vector2d();
		
		if (forward) movementDirection.x += 1.0;
		if (backward) movementDirection.x -= 1.0;
		if (strafeLeft) movementDirection.y += 1.0;
		if (strafeRight) movementDirection.y -= 1.0;
		
		if (movementDirection.length() == 0 && !interact) return;
		if (movementDirection.length() != 0) movementDirection.normalize();
		
		Vector2d playerDirection = new Vector2d(player.getDirection());
		Vector2d velocityDirection = findTrueVelocityVector(playerDirection, movementDirection);
		
		Vector2d p = new Vector2d(player.getPosition());
		Vector2i hitBox = new Vector2i(
				(velocityDirection.x < 0) ? -BOUNDING_BOX : BOUNDING_BOX,
				(velocityDirection.y < 0) ? -BOUNDING_BOX : BOUNDING_BOX
		);
		
		Vector2i iPos = new Vector2i((int)(p.x/level.BLOCK_TRUE_SIZE.x), (int)(p.y/level.BLOCK_TRUE_SIZE.y));
		Vector2i iPosOffset = new Vector2i(
				(int)((p.x + hitBox.x)/level.BLOCK_TRUE_SIZE.x),
				(int)((p.y + hitBox.y)/level.BLOCK_TRUE_SIZE.y)
		);
		
		Vector2d velocity = new Vector2d(velocityDirection).mul(player.SPEED).mul(deltaT);
		
		if (level.getWalls(iPosOffset.x, iPos.y) == 0 && velocity.isFinite()) {
			player.getPosition().x += velocity.x;
		}
		if (level.getWalls(iPos.x, iPosOffset.y) == 0 && velocity.isFinite()) {
			player.getPosition().y += velocity.y;
		}
		
		if (interact) {
			Vector2i interactionRange = new Vector2i(
					(playerDirection.x < 0) ? -INTERACTION_DISTANCE : INTERACTION_DISTANCE,
					(playerDirection.y < 0) ? -INTERACTION_DISTANCE : INTERACTION_DISTANCE
			);
			
			iPosOffset = new Vector2i(
					(int)((p.x + interactionRange.x)/level.BLOCK_TRUE_SIZE.x),
					(int)((p.y + interactionRange.y)/level.BLOCK_TRUE_SIZE.y)
			);
			
			if (level.getWalls(iPosOffset) == DOORS) {
				level.setMapCell(iPosOffset, NULLS);
			}
		}
	}
	
	@Override
	public void render() {
		timeElapsed += deltaT;
		//level.drawMap2D();
		//player.drawPlayer();
		drawRenderArea();
		drawSky();
		drawMapRays();
	}
	
	private void drawMapRays() {
		double rayAngle = player.getAngleRads() - (FOV/2 * DEGREE);
		rayAngle = fixAngle(rayAngle);
		
		Vector2d p = player.getPosition();
		Texture vTexture, hTexture;
		Vector2d hRay, vRay;
		double distH, distV;
		var renderer = new RenderManager();
		
		for (int colombIndex = 0; colombIndex < RENDER_WIDTH; colombIndex++) {
			var temp = checkHorizontalLines(rayAngle, p);
			
			hRay = temp.x;
			hTexture = temp.y;
			
			temp = checkVerticalLines(rayAngle, p);
			
			vRay = temp.x;
			vTexture = temp.y;
			
			distV = vRay.distance(p);
			distH = hRay.distance(p);
			renderer.rayIndex = colombIndex;
			renderer.rayAngle = rayAngle;
			renderer.trueDistance = min(distH, distV);
			
			if (distV < distH) {
				renderer.ray = new Vector2d(vRay);
				renderer.shade = 1.0f;
				renderer.texture = vTexture;
			} else {
				renderer.ray = new Vector2d(hRay);
				renderer.shade = 0.5f;
				renderer.texture = hTexture;
			}
			
			renderer.rayCast();
			
			//drawRay(p, renderer.ray);
			
			rayAngle += (FOV * DEGREE)/RENDER_WIDTH;
			rayAngle = fixAngle(rayAngle);
		}
	}
	
	private void drawRenderArea() {
		glColor3f(BLACK.x, BLACK.x, BLACK.x);
		glBegin(GL_QUADS);
		glVertex2d(RENDER_OFFSET.x, RENDER_OFFSET.y);
		glVertex2d((RENDER_WIDTH * RENDER_SCALE) + RENDER_OFFSET.x, RENDER_OFFSET.y);
		glVertex2d((RENDER_WIDTH * RENDER_SCALE) + RENDER_OFFSET.x, (RENDER_HEIGHT * RENDER_SCALE) + RENDER_OFFSET.y);
		glVertex2d(RENDER_OFFSET.x, (RENDER_HEIGHT * RENDER_SCALE) + RENDER_OFFSET.y);
		glEnd();
	}
	
	private void drawRay(Vector2d start, Vector2d end) {
		glLineWidth(1);
		glColor3f(0, 1, 0);
		glBegin(GL_LINES);
		glVertex2d(start.x, start.y);
		glVertex2d(end.x, end.y);
		glEnd();
	}
	
	private class RenderManager {
		private float shade;
		private Vector2d ray;
		private double rayAngle;
		private double trueDistance;
		private double relativeDistance;
		private int rayIndex;
		private Texture texture;
		private Vector2d textureOffset;
		private Vector2d textureStep;
		private int projWallHeight;
		private int lineOffset;
		private final double projPlaneDistance = (RENDER_WIDTH + RENDER_HEIGHT) * tan((FOV * DEGREE)/2);
		
		public void rayCast() {
			relativeDistance = trueDistance * cos(fixAngle(player.getAngleRads() - rayAngle));
			textureOffset = new Vector2d();
			textureStep = new Vector2d();
			
			projWallHeight = (int)((level.BLOCK_TRUE_SIZE.z * projPlaneDistance) / relativeDistance);
			textureStep.y = (double)(texture.RESOLUTION.y)/ projWallHeight;
			if (projWallHeight > RENDER_HEIGHT) {
				textureOffset.y = (projWallHeight - RENDER_HEIGHT) * 0.5;
				projWallHeight = RENDER_HEIGHT;
			}
			lineOffset = (int)(RENDER_SCALE * ((RENDER_HEIGHT >> 1) - (projWallHeight >> 1)));
			
			glPointSize((float)(2 * RENDER_SCALE));
			drawFloorsAndCeilings();
			drawWalls();
		}
		
		private void drawWalls() {
			Vector2d textureCoords = new Vector2d();
			if (shade == 1.0) {
				textureCoords.x = (int)(ray.y * 0.5)% texture.RESOLUTION.x;
				if (rayAngle > P2 && rayAngle < P3) textureCoords.x = 31 - textureCoords.x;
			} else {
				textureCoords.x = 31 - (int)(ray.x * 0.5)% texture.RESOLUTION.x;
				if (rayAngle > PI) textureCoords.x = 31 - textureCoords.x;
			}
			textureCoords.y = textureOffset.y * textureStep.y;
			
			Vector2i index = new Vector2i((int) textureCoords.x, 0);
			for (int i = 0; i < projWallHeight; i++) {
				index.y = (int) textureCoords.y;
				Vector3f color = texture.getColor(index);
				Vector2d pos = new Vector2d(rayIndex, i + (lineOffset/RENDER_SCALE));
				color.mul(shade);
				drawPixel(color, pos);
				textureCoords.y += textureStep.y;
			}
		}
		
		//TODO: This shit doesn't fucking work, I cannot figure out the math for the life of me!
		private void drawFloorsAndCeilings() {
			Vector2d p = new Vector2d(player.getPosition());
			Vector2d permadi = new Vector2d();
			for (int y = (int)((lineOffset/RENDER_SCALE) + projWallHeight); y <= RENDER_HEIGHT; y++) {
				double dy = y - (RENDER_WIDTH/4.0) - 20; //???
				double fixedAngle = cos(fixAngle(player.getAngleRads() - rayAngle));
				double ar = ((double)RENDER_WIDTH / (double)RENDER_HEIGHT);
				double playerHeight = (level.BLOCK_TRUE_SIZE.z * RENDER_SCALE) * 0.5 - 42; //???
				double arcf = 2/ar * tan((FOV * 0.5) * DEGREE);
				//double magic = (2 * RENDER_SCALE)/tan((FOV * 0.5) * DEGREE); Fucking magic numbers everywhere...
				
				permadi.x = (p.x/2 + cos(rayAngle) * playerHeight * projPlaneDistance * arcf/dy/fixedAngle);
				permadi.y = (p.y/2 + sin(rayAngle) * playerHeight * projPlaneDistance * arcf/dy/fixedAngle);
				Vector2i mapPos = new Vector2i((int)(permadi.x/32), (int)(permadi.y/32));
				
				Vector3f color;
				Vector2d pos;
				Texture fTexture = level.getFloorTexture(mapPos);
				Texture cTexture = level.getCeilingTexture(mapPos);
				if (permadi.x/32 < level.MAP_DIMENSIONS.x && permadi.y/32 < level.MAP_DIMENSIONS.y && permadi.x/32 >= 0 && permadi.y/32 >= 0) {
					if (y != RENDER_HEIGHT) {//draw Floors
						color = fTexture.getColor(((int) (permadi.x) & 31), ((int) (permadi.y) & 31));
						color.mul(0.7f);
						pos = new Vector2d(rayIndex, y);
						drawPixel(color, pos);
					}
					if (cTexture != TextureManager.SKY1) {//draw Ceiling
						color = cTexture.getColor(((int) (permadi.x) & 31), ((int) (permadi.y) & 31));
						color.mul(1.0f);
						pos = new Vector2d(rayIndex, RENDER_HEIGHT - y);
						drawPixel(color, pos);
					}
				}
			}
		}
	}
	
	private Pair<Vector2d, Texture> checkHorizontalLines(double ra, Vector2d p) {
		int depth = 0;
		double aTan = -1/tan(ra);
		Vector2d r = new Vector2d();
		Vector2d o = new Vector2d();
		
		if (ra > PI) {
			r.y = (((int)(p.y/level.BLOCK_TRUE_SIZE.y))*level.BLOCK_TRUE_SIZE.y) - 0.0001;
			r.x = (p.y - r.y) * aTan + p.x;
			o.y = -level.BLOCK_TRUE_SIZE.y;
			o.x = -o.y * aTan;
		}
		if (ra < PI) {
			r.y = (((int)(p.y/level.BLOCK_TRUE_SIZE.y))*level.BLOCK_TRUE_SIZE.y) + level.BLOCK_TRUE_SIZE.y;
			r.x = (p.y - r.y) * aTan + p.x;
			o.y = level.BLOCK_TRUE_SIZE.y;
			o.x = -o.y * aTan;
		}
		if (ra == 0 || ra == PI) {
			r.x = p.x;
			r.y = p.y;
			depth = 8;
		}
		
		return getRayAndTexture(r, o, depth);
	}
	
	private Pair<Vector2d, Texture> checkVerticalLines(double ra, Vector2d p) {
		int depth = 0;
		double nTan = -tan(ra);
		Vector2d r = new Vector2d();
		Vector2d o = new Vector2d();
		
		if (ra > P2 && ra < P3) {
			r.x = (((int)(p.x/level.BLOCK_TRUE_SIZE.x))*level.BLOCK_TRUE_SIZE.x) - 0.0001;
			r.y = (p.x - r.x) * nTan + p.y;
			o.x = -level.BLOCK_TRUE_SIZE.x;
			o.y = -o.x * nTan;
		}
		if (ra < P2 || ra > P3) {
			r.x = (((int)(p.x/level.BLOCK_TRUE_SIZE.x))*level.BLOCK_TRUE_SIZE.x) + level.BLOCK_TRUE_SIZE.x;
			r.y = (p.x - r.x) * nTan + p.y;
			o.x = level.BLOCK_TRUE_SIZE.x;
			o.y = -o.x * nTan;
		}
		if (ra == P2 || ra == P3) {
			r.x = p.x;
			r.y = p.y;
			depth = max(level.MAP_DIMENSIONS.x + 1, level.MAP_DIMENSIONS.y + 1);
		}
		
		return getRayAndTexture(r, o, depth);
	}
	
	private Pair<Vector2d, Texture> getRayAndTexture(Vector2d r, Vector2d o, int initDepth) {
		Vector2d ray = new Vector2d(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		Texture texture = TextureManager.NULL;
		Vector2i mp = new Vector2i();
		int depth = initDepth;
		
		while (depth < max(level.MAP_DIMENSIONS.x + 1, level.MAP_DIMENSIONS.y + 1)) {
			mp.x = (int)(r.x/level.BLOCK_TRUE_SIZE.x);
			mp.y = (int)(r.y/level.BLOCK_TRUE_SIZE.y);
			
			if (isInBounds(mp) && level.getWalls(mp) > 0) {
				ray.x = r.x;
				ray.y = r.y;
				texture = level.getWallTexture(mp);
				break;
			} else {
				r.x += o.x;
				r.y += o.y;
				depth++;
			}
		}
		
		return new Pair<>(ray, texture);
	}
	
	private double fixAngle(double a) {
		if (a > 2 * PI) a -= 2 * PI;
		if (a < 0) a += 2 * PI;
		return a;
	}
	
	private Vector2d rotateVectorBy(Vector2d vec, double theta) {
		var mat = new Matrix2d();
		mat.m00 = cos(theta);
		mat.m10 = -sin(theta);
		mat.m01 = sin(theta);
		mat.m11 = cos(theta);
		
		return new Vector2d(vec).mul(mat);
	}
	
	//Movement Direction = direction of movement relative to the player, forward -> +x, right -> +y
	//Player Direction = the direction of the player relative to the world
	private Vector2d findTrueVelocityVector(Vector2d playerDirection, Vector2d movementDirection) {
		Vector2d absoluteDirection = new Vector2d(1, 0);
		double playerAngle = -fixAngle(playerDirection.angle(absoluteDirection));
		return rotateVectorBy(movementDirection, playerAngle);
	}
	
	private boolean isInBounds(Vector2i point) {
		boolean isInBoundsX, isInBoundsY;
		isInBoundsX = point.x >= 0 && point.x < level.MAP_DIMENSIONS.x;
		isInBoundsY = point.y >= 0 && point.y < level.MAP_DIMENSIONS.y;
		return isInBoundsX && isInBoundsY;
	}
	
	private void drawSky() {
		for (int y = 0; y < RENDER_HEIGHT >> 1; y++) {
			for (int x = 0; x < RENDER_WIDTH; x++) {
				int xOffset = (int)((((-player.getAngleRads() * 2)/(PI)) * TextureManager.SKY1.RESOLUTION.x) - x);
				xOffset %= RENDER_WIDTH;
				if (xOffset < 0) xOffset += RENDER_WIDTH;
				
				Vector3f color = TextureManager.SKY1.getColor(xOffset, y);
				Vector2d pos = new Vector2d(x, y);
				drawPixel(color, pos);
			}
		}
	}
	
	private void drawPixel(Vector3f color, Vector2d pos) {
		glColor3f(color.x, color.y, color.z);
		glBegin(GL_POINTS);
		glVertex2d(pos.x * RENDER_SCALE + RENDER_OFFSET.x, pos.y * RENDER_SCALE + RENDER_OFFSET.y);
		glEnd();
	}
}
