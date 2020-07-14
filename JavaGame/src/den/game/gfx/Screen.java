package den.game.gfx;

public class Screen {

	public static final int MAP_WIDTH = 64;
	public static int MAP_WIDTH_MASK = MAP_WIDTH -1;
	
	//flip the mob in the game to signify turning around etc
	public static final byte BIT_MIRROR_X = 0x01;
	public static final byte BIT_MIRROR_Y = 0x0;
	
	public int[] pixels;
	
	/*x will move the screen left or right
	  y will move the screen up and down 
	 */
	public int xOffset = 0;
	public int yOffset = 0;
	
	public int width;
	public int height;
	
	public SpriteSheet sheet;
	
	public Screen(int width, int height,SpriteSheet sheet){
		this.width = width;
		this.height = height;
		this.sheet = sheet;
		
		pixels = new int[width * height];
	}

	//tile is the tile index on the sprite sheet
	public void render(int xPos, int yPos, int tile, int colour, int mirrorDir, int scale){
		//to keep it in the boundaries of the screen
		xPos -= xOffset;
		yPos -= yOffset;
		
		//mirroring on the two axis'
		boolean mirrorX = (mirrorDir & BIT_MIRROR_X) > 0;
		boolean mirrorY = (mirrorDir & BIT_MIRROR_Y) > 0;
		
		int scaleMap = scale - 1;
		
		//x and y coordinates of the sprite sheet
		int xTile = tile % 32;
		int yTile = tile / 32;
		//sprite sheet pixel location
		int tileOffset = (xTile << 3) + (yTile << 3) * sheet.width;
		for(int y = 0; y < 8; y++){
			//y position is in the screen 
			int ySheet = y;
			if(mirrorY)ySheet = 7-y;
			
			//scale and center the pixel
			int yPixel = y + yPos + (y * scaleMap) - ((scaleMap << 3) / 2);

			for(int x = 0; x < 8; x++){

				int xSheet = x;
				if(mirrorX)xSheet = 7-x;
				int xPixel = x + xPos + (x* scaleMap) - ((scaleMap << 3) /2 );
				/*get the actual colour, but shift back and forth depending
				 * on where which colour were doing and where on the pixel were doing it
				 * and verify were in the bounds of the colours (255)
				 */
				int col = (colour >> (sheet.pixels[xSheet + ySheet * sheet.width + tileOffset] * 8)) & 255;
				//set the pixel data//rendering the pixels
				if(col < 255) {
					for(int yScale = 0; yScale < scale; yScale++){
						//y position is in the screen
						if(yPixel + yScale < 0 ||yPixel + yScale >= height)
							continue;
						for(int xScale = 0; xScale < scale; xScale++){
							//x position is in the screen
							if(xPixel + xScale < 0 || xPixel + xScale >= width)
								continue;
							pixels[(xPixel + xScale) + (yPixel + yScale) * width] = col;
						}
					}
				}
			}
		}
	}
	public void setOffset(int xOffset, int yOffset) {     
		this.xOffset = xOffset;     
		this.yOffset = yOffset; 
	}
}
