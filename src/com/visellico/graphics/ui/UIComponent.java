package com.visellico.graphics.ui;

import java.awt.Color;
import java.awt.Graphics;

import com.farr.Events.types.KeyTypedEvent;
import com.farr.Events.types.MouseMovedEvent;
import com.farr.Events.types.MousePressedEvent;
import com.farr.Events.types.MouseReleasedEvent;
import com.farr.Events.types.MouseScrollEvent;
import com.visellico.util.Vector2i;

//TODO need to cement that "actionable" part of UIComponents. Things like UISCrollLists and UIPanels should implement UIActionListener, then distribute the Action to their components
public class UIComponent {

	public Vector2i position, size;
//	public Vector2i renderPos;	//In case this needs to be modified by a render object
	
	protected Vector2i offset;
	protected Color color;
	protected UIPanel panel;	//"parent" panel of this UI component
	
	//the uitooltip is activated upon mousehover
	protected UITooltip tooltip;
	protected boolean showTooltip = false;
	
	boolean active;
	
//	public UIComponent(Vector2i position) {
//		this.position = position;
//		offset = new Vector2i();
//		color = new Color(0xFF00FF);
//	}	
	
	public UIComponent(Vector2i position, Vector2i size) {
		this.position = position;
		this.size = size;
		offset = new Vector2i();
		color = new Color(0xFF00FF);
	}
	
	public void update() {
		
	}
	
	public void render(Graphics g) {
		
	}
	
	public void init(UIPanel p) {
		this.panel = p;	//sets a pointer to the panel that contains this component
	}
	
	/**
	 * Set color based on an RGB value
	 * Allows inline creating of new components with non-default colors
	 * @param rgb New RGB value the component should have in it's color
	 * @return an instance of UIComponent with the new color constructed from the supplied RGB value.
	 */
	public UIComponent setColor(int rgb) {
		color = new Color(rgb);
		return this;
	}
	
	public UIComponent setColor(int rgba, boolean alpha) {
		color = new Color(rgba, alpha);
		return this;
	}
	
	/**
	 * Set color based on an RGB value
	 * Allows inline creating of new components with non-default colors
	 * @param c New color the component should have
	 * @return an instance of UIComponent with the new color constructed from the supplied RGB value.
	 */
	public UIComponent setColor(Color c) {
		color = c;//new Color(c.getRGB());
		return this;
	}
	
	public Vector2i getAbsolutePosition() {
		return new Vector2i(position).add(offset);
	}
	
	protected void setOffset(Vector2i offset) {
		this.offset = offset;
	}

	//TODO make UIComponent abstract
	public boolean onMousePress(MousePressedEvent e) {
		return false;
	}
	
	public boolean onMouseRelease(MouseReleasedEvent e) {
		return false;
	}
	
	public boolean onMouseMove(MouseMovedEvent e) {
		return false;
	}
	
	public boolean onMouseScroll(MouseScrollEvent e) {
		return false;
	}
	
//	public boolean onKeyPress(KeyPressedEvent e) {
//		return false;
//	}
//	
//	public boolean onKeyRelease(KeyReleasedEvent e) {
//		return false;
//	}
	
	public boolean onKeyType(KeyTypedEvent e) {
		return false;
	}
	
	
}
