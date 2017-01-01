package com.visellico.graphics;

import java.io.IOException;

public class Sprite {

	public int spriteSize;	//size of this sprite
	private int width, height;	//w,h of sprite when non-square (where SIZE would normally be both for square ones)
	private int x,y;	//Coordinates of the sprite on the sprite sheet in gridsize
	SpriteSheet sheet;	//sheet that this exists on
	
	public int[] pixels;	//may not need this to be public
	
	private Sprite() {
	}
	
	/**
	 * This is pretty hacky and terrible. See the TODO message inside for why.
	 * Loads a sprite from a file- only png files are really supported but I think it could technically work with, well, anything.
	 * @param path Path to the sprite file
	 * @throws IOException 
	 */
	public Sprite(String path) throws IOException {
		
		//TODO This is a butchered way of doing this, but..
		//	I don't really want to copy image loading code into here. This sprite class was not built to do things that don't use spritesheets-
		//	And here I am, loading assets by having each sprite be its own sheet. Not too bueno, so TODO: Rework this infrastructure to either use proper spriteshits or single sprite directories like we are currently using
		SpriteSheet temp = new SpriteSheet(path);
		sheet = temp;	//Important for how we load this.
		width = sheet.width;
		height = sheet.height;
		spriteSize = width;
		x = 0;
		y = 0;
		pixels = sheet.pixels;
		load();
		
	}
	
	protected Sprite(SpriteSheet sheet, int width, int height) {
		
		this.spriteSize = (width == height) ? width : -1;	//ternary operations! If square, SIZE is our new dimension. Else size is set to "unused"
		this.width = width;
		this.height = height;
		this.sheet = sheet;
	}
	
	//Loads a sprite from the sprite sheet for the new sprite
	public Sprite(int size, int x, int y, SpriteSheet sheet) {
		this.spriteSize = size;
		this.width = size;
		this.height = size;
		pixels = new int[spriteSize*spriteSize];
		this.x = x * spriteSize;	//the XY come as grid coordinates. The X and Y are not pixels, they are grid locations, and the locations are all 16x, so multiply
		this.y = y * spriteSize;	//each by 16 to get what the their pixel value would be.
		this.sheet = sheet;
		load();
		
	}
	
	//loads a non-square sprite, of a specific color (like void sprite)
	public Sprite(int w, int h, int color) {
		this.spriteSize = (w == h) ? w : -1;	//unused for non-square sprites, but generally used as a width
		this.width = w;
		this.height = h;
		pixels = new int[width * height];
		setColor(color);
		
	}
	
	//Uniformly colors the new sprite with one color
	public Sprite(int size, int color) {	//color should be delivered as a hex value
		this(size, size, color);
	}
	

	
	public Sprite(int[] spritePixels, int width, int height) {
		this.spriteSize = (width == height) ? width : -1;
		this.width = width;
		this.height = height;
		pixels = spritePixels;
//		for (int i = 0; i < spritePixels.length; i++) {
//			pixels[i] = spritePixels[i];
//		}
	}
	
	//extract sprites. ugh this is a bad way to do this. SpriteSheet has a field that is an array of sprites.
	public static Sprite[] split(SpriteSheet sheet) {
		
		//Pixel area of the sheet divided by pixel area of one sprite
		int amount = (sheet.getWidth() * sheet.getHeight()) / (sheet.SPRITE_HEIGHT * sheet.SPRITE_WIDTH);	//uh, amount of sprites in the sheet.	//sheet.HEIGHT * sheet.width;
		Sprite[] sprites = new Sprite[amount];
//		int pixels[] = new int[sheet.SPRITE_HEIGHT * sheet.SPRITE_WIDTH];	//THIS SHOULD NOT BE OUT HERE. SO I moved it. But frankly I think it should work out here..
		int current = 0;
		
		for (int yp = 0; yp < sheet.getHeight() / sheet.SPRITE_HEIGHT; yp++) {
			for (int xp = 0; xp < sheet.getWidth() / sheet.SPRITE_WIDTH; xp++) {
				//Sprite coords in sprite precision. Inside this loop looks at one sprite.
																					
				int pixels[] = new int[sheet.SPRITE_HEIGHT * sheet.SPRITE_WIDTH];	
				
				for (int y = 0; y < sheet.SPRITE_HEIGHT; y++) {
					for (int x = 0; x < sheet.SPRITE_WIDTH; x++) {
						//Pixel coords in a single sprite. also no, Im not going to use a getter method for SpriteSheet pixels. :V
						int xo = xp * sheet.SPRITE_WIDTH;	//x offset
						int yo = yp * sheet.SPRITE_HEIGHT;	//y offset
						pixels[x + y * sheet.SPRITE_WIDTH] = sheet.getPixels()[(xo + x) + (yo + y) * sheet.getWidth()];
					}
				}
				
//				sprites[xp + yp * sheet.getWidth()] = new Sprite(pixels, sheet.SPRITE_WIDTH, sheet.SPRITE_HEIGHT);
				sprites[current++] = new Sprite(pixels, sheet.SPRITE_WIDTH, sheet.SPRITE_HEIGHT);
			}
		}
		
		return sprites;
	}
		
