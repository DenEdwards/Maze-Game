package den.game;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import den.game.net.packets.Packet01Disconnect;

public class WindowHandler implements WindowListener{

	private final Game game;
	//set game in WindowHandler
	public WindowHandler(Game game){
		this.game = game;
		this.game.frame.addWindowListener(this);
	}
	
	@Override
	//sets window to active widow
	public void windowActivated(WindowEvent event) {	
	}

	@Override
	//closes window
	public void windowClosed(WindowEvent event) {
	}

	@Override
	//starts close window
	public void windowClosing(WindowEvent event) {
		Packet01Disconnect packet = new Packet01Disconnect(this.game.player.getUsername());
		//send disconnect to server
		packet.writeData(this.game.socketClient);
	}

	@Override
	//puts windows behind ohter clicked on windows
	public void windowDeactivated(WindowEvent event) {
	}

	@Override
	//window maximized
	public void windowDeiconified(WindowEvent event) {	
	}

	@Override
	//window minimized
	public void windowIconified(WindowEvent event) {	
	}

	@Override
	//window made visible
	public void windowOpened(WindowEvent event) {	
	}
}
