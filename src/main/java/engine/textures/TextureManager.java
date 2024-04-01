package engine.textures;

public class TextureManager {
	public static final Texture TILES;
	public static final Texture BRICKS;
	public static final Texture WOOD;
	public static final Texture DOOR;
	public static final Texture NULL;
	public static final Texture DEV_WALL;
	public static final Texture DEV_FLOOR;
	public static final Texture SKY1;
	
	static {
		try {
			TILES = new Texture("/textures/tiles.ppm");
			BRICKS = new Texture("/textures/bricks.ppm");
			WOOD = new Texture("/textures/wood.ppm");
			DOOR = new Texture("/textures/door.ppm");
			NULL = new Texture("/textures/null.ppm");
			DEV_WALL = new Texture("/textures/dev_wall.ppm");
			DEV_FLOOR = new Texture("/textures/dev_floor.ppm");
			SKY1 = new Texture("/textures/sky1.ppm");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
