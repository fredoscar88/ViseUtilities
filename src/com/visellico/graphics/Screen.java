package com.visellico.graphics;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Screen {

	public int pixels[];
	public int width, height;
	public int scale;
	public int xOffset, yOffset;
	
	public BufferedImage image;
	public int[] imgPixels;
	
	public Screen(int width, int height, int scale) {
		
		this.width = width;
		this.height = height;
		this.scale = scale;
		pixels = new int[width * height];
		
		image = new BufferedImage(this.width, this.height , BufferedImage.TYPE_INT_RGB);
		imgPixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	}
	
	public void clear(int col) {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = col;	//Colors the screen pink wherever we aren't rendering something, as a debug tool
		}
	}

	
//	private int AAAAAHHH = 0;
//	private int AAAAAHHH2 = 0;
	
	/**
	 * Draws screen pixels to the image.
	 */
	public void pack() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {				
				//TODO remove this demonstration
//				if (x < width/2) {
////					imgPixels[x + y * width] = pixels[x + y * width] & 0xFF7FFF;
////					imgPixels[x + y * width] = pixels[x + y * width] & 0xFFFFFF;
////					imgPixels[x + y * width] = pixels[x + y * width] / 0x7FFFFF;
//					
//				}
//				else {
				//Some good changes
				/*
				 * & 0x7F7F7F sorta desaturates
				 * | 0xFF0000 Fills red channel
				 * | 0x0000FF Fills blue channel
				 * | 0xFF00FF Fills "pink" channel, giving it a princessy look
				 */
				imgPixels[x + y * width] = pixels[x + y * width];// + AAAAAHHH;
//				AAAAAHHH += 1024;// AAAAAHHH2;	//SEIZURE WARNING
//				AAAAAHHH2 += 1;
//				}
			}
		}
	}
	
	/**
	 * @deprecated
	 * @param points
	 */
	public void renderBackground(int[] points) {
		
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = points[i];
		}
		
	}

	public void fillRect(int x, int y, int width, int height, int color) {
		
		for (int yi = 0; yi < height; yi++) {
			
			int yf = yi + y;
			if (yf >= this.height || yf < 0) continue;
			for (int xi = 0; xi < width; xi++) {
				int xf = xi + x;
				if (xf >= this.width || xf < 0) continue;
				pixels[xf + yf * this.width] = color;
			}
		}
		
	}
	
	public void renderPoint(int x, int y, int color) {
		//-------------PLATTY-----------------
//		System.out.println(y);
		y = getScreenY(y);
		//-------------PLATTY-----------------
		x -= xOffset;
		y -= yOffset;
		if (x < 0 || x >= this.width) return;
		if (y < 0 || y >= this.height) return;
		pixels[x + y * width] = color;
	}
	
	/**
	 * Same as render point, except takes a pixel already drawn to the array and inverts it.
	 * If called before other objects on screen are rendered it won't accurately reflect them.
	 * @param x
	 * @param y
	 */
	public void renderInvertedPoint(int x, int y) {
		y = getScreenY(y);
		x = x - xOffset;
		y = y - yOffset;
		if (x < 0 || x >= this.width) return;
		if (y < 0 || y >= this.height) return;
		pixels[x + y * width] = ~pixels[x + y * width] & 0x00FFFFFF;
	}
		
	public void renderLine(int x1, int y1, int x2, int y2, int color) {
		
//		x1 = x1 - xOffset;
//		x2 = x2 - xOffset;
//		y1 = y1 - yOffset;
//		y2 = y2 - yOffset;
		
		double dx = x2 - x1;
		double dy = y2 - y1;

		double theta = Math.atan2(dy, dx);
		
		dx = Math.cos(theta);
		dy = Math.sin(theta);

		double ya = y1;
		double xa = x1;
		
		while (Math.abs(ya - y2) >= 1 || Math.abs(xa - x2) >= 1) {
			
			if (xa - xOffset < 0 && dx < 0) break;
			if (getScreenY(ya) - yOffset < 0 && getScreenY(dy) < 0) break;
			if (xa - xOffset >= width && dx > 0) break;
			if (getScreenY(ya) - yOffset >= height && getScreenY(dy) > 0) break;
			
			
			renderPoint((int) Math.round(xa), (int) Math.round(ya), color);
			xa += dx;
			ya += dy;
		}
				
	}
	
	public void renderFilledRec(int x, int y, int width, int height, int color) {
		//-------------PLATTY-----------------
		y = getScreenY(y);
		height = getScreenY(height);
		//-------------PLATTY-----------------
		
		x -= xOffset;
		y -= yOffset;
				
		for (int i = 0; i > height; i--) {
			
			int yn = y + i;
			if (yn < 0 || yn >= this.height) continue;
			
			for (int j = 0; j < width; j++) {
				int xn = x + j;
				if (xn < 0 || xn >= this.width) continue;
				pixels[xn + yn * this.width] = color;
			}
		} 
	}
	
	public void renderSprite(int x, int y, Sprite sprite) {
		
		y = getScreenY(y) - sprite.getHeight();
		x -= xOffset;	//puts it on the screen
		y -= yOffset;
		
		for (int tileY = 0; tileY < sprite.getHeight(); tileY++) {
			int ya = y + tileY;
			for (int tileX = 0; tileX < sprite.getWidth(); tileX++) {
				int xa = x + tileX;
				
				if (xa < 0|| xa >= width || ya < 0 || ya >= height) continue;
				if (sprite.pixels[tileX + tileY * sprite.getWidth()] == 0xFFFF00FF) continue;
				
				pixels[xa + ya * width] = sprite.pixels[tileX + tileY * sprite.getWidth()];
				
			}
		}	
	}
	
	public void renderSpriteFar(int x, int y, Sprite sprite, int distFromForeground) {
		
		y = getScreenY(y) - sprite.getHeight();
		
		int distScale = distFromForeground;
		
//		x += xOffset;
//		y += yOffset;
		
		x /= distScale;
//		y /= distScale;
		
		x -= xOffset/distScale;	//puts it on the screen
		y -= yOffset;///distScale;
		
		for (int tileY = 0; tileY < sprite.getHeight(); tileY++) {
			int ya = y + tileY;
			for (int tileX = 0; tileX < sprite.getWidth(); tileX++) {
				int xa = x + tileX;
				
				if (xa < 0|| xa >= width || ya < 0 || ya >= height) continue;
				if (sprite.pixels[tileX + tileY * sprite.getWidth()] == 0xFFFF00FF) continue;
				
				pixels[xa + ya * width] = sprite.pixels[tileX + tileY * sprite.getWidth()];
				
			}
		}	
	}
	
	/**
	 * Draws a tileable sprite, similar to a filledRect.
	 * Unlike a filled rect, draws from the upper right, going left to right up to down.
	 * In this fashion height need not be inverted like it is for the filled rectangle which draws
	 * lower left, left to right down to up.
	 * 
	 * A little inconsistent. So TODO absolve inconsistencies in where/how I draw
	 * Note that if I draw it like a rectangle, the sprites are all inverted. so I could just flip the sprite, to make this draw like rectangles.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param sprite
	 */
	public void renderSpriteTiled(int x, int y, int width, int height, Sprite sprite) {
		renderPoint(x, y, 0xFF00FF);
//		System.out.println("whew " + x + " " + y + " " + width + " " + height);
		y = getScreenY(y);
//		height = getScreenY(height);
		
		x -= xOffset;
		y -= yOffset;
		
		for (int objY = 0; objY < height; objY++) {
			int ya = y + objY;
			if (ya < 0 || ya >= this.height) continue;
			
			for (int objX = 0; objX < width; objX++) {
				int xa = x + objX;
				if (xa < 0 || xa >= this.width) continue;
				
				int spriteY = Math.abs(objY) % sprite.getHeight();
				int spriteX = objX % sprite.getWidth();
				
				if (sprite.pixels[spriteX + spriteY * sprite.getWidth()] == 0xFFFF00FF) continue;
				pixels[xa + ya * this.width] = sprite.pixels[spriteX + spriteY * sprite.getWidth()];
				
			}
		}
		
	}
	
	public static int getScreenY(int levelY) {
		return -levelY;
	}
	
	public static double getScreenY(double levelY) {
		return -levelY;
	}
	
	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	
}
