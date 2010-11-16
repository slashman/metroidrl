package mrl.item;

import java.io.Serializable;

import mrl.ui.Appearance;
import mrl.ui.AppearanceFactory;

public class AmmoType implements Serializable{
	private String ID;
	private String name;
	private int packSize;
	private Appearance appearance;
	private String appearanceId;
	
	public String getID() {
		return ID;
	}
	public void setID(String id) {
		ID = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPackSize() {
		return packSize;
	}
	public void setPackSize(int packSize) {
		this.packSize = packSize;
	}
	public Appearance getAppearance() {
		if (appearance == null){
			appearance = AppearanceFactory.getAppearanceFactory().getAppearance(appearanceId);
		}
		return appearance;
	}
	public void setAppearance(Appearance appearance) {
		this.appearance = appearance;
	}
	public String getAppearanceId() {
		return appearanceId;
	}
	public void setAppearanceId(String appearanceId) {
		this.appearanceId = appearanceId;
	}
}
