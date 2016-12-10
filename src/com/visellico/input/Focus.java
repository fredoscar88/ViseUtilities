package com.visellico.input;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import com.farr.Events.EventListener;
import com.farr.Events.types.FocusGainedEvent;
import com.farr.Events.types.FocusLostEvent;

public class Focus implements FocusListener {

	EventListener listener;
	
	public Focus(EventListener eventListener) {
		listener = eventListener;
	}
	
	public void focusGained(FocusEvent e) {
		FocusGainedEvent fe = new FocusGainedEvent();
		listener.onEvent(fe);
	}

	public void focusLost(FocusEvent e) {
		//nothing to put in the FocusLost constructor
		FocusLostEvent fe = new FocusLostEvent();
		listener.onEvent(fe);
	}

	
	
}