	public static Sprite rotate(Sprite sprite, double angle) {
		
		//can use private fields because we are in the class in which they're declared :I
		return new Sprite(rotate(sprite.pixels, sprite.width, sprite.height, angle), sprite.width, sprite.height);
		
	}
	//could pass in a sprite, but will act on any pixel array
	private static int[] rotate(int[] pixels, int width, int height, double angle) {
		int[] result = new int[width * height];	//NEW integer array!
		
		//again, negative angle here
		//uh.... (TO-DO) git understood
		double nx_x = rotationX(-angle, 1.0, 0.0);	//y is cancelled out
		double nx_y = rotationY(-angle, 1.0, 0.0);
		double ny_x = rotationX(-angle, 0.0, 1.0);	//y is cancelled out
		double ny_y = rotationY(-angle, 0.0, 1.0);
		
		double x0 = rotationX(-angle, -width / 2.0, -height / 2.0) + width / 2.0;
		double y0 = rotationY(-angle, -width / 2.0, -height / 2.0) + height / 2.0;
		
		for (int y = 0; y < height; y++) {
			double x1 = x0;
			double y1 = y0;	//copies of initial x, y vals, animating it a bit.
			for (int x = 0; x < width; x++) {
				int xx = (int) x1;
				int yy = (int) y1;
				int col = 0;
				if (xx < 0 || xx >= width || yy < 0 || yy >= height) col = 0xffff00ff;
				else col = pixels[xx + yy * width];
				result[x + y * width] = col;
				x1 += nx_x;
				y1 += nx_y;
			}
			x0 += ny_x;	//x0???
			y0 += ny_y;
		}
		
		return result;
	}
	
	/**
	 * Flips a sprite
	 */
	public Sprite yFlip() {
		
		Sprite newSprite = copy();
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				newSprite.pixels[x + y * width] = pixels[x + (width - 1 - y) * width];
			}
		}
		
		return newSprite;
		
	}
	
	//TODO TBH could just override the clone method here
	public Sprite copy() {
		
		Sprite newSprite = new Sprite();
		
		newSprite.height = height;
		newSprite.width = width;
//		newSprite.x = x;
//		newSprite.y = y;
		newSprite.pixels = new int[width * height];
		
		return newSprite;
	}
	
	/**
	 * 
	 * @param angle
	 * @param x
	 * @param y
	 * @return
	 */
	private static double rotationX(double angle, double x, double y) {
		double cos = Math.cos(angle - (Math.PI/2.0));
		double sin = Math.sin(angle - (Math.PI/2.0));
		//negative y, because we're flipping our unit circle :C
		return x * cos + y * -sin;
	}
	
	private static double rotationY(double angle, double x, double y) {
		double cos = Math.cos(angle - (Math.PI/2.0));
		double sin = Math.sin(angle - (Math.PI/2.0));
		//not sure whats going on
		return x * sin + y * cos;
	}
	
	//thanks youtube comments on TheCherno vid 114
	public static Sprite scale(int[] pixels, int w1, int h1, int w2, int h2) {

		int[] new_pixels = new int[w2 * h2];

		int xr = (int) ((w1 << 16) / w2) + 1;
		int yr = (int) ((h1 << 16) / h2) + 1;

		int x2, y2;

		for (int i = 0; i < h2; i++) {
			for (int j = 0; j < w2; j++) {
				x2 = ((j * xr) >> 16);
				y2 = ((i * yr) >> 16);
				new_pixels[(i * w2) + j] = pixels[(y2 * w1) + x2];
			}
		}
		// Create a new sprite from your new pixels, new width, and new height
		Sprite new_sprite = new Sprite(new_pixels, w2, h2);

		return new_sprite;
	}
	
	
	//This seems unnecessary, unless we want to change the color during runtime :S
	private void setColor(int color) {
		for (int i = 0; i < pixels.length; i++) {	//length = width * height or SIZE^2
			pixels[i] = color;
		}
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	private void load() {
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				//"coordinate" stuff similar to what we find in Screen for drawing
				pixels [x+y*spriteSize] = sheet.pixels[(x+this.x) + (y + this.y)*sheet.sWidth];		
			}
		}
		
	}
	
}
