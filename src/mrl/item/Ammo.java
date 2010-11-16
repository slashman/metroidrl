package mrl.item;

import mrl.ui.Appearance;

public class Ammo extends Item{
	private AmmoType ammoType;
	private int current;
	
	public void reload(Weapon weapon){
		if (!weapon.t().getAmmoType().equals(ammoType))
			return;
		int maxReload = weapon.t().getMagazineSize() - weapon.getLoadedAmmo();
		if (current > maxReload){
			current -= maxReload;
			weapon.addLoadedAmmo(maxReload);
		} else {
			current = 0;
			weapon.addLoadedAmmo(current);
		}
	}

	public Ammo(AmmoType ammoType){
		this.ammoType = ammoType;
		current = ammoType.getPackSize();
	}
	
	public Appearance getAppearance() {
		return t().getAppearance();
	}
	
	public AmmoType t(){
		return ammoType;
	}
	
	@Override
	public String getID() {
		return ammoType.getID()+hashCode();
	}
	
	public String getDescription() {
		return current +" of " + ammoType.getName();
	}
	
	@Override
	public String getAttributesDescription() {
		return getDescription();
	}
	
	@Override
	public String getFullID() {
		return getID();
	}
	

}
