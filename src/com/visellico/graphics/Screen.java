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
		System.out.println("Class SCREEN: breakpoint");
	}
	
	public void clear(int col) {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = col;	//Colors the screen pink wherever we aren't rendering something, as a debug tool
		}
	}

	/**
	 * Draws screen pixels to the image.
	 */
	public void pack() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {				
				//TODO remove this demonstration
//				if (x < width/2) {
////					imgPixels[x + y * width] = pixels[x + y * width] & 0xFF7FFF;
//					imgPixels[x + y * width] = pixels[x + y * width] & 0xFFFFFF;
//					imgPixels[x + y * width] = pixels[x + y * width] / 0x7FFFFF;
//					
//				}
//				else {
				imgPixels[x + y * width] = pixels[x + y * width];
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
		x = x - xOffset;
		y = y - yOffset;
		if (x < 0 || x >= this.width) return;
		if (y < 0 || y >= this.height) return;
		pixels[x + y * width] = color;
	}

	public void renderTextCharacter(int i, int j, Sprite sprite, int color, boolean b) {
		
	}
	
//	@Deprecated
//	public void renderTile(int x, int y, Tile tile) {
//		
//		x = x - xOffset;	//puts it on the screen
//		y = y - yOffset;
//		
//		for (int tileY = 0; tileY < tile.sprite.getHeight(); tileY++) {
//			int ya = y + tileY;
//			for (int tileX = 0; tileX < tile.sprite.getWidth(); tileX++) {
//				int xa = x + tileX;
//				
//				if (xa < 0|| xa >= width || ya < 0 || ya >= height) continue;
//				pixels[xa + ya * width] = tile.sprite.pixels[tileX + tileY * tile.sprite.getWidth()];
//				
//			}
//		}	
//	}
	
	public void renderSprite(int x, int y, Sprite sprite) {
		
		x = x - xOffset;	//puts it on the screen
		y = y - yOffset;
		
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
	
	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	
}
