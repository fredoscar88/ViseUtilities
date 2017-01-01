package com.visellico.graphics.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import com.farr.Events.Event;
import com.farr.Events.EventDispatcher;
import com.farr.Events.Layer;
import com.farr.Events.types.MouseReleasedEvent;
import com.visellico.util.Vector2i;

public class UIPromptOption implements Layer { 
	
	public List<Layer> parentList;
	public UIPanel screenCover;
	public UIPanel displayRegion;
	
	public UILabel lblQuery;
	
	private static final int BUTTON_WIDTH = 300;
	private static final int BUTTON_HEIGHT = 40;
	private static final int MARGIN = 4;
	
	public UIButton[] buttons;
	
	/*
	 * Value: -2 means creation failed
	 * -1 means ready to prompt
	 * Anything else is the value of the button push
	 */
	public volatile int value = -2;
	
	
	//TODO THIS IS GODDAWFUL I NEED TO REWRITE MY UI LIBRARIES AND STUFF SO ITMAKES SOME ACTUAL SENSE
	public UIPromptOption(int w, int h, String query, String... responses) {
	
		int responsesLength = responses.length;
		if (responsesLength > 4) return; 
		
		value = -1;
		
		screenCover = new UIPanel(new Vector2i(0,0), new Vector2i(w, h));
		screenCover.setColor(new Color(0x7F202020, true));
		
		displayRegion = new UIPanel(new Vector2i(screenCover.size.x / 2 - 250, screenCover.size.y / 2 - 50), new Vector2i(500, responsesLength * BUTTON_HEIGHT + 80));
		displayRegion.setColor(0x55BAE8);
		
		lblQuery = new UILabel(new Vector2i(10,10), query);
		
		displayRegion.add(lblQuery);
		
		if (responsesLength == 0) {
			displayRegion.add(new UIButton(new Vector2i(30, 60), new Vector2i(BUTTON_WIDTH,BUTTON_HEIGHT), () -> {value = 0;}, "OK"));
			displayRegion.size.y = BUTTON_HEIGHT + 80;
		}
			
		for (int i = 0; i < responsesLength; i++) {
			final int index = i;
			displayRegion.add(new UIButton(new Vector2i(30, i*(BUTTON_HEIGHT + MARGIN) + 50), new Vector2i(BUTTON_WIDTH, BUTTON_HEIGHT), () -> {value = index;}, responses[i]));
		}
		
		
	}
	
	public void remove() {
		parentList.remove(this);
	}
	
	public void prompt() {
		value = -1;
		parentList.add(this);
		
	}
	
	public int awaitResponse() {
		parentList.add(this);
		while (value == -1) {
//			System.out.println(value);
		}
		remove();
		return value;
	}

	public void render(Graphics g) {
		screenCover.render(g);
		displayRegion.render(g);
		
	}

	public void update() {
	}

	public void onEvent(Event event) {
			
		EventDispatcher dispatcher = new EventDispatcher(event);
		displayRegion.onEvent(event);
//		dispatcher.dispatch(Event.Type.MOUSE_RELEASED, (Event e) -> onMouseRelease((MouseReleasedEvent) e));
		
		dispatcher.blockEvent();
	}
	
	/**
	 * All events are blocked after this layer so no point in using an event handler, listener works fine
	 * @param e
	 */
	public void onMouseRelease(MouseReleasedEvent e) {
		
		
		
	}

	public void init(List<Layer> l) {
		parentList = l;
	}
	
}
