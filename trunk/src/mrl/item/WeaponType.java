package mrl.item;

import mrl.ui.Appearance;
import mrl.ui.AppearanceFactory;
import sz.rpg.Roll;

public class WeaponType {
	private Roll damage;
	private int burst;
	private int speed;
	private int blast;
	private String name;
	private String ammoTypeId;
	private AmmoType ammoType;
	private int magazineSize;
	private Appearance appearance;
	private String appearanceId;
	private String ID;
	
	public int getBlast() {
		return blast;
	}
	public void setBlast(int blast) {
		this.blast = blast;
	}
	public int getBurst() {
		return burst;
	}
	public void setBurst(int burst) {
		this.burst = burst;
	}
	public Roll getDamage() {
		return damage;
	}
	public void setDamage(Roll damage) {
		this.damage = damage;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public AmmoType getAmmoType() {
		if (ammoType == null){
			ammoType = ItemFactory.thus.getAmmoType(ammoTypeId);
		}
		return ammoType;
	}
	public void setAmmoType(AmmoType ammoType) {
		this.ammoType = ammoType;
	}
	public int getMagazineSize() {
		return magazineSize;
	}
	public void setMagazineSize(int magazineSize) {
		this.magazineSize = magazineSize;
	}
	public String getAmmoTypeId() {
		return ammoTypeId;
	}
	public void setAmmoTypeId(String ammoTypeId) {
		this.ammoTypeId = ammoTypeId;
	}
	public Appearance getAppearance() {
		if (appearance == null)
			appearance = AppearanceFactory.getAppearanceFactory().getAppearance(appearanceId);
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
	public String getID() {
		return ID;
	}
	public void setID(String id) {
		ID = id;
	}
	
	
}
