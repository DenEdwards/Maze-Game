package den.game.entities;

import den.game.level.Level;
import den.game.level.tiles.Tile;

public abstract class Mob extends Entity{
	
	//mob name
	protected String name;
	//mob speed
	protected int speed;
	//number of steps the mobs taken
	protected int numSteps = 0;
	//if mobs are moving
	protected boolean isMoving;
	//mob direction 0 is up 1 is down 3 left 4 right
	protected int movingDir = 1;
	protected int scale = 1;
	
	public Mob(Level level, String name, int x, int y, int speed) {
		//super calls the blueprint class(Entity)
	    super(level);
	    this.name = name;
	    this.x = x;
	    this.y = y;
	    this.speed = speed;	    
   }

    /*how ever many were moving in a certain direction 
     * xa= 1 moving right 1     xa = -1 moving 1 left   xa = 0 not moving in the x
     * ya = 1 moving 1 down     ya = -1 moving 1 up     ya = 0 not moving in the y
     */
    public void move(int xa, int ya){
	
    	if(xa != 0 && ya != 0){
    		//Can't move diagonal make them move direction at a time
    		move(xa,0);
    		move(0,ya);
    		//make it so it counts as only one move not two
    		numSteps--;
    		return;
    	}
    	//taken a step
    	numSteps++;
    	//collision detection if you hit a block
    	if (!hasCollided(xa, ya)) {
			if (ya < 0)
				movingDir = 0;
			if (ya > 0)
				movingDir = 1;
			if (xa < 0)
				movingDir = 2;
			if (xa > 0)
				movingDir = 3;
			x += xa * speed;
			y += ya * speed;
		}
    }
    public abstract boolean hasCollided(int xa, int ya);
    
	//to see if tile is solid / compares current tile your standing on and next tile
    protected boolean isSolidTile(int xa, int ya, int x, int y){
    	if(level == null) {return false;}
    	//current tile
    	Tile lastTile = level.getTile((this.x + x) >> 3, (this.y + y) >> 3);
    	//next tile
    	Tile newTile = level.getTile((this.x + x + xa) >> 3,(this.y + y + ya) >> 3);
    	//verify tiles aren't the same and the next one is solid
    	if (!lastTile.equals(newTile) && newTile.isSolid())
			return true;
		return false;
	}
    
    public String getName(){
    	return name;
    }

	public int getNumSteps() {
		return numSteps;
	}

	public boolean isMoving() {
		return isMoving;
	}
	
	public int getMovingDir() {
		return movingDir;
	}

	public void setNumSteps(int numSteps) {
		this.numSteps = numSteps;
	}

	public void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}

	public void setMovingDir(int movingDir) {
		this.movingDir = movingDir;
	}
}
