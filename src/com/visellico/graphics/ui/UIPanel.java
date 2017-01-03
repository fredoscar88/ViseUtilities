package com.visellico.graphics.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.farr.Events.Event;
import com.farr.Events.Event.Type;
import com.farr.Events.EventDispatcher;
import com.farr.Events.Layer;
import com.farr.Events.types.KeyTypedEvent;
import com.farr.Events.types.MouseMovedEvent;
import com.farr.Events.types.MousePressedEvent;
import com.farr.Events.types.MouseReleasedEvent;
import com.farr.Events.types.MouseScrollEvent;
import com.visellico.util.Vector2i;

//TODO Hotdiggity I hope we never have to scale our UI... or if we do, then we git smart about how it will work.
public class UIPanel extends UIComponent implements Layer {
	
	private List<UIComponent> components = new ArrayList<UIComponent>();
	private List<UIFocusable> focusableComponents = new ArrayList<>();
	
	//TODO @devnote; we are fooling around with UI a lot and may, at some point, need to reconfigure each of these, like putting rect in UIComponent for instance.
	//	If I had to redo this, I would be a lot more persnickety about it.
	private Rectangle rect;
	
	public UIPanel(Vector2i position) {
		super(position, new Vector2i(0,0));
		this.position = position;
	}
	
	public UIPanel(Vector2i position, Vector2i size) {
		super(position, new Vector2i(0,0));
		this.position = position;
		this.size = size;
		color = new Color(0xAACACACA, true);	//surely we can make this a parameter, somewhere. Also this is a default param.
		rect = new Rectangle(position.x, position.y, size.x, size.y);
	}
	
	public void add(UIComponent c) {
		components.add(c);
		c.setOffset(position);	//Should only need to do this once, or whenever we update position. So I'm going to go ahead and add in a setPosition, that incorporates this, so I don't forget
		c.init(this);
	}
	
	public void addFocusable(UIFocusable c) {
		focusableComponents.add(c);
//		System.out.println("Focusable elements " + focusableComponents.size());
				
	}
	
	public void setFocus(UIFocusable f) {
		for (UIFocusable c : focusableComponents) {
			c.removeFocus();
			if (c.equals(f)) {
				c.giveFocus();
			}
		}
	}
	
	public void clearAllFocus() {
		for (UIComponent c : components) {
			if (c instanceof UIPanel) ((UIPanel) c).clearAllFocus();
			if (c instanceof UIFocusable) ((UIFocusable) c).removeFocus();
		}
	}
	
	public void setPosition(Vector2i position) {
		this.position = position;
		for (UIComponent component : components) {	//Hey now we're thinking like programmers
			component.setOffset(this.position);	//Updates each of the component's positions with the set position.
		}
	}
	
	public UIPanel setColor(Color c) {
		this.color = c;
		return this;
	}
	
	public UIPanel setColor(int rgba, boolean alpha) {
		super.setColor(rgba, alpha);
//		this.color = new Color(rgba, true);
		return this;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public boolean onMousePress(MousePressedEvent e) {
		for (UIComponent component : components) {
			if (component.onMousePress(e))
				return true;
		}
		return false;
	}
	
	public boolean onMouseRelease(MouseReleasedEvent e) {
		for (UIComponent component : components) {
			if (component.onMouseRelease(e))
				return true;
		}
		return false;
	}
	
	public boolean onMouseMove(MouseMovedEvent e) {
		
		for (UIComponent component : components) {
			if (component.onMouseMove(e))
				return true;
		}
		if (rect.contains(e.getX(), e.getY()))
			return true;
		return false;
	}
	
	public boolean onMouseScroll(MouseScrollEvent e) {
		
		for (UIComponent component : components) {
			if (component.onMouseScroll(e)) 
				return true;
		}
		if (rect.contains(e.getX(), e.getY())) 
			return true;
		return false;
	}
	
	/**
	 * Distributes a keytyped event to the currently focused focusable component.
	 * @param e The keypressed event to sent
	 */
	public boolean onKeyType(KeyTypedEvent e) {
		
//		System.out.printf("%04X\n", (short) e.getKeyChar());
		
		
		for (UIFocusable focusable : focusableComponents) {
			if (!focusable.getFocused()) continue;
			if (focusable.onKeyType(e))
				return true;
		}
		for (UIComponent c : components) {
			if (c instanceof UIPanel) {
				if (c.onKeyType(e)) 
					return true;
			}
		}
		return false;
	}
	
	public void update() {
		for(UIComponent component : components) {
//			component.setOffset(position);	//This only needs to happen once? Or, if more than once, only ever infrequently. Maybe not tho
			component.update();
		}
	}
	
	public void render(Graphics g) {
		g.setColor(color);	//right? right! confurmed by TerCherners
		g.fillRect(position.x, position.y, size.x, size.y);
		
		for(UIComponent component : components) {
			component.render(g);
		}
	}

	public void onEvent(Event event) {
		
		EventDispatcher dispatcher = new EventDispatcher(event);
		
		dispatcher.dispatch(Type.MOUSE_MOVED, (Event e) -> onMouseMove((MouseMovedEvent) e));
		dispatcher.dispatch(Type.MOUSE_PRESSED, (Event e) -> onMousePress((MousePressedEvent) e));
		dispatcher.dispatch(Type.MOUSE_RELEASED, (Event e) -> onMouseRelease((MouseReleasedEvent) e));
		dispatcher.dispatch(Type.MOUSE_SCROLLED, (Event e) -> onMouseScroll((MouseScrollEvent) e)); 
		dispatcher.dispatch(Type.KEY_TYPED, (Event e) -> onKeyType((KeyTypedEvent) e));
		
	}

	public void init(List<Layer> l) {
		
	}
	
}
