package den.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//every time a key is pressed the class is called to execute specific keys
public class InputHandler implements KeyListener{

	public InputHandler(Game game){
		//add to the JFrame
		game.addKeyListener(this);
	}
	
	public class Key {
		//increment every time a buttons pressed to record how many times it happens, for stuff like double jumping
		private int numTimesPressed = 0;
		private boolean pressed = false;
		
		public int getNumTimesPressed(){
			return numTimesPressed;
		}
		
		public boolean isPressed(){
			return pressed;
		}
		
		public void toggle(boolean isPressed){
			pressed = isPressed;
			if(isPressed)numTimesPressed++;
		}
	}
	
	public enum InputEventType {
		PRESSED, RELEASED;
	}

	public class InputEvent {
		public InputEventType type;
		public Key key;

		public InputEvent(Key key, InputEventType type) {
			this.key = key;
			this.type = type;
		}
	}

	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();
	
	public void keyPressed(KeyEvent e) {
		toggleKey(e.getKeyCode(), true);
	}

	public void keyReleased(KeyEvent e) {
		toggleKey(e.getKeyCode(), false);
	}

	public void keyTyped(KeyEvent e) {
	}

	//verifies that the keys are equal and toggles them as pressed or not
	public void toggleKey(int keyCode, boolean isPressed){
		if(keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP){
			up.toggle(isPressed);
		}
		if(keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN){
			down.toggle(isPressed);
		}
		if(keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT){
			left.toggle(isPressed);
		}
		if(keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT){
			right.toggle(isPressed);
		}
	}
	public interface GameActionListener {
		public abstract void actionPerformed(InputEvent event);
	}
}