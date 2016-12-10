package com.visellico.graphics.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.visellico.input.Mouse;

public class UITooltip {

	private Color backgroundColor;
	
	private String tooltipText;
	private Font font = new Font("Times New Roman", Font.PLAIN, 16);
	private Color stringColor = new Color(0xFFFFFF);
	
	public UITooltip(Color backgroundColor, String tooltipText, Font font, Color stringColor) {
		this(backgroundColor, tooltipText);
		this.font = font;
		this.stringColor = stringColor;
	}
	
	public UITooltip(Color backgroundColor, String tooltipText, Color stringColor) {
		this(backgroundColor, tooltipText);
		this.stringColor = stringColor;
	}
	
	public UITooltip(Color backgroundColor, String tooltipText, Font font) {
		this(backgroundColor, tooltipText);
		this.font = font;
	}
	
	public UITooltip(Color backgroundColor, String tooltipText) {
		this.backgroundColor = backgroundColor;
		this.tooltipText = tooltipText;
	}
	
	//TODO need a way to efficiently render the text of the tooltip onto the background. For now, just having it render the string
	
	//TODO update the render direction so it doesnt go offscreen
	public void render(Graphics g) {
		//TODO render the background
		g.setColor(stringColor);
		g.setFont(font);
		g.drawString(tooltipText, Mouse.getX() + 5, Mouse.getY() + 5);
	}
	
}
