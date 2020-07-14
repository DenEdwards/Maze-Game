package den.game.level.tiles;

import den.game.gfx.Screen;
import den.game.level.Level;

public class BasicTile extends Tile{
    //location of the tile in the array
	protected int tileId;
	//actual colours on the tile
	protected int tileColour;
	
	public BasicTile(int id, int x, int y, int tileColour,int levelColour) {
		super(id,false, false, levelColour);
		//32 tiles width
		this.tileId = x +y * 32;
		this.tileColour = tileColour;
	}
	
	public void tick(){
	}

	public void render(Screen screen, Level level, int x, int y) {
		screen.render(x, y, tileId, tileColour,0x00, 1);
	}
}
