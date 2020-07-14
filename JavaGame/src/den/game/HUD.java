package den.game;

import java.awt.Color;
import java.awt.Graphics;

public class HUD {

	protected float MaxHealthX;
	public static double HEALTH = 200;
	public static double greenValue = 254;
	
	public void tick(){
		HEALTH = Game.health(HEALTH, 0, 100);
		greenValue = Game.health(greenValue, 0, 255);
		
		greenValue = HEALTH*2;
	}
	
	public void render (Graphics g){
		g.setColor(Color.gray);
		g.fillRect(15,15, 400, 32);
		
		g.setColor(new Color(75,(int)greenValue, 0));
		g.fillRect(15,15, (int)HEALTH*2, 32);
		
		g.setColor(Color.white);
		g.drawRect(15, 15, 400, 32);
		
		if(greenValue < 55){
			System.out.println("YOU LOST");
			System.exit(0);		
			}else{
			
		}
	}
	
	
		
	}

