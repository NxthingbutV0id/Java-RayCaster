package engine.textures;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TextureReader {
	public static float[][][] getTextureFromFile(String path) throws Exception {
		InputStream inputStream = TextureReader.class.getResourceAsStream(path);
		
		if (inputStream == null) throw new FileNotFoundException(path);
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.US_ASCII));
		
		String[] firstLine = reader.readLine().split(",");
		
		int xSize = Integer.parseInt(firstLine[0].trim());
		int ySize = Integer.parseInt(firstLine[1].trim());
		int max = Integer.parseInt(reader.readLine().replace(',', ' ').trim());
		
		float[][][] output = new float[ySize][xSize][3];
		List<Integer> values = new ArrayList<>();
		String line;
		while ((line = reader.readLine()) != null) {
			String[] numbers = line.split(",");
			for (String number : numbers) {
				values.add(Integer.parseInt(number.trim()));
			}
		}
		reader.close();
		
		int i = 0;
		for (int y = 0; y < ySize; y++) {
			for (int x = 0; x < xSize; x++) {
				for (int c = 0; c < 3; c++) {
					if (i < values.size()) {
						output[y][x][c] = (float)(values.get(i))/(float)(max);
						++i;
					}
				}
			}
		}
		
		return output;
	}
	
	
}
