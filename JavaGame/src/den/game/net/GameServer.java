package den.game.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import den.game.Game;
import den.game.entities.PlayerMP;
import den.game.net.packets.Packet;
import den.game.net.packets.Packet.PacketTypes;
import den.game.net.packets.Packet00Login;
import den.game.net.packets.Packet01Disconnect;
import den.game.net.packets.Packet02Move;

public class GameServer extends Thread {

    private DatagramSocket socket;
    private Game game;
	//list of all connected players
    private List<PlayerMP> connectedPlayers = new ArrayList<PlayerMP>();

    public GameServer(Game game) {
        this.game = game;
        try {
			//socket listens on this port
            this.socket = new DatagramSocket(1331);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
    public void run() {
        while (true) {
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
            this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
        }
    }
	//take all the different packets we could have find out which one it is and execute it
    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
		//every packet between 0 and 2 is going to be looked up
        PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
        Packet packet = null;
        switch (type) {
        default:
        case INVALID:
            break;
        case LOGIN:
            packet = new Packet00Login(data);
	    	//tell the person they've connected
            System.out.println("[" + address.getHostAddress() + ":" + port + "] "
                    + ((Packet00Login) packet).getUsername() + " has connected...");
            PlayerMP player = new PlayerMP(game.level, 100, 100, ((Packet00Login) packet).getUsername(), address, port);
            this.addConnection(player, (Packet00Login) packet);
            break;
        case DISCONNECT:
        	 packet = new Packet01Disconnect(data);
 	    	//tell the person they've connected
             System.out.println("[" + address.getHostAddress() + ":" + port + "] "
                     + ((Packet01Disconnect) packet).getUsername() + " has disconnected...");
             this.removeConnection( (Packet01Disconnect) packet);
            break;
        case MOVE:
        	packet = new Packet02Move(data);
        	System.out.println(((Packet02Move)packet).getUsername() + " has moved to " + ((Packet02Move)packet).getX()+","+ ((Packet02Move)packet).getY());
        	this.handleMove(((Packet02Move)packet));
        	break;
        }
    }

	public void addConnection(PlayerMP player, Packet00Login packet) {
		//verify connection doesn't already exist
        boolean alreadyConnected = false;
		//loop through all connected players and check connection
        for (PlayerMP p : this.connectedPlayers) {
            if (player.getUsername().equalsIgnoreCase(p.getUsername())) {
				//if we don't know the address and we don't know the port update
                if (p.ipAddress == null) {
                    p.ipAddress = player.ipAddress;
                }
                if (p.port == -1) {
                    p.port = player.port;
                }
                alreadyConnected = true;
            } else {
                // relay to the current connected player that there is a new player
                sendData(packet.getData(), p.ipAddress, p.port);

                // relay to the new player that the currently connected player exists
                packet = new Packet00Login(p.getUsername(),p.x,p.y);
                sendData(packet.getData(), player.ipAddress, player.port);
            }
        }
		//if your not already connected add player
        if (!alreadyConnected) {
            this.connectedPlayers.add(player);
        }
    }

    public void removeConnection(Packet01Disconnect packet) {
    	//remove index of disconnected player
    	this.connectedPlayers.remove(getPlayerMPindex(packet.getUsername()));
    	//send data to all other clients that the player has disconnected
    	packet.writeData(this);
	}
    
    public PlayerMP getPlayerMP(String username){
    	//go through all the players in the connected players array
    	for(PlayerMP player : this.connectedPlayers){
    		if(player.getUsername().equals(username)){
    			return player;
    		}
    	}
    	return null;
    }
    
    //loop through all players when it finds disconnected player return index number
    public int getPlayerMPindex(String username){
    	int index = 0;
    	//go through all the players in the connected players array
    	for(PlayerMP player : this.connectedPlayers){
    		if(player.getUsername().equals(username)){
              break;    		
              }
    		index++;
    	}
    	return index;
    }
	
	//send data// using port  1331
    public void sendData(byte[] data, InetAddress ipAddress, int port) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        try {
            this.socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
  //loop through all connected players
    public void sendDataToAllClients(byte[] data) {
        for (PlayerMP p : connectedPlayers) {
			//send data to all the different clients
            sendData(data, p.ipAddress, p.port);
        }
    }
    private void handleMove(Packet02Move packet){
    	//see if player exists
    	if(getPlayerMP(packet.getUsername())!= null){
    		//get the players index
    		int index = getPlayerMPindex(packet.getUsername());
    		PlayerMP player = this.connectedPlayers.get(index);
    		//update the array
    		player.x = packet.getX();
    		player.y = packet.getY();
    		player.setMoving(packet.isMoving());
    		player.setMovingDir(packet.getMovingDir());
    		player.setNumSteps(packet.getNumSteps());
    		//send it to the client
    		packet.writeData(this);

    	}
    }
}
