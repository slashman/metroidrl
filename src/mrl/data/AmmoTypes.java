package mrl.data;

import java.util.ArrayList;

import mrl.item.AmmoType;
import mrl.item.Item;

public class AmmoTypes {
	public static ArrayList<AmmoType> getAmmoTypes(){
		ArrayList<AmmoType> ret = new ArrayList<AmmoType>();
		AmmoType temp = null;
		
		temp = new AmmoType();
		temp.setID("MAGNALITH");
		temp.setAppearanceId("MAGNALITH_CRYSTALS");
		temp.setName("Magnalith Crystals");
		temp.setPackSize(5);
		ret.add(temp);
		
		return ret;
	}
}
