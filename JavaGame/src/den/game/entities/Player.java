package den.game.entities;

import den.game.Game;
import den.game.InputHandler;
import den.game.gfx.Colours;
import den.game.gfx.Font;
import den.game.gfx.Screen;
import den.game.level.Level;
import den.game.net.packets.Packet02Move;
import den.game.HUD;

public class Player extends Mob{

	private InputHandler input;
	private int colour = Colours.get(-1, 111, 150, 267);
	private int scale = 1;
	//player swimming
	protected boolean isSwimming = false;
	protected boolean isHurt = false;

	//how many ticks gave gone by since the game updated
	private int tickCount = 0;
	//username//pop up box
	private String username;

	public Player(Level level,int x, int y, InputHandler input, String username) {
		super(level, "Player", x, y, 1);
		this.input = input;
		this.username = username;
	}

	public void tick() {

		int xa = 0;
		int ya =0;
		if(input != null){
			if(input.up.isPressed()){ya--;}
			if(input.down.isPressed()){ya++;}
			if(input.left.isPressed()){xa--;}
			if(input.right.isPressed()){xa++;}	
		}

		//if were moving in some direction move
		if(xa != 0 || ya != 0){
			move(xa,ya); 
			isMoving = true;

			//send player data to the server
			Packet02Move packet = new Packet02Move(this.getUsername(), this.x,this.y,this.numSteps,this.isMoving,this.movingDir);
			packet.writeData(Game.game.socketClient);

		}else{
			isMoving = false;
		}

		//to see if the player is in the water to swim
		if(level.getTile(this.x >> 3,this.y >> 3).getId() == 3){
			isSwimming = true;
		}
		//out of water don't swim
		if(isSwimming && level.getTile(this.x >> 3,  this.y >> 3).getId() != 3){
			isSwimming = false;
		}
		//to see if the player is hurt// lava traps
		if(level.getTile(this.x >> 3,this.y >> 3).getId() == 6 || level.getTile(this.x >> 3,this.y >> 3).getId() == 4){
			isHurt = true;
		}
		else{
			//out of trap don't get hurt
			isHurt = false;
		}
		if(this.x == 712 && this.y == 424 || this.x == 712 && this.y == 425){
			System.out.println("YOU WON");
			System.out.println("YOU WON");
			System.out.println("YOU WON");
			System.out.println("YOU WON");
			System.out.println("YOU WON");
			System.exit(0);
		}

		tickCount++;	
	}

	public void render(Screen screen) {
		//player on sprite sheet
		int xTile = 0;
		int yTile = 28;
		//display walking speed
		int walkingSpeed = 4;
		int flipTop = (numSteps  >> walkingSpeed) & 1;
		int flipBottom = (numSteps  >> walkingSpeed) & 1;

		//Distinguish direction he's moving a show accordingly
		if(movingDir == 1){
			//go to second tile
			xTile += 2;
		}else if (movingDir > 1){
			//where the player is in the animation
			xTile += 4 + ((numSteps >> walkingSpeed) & 1) * 2;
			flipTop = (movingDir - 1 )% 2;
		}

		//size of the player
		int modifier = 8* scale;
		int xOffset = x - modifier / 2;
		int yOffset = y - modifier / 2-4;
		//render in water distortion around player
		if(isSwimming){
			int waterColour = 0;
			yOffset += 4;
			//water colour based on where you moved around in water based on how many ticks have gone by
			if(tickCount % 60 < 15){
				waterColour = Colours.get(-1, -1, 225, -1);
			}else if (15 <= tickCount % 60 && tickCount % 60 < 30){
				//bob in the water
				yOffset  -=1;
				waterColour = Colours.get(-1, 225, 115, -1);
			}else if (30 <= tickCount % 60 && tickCount % 60 < 45){
				waterColour = Colours.get(-1, 115, -1,225);
			}else{
				//bob in the water
				yOffset  -=1;
				waterColour = Colours.get(-1, 225, 115, -1);
			}
			//the water distortion and mirroring the water distortion on the sprite sheet
			screen.render(xOffset, yOffset + 3, 0 + 27 * 32, waterColour, 0x00, 1);
			screen.render(xOffset + 8, yOffset + 3, 0 + 27 * 32, waterColour, 0x01, 1);
		}

		if(isHurt == true){
			HUD.HEALTH = HUD.HEALTH - 0.2 ;
			HUD.greenValue = HUD.greenValue  - 0.2 ;
		}

		// first to fourth tile on sprite sheet character
		//upper body
		screen.render(xOffset + (modifier * flipTop), yOffset, xTile  + yTile * 32, colour,flipTop, scale);
		//upper body
		screen.render(xOffset + modifier - (modifier * flipTop ), yOffset, (xTile + 1) + yTile * 32, colour,flipTop,scale);

		//if were swimming bottom half of body disappears
		if(!isSwimming){
			//bottom body
			screen.render(xOffset + (modifier * flipBottom), yOffset + modifier, xTile +(yTile + 1) * 32, colour,flipBottom, scale);
			//bottom body
			screen.render(xOffset + modifier  - (modifier * flipBottom ),  yOffset + modifier , (xTile + 1) + (yTile + 1) * 32, colour,flipBottom,scale);
		}
		if(username != null){
			Font.render(username, screen, xOffset - ((username.length() -1) / 2 * 8), yOffset -10, Colours.get(-1, -1, -1, 555), 1);
		}
	}

	//so you can't go off the screen or through blocks
	public boolean hasCollided(int xa, int ya) {
		//collision box parameters
		int xMin = 0;
		int xMax = 7;
		int yMin = 3;
		int yMax = 7;

		//top side xMin -> xMax
		for(int x = xMin; x < xMax; x++){
			if(isSolidTile(xa,ya,x, yMin)){
				return true;
			}
		}

		//bottom side xMin -> xMax
		for(int x = xMin; x < xMax; x++){
			if(isSolidTile(xa,ya,x, yMax)){
				return true;
			}
		}
		//left side yMin -> yMax
		for(int y = yMin; y < yMax; y++){
			if(isSolidTile(xa,ya,xMin, y)){
				return true;
			}
		}
		//right side yMin -> yMax
		for(int y = yMin; y < yMax; y++){
			if(isSolidTile(xa,ya,xMax, y)){
				return true;
			}
		}
		return false;
	}

	public String getUsername(){
		return this.username;
	}
}
