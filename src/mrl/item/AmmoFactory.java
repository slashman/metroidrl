package mrl.item;

import java.util.ArrayList;
import java.util.Hashtable;

public class AmmoFactory {
	public static AmmoType getAmmoType(String id){
		return (AmmoType)definitions.get(id);
	}
	
	private static Hashtable definitions;
	
	public void initDefinitions(ArrayList defs){
		for (int i = 0; i < defs.size(); i++){
			AmmoType type = (AmmoType) defs.get(i);
			definitions.put(type.getID(), type);
		}
	}
	
	public static Ammo createAmmo(String id){
		Ammo ret = new Ammo((AmmoType)definitions.get(id));
		return ret;
	}
}
