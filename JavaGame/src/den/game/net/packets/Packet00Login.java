package den.game.net.packets;

import den.game.net.GameClient;
import den.game.net.GameServer;

//send in the username
public class Packet00Login extends Packet {

	private String username;
private int x,y;
	
	//when were receiving the data
	public Packet00Login(byte[] data) {
		super(00);
		//read the data put it in array
		String[] dataArray = readData(data).split(",");
		this.username = dataArray[0];
		this.x = Integer.parseInt(dataArray[1]);
		this.y = Integer.parseInt(dataArray[2]);

	}
	
	//when were sending it from the client
	public Packet00Login(String username, int x, int y) {
		super(00);
		this.username = username;
		//populate at these coordinates
		this.x = x;
		this.y = y;
	}
	
	
	@Override
	public void writeData(GameClient client) {
		client.sendData(getData());
	}
	@Override
	public void writeData(GameServer server) {
		server.sendDataToAllClients(getData());
	}

	@Override
	public byte[] getData() {
		return ("00" + this.username + "," +getX() + "," + getY()).getBytes();
	}

	public String getUsername(){
		return username;
	}
	
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
}
