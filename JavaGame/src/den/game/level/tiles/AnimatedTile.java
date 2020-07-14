package den.game.level.tiles;

import den.game.gfx.Screen;
import den.game.level.Level;

//"moving" tiles
@SuppressWarnings("unused")
public class AnimatedTile extends BasicTile {

	private int[][] animationTileCoords;
	//which number of the first array were in
	public int currentAnimationIndex;
	//milliseconds that we last updated
	private long lastIterationTime;
	//delay in the animation switching
	private int animationSwitchDelay;

	public AnimatedTile(int id, int[][] animationCoords, int tileColour,int levelColour, int animationSwitchDelay) {
		//2D array[x-coordinate][y-coordinate] INFO
		super(id, animationCoords[0][0], animationCoords[0][1], tileColour,levelColour);
		this.animationTileCoords = animationCoords;
		this.currentAnimationIndex = 0;
		this.lastIterationTime = System.currentTimeMillis();
		this.animationSwitchDelay = animationSwitchDelay;
	}

	public void tick() {
		//current time were currently at - the last update time
		if ((System.currentTimeMillis() - lastIterationTime) >= (animationSwitchDelay)) {
			//update everything
			lastIterationTime = System.currentTimeMillis();
			//move to which number in the array we are till we get to the third water tile and then loop back to the first one
			currentAnimationIndex = (currentAnimationIndex + 1) % animationTileCoords.length;
			//update the tileId
			this.tileId = (animationTileCoords[currentAnimationIndex][0] + (animationTileCoords[currentAnimationIndex][1] * 32));
		}
	}	
}