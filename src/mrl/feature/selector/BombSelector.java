package mrl.feature.selector;

import mrl.action.Action;
import mrl.actor.Actor;
import mrl.ai.ActionSelector;
import mrl.feature.SmartFeature;
import mrl.feature.action.BombBlast;

public class BombSelector implements ActionSelector, Cloneable{
	private int turnsToBlast; 
	private boolean activated;
	
	public String getID(){
	     return "BOMB_SELECTOR";
	}

	public Action selectAction(Actor who){
		if (activated){
			turnsToBlast--;
			if (turnsToBlast == 0) {
				Action ret = new BombBlast();
				ret.setPosition(who.getPosition());
				who.die();
				who.getLevel().removeSmartFeature((SmartFeature)who);
				activated = false;
				return ret;
			} else {
				return null;
			}
		} else {
			turnsToBlast = 2;
			activated = true;
			return null;
		}
 	}

 	public ActionSelector derive(){
 		try {
	 		return (ActionSelector) clone();
	 	} catch (CloneNotSupportedException cnse){
			return null;
	 	}
 	}

}