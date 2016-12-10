package com.visellico.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.farr.Events.Event;
import com.farr.Events.EventListener;
import com.farr.Events.types.KeyPressedEvent;
import com.farr.Events.types.KeyReleasedEvent;
import com.farr.Events.types.KeyTypedEvent;

public class Keyboard implements KeyListener {

	//May remove this. Plus it MIGHT need to be static.
	public static boolean[] keys = new boolean[120];	//good all around number I guess
	
	EventListener eventListener;
	
	public Keyboard(EventListener listener) {
		eventListener = listener;
	}
	
	
	public boolean[] getKeysPressed() {
		boolean[] result = new boolean[keys.length];
		for (int i = 0; i < keys.length; i++) {
			result[i] = keys[i];
		}
		return result;
	}
	
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
		Event event = new KeyPressedEvent(e.getKeyCode());
		eventListener.onEvent(event);
	}

	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
		Event event = new KeyReleasedEvent(e.getKeyCode());
		eventListener.onEvent(event);
	}

	public void keyTyped(KeyEvent e) {
//		keys[e.getKeyCode()] = false;
		Event event = new KeyTypedEvent(e.getKeyChar());
		eventListener.onEvent(event);
	}

}
