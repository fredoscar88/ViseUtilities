package com.visellico.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.farr.Events.Event;
import com.farr.Events.EventListener;
import com.farr.Events.types.MouseMovedEvent;
import com.farr.Events.types.MousePressedEvent;
import com.farr.Events.types.MouseReleasedEvent;

public class Mouse implements MouseListener, MouseMotionListener, MouseWheelListener {

	private static int mouseX = -1;
	private static int mouseY = -1;
	private static int mouseB = -1;	//button
	
	private EventListener eventListener;
	
	public Mouse(EventListener listener) {
		eventListener = listener;	//Private listener that references the events in Game
	}
	
	public static int getX() {
		return mouseX;
	}
	
	public static int getY() {
		return mouseY;
	}
	
	public static int getButton() {
		return mouseB;
	}

	public void mousePressed(MouseEvent e) {
		mouseB = e.getButton();
		Event event = new MousePressedEvent(e.getButton(), e.getX(), e.getY());
		eventListener.onEvent(event);
	}

	public void mouseReleased(MouseEvent e) {
		mouseB = e.getButton();
		Event event = new MouseReleasedEvent(e.getButton(), e.getX(), e.getY());
		eventListener.onEvent(event);
	}

	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		Event event = new MouseMovedEvent(e.getX(), e.getY(), true);
		eventListener.onEvent(event);
	}

	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		Event event = new MouseMovedEvent(e.getX(), e.getY(), false);
		eventListener.onEvent(event);
	}
	
	/**
	 * Unused
	 */
	public void mouseClicked(MouseEvent e) {
		
	}

	/**
	 * Unused
	 */
	public void mouseEntered(MouseEvent e) {
	}
	
	/**
	 * Unused
	 */
	public void mouseExited(MouseEvent e) {
		
	}

	/**
	 * Unused?
	 */
	public void mouseWheelMoved(MouseWheelEvent e) {
//		System.out.print(e.getWheelRotation());
	}

}
