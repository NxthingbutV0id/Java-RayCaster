package engine.objects;

import engine.utils.Utils;
import org.joml.Matrix2d;
import org.joml.Vector2d;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.tinylog.Logger;

import static engine.utils.Constants.DEGREE;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glEnd;
import static java.lang.Math.PI;
import static java.lang.Math.sin;
import static java.lang.Math.cos;

public class Player {
	private Vector2d position;
	private Vector3f color;
	public final double SPEED;
	public final double ROTATION_RATE = 90.0 * DEGREE;
	private Vector2d direction;
	private double facingAngle;
	
	public Player(double x, double y, double degrees) {
		position = new Vector2d(x, y);
		color = new Vector3f(1.0f, 1.0f, 0.0f);
		SPEED = 200.0;
		facingAngle = degrees * DEGREE;
		direction = new Vector2d(cos(facingAngle), sin(facingAngle));
	}
	
	public Vector2d getPosition() {return position;}
	
	public void setPosition(Vector2d position) {this.position = position;}
	
	public Vector2d getDirection() {return direction;}
	
	public void drawPlayer() {
		glColor3f(color.x, color.y, color.z);
		glPointSize(8.0f);
		glBegin(GL_POINTS);
		glVertex2d(position.x, position.y);
		glEnd();
		
		glLineWidth(3);
		glBegin(GL_LINES);
		glVertex2d(position.x, position.y);
		glVertex2d(position.x + direction.x * 20, position.y + direction.y * 20);
		glEnd();
	}
	
	public double getAngleRads() {
		return facingAngle;
	}
	
	public void updateAngle(double delta) {
		facingAngle += delta;
		if (facingAngle > 2 * PI) facingAngle -= 2 * PI;
		if (facingAngle < 0) facingAngle += 2 * PI;
		
		direction.x = cos(facingAngle);
		direction.y = sin(facingAngle);
	}
	
	public void logPlayer() {
		String message = "PLAYER DEBUG LOG";
		message += "\nPosition: " + Utils.vecToString(position);
		message += "\nDirection: " + Utils.vecToString(direction);
		message += String.format("\nFacing angle: %.3f radians (%.3f degrees)", facingAngle, Math.toDegrees(facingAngle));
		message += "\nColor: " + Utils.vecToString(color);
		Logger.debug(message);
	}
}
