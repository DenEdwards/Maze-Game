package den.game;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import den.game.entities.Player;
import den.game.entities.PlayerMP;
import den.game.gfx.Colours;
import den.game.gfx.Screen;
import den.game.gfx.SpriteSheet;
import den.game.level.Level;
import den.game.net.GameClient;
import den.game.net.GameServer;
import den.game.net.packets.Packet00Login;

public class Game extends Canvas implements Runnable{

	private static final long serialVersionUID = 1L;

	//represent game class to call from other classes like game launcher
	public static Game game;
	// a variable thats not going to change
	public static final int WIDTH = 160;
	public static final int HEIGHT = WIDTH/12 * 9;
	//make the game screen bigger
	public static final int SCALE = 6;
	public static final String NAME = "Game";
    public HUD hud;

    public static final Dimension DIMENSIONS = new Dimension(WIDTH*SCALE, HEIGHT*SCALE);
	
	public JFrame frame; 
	
	private Thread thread;
	public boolean running = false;
	//how many ticks there are
	public int tickCount= 0;
	
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	//pixels in image
	private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	//six different shades of the three different colours
	private int colours[] = new int[6*6*6];
	
	private Screen screen;
	public InputHandler input;
	public WindowHandler windowHandler;
	
	public Level level;
	public Player player;
	
	public GameClient socketClient;
	public GameServer socketServer;
	
	public boolean debug = true;
	
	//initializing the core methods and class in the game
	public void init(){
		game = this;
		int index = 0;
		//looping through shades of reds,greens and blues
		for(int r = 0; r < 6; r++){
			for(int g = 0; g < 6; g++){
				for(int b = 0; b < 6; b++){
					//actual red,green and blue colour 
					int rr = (r*255/5);
					int gg = (g*255/5);
					int bb = (b*255/5);
					
					colours[index++] = rr << 16 | gg << 8 | bb ;
				}
		    }
		}
		
		 hud = new HUD();
		//screen
		screen = new Screen(WIDTH,HEIGHT,new SpriteSheet("/sprite_sheet.png"));
		input = new InputHandler(this);
		level = new Level("/levels/maze_test_level.png");
		//adds player
		player = new PlayerMP(level,100,100, input, JOptionPane.showInputDialog(this, "Please enter a username"), null, -1);
		level.addEntity(player);
		Packet00Login loginPacket = new Packet00Login( player.getUsername(), player.x, player.y);
		//if were running the socket client sided
		if(socketServer != null){
			socketServer.addConnection((PlayerMP)player, loginPacket);
		}
		
		//will send to the server from client
		loginPacket.writeData(socketClient);
	}
	
	/*Synchronized so we can call it from our applet
	 * when the thread is run it will run run()
	 */
	public synchronized void start(){
		running = true;
		
		thread = new Thread(this, NAME + "_main");
		thread.start();		
		if(JOptionPane.showConfirmDialog(this,  "Do you want to run the server?")== 0){
			socketServer = new GameServer(this);
			socketServer.start();
		}
		
		socketClient = new GameClient(this, "localhost");
		socketClient.start();
		
	}
	
    public synchronized void stop(){
		running = false;
	
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
    ///main game loop
	public void run() {
		//gets the current time from the system
		long lastTime = System.nanoTime();
		//nano-seconds per tick
		double nsPerTick = 1000000000D/60;
		
		int ticks = 0;
		int frames =0;
		
		//time to reset the data, to keep updating the ticks and the frames
		long lastTimer = System.currentTimeMillis();
		//how many nanoseconds have gone by, one it hits one we minus one from it to reset
		double delta = 0;
		
		init();
		
		while(running){
			//current time to check against last time
			long now = System.nanoTime();
			delta += (now-lastTime) /nsPerTick;
			lastTime = now;
			boolean shouldRender = true;
			
			while(delta >= 1){
				ticks++;
			    tick();
				delta -= 1;
				//render every time ticks and delta are done
				shouldRender = true;
			}
			
			//so the system doesn't get overloaded
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(shouldRender){
				frames++;
				render();
			}
		    
		    //if the current time - last time we updated is more than one second than update
		    if(System.currentTimeMillis() - lastTimer >= 1000){
		    	lastTimer += 1000;
		    	//reset frames and ticks
			    debug(DebugLevel.INFO, frames + " frames" + ", "  + ticks + " ticks");
			    frames = 0;
			    ticks = 0;
		    }		    	    
		}
	}
	
	
	
	//update the logic of the game
	public void tick(){
		tickCount ++;
		level.tick();		
	}
	
	 
	//print out what the tick method says to print out
	public void render(){
		//display image when we render
		BufferStrategy bs = getBufferStrategy();
		if(bs == null){
		    //reducing tearing and cross image pixelation, clears the image and runs it again
			createBufferStrategy(3);
			return;
		}   
		
		//so the players in the center of the screen
		int xOffset = player.x - (screen.width/2);
		int yOffset = player.y - (screen.height/2);
		
		level.renderTiles(screen, xOffset, yOffset);
		
		for(int x = 0; x < level.width; x++){
			@SuppressWarnings("unused")
			int colour = Colours.get(-1,-1,-1,000);
			if(x % 10 == 0 && x!= 0){
				colour = Colours.get(-1,-1,-1,500);
			}
		}
		
		//render entities after fonts
		level.renderEntities(screen);
		
		
		for(int y = 0; y < screen.height; y++){
			for(int x = 0; x < screen.width; x++){
				int colourCode = screen.pixels[x +y * screen.width];
				if(colourCode < 255) pixels[x + y * WIDTH] = colours[colourCode];
			}
		}
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(),  getHeight(), null);
		hud.render(g);
		//free memory by disposing the graphics
		g.dispose();
		bs.show();
	}
	
	public static long fact(int n){
		if(n <= 1){
			return 1;
		}else{
			return n* fact(n-1);
		}
	}
	
	public void debug(DebugLevel level, String msg){
		//identify debug and execute accordingly
		switch(level){
		default: 
		case INFO:
			if(debug){
				System.out.println("[" + NAME + "]" + msg);
			}
			break;
		case WARNING:
			System.out.println("[" + NAME + "][WARNING] " + msg);
			break;
		case SEVERE:
			System.out.println("[" + NAME + "][SEVERE] " + msg);
			this.stop();
			break;
		}
	}
	
	
	public static enum DebugLevel{
		//INFO- generic debug info//WARNING- warning you have to deal with//SEVERE- harmful or critical to the program runnning, system will exit
		INFO,WARNING, SEVERE;
	}
	
	public static double health(double greenValue, float min, float max){
		if(greenValue >= max) return greenValue = max;
		else if(greenValue <= min) return greenValue = min;
		else return greenValue;
	}
}