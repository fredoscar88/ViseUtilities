package com.visellico.graphics.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import com.farr.Events.types.KeyTypedEvent;
import com.farr.Events.types.MousePressedEvent;
import com.visellico.util.Vector2i;

public class UITextField extends UIComponent implements UIFocusable {

	private String enteredText = "";
	private String displayWhenEmpty = "";
	private Font font = new Font("Times New Roman", Font.PLAIN, 24); //default font;
	private FontMetrics fontMetrics;
	private Color backgroundColor = Color.WHITE;
	private Color textColor = Color.BLACK;
	private Color emptyColor = Color.gray;
	
	private Graphics g;
	private Rectangle rect;
	
	private int xMargin = 2;
	private int yMargin = 2;
	
	private int cursorOffset = 0;
	private int cursorAnimate = 0;
	
	//TODO incorporate maxLength into UITextField
	private int maxLength = -1;
	
	private boolean focused = false;
	
	//TODO create a larger variety of constructors
	/**
	 * Constructs a UITextField component
	 * @param position
	 * @param displayWhenEmpty
	 */
	public UITextField(Vector2i position, int width, String displayWhenEmpty) {
				
		super(position);
		this.size = new Vector2i(width, font.getSize() + 3 * yMargin);
		this.displayWhenEmpty = displayWhenEmpty;
	}
	
	public String getText() {
		return enteredText;
	}
	
	public boolean onMousePress(MousePressedEvent e) {
		if (rect.contains(new Point(e.getX(), e.getY()))) {
			
			panel.setFocus(this);
			
			return true;
		}
		return false;
	}
	
	//TODO Refine this so we can use stuff that comes with actual key typed events- stuff like action key, or modifiers, etc.
	public boolean onKeyType(KeyTypedEvent e) {
		char key = e.getKeyChar();
		int keyUnicode = (Character.codePointAt(new char[] {key}, 0));	//TODO do we really have to allocate an array each time
		
		//Backspace handling- set the string to a substring of itself, 1 minus it's former length.
		if ((keyUnicode == 8) && !enteredText.equals("")) {
			enteredText = enteredText.substring(0, enteredText.length() - 1);
		}
		
		//If we are an appropriate character,  concatenate it onto the end.
		if (Character.isDefined(key) && keyUnicode != 8) {
			enteredText = enteredText + key;
		}
		//Thanks stack overflow <3 http://stackoverflow.com/questions/1524855/how-to-calculate-the-fonts-width
		fontMetrics = g.getFontMetrics(font);
		cursorOffset = fontMetrics.stringWidth(enteredText);
		return false;
	}
		
	public void update() {
		
		if (!getFocused()) 
			cursorAnimate = 0;
		else 
			cursorAnimate = ++cursorAnimate % 60;
	}
	
	//TODO render the string so that if we exceed a certain length, likely determined by the width of the text box, it will render it so that the string appears to move left.
	public void render(Graphics g) {
		if (this.g == null) this.g = g;
		int x = position.x + offset.x;
		int y = position.y + offset.y;
		
		g.setColor(backgroundColor);
		g.fillRect(x, y, size.x, size.y);
		
		g.setColor(textColor);
		g.setFont(font);
		String drawString;// = getFocused() ? displayWhenEmpty : enteredText;
		
		if (!getFocused() && enteredText.equals("")) {
			drawString = displayWhenEmpty;
			g.setColor(emptyColor);
		} else {
			drawString = enteredText;
		}
		
		//TODO Magic numbers, dat 3 * yMargin tho
		g.drawString(drawString, x + xMargin, y + size.y - (3*yMargin));
		
		if (getFocused() && cursorAnimate < 30) {
			g.fillRect(x + xMargin + cursorOffset, y + yMargin, 2, font.getSize());
		}
	}
	
	public void init(UIPanel p) {
		super.init(p);
		panel.addFocusable(this);
		rect = new Rectangle(position.x + offset.x, position.y + offset.y, size.x, size.y);
	}

	public boolean getFocused() {
		return focused;
	}
	public void removeFocus() {
		focused = false;
	}
	public void giveFocus() {
		focused = true;
	}
	
}
