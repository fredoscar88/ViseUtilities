package com.visellico.graphics.ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.farr.Events.types.MousePressedEvent;
import com.farr.Events.types.MouseScrollEvent;
import com.visellico.util.CoordinatePair;
import com.visellico.util.MathUtils;
import com.visellico.util.Vector2i;

public class UIScrollList extends UIComponent {

	private List<CoordinatePair<UIComponent>> componentList;
	
	private Vector2i spaceTotal;
	private Vector2i spaceVisible;
	Rectangle region;// = new Rectangle(position.x, position.y, size.x, size.y);
	
	public static int defaultColor = 0x202020;
	
	private int defX;
	public int scrollY = 0;
	private int margin;
	
	public volatile String selectedItem = null;
	
	public UIScrollList(Vector2i pos, Vector2i size) {
		this(pos, size, 2);
	}
	
	public UIScrollList(Vector2i pos, Vector2i size, int margin) {
		super(pos, size);
		
		componentList = new ArrayList<CoordinatePair<UIComponent>>();
		this.margin = margin;
		defX = 2;
		
		spaceVisible = size;
		spaceTotal = new Vector2i(spaceVisible.x, getTotalHeight());
		region = new Rectangle(super.position.x, super.position.y, super.size.x, super.size.y);
		
		calculateCoords();
		
		setColor(defaultColor);
//		add(new UILabel(new Vector2i(0,0), "UI Scroll List", new Font("Times New Roman", Font.PLAIN, 10)).setYPaddingOffset(2));
		
	}
	
	public void add(UIComponent c) {
		componentList.add(new CoordinatePair<>(c, new Vector2i(0,0)));
		calculateCoords();
		c.position = new Vector2i(
				getAbsolutePosition().x + componentList.get(componentList.size() - 1).coords.x, 
				getAbsolutePosition().y + componentList.get(componentList.size() - 1).coords.y - scrollY);
	}
	
	public void clear() {
		componentList = new ArrayList<CoordinatePair<UIComponent>>();
	}
	
	public void updateAllPos() {
		
		CoordinatePair<UIComponent> c;
		
		for (int i = 0; i < componentList.size(); i++) {
			c = componentList.get(i);
			c.obj.position = new Vector2i(
					getAbsolutePosition().x + c.coords.x,
					getAbsolutePosition().y + c.coords.y - scrollY);
		}
		
//		for (CoordinatePair<UIComponent> d : componentList) {
//			d.obj.position = new Vector2i(
//					getAbsolutePosition().x + d.coords.x,
//					0);
//		}
	}
	
	public void remove(UIComponent c) {
		
	}
	
	public void remove(int index) {
		
	}
	
	public void calculateCoords() {
		
		int runningHeight = margin;
		CoordinatePair<UIComponent> cp;
		
		for (int i = 0; i < componentList.size(); i++) {
			
			cp = componentList.get(i);
			cp.coords.y = runningHeight;
			cp.coords.x = defX;
			
//			runningHeight = cp.coords.y + cp.obj.size.y;
			//this is equal to
			runningHeight += cp.obj.size.y + margin;
			//well, it should be at any rate.
//			System.out.println(timesCalled + ": " + runningHeight);
			
		}
		spaceTotal.y = runningHeight;
	}
	
	public void render(Graphics g) {
		
		g.setColor(color);
		g.fillRect(region.x, region.y, region.width, region.height);
		g.setClip(region);
		
//		g.fillRect(getAbsolutePosition().x, getAbsolutePosition().y, region.width, region.height);
		
		for (CoordinatePair<UIComponent> comp : componentList) {
			if (!componentIsVisible(comp)) continue;
			comp.obj.render(g);
		}
		
		
		g.setClip(null);
		
	}
	
	public void update() {
		//Forces scrollY to not try and display stuff outside of the total space
		scrollY = MathUtils.clamp(scrollY, 0, spaceTotal.y - spaceVisible.y);
		if (scrollY < 0) scrollY = 0;
		
		updateAllPos();
		
	}
	
	public int getTotalHeight() {
		int totalHeight;
		if (componentList.size() == 0) return 0;
//		CoordinatePair cp = new CoordinatePair<UIComponent>(new Object(), new Vector2i(0,0));
//		System.out.println(cp.coords.y);
		
		CoordinatePair<UIComponent> lastCoordPair = componentList.get(componentList.size() - 1);
		totalHeight = lastCoordPair.coords.y + lastCoordPair.obj.size.y;
		
//		for (CoordinatePair<UIComponent> comp : componentList) {
//			totalHeight += comp.coords.y;
//		}
		
		return totalHeight;
	}
	
	public boolean componentIsVisible(CoordinatePair<UIComponent> cp) {
		
		//Buttom cut of the component needs to be in frame, or top left part needs to be in frame
		return (cp.coords.y + cp.obj.size.y) > scrollY && cp.coords.y - scrollY < size.y;
		
	}
	
	public boolean onMouseScroll(MouseScrollEvent e) {
		
		if (region.contains(e.getX(), e.getY())) {
			scrollY += e.getRotation() * 10;
			return true;
		}
		return false;
	}
	
	public boolean onMousePress(MousePressedEvent e) {
		
		if (region.contains(e.getX(), e.getY())) {
			
			for (CoordinatePair<UIComponent> cp : componentList) {
				cp.obj.onMousePress(e);
			}
			
			return true;
		}
		
		return false;	
	}
	
	public int size() {
		
		return componentList.size();
	}
	
	public void init(UIPanel p) {
		region = new Rectangle(super.position.x + offset.x, super.position.y + offset.y, super.size.x, super.size.y);
		this.panel = p;
	}
	
}
