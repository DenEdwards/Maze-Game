package den.game.net.packets;


import den.game.net.GameClient;
import den.game.net.GameServer;


public class Packet02Move extends Packet{

		private String username;
		private int x,y;
		//number of steps the mobs taken
		private int numSteps = 0;
		//if mobs are moving
		private boolean isMoving;
		//mob direction 0 is up 1 is down 3 left 4 right
		private int movingDir = 1;
		
		//when were receiving the data
		public Packet02Move(byte[] data) {
			super(02);
			String[] dataArray = readData(data).split(",");
			this.username = dataArray[0];
			this.x = Integer.parseInt(dataArray[1]);
			this.y = Integer.parseInt(dataArray[2]);
			this.numSteps = Integer.parseInt(dataArray[3]);
			//1 true anything else false
			this.isMoving = Integer.parseInt(dataArray[4]) == 1;
			this.movingDir= Integer.parseInt(dataArray[5]);
		}
		
		//when were sending it from the client
		public Packet02Move(String username, int x,int y, int numSteps, boolean isMoving, int movingDir){
			super(02);
			this.username = username;
			this.x = x;
			this.y = y;
			this.numSteps =numSteps;
			this.isMoving = isMoving ;
			this.movingDir = movingDir;
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
		public byte[] getData() {                                                                 //form mini if statement
			return ("02" + this.username + "," + this.x +"," + this.y + ","+ this.numSteps + ","+ (isMoving?1:0)+","+ this.movingDir).getBytes();
		}

		public String getUsername(){
			return username;
		}
		public int getX(){
			return this.x;
		}
		public int getY(){
			return this.y;
		}

		public int getNumSteps() {
			return numSteps;
		}

		public boolean isMoving() {
			return isMoving;
		}

		public int getMovingDir() {
			return movingDir;
		}
}
		