
package den.game.level;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;


import den.game.entities.Entity;
import den.game.entities.PlayerMP;
import den.game.entities.PlayerMP;
import den.game.gfx.Screen;
import den.game.level.tiles.Tile;

@SuppressWarnings("unused")
public class Level {

	//array of IDs for what tiles reside in a specific place
	private byte[] tiles;
	public int width;
	public int height;
	//array of entities /mobs
	private List<Entity> entities = new ArrayList<Entity>();
	private String imagePath;
	private BufferedImage image;

	public Level(String imagePath) {
		if (imagePath != null) {
			this.imagePath = imagePath;
			this.loadLevelFromFile();
		} else {
			//if we send in nothing it generates default
			this.width = 64;
			this.height = 64;
			//create a new array of the board size 
			tiles = new byte[width * height];
			this.generateLevel();
		}
	}

	//load image from file
	private void loadLevelFromFile() {
		try {
			this.image = ImageIO.read(Level.class.getResource(this.imagePath));
			this.width = image.getWidth();
			this.height = image.getHeight();
			tiles = new byte[width * height];
			this.loadTiles();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//function to load tiles from image
	private void loadTiles() {
		//translate image data into an int, colours of the tile included
		int[] tileColours = this.image.getRGB(0, 0, width, height, null, 0,
				width);
		//translate data into actual colours
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				//loop through all possible tiles// for every tile in tile array 
    			//Label INFO
				tileCheck: for (Tile t : Tile.tiles) {
					if (t != null
							&& t.getLevelColour() == tileColours[x + y * width]) {
						this.tiles[x + y * width] = t.getId();
						break tileCheck;
					}
				}
			}
		}
	}
	
	//saving picture file feature
	private void saveLevelToFile() {
		try {
			//path to the file
			ImageIO.write(image, "png",
					new File(Level.class.getResource(this.imagePath).getFile()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	 //alter and set image
	public void alterTile(int x, int y, Tile newTile) {
		//update it locally within the level
		this.tiles[x + y * width] = newTile.getId();
		image.setRGB(x, y, newTile.getLevelColour());
	}

	public void generateLevel() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (x * y % 10 < 7) {
					//to get the coordinates were at PUT GRASS
					tiles[x + y * width] = Tile.GRASS.getId();
				} else {
					//otherwise put stone
					tiles[x + y * width] = Tile.STONE.getId();
				}
			}
		}
	}
	//make it so it either updates or adds getEntities() not both at the same time
    public synchronized List<Entity>getEntities(){
    	return this.entities;
    }
	
	public void tick() {		
		//render entity
		for (Entity e : getEntities()) {
			e.tick();
		}
		
        // loop tile
		for (Tile t : Tile.tiles) {
			if (t == null) {
				break;
			}
			t.tick();
		}

	}

	//this what the cameras view would be
	public void renderTiles(Screen screen, int xOffset, int yOffset) {
		//width << 3 - screen.width = width of the total board
		if (xOffset < 0)
			xOffset = 0;
		if (xOffset > ((width << 3) - screen.width))
			xOffset = ((width << 3) - screen.width);
		if (yOffset < 0)
			yOffset = 0;
		//height << 3 - screen.height = width of the total board
		if (yOffset > ((height << 3) - screen.height))
			yOffset = ((height << 3) - screen.height);

		screen.setOffset(xOffset, yOffset);

		//rendering tile
		for (int y = (yOffset >> 3); y < (yOffset + screen.height >> 3) + 1; y++) {
			for (int x = (xOffset >> 3); x < (xOffset + screen.width >> 3) + 1; x++) {
				getTile(x, y).render(screen, this, x << 3, y << 3);
			}
		}
	}

	//render getEntities() above the tiles
	public void renderEntities(Screen screen) {
		//render entity
		for (Entity e : getEntities()) {
			e.render(screen);
		}
	}

	public Tile getTile(int x, int y) {
		//if tile is outside the bounds of the board return a void tile
		if (0 > x || x >= width || 0 > y || y >= height)
			return Tile.VOID;
		return Tile.tiles[tiles[x + y * width]];
	}

	public void addEntity(Entity entity) {
		//puts entity in
		this.getEntities().add(entity);
	}

	public void removePlayerMp(String username) {
        int index = 0;
		//loop through all getEntities() in our level
		for(Entity e : getEntities()){
			//if its a multi-player player loop through the index till you find the player
			if(e instanceof PlayerMP && ((PlayerMP)e).getUsername().equals(username)){
				break;
			}
			index++;
		}
		//remove them
		this.getEntities().remove(index);
	}
	//update getEntities() if we have received it in the client
	private int getPlayerMPindex(String username){
		int index = 0;
		for(Entity e : getEntities()){
			//if its the same player
			if(e instanceof PlayerMP && ((PlayerMP)e).getUsername().equals(username)){
				break;
			}
			index++;
		}
		return index;
	}
	//move player
	public void movePlayer(String username, int x, int y, int numSteps, boolean isMoving, int movingDir){
		int index = getPlayerMPindex(username);
		PlayerMP player = (PlayerMP) this.getEntities().get(index);
		//update player to these coordinates
		player.x = x;
		player.y = y;
		player.setMoving(isMoving);
		player.setMovingDir(movingDir);
		player.setNumSteps(numSteps);
	}
			
}