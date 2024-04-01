package engine.textures;

import org.joml.Vector2i;
import org.joml.Vector3f;

public class Texture {
	private final float[][][] colorMap;
	public final Vector2i RESOLUTION = new Vector2i();
	
	public Texture(String filePath) throws Exception {
		colorMap = TextureReader.getTextureFromFile(filePath);
		RESOLUTION.x = colorMap[0].length;
		RESOLUTION.y = colorMap.length;
	}
	
	public Vector3f getColor(Vector2i position) {
		return new Vector3f(colorMap[position.y][position.x]);
	}
	
	public Vector3f getColor(int x, int y) {
		return new Vector3f(colorMap[y][x]);
	}
}
