package engine.objects;

import engine.textures.Texture;
import engine.textures.TextureManager;
import org.joml.Vector2i;
import org.joml.Vector3i;

import static engine.utils.Constants.*;
import static org.lwjgl.opengl.GL46.*;

public class Level {
	private final int[][] WALLS;
	private final int[][] FLOOR;
	private final int[][] CEILING;
	public final Vector2i MAP_DIMENSIONS = new Vector2i(16, 16);
	public final Vector3i BLOCK_TRUE_SIZE = new Vector3i(64, 64, 64);
	
	public Level() {
		WALLS = new int[][] {
				{W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV},
				{W_DEV, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, W_DEV},
				{W_DEV, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, W_DEV},
				{W_DEV, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, W_DEV},
				{W_DEV, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, W_DEV},
				{W_DEV, NULLS, W_DEV, W_DEV, W_DEV, W_DEV, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, W_DEV},
				{W_DEV, NULLS, W_DEV, NULLS, NULLS, W_DEV, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, W_DEV},
				{W_DEV, NULLS, W_DEV, NULLS, NULLS, DOORS, NULLS, NULLS, NULLS, NULLS, NULLS, W_DEV, W_DEV, NULLS, NULLS, W_DEV},
				{W_DEV, NULLS, W_DEV, NULLS, NULLS, W_DEV, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, W_DEV},
				{W_DEV, NULLS, W_DEV, W_DEV, W_DEV, W_DEV, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, W_DEV},
				{W_DEV, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, W_DEV},
				{W_DEV, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, W_DEV},
				{W_DEV, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, W_DEV, NULLS, NULLS, W_DEV},
				{W_DEV, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, W_DEV},
				{W_DEV, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, NULLS, W_DEV},
				{W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV}
		};
		
		FLOOR = new int[][] {
				{W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV},
				{W_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, W_DEV},
				{W_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, W_DEV},
				{W_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, W_DEV},
				{W_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, W_DEV},
				{W_DEV, F_DEV, W_DEV, W_DEV, W_DEV, W_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, W_DEV},
				{W_DEV, F_DEV, W_DEV, F_DEV, F_DEV, W_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, W_DEV},
				{W_DEV, F_DEV, W_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, W_DEV, W_DEV, F_DEV, F_DEV, W_DEV},
				{W_DEV, F_DEV, W_DEV, F_DEV, F_DEV, W_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, W_DEV},
				{W_DEV, F_DEV, W_DEV, W_DEV, W_DEV, W_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, W_DEV},
				{W_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, W_DEV},
				{W_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, W_DEV},
				{W_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, W_DEV, F_DEV, F_DEV, W_DEV},
				{W_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, W_DEV},
				{W_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, W_DEV},
				{W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV}
		};
		
		CEILING = new int[][] {
				{W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV},
				{W_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, W_DEV},
				{W_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, SKY_1, SKY_1, SKY_1, F_DEV, W_DEV},
				{W_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, SKY_1, SKY_1, SKY_1, F_DEV, W_DEV},
				{W_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, SKY_1, SKY_1, SKY_1, F_DEV, W_DEV},
				{W_DEV, F_DEV, W_DEV, W_DEV, W_DEV, W_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, W_DEV},
				{W_DEV, F_DEV, W_DEV, F_DEV, F_DEV, W_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, W_DEV},
				{W_DEV, F_DEV, W_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, W_DEV, W_DEV, F_DEV, F_DEV, W_DEV},
				{W_DEV, F_DEV, W_DEV, F_DEV, F_DEV, W_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, W_DEV},
				{W_DEV, F_DEV, W_DEV, W_DEV, W_DEV, W_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, W_DEV},
				{W_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, W_DEV},
				{W_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, W_DEV},
				{W_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, W_DEV, F_DEV, F_DEV, W_DEV},
				{W_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, W_DEV},
				{W_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, F_DEV, W_DEV},
				{W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV, W_DEV}
		};
	}
	
	public int getWalls(int x, int y) {
		try {
			return WALLS[y][x];
		} catch (ArrayIndexOutOfBoundsException e) {
			return -1;
		}
	}
	
	public int getWalls(Vector2i vec) {
		try {
			return WALLS[vec.y][vec.x];
		} catch (ArrayIndexOutOfBoundsException e) {
			return -1;
		}
	}
	
	public int getFloor(int x, int y) {
		try {
			return FLOOR[y][x];
		} catch (ArrayIndexOutOfBoundsException e) {
			return -1;
		}
	}
	
	public int getFloor(Vector2i vec) {
		try {
			return FLOOR[vec.y][vec.x];
		} catch (ArrayIndexOutOfBoundsException e) {
			return -1;
		}
	}
	
	public int getCeiling(int x, int y) {
		try {
			return CEILING[y][x];
		} catch (ArrayIndexOutOfBoundsException e) {
			return -1;
		}
	}
	
	public int getCeiling(Vector2i vec) {
		try {
			return CEILING[vec.y][vec.x];
		} catch (ArrayIndexOutOfBoundsException e) {
			return -1;
		}
	}
	
	public void setMapCell(int x, int y, int value) {WALLS[y][x] = value;}
	
	public void setMapCell(Vector2i vec, int value) {WALLS[vec.y][vec.x] = value;}
	
	public void drawMap2D() {
		Vector2i offset = new Vector2i();
		for (int y = 0; y < MAP_DIMENSIONS.y; y++) {
			for (int x = 0; x < MAP_DIMENSIONS.x; x++) {
				if (getWalls(x, y) > 0) {
					glColor3f(1, 1, 1);
				} else {
					glColor3f(0, 0, 0);
				}
				offset.x = x * BLOCK_TRUE_SIZE.x;
				offset.y = y * BLOCK_TRUE_SIZE.y;
				
				glBegin(GL_QUADS);
				glVertex2i(offset.x + 1, offset.y + 1);
				glVertex2i(offset.x + 1, offset.y + BLOCK_TRUE_SIZE.y - 1);
				glVertex2i(offset.x + BLOCK_TRUE_SIZE.x - 1, offset.y + BLOCK_TRUE_SIZE.y - 1);
				glVertex2i(offset.x + BLOCK_TRUE_SIZE.x - 1, offset.y + 1);
				glEnd();
			}
		}
	}
	
	public Texture getWallTexture(int x, int y) {return getTexture(getWalls(x, y));}
	
	public Texture getWallTexture(Vector2i vec) {return getTexture(getWalls(vec));}
	
	public Texture getFloorTexture(int x, int y) {return getTexture(getFloor(x, y));}
	
	public Texture getFloorTexture(Vector2i vec) {return getTexture(getFloor(vec));}
	
	public Texture getCeilingTexture(int x, int y) {return getTexture(getCeiling(x, y));}
	
	public Texture getCeilingTexture(Vector2i vec) {return getTexture(getCeiling(vec));}
	
	private Texture getTexture(int index) {
		return switch (index) {
			case 1 -> TextureManager.TILES;
			case 2 -> TextureManager.BRICKS;
			case 3 -> TextureManager.WOOD;
			case 4 -> TextureManager.DOOR;
			case 5 -> TextureManager.DEV_WALL;
			case 6 -> TextureManager.DEV_FLOOR;
			case 7 -> TextureManager.SKY1;
			default -> TextureManager.NULL;
		};
	}
}
