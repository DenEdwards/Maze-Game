package den.game.entities;

import java.net.InetAddress;

import den.game.InputHandler;
import den.game.level.Level;

//Multi-player
public class PlayerMP extends Player{

	public InetAddress ipAddress;
	public int port;
	
	public PlayerMP(Level level, int x, int y, InputHandler input, String username, InetAddress ipAddress,  int port) {
		super(level, x, y, input, username);
		this.ipAddress = ipAddress;
		this.port = port;
	}
	
	//if the player is connecting locally, lets you do a local connection
	public PlayerMP(Level level, int x, int y, String username, InetAddress ipAddress,  int port) {
		super(level, x, y, null, username);
		this.ipAddress = ipAddress;
		this.port = port;
	}
	
     @Override
     //Establish and maintain the connection
     public void tick(){
    	super.tick(); 
     }
}
