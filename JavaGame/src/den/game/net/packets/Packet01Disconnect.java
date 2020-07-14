package den.game.net.packets;

import den.game.net.GameClient;
import den.game.net.GameServer;

public class Packet01Disconnect extends Packet{
	
		private String username;
		
		//when were receiving the data
		public Packet01Disconnect(byte[] data) {
			super(01);
			this.username = readData(data);
		}
		
		//when were sending it from the client
		public Packet01Disconnect(String username) {
			super(01);
			this.username = username;
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
			return ("01" + this.username).getBytes();
		}

		public String getUsername(){
			return username;
		}
}


