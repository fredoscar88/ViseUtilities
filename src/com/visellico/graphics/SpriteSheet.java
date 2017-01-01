package com.visellico.graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

//in charge of any sprite sheets and caching to memory, a variable
public class SpriteSheet {

	private String path;	//path to this spritesheet
	public final int SIZE;	//sprite sheet is square, so only one dimension is needed. LOL now we have two dimensions, bitches
	public int sWidth, sHeight;	//I guess these are in sprite precision... oh no, it's the width and height of each SPRITE. This is why my method split() in Sprite messed up... (todo)
						//well nope. SPRITEWIDTH and SPRITEHEIGHT are definitely NOT sprite width, sprite height.
	public final int SPRITE_WIDTH = 16, SPRITE_HEIGHT = 16;
	public int width, height;	//w and h of the actual sprite sheet
	public int[] pixels;
	
//	//start off path WITH a forward slash
//	public static SpriteSheet tiles = new SpriteSheet("/textures/sheets/spritesheet.png",256);
//	public static SpriteSheet spawnLevelTiles = new SpriteSheet("/textures/sheets/spawnlevelTiles.png", 48);
//	public static SpriteSheet wizardProjectiles = new SpriteSheet("/textures/sheets/Projectiles/wizardprojectiles.png", 48);
//	
//	public static SpriteSheet player = new SpriteSheet("/textures/sheets/player_sheet.png", 128, 128);
//	public static SpriteSheet player_down = new SpriteSheet(player, 2 , 0, 1, 4, 32);
//	public static SpriteSheet player_up = new SpriteSheet(player, 0 , 0, 1, 4, 32);
//	public static SpriteSheet player_left = new SpriteSheet(player, 3 , 0, 1, 4, 32);
//	public static SpriteSheet player_right = new SpriteSheet(player, 1 , 0, 1, 4, 32);
//	
//	//Frankly this is the same as the player. But atleast we have the option to replace it, quite easily, in the future
//	public static SpriteSheet dummy = new SpriteSheet("/textures/sheets/MobDummy.png", 128, 128);
//	public static SpriteSheet dummy_down = new SpriteSheet(dummy, 2 , 0, 1, 4, 32);
//	public static SpriteSheet dummy_up = new SpriteSheet(dummy, 0 , 0, 1, 4, 32);
//	public static SpriteSheet dummy_left = new SpriteSheet(dummy, 3 , 0, 1, 4, 32);
//	public static SpriteSheet dummy_right = new SpriteSheet(dummy, 1 , 0, 1, 4, 32);		
	
	private Sprite[] sprites;
	
	//create sheet from another sheet (sub sheet)
	@Deprecated
	public SpriteSheet(SpriteSheet sheet, int x, int y, int width, int height, int spriteSize) {	//may not need spriteSize parameter
		
		//PUTS X, Y, WIDTH, HEIGHT in PIXEL PRECISION from what we're given, SPRITE PRECISION;
		int xx = x * spriteSize;
		int yy = y * spriteSize;
		int w = width * spriteSize;
		int h = height * spriteSize;
		
		if (w == h) SIZE = w;
		else SIZE = -1;
		
		this.sWidth = w;
		this.sHeight = h;
		
		pixels = new int[sWidth * sHeight];
//		System.out.println("h: " + h + ", ");
		
		for (int y0 = 0; y0 < h; y0++) {
			int yp = yy + y0;
			for (int x0 = 0; x0 < w; x0++) {
				int xp = xx + x0;
				
				//may be redundant if we also store the sprites in the sprites array
				pixels[x0 + y0 * w] = sheet.pixels[xp + yp * sheet.sWidth];
			}
		}
		
		//This can go... do a rude thing to itself. (TO-DO), git understood, but frankly I think I understand it better than TheCherno! I debugged it better (well quicker)!
		int frame = 0;
		sprites = new Sprite[width * height];
		for (int ya = 0; ya < height; ya++) {
			for (int xa = 0; xa < width; xa++) {				
				int[] spritePixels = new int[spriteSize * spriteSize];	//all sprites are square
				
				for ( int y0 = 0; y0 < spriteSize; y0++) {
					for (int x0 = 0; x0 < spriteSize; x0++) {
						spritePixels[x0 + y0 * spriteSize] = pixels[(x0 + xa * spriteSize) + (y0 + ya * spriteSize) * sWidth];	//(TO-DO) Take some time to understand.
						/*
						 * Here we are, for every sprite in a (sub) spritesheet, since this is the only constructor that supports what we're doing, (height,width in sprite precision),
						 * populate an array of pixels that will be used to create a new sprite. add new sprite to sprites[] array.
						 * Possibly want to include populating sprites array in other constructors.
						 * So spritePixels gets pixels from the sheet pixels, offset by what sprite-precise coords we have, multiplied by sprite size to move our selection
						 */
					}
				}
				Sprite sprite = new Sprite(spritePixels, spriteSize, spriteSize);
				sprites[frame++] = sprite;	//referencing frame like this, use it's current val then add 1 (for the next iteration)
			}
		}
		//wow what I have below is a more mathematical approach, as opposed to the above, practical approach, which does the logical thing, find the appropriate x, y in our old 
		//	sheet, feed to our new one, while this one uses i to determine those, using the given parameters.
		//(TO-DO I did test this, did not work out. :c
//		for (int i = 0; i < WIDTH * HEIGHT; i++) {	//NEVERMIND MY MORE COMPLICATED BUT ALSO, I ASSUME, CORRECT APPROACH
//			int newX = (i % w) + xx;	//Determines the x value, in the spritesheet we're generating from, of the i-th pixel in the new one.
//			int newY = (i / h) + yy;	//Determines the y value, in the spritesheet we're generating from, of the i-th pixel in the new one.
//			
//			pixels[i] = sheet.pixels[newX + newY * w]; //(TODO) I believe it had to do with this part, not calling the correct pixels from the given sheet.
//		}
		
	}
	
