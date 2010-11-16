package mrl.item;

import sz.rpg.Roll;
import mrl.ui.Appearance;

public class Weapon extends Item{
	private WeaponType weaponType;
	private int loadedAmmo;
	
	public Weapon(WeaponType t){
		weaponType = t;
	}
	
	public String getName() {
		return weaponType.getName();
	}
	
	public WeaponType t(){
		return weaponType;
	}

	public int getLoadedAmmo() {
		return loadedAmmo;
	}

	public void setLoadedAmmo(int loadedAmmo) {
		this.loadedAmmo = loadedAmmo;
	}
	public void addLoadedAmmo(int q){
		loadedAmmo += q;
		if (loadedAmmo > t().getMagazineSize())
			loadedAmmo = t().getMagazineSize();
	}
	
	public Appearance getAppearance() {
		return t().getAppearance();
	}
	
	@Override
	public String getID() {
		return weaponType.getID()+hashCode();
	}
	
	@Override
	public String getAttributesDescription() {
		String base = getDescription() + " "+getDamage().getDescription();
		return base;
	}
	
	@Override
	public String getDescription() {
		return getName();
	}
	
	public Roll getDamage(){
		return weaponType.getDamage();
	}
	
	@Override
	public String getFullID(){
		String toAddID = weaponType.getID();
		return toAddID;
	}
}