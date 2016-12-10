package com.visellico.graphics.ui;

import com.farr.Events.types.KeyTypedEvent;

//Focusable interface that controls which UI element receives certain events (mainly key press events)
//TODO Focusables should lose focus whenever a mouse press is registered. Focus robs all sorts of events from other layers so we have to be sure we can get rid of it.
//	I.E if you're focused into a text field, you cant move. Hopefully that only happens in another UI layer.
/**
 * Interface designed to manage which UIComponents receive KeyEvents (press and release), by distributing events only to the element that implements this interface and is focused.
 * In implementing this interface it is important to not let more than one element have focus (unless that is desirable behavior);
 * @author Henry
 *
 */
interface UIFocusable {

	public boolean getFocused();
	
	public void removeFocus();
	
	public void giveFocus();
	
//	public boolean onKeyPress(KeyPressedEvent e);
	
//	public boolean onKeyRelease(KeyReleasedEvent e);
	
	public boolean onKeyType(KeyTypedEvent e);
	
}
