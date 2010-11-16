package mrl.item;

import java.io.Serializable;

import mrl.ui.Appearance;
import sz.util.Position;

public abstract class Item implements Serializable{
	private Position position;
	private Appearance appearance;
	
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	public Appearance getAppearance() {
		return appearance;
	}
	public void setAppearance(Appearance appearance) {
		this.appearance = appearance;
	}
	
	public abstract String getID();
	
	public abstract String getDescription();
	
	public abstract String getAttributesDescription();
	
	public abstract String getFullID();
}
