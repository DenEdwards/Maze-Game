package den.game.level.tiles;

	import den.game.gfx.Colours;
	import den.game.gfx.Screen;
	import den.game.level.Level;

	//abstract- a tile class blueprint to access from different sub classes
	public abstract class Tile {
		
		//array of maximum amount of tiles we can possibly have
		public static  Tile[] tiles = new Tile[256];
		public static  Tile VOID = new BasicSolidTile(0, 0, 0, Colours.get(000, -1, -1, -1), 0xff000000);
		public static  Tile STONE = new BasicSolidTile(1, 1, 0, Colours.get(-1, 333, 000, -1), 0xff555555);
		public static  Tile GRASS = new BasicTile(2, 2, 0, Colours.get(-1,131, 141, -1), 0xff00ff00);
		public static  Tile WATER = new AnimatedTile(3, new int[][]{{0,5}, {1,5}, {2,5},{1,5}}, Colours.get(-1, 004, 115, -1), 0xff0000ff, 500);
		public static  Tile LAVA = new AnimatedTile(4, new int[][]{{0,6}, {1,6}, {2,6},{1,6}}, Colours.get(-1, 210, 000,-1), 0xffff0000, 500);
		
		public static  Tile FIRE = new AnimatedSolidTile(5, new int[][]{{0,7}, {1,7}, {2,7},{1,7}}, Colours.get(-1,200, 210,333), 0xffffFF00, 500);
		public static  Tile TRAP = new AnimatedTile(6, new int[][]{{0,8}, {1,8}, {2,8},{1,8}}, Colours.get(141, 131,170,200), 0xffff0ff0, 1000);
		public static  Tile CHEST = new AnimatedTile(7, new int[][]{{0,9}, {1,9}, {2,9},{1,9}}, Colours.get(-1,333, 170,000), 0xffF0F0F0, 1000);
		public static  Tile CHEST2 = new AnimatedTile(8, new int[][]{{0,10}, {1,10}, {2,10},{1,10}}, Colours.get(-1,333,170,131), 0xff00000f, 1000);

		
		//Protected- any subclass of this class can see these values  coordinates of middle 712,425 0xfff0f0f0
		
		//location in the array of where the title is located
		protected byte id;
		//collision detection example: can't pass through walls
		protected boolean solid;
		//light
		protected boolean emitter;
		private int levelColour;
		
		
		
		/**
		 * @param id
		 * @param isSolid
		 * @param isEmitter
		 * @param levelColour
		 */
		public Tile(int id, boolean isSolid, boolean isEmitter, int levelColour) {
			this.id = (byte) id;
			//if there's already a tile that has this id
			if (tiles[id] != null)
				throw new RuntimeException("Duplicate the id on" + id);
			this.solid = isSolid;
			this.emitter = isEmitter;
			this.levelColour = levelColour;
			tiles[id] = this;
		}

		
		
		public byte getId(){
		 return id;
		}
		
		public boolean isSolid(){
			return solid;
		}
		
		public boolean isEmitter(){
			return emitter;
		}
		
		public int getLevelColour(){
			return levelColour;
		}
		
		public abstract void tick();
		
		public abstract void render(Screen screen, Level level, int x, int y);
	}


