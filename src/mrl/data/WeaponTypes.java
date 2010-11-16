package mrl.data;

import java.util.ArrayList;

import sz.rpg.Roll;

import mrl.item.AmmoType;
import mrl.item.WeaponType;

public class WeaponTypes {
	public static ArrayList<WeaponType> getWeaponTypes(){
		ArrayList<WeaponType> ret = new ArrayList<WeaponType>();
		WeaponType temp = null;
		
		temp = new WeaponType();
		temp.setAmmoTypeId("MAGNALITH");
		temp.setDamage(new Roll(1,5));
		temp.setMagazineSize(10);
		temp.setName("Energy Pellet");
		temp.setAppearanceId("ENERGY_PELLET");
		temp.setID("ENERGY_PELLET");
		ret.add(temp);
		
		return ret;
	}
}
