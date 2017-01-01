package com.visellico.graphics.ui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.farr.Events.types.MouseMovedEvent;
import com.farr.Events.types.MousePressedEvent;
import com.farr.Events.types.MouseReleasedEvent;
import com.visellico.util.Vector2i;

//TODO make buttons and other "interactable" UI elements implement UIFocusable
public class UIButton extends UIComponent {
	
	private UIButtonListener buttonListener;
	private UIActionListener actionListener;
	
	private Rectangle rect;
	
	private boolean inside = false;
	private boolean pressed = false;
	
	private boolean ignorePressed = false;
	private boolean enteredButton = false;
	
	public UILabel label;
	private Image image;	//make this a component?
	
	public UIButton(Vector2i position, Vector2i size) {
		this(position,size, new UIActionListener() {
			public void perform() {
				//default blank, the assumption is that the button will overrider this during creation
			}
		}, "");

	}
	
	public UIButton(Vector2i position, Vector2i size, UIActionListener actionListener) {
		this(position,size, actionListener, "");

	}
	
	public UIButton(Vector2i position, Vector2i size, UIActionListener actionListener, String text) {
		super(position, size);
		this.actionListener = actionListener;
		Vector2i lp = new Vector2i(position);
		//TODO This padding is killing me :'(((
		lp.x += 4;
		lp.y += 4;
		
		//TODO center label on button
		label = (UILabel) new UILabel(new Vector2i(lp), text);//.setColor(0);
		label.setColor(0x444444);
		label.active = false;

//		init();

	}
	
	public UIButton(Vector2i position, BufferedImage image, UIActionListener actionListener) {
		super(position, new Vector2i(image.getWidth(), image.getHeight()));
		this.actionListener = actionListener;
		
		setImage(image);
//		init();
		
	}
	
	
	@Deprecated //moved to the OTHER init method, the one I should be using.
	private void init() {
		setColor(0xAAAAAA);
		
		buttonListener = new UIButtonListener();
	}
	//I think I prefer my setOffset method, if only because I like the notion of SOME components containing others in unique ways, but I guess this is fine.
	//	and in Button we still have access to the label after we add it, so that's cool I guess. yeah TheCherno's is probably bet.rar
	public void init(UIPanel p) {
		
		super.init(p);
		if (label != null) {
			panel.add(label);
		}
//		rect = new Rectangle(getAbsolutePosition().x, getAbsolutePosition().y, size.x, size.y);
		rect = new Rectangle(position.x + offset.x, position.y + offset.y, size.x, size.y);
		
		setColor(0xAAAAAA);
		buttonListener = new UIButtonListener();
		
	}
		
	public void setButtonListener(UIButtonListener buttonListener) {
		this.buttonListener = buttonListener;	//we can override default behavior this way
	}
	
	public void setActionListener(UIActionListener actionListener) {
		this.actionListener = actionListener;	//we give it a new actionListener (and yeah, this is not possible if we implemented ActionListener because we would have
												//	had to create a whole new button just to get a new ActionListener!
	}
	//This should not be implemented through the UIActionListener interface! It's only to access perform without making actionListener public
	//	If it were to overrider perform() in UIActionListener, we'd need to implement it and refactor this to be called perform()
	public void performAction() {	
		actionListener.perform();
	}
	
	public void setImage(Image image) {
		this.image = image;
	}
	
	public void setText(String text) {
		if (text == "") {
			label.active = false;
		}
		else {
			label.setText(text);
//			label.setOffset(offset); see if we don't initialize the label upon button creation, we never get another chance to set it's offset
			label.active = true;
		}
	}

	public boolean onMouseMove(MouseMovedEvent e) {
		
		boolean mouseInside = rect.contains(new Point(e.getX(), e.getY()));
		
		if (mouseInside && !enteredButton) {
			enteredButton = true;
			showTooltip = true;
			buttonListener.entered(this);
			inside = true;
		} else if (!mouseInside) {
			buttonListener.exited(this);
			enteredButton = false;
			ignorePressed = true;
			inside = false;
		}
		return mouseInside;
	}
	
	//TODO *IMPORTANT* change button listener to also accept event parameters, so we can determine which button was pressed, and other stuff
	public boolean onMousePress(MousePressedEvent e) {
		if (inside) {
			ignorePressed = false;
			buttonListener.pressed(this);
		}
		return inside;
	}
	
	public boolean onMouseRelease(MouseReleasedEvent e) {
		if (inside && !ignorePressed) {
			buttonListener.released(this);
		}
		return inside;
	}
	
	public void update() {
		//May have some sort of animation here...
	}

	public void render(Graphics g) {
		
		int x = position.x + offset.x;
		int y = position.y + offset.y;
		
		if (image != null) {
			
			g.drawImage(image, x, y, null);
		} else {
			
			//Im thinking color will be set in update(), like when it's pressed
			g.setColor(color);
			g.fillRect(x, y, size.x, size.y);
			if (label != null) label.render(g);			
		}
		
		if (tooltip != null && showTooltip) {
			tooltip.render(g);
		}
		
	}

}
