package mrl.item;

import java.util.ArrayList;
import java.util.Hashtable;

public class ItemFactory {
	public static ItemFactory thus = new ItemFactory();
	private Hashtable<String, WeaponType> weaponDefs = new Hashtable<String, WeaponType>();
	private Hashtable<String, AmmoType> ammoDefs = new Hashtable<String, AmmoType>();
	
	public Weapon createWeapon(String weaponTypeId) {
		WeaponType type = weaponDefs.get(weaponTypeId);
		if (type != null)
			return new Weapon(type);
		else
			return null;
	}
	
	public Ammo createAmmo(String ammoTypeId){
		AmmoType type = ammoDefs.get(ammoTypeId);
		if (type != null)		
			return new Ammo(type);
		else
			return null;
	}
	
	public AmmoType getAmmoType(String ammoTypeId){
		return ammoDefs.get(ammoTypeId);
	}
	
	public Item createItem(String itemId){
		Item ret = createWeapon(itemId);
		if (ret == null)
			ret = createAmmo(itemId);
		return ret;
	}

	public void setAmmoDefs(ArrayList<AmmoType> ammoDefsA) {
		for (AmmoType type : ammoDefsA) {
			ammoDefs.put(type.getID(), type);
		}
	}

	public void setWeaponDefs(ArrayList<WeaponType> weaponDefsA) {
		for (WeaponType type : weaponDefsA) {
			weaponDefs.put(type.getID(), type);
		}
	}
	
	
	
}
