package den.game.net.packets;

import den.game.net.GameClient;
import den.game.net.GameServer;

public abstract class Packet {

	public static enum PacketTypes {
		//DIFFERENT PACKET TYPES THAT CAN BE SENT
		INVALID(-1), LOGIN(00), DISCONNECT(01), MOVE(02);
		
		private int packetId;
		private PacketTypes(int packetId){
			this.packetId = packetId;
		}
		public int getId(){
			return packetId;
		}
	}
	
	public byte packetId;
	
	public  Packet(int packetId){
		this.packetId =  (byte)packetId;
	}
	//if its sent from client its sent to the one server
	public abstract void writeData(GameClient client);
	//if its sent from server its sent  to all the clients
	public abstract void writeData(GameServer server);
	
	public String readData(byte[] data){
		//strip out the id send the rest
		String message = new String(data).trim();
		return message.substring(2);		
	}
	
	//byte array were sending back and forth from the client
	public abstract byte[] getData();
	
	//verify the packet is a valid packet
	public static PacketTypes lookupPacket(String packetId){
		try{
			return lookupPacket(Integer.parseInt(packetId));
		}catch(NumberFormatException e){
			return PacketTypes.INVALID;
		}
	}
	
	//getting the type of packets- send in int id of packet and finds out what packet were pertaining to- and return the id
	public static PacketTypes lookupPacket(int id){
		//loop through invalid login and disconnect
		for (PacketTypes p : PacketTypes.values()){
			if(p.getId() == id){
				return p;
			}
		}
		
		return PacketTypes.INVALID;
	}
}
