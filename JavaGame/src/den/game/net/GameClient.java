package den.game.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import den.game.Game;
import den.game.entities.PlayerMP;
import den.game.net.packets.Packet;
import den.game.net.packets.Packet00Login;
import den.game.net.packets.Packet01Disconnect;
import den.game.net.packets.Packet02Move;
import den.game.net.packets.Packet.PacketTypes;

public class GameClient extends Thread {

	//ip address of the server were connecting to
	private InetAddress ipAddress;
	private DatagramSocket socket;
	private Game game;
	
	public GameClient(Game game, String ipAddress){
		this.game = game;
		try {
			this.socket = new DatagramSocket();
		     this.ipAddress = InetAddress.getByName(ipAddress);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		while(true){
			//array of bytes of data sent to the server
			byte[] data = new byte[1024];
			//packet sent to and from the server
			DatagramPacket packet = new DatagramPacket(data, data.length);
			//accept, receive the data 
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.parsePacket(packet.getData(),packet.getAddress(), packet.getPort());
			//print out array of the bytes and turn them into a string
			//String message = new String(packet.getData());
			//System.out.println("SERVER > " + message);
		}
	}

	//take all the different packets we could have find out which one it is and execute it
	private void parsePacket(byte[] data, InetAddress address, int port) {
		String message = new String(data).trim();
		//every packet between 0 and 2 is going to be looked up
		PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
		Packet packet  = null;
	    switch(type){
	    default:
	    case INVALID:
	    	break;
	    case LOGIN:
	    	 packet = new Packet00Login(data);
	    	 handleLogin((Packet00Login)packet, address, port);
	    	break;
	    case DISCONNECT:
	    	packet = new Packet01Disconnect(data);
 	    	//tell the person they've connected
             System.out.println("[" + address.getHostAddress() + ":" + port + "] "
                     + ((Packet01Disconnect) packet).getUsername() + " has left the world...");
             //remove them from the level
             game.level.removePlayerMp(((Packet01Disconnect) packet).getUsername());
	    	break;
	    case MOVE:
        	packet = new Packet02Move(data);
        	handleMove((Packet02Move)packet);
        	break;
	    }
	}
	
	//send data// using port  1331- socket listens on this port
	public void sendData(byte[] data){
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void handleLogin(Packet00Login packet, InetAddress address,int port){
    	//tell the person they've logged in
    	System.out.println("[" + address.getHostAddress() + ":"+ port +"] " + packet.getUsername() + " Has joined the game...");
    	//move player to coordinates and log them in
    	PlayerMP player =  new PlayerMP(game.level, packet.getX(), packet.getY(), packet.getUsername(), address, port);
    	game.level.addEntity(player);
	}
	
	private void handleMove(Packet02Move packet){
		//update the player to see him move
		this.game.level.movePlayer(packet.getUsername(),packet.getX(), packet.getY(), packet.getNumSteps(),packet.isMoving(),packet.getMovingDir());	
	}
}
