package com.visellico.graphics.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.farr.Events.types.MousePressedEvent;
import com.visellico.util.Vector2i;

public class UILabel extends UIComponent {

	private String text;	//private?
	private Font font;
	private FontMetrics metrics;
	private Graphics g;
	
	public int yPaddingOffset = 4;
	
	public int dropShadowOffset = 2;
	public boolean dropShadow = false;
	
	public boolean backgrndVisible = true;
	
	private UIActionListener actionListener;
	
//	public UILabel(Vector2i position) {
//		
//		this(position, "");
//		
//	}
	
	public UILabel(Vector2i position, String text) {
		this(position, text, new Font("Helvetica", Font.PLAIN, 32));
//		super(position, new Vector2i(0, 0));
//		font = new Font("Helvetica", Font.PLAIN, 32);	//default font
//		
//		
//		
//		size.x = 0;
//		size.y = font.getSize();
//		this.text = text;
		
	}
	
	public UILabel(Vector2i position, String text, Font font) {
		this(position, text, font, () -> {});
//		super(position, new Vector2i(0, 0));
//		this.font = font;
//		
//		size.x = 0;
//		size.y = font.getSize();
//		this.text = text;
//		
//		actionListener = new UIActionListener() {public void perform() {}};
//		System.out.println(text + " FROM UILABEL " + getAbsolutePosition().y);
	}
	
	public UILabel(Vector2i position, String text, Font font, UIActionListener actionListener) {
		super(position, new Vector2i(0, 0));
		this.font = font;
		
		size.x = 0;
		size.y = font.getSize();
		this.text = text;
		
		this.actionListener = actionListener;
//		System.out.println(text + " FROM UILABEL " + getAbsolutePosition().y);
	}
	
	public void setText(String text) {
		this.text = text;
		size.x = metrics.stringWidth(text);
	}
	
	/**
	 * DOESNT WORK DONT USE BY APPENDING TO CONSTRUCTOR, G WILL BE NULL!
	 * @param f
	 * @return
	 */
	public UILabel setFont(Font f) {	//for inline setting the font when constructing a new UILabel
		font = f;
		g.setFont(font);
		updateMetrics(g);
		return this;
	}
	
	public UILabel setYPaddingOffset(int newPadding) {
		this.yPaddingOffset = newPadding;
		return this;
	}
	
	public UILabel setFont(String fontName, int fontSize) {
		font = new Font(fontName, Font.PLAIN, fontSize);
		g.setFont(font);
		updateMetrics(g);
		return this;
	}
	
	private void updateMetrics(Graphics g) {
		this.g = g;
		metrics = this.g.getFontMetrics();
		size.x = metrics.stringWidth(text);
		size.y = font.getSize();
		setYPaddingOffset((metrics.getHeight() - size.y)/2);
	}
	
	public void update() {
		
	}
	
	public void render(Graphics g) {
		
		if (backgrndVisible) {			
			g.setColor(Color.gray);
			g.fillRect(getAbsolutePosition().x, getAbsolutePosition().y, size.x, size.y);
		}
				
		g.setFont(font);
		if (this.g == null) {
			updateMetrics(g);
		}
		
		//Font shadow! well, ""outline"" but nah. it's a little disgusting.
		if (dropShadow) {
			g.setColor(new Color(0));
//			g.setFont(font);//new Font(font.getFontName(), font.getStyle(), font.getSize() + 1));
			g.drawString(text, position.x + offset.x + dropShadowOffset, position.y + offset.y + dropShadowOffset);			
		}
		g.setColor(color);
		
		//Dont really want to do this every render call, but I cant think of a better way. Also + 2 is pretty shite there at the end :c
		setYPaddingOffset((metrics.getHeight() - size.y)/2 + 2);
		//TODO find better fix for the below
		//The - 4 is for some padding issues. Yes, it's a magic number and I have no idea how it will impact other font sizes.
		g.drawString(text, getAbsolutePosition().x, getAbsolutePosition().y + size.y - yPaddingOffset);
		
//		g.setColor(Color.white);
//		g.drawLine(getAbsolutePosition().x, getAbsolutePosition().y, getAbsolutePosition().x + size.x, getAbsolutePosition().y + size.y);
		
	}
	
	public boolean onMousePress(MousePressedEvent e) {
		if (new Rectangle(getAbsolutePosition().x, getAbsolutePosition().y, size.x, size.y).contains(e.getX(), e.getY())) {
			actionListener.perform();
			return true;
		}
		return false;
	}
	
	public void setActionListener(UIActionListener listener) {
		this.actionListener = listener;
	}
	
}
