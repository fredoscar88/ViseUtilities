package com.visellico.graphics.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.visellico.util.Vector2i;

public class UILabel extends UIComponent {

	private String text;	//private?
	private Font font;
	
	public int dropShadowOffset = 2;
	public boolean dropShadow = false;
	
	public UILabel(Vector2i position) {
		
		this(position, "");
		
	}
	
	public UILabel(Vector2i position, String text) {
		super(position);
		font = new Font("Helvetica", Font.PLAIN, 32);	//default font
		this.text = text;
	}
	
	public void setText(String text) {
		this.text = text;
		
	}
	
	public UILabel setFont(Font f) {	//for inline setting the font when constructing a new UILabel
		font = f;
		return this;
	}
	
	public UILabel setFont(String fontName, int fontSize) {
		font = new Font(fontName, Font.PLAIN, fontSize);
		return this;
	}
	
 
	public void update() {
		
	}
	
	public void render(Graphics g) {
		//Font shadow! well, ""outline"" but nah. it's a little disgusting.
		if (dropShadow) {
			g.setColor(new Color(0));
			g.setFont(font);//new Font(font.getFontName(), font.getStyle(), font.getSize() + 1));
			g.drawString(text, position.x + offset.x + dropShadowOffset, position.y + offset.y + dropShadowOffset);			
		}
		g.setColor(color);
		g.setFont(font);
		g.drawString(text, position.x + offset.x, position.y + offset.y);
		
	}
	
}