	@Deprecated
	public SpriteSheet(String path, int size) {
		this.path = path;
		this.SIZE = size;	//does this work?? apparently- it can only be initialized once, doesnt have to be at compile time, note that this is in the constructor
		this.sWidth = SIZE;
		this.sHeight = SIZE;
		
		pixels = new int[SIZE*SIZE];
		load();
	}
	
	@Deprecated
	public SpriteSheet(String path, int width, int height) {
		this.path = path;
		this.sWidth = width;
		this.sHeight = height;
		this.SIZE = -1;	//unused for nonsquare
		
		pixels = new int[width * height];
		load();
	}
	
	/**
	 * Currently the only supported constructor for this particular game. Likely, ViseUtilities as a whole needs to be redone
	 * in a separate project. What this does is loads a sprite sheet from a file that contains only one sprite, that takes up the whole sheet. yeah.
	 * Makes this whole class useless but whatever.
	 * Doesn't jive with my architecture, so TODO git fixed
	 * @param path Path to the sprite
	 * @throws IOException 
	 */
	public SpriteSheet(String path) throws IOException {
		SIZE = -1;
		this.path = path;
		
		loadNotPath();
	}
	
	public Sprite[] getSprites() {
		
		return sprites;
		
	}

	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	
	private void load() {
		
		try {
			System.out.print("DEBUG SpriteSheet.load() - Trying to load: " + path);
//			path = path.substring(1, path.length());
//			System.out.println("\n" + path);
											/*
											 * In my interpretation, class.getResource() is for loading a resource into a memory cache
											 * okay, great lost my train of thought.
											 * Anyway I believe that when you specify a class infront of class, then it uses that class's root folder. maybe. idk.
											 * a commenter on the youtubes mentioned it being jar-safe, so the path won't be wildly varied as it doesnt stem from C: or something, I guess
											 * NOTE: a quick google has shown the getResource does indeed work with a path relative to the code base, NOT file paths.
											 * Specifically, Im guessing, relative to SpriteSheet- or well, the whole project, I dont know why SpriteSheet was specifically invoked
											 */
			BufferedImage image = ImageIO.read(SpriteSheet.class.getResource(path));	//whew (TO-DO) get understood; ep 19 of TheCherno. ~9-10 min mark
			
		
			
			
			//Buffered Image loads the alpha channel, too
			System.out.println(" succeeded!");
			width = image.getWidth();
			height = image.getHeight();
			
			//NB THESE THREE STATEMENTS BREAKS COMPATIBILITY WITH OTHER RAINE STUFF
			//Frankly this should be here ANYWAY but meh
				pixels = new int[width * height];
				sWidth = width;
				sHeight = height;
				
			image.getRGB(0, 0, width, height, pixels, 0, width);
			
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(" failed!");
		}	//could do a finally to do our println since we lack that 'ln' up top
		
	}
	
	private void loadNotPath() throws IOException {
		
//		path = System.getProperty("user.dir") + path;
		
//		System.out.print("DEBUG SpriteSheet.load() - Trying to load: " + path);

		
		BufferedImage image = ImageIO.read(new File(path));

		//Buffered Image loads the alpha channel, too
//		System.out.println(" succeeded!");
		width = image.getWidth();
		height = image.getHeight();
		
		//NB THESE THREE STATEMENTS BREAKS COMPATIBILITY WITH OTHER RAINE STUFF
		//Frankly this should be here ANYWAY but meh
		pixels = new int[width * height];
		sWidth = width;
		sHeight = height;
			
		image.getRGB(0, 0, width, height, pixels, 0, width);
		
	}

	public int[] getPixels() {
		return pixels;
	}
	
}
