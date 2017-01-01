package com.visellico.graphics.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import com.farr.Events.Event;
import com.farr.Events.Layer;
import com.visellico.util.Vector2i;

public class UIPromptInfoOk implements Layer {

	public UIComponent comp;
	public UIButton okayButton;
	public UIPanel screenCover;
	public UIPanel displayRegion;
	
	public String value;
	
	public UIPromptInfoOk(int w, int h, String query, UIScrollList c) {
		value = null;
		
		this.comp = c;
		okayButton = new UIButton(new Vector2i(),new Vector2i(),() -> {},"Okay");
		
		screenCover = new UIPanel(new Vector2i(0,0), new Vector2i(w, h));
		screenCover.setColor(new Color(0x7F202020));
		
		displayRegion = new UIPanel(new Vector2i(screenCover.size.x / 2 - 250, screenCover.size.y / 2 - 50), new Vector2i(500, c.size.y + 80));
		displayRegion.setColor(0x55BAE8);
		
	}
	
	public UIPromptInfoOk(int w, int h, String query, UITextField c) {
		value = null;
		
		this.comp = c;
		okayButton = new UIButton(new Vector2i(),new Vector2i(),() -> {},"Okay");
		
		screenCover = new UIPanel(new Vector2i(0,0), new Vector2i(w, h));
		screenCover.setColor(new Color(0x7F202020));
		
		displayRegion = new UIPanel(new Vector2i(screenCover.size.x / 2 - 250, screenCover.size.y / 2 - 50), new Vector2i(500, c.size.y + 80));
		displayRegion.setColor(0x55BAE8);
		
	}
	
	public void render(Graphics g) {
		
	}

	public void update() {
		
	}

	@Override
	public void onEvent(Event e) {
		
	}

	@Override
	public void init(List<Layer> l) {
		
	}

	
	
}
