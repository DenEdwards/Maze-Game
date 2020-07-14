package den.game.entities;

import den.game.gfx.Screen;
import den.game.level.Level;

//abstract class-blueprint- can base other classes off this class 
public abstract class Entity {
	public int x,y;
	
	//only subclass can see it
	protected Level level;
	
	public Entity(Level level){
		//this will keep running once this class is called once
		init(level);
	}
	
	public final void init(Level level){
		this.level = level;
	}
	
	public abstract void tick();

	public abstract void render(Screen screen);
}