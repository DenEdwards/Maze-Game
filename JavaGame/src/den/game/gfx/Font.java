package den.game.gfx;

public class Font {

	//all our characters from the sprite sheet
	private static String chars ="" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ      " + "0123456789.,:;'\"!?$%()-=+/      ";
	
	public static void render(String msg, Screen screen, int x, int y, int colour, int scale){
		msg = msg.toUpperCase();
		
		for(int i = 0; i < msg.length(); i++){
			//where we are with in the letters,numbers and symbols string
			int charIndex = chars.indexOf(msg.charAt(i));
			/*if the character doesn't exist in the character array we don't want to draw it
			 * x+(i*8) because there are 8 pixels per tile , charIndex + 30 * 32 because it starts 30 rows down and its within the bounds
			 * of the array
			 */
			if(charIndex >= 0)screen.render(x + (i*8), y, charIndex + 30 *32, colour, 0x00, scale);
			
		}
	}
}
