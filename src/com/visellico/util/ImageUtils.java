package com.visellico.util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.IOException;

import javax.imageio.ImageIO;

import static com.visellico.util.MathUtils.clamp;

public class ImageUtils {

	private ImageUtils(){
	}
	
	/**
	 * Changes brightness by adding a given value to each of the color channels for every pixel in a given image.
	 * Takes input image as TYPE_4BYTE_ABGR, but output image is TYPE_INT_ARGB.
	 * @param original A BufferedImage of type TYPE_4BYTE_ABGR (4x longer than TYPE_INT_ARGB)
	 * @param amount Amount to add to each color channel, negative or positive
	 * @return An image that is brighter or darker than the original
	 */
	public static BufferedImage changeBrightness(BufferedImage original, int amount) {

		BufferedImage result = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);
		System.out.println(original.getType());
		//NOTE BIEN the stuff beneath returns the images of a pixel array using BufferedImage.TYPE_BYTE_ABGR (or something like that)
		byte[] originalPixels = ((DataBufferByte) original.getRaster().getDataBuffer()).getData();	//class casting error if we cast to DataBufferInt
		int[] resultPixels = ((DataBufferInt) result.getRaster().getDataBuffer()).getData();
		
		int offset = 0;
		
		for (int yy = 0; yy < original.getHeight(); yy++) {
			for (int xx = 0; xx < original.getWidth(); xx++) {

				//This is a mad house.
				int a = (int) Byte.toUnsignedInt(originalPixels[offset++]);	//whew this all has to do with how the different color channels are used with DataBufferByte
				int b = (int) Byte.toUnsignedInt(originalPixels[offset++]);	//	So, the TYPE of the BufferedImage original == BufferedImage.TYPE_BYTE_ABGR
				int g = (int) Byte.toUnsignedInt(originalPixels[offset++]);	//I frankly don't have a good idea of why each pixel in this array stores it's channels, well, collated
				int r = (int) Byte.toUnsignedInt(originalPixels[offset++]);	//	like this- It's not << 24 for alpha, it is in fact every four bits is one of the alpha bits. Yeah. This is a bit weid but I've made my peace and accept it.

				r = clamp(r + amount, 0, 0xFF);
				g = clamp(g + amount, 0, 0xFF);
				b = clamp(b + amount, 0, 0xFF);
				
				resultPixels[xx + yy * original.getWidth()] = (a << 24 | (r << 16) | (g << 8) | b);
				
			}
		}
		return result;
		
	}
	
	/**
	 * Replaces the 
	 * @param image
	 * @param color
	 * @param replacementColor
	 * @param hasAlpha
	 * @return
	 */
	public static BufferedImage replaceColor(BufferedImage image, int color, int replacementColor, boolean hasAlpha) {

		int width = image.getWidth();
		int height = image.getHeight();
		int[] pixels;
			
		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		pixels = ((DataBufferInt) result.getRaster().getDataBuffer()).getData();
		
//		System.out.println("Image type " + image.getType());
		image.getRGB(0, 0, width, height, pixels, 0, width);
		
//		System.out.printf("%x", pixels[0]);
		
		for (int i = 0; i < pixels.length; i++) {
			if (pixels[i] == color) pixels[i] = replacementColor;
		}
				
		return result;
	}
	
	/**
	 * @note UNTESTED
	 * @info Replaces the RGB channels of each pixel of the given color in a pixel array with the given replace RGB channels.
	 * @param pixels Pixel array to be modified
	 * @param color Color to be replaced
	 * @param replacementColor Color that will replace
	 * @param preserveAlpha True if the original color's alpha channel should be maintained. If false, will assume the given replacement color's alpha
	 */
	public static void replaceColor(int[] pixels, int color, int replacementColor, boolean preserveAlpha) {
		
		if (preserveAlpha) {
			int alpha = color & 0xFF000000;
			replacementColor = (replacementColor & 0xFFFFFF) + alpha;
		}
		
		for (int i = 0; i < pixels.length; i++) {
			if ((pixels[i] & 0xFFFFFF) == (color & 0xFFFFFF)) {
				pixels[i] = replacementColor;
			}
		}
		
	}
	
	/**
	 * 
	 * @note not working
	 * @param pixels
	 */
	public static void filterBlackWhite(int[] pixels) {
		int newColor;
		double average;
		double r, g, b;
		for (int i = 0; i < pixels.length; i++) {
			r = pixels[i] & 0xFF0000;
			g = pixels[i] & 0x00FF00;
			b = pixels[i] & 0x0000FF;
			average = Math.round((r * .21) + (g * .72) + (b * .07));
			newColor = (int) average;
			newColor <<= 4;
			newColor |= (newColor << 2);
			newColor |= newColor;
			System.out.printf("%x", newColor);
			System.out.println();
			pixels[i] = newColor;
		}
	}
	
	public static BufferedImage loadImageFromFile(String path) {
		
		//It might be silly to create a reference and allocate memory that will just be trashed soon. TODO remove
		BufferedImage image;
		try {
			image = ImageIO.read(ImageUtils.class.getResource(path));
			return image;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void changeSaturation(int[] pixels) {
		//TODO implement
		//Need to create a hex -> hsv method, then take that output, change it's saturation, and pump it back into hex to modify the pixels.
	}
	
}
