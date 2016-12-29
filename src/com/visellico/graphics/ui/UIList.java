package com.visellico.graphics.ui;

import java.util.ArrayList;
import java.util.List;

import com.visellico.util.Vector2i;

public class UIList extends UIComponent {

	List<? extends UIComponent> listItems = new ArrayList<>();
	
	public UIList(Vector2i position) {
		super(position);
		
	}
	
	public UIList(Vector2i position, Vector2i size) {
		super(position, size);
		
	}
	

}
