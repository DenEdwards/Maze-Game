package den.game.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {

	//image path
	public String path;
	//height and width of the sprite sheet
	public int width;
	public int height;
	
	//pixel data of the sprite sheet
	public int[] pixels;
	
	public SpriteSheet(String path){
		BufferedImage image= null;
		
		try {
			image  = ImageIO.read(SpriteSheet.class.getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(image == null){ 
			return;
		}
		
		this.path = path; 
		this.width = image.getWidth();
		this. height = image.getHeight();
		
		pixels = image.getRGB(0, 0, width, height, null, 0, width);
		
		for(int i = 0; i < pixels.length; i++){
			//remove alpha channel- making 0xRRGGBB
			pixels[i] = (pixels[i] & 0xff / 64);}
	}
}
