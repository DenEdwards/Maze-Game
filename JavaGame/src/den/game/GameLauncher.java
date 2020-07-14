package den.game;

import java.applet.Applet;
import java.awt.BorderLayout;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class GameLauncher extends Applet{
	
	private static Game game = new Game();
	
	public static final boolean DEBUG = false;
	
	@Override
	public void init(){
		setLayout(new BorderLayout());
		add(game,BorderLayout.CENTER);
		setMaximumSize(Game.DIMENSIONS);
		setMinimumSize(Game.DIMENSIONS);
		setPreferredSize(Game.DIMENSIONS);
		game.debug = DEBUG;
	}
	
	@Override
	public void start(){
		game.start();
	}
	
	@Override
	public void stop(){
		game.stop();
	}
	
	public static void main(String[]args0){
		
		//set the size of your frame and keep your frame at one size
				game.setMinimumSize(Game.DIMENSIONS);
				game.setMaximumSize(Game.DIMENSIONS);
				game.setPreferredSize(Game.DIMENSIONS);
								
				game.frame = new JFrame(Game.NAME);
				
				//exit  system  when you push the exit button 
				game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				//set the layout, so frame is centered
				game.frame.setLayout(new BorderLayout());
				//add game to the frame
				game.frame.add(game,BorderLayout.CENTER);
				//set the frame so the sizes are above or at preferred size
				game.frame.pack();
				//so its not resizable
				game.frame.setResizable(false);
				game.frame.setLocationRelativeTo(null);
				game.frame.setVisible(true);
				game.windowHandler = new WindowHandler(game);
				game.debug = DEBUG;
	
		game.start();
		}
}
