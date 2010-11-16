package mrl.level;

import mrl.*;
import mrl.action.*;
import mrl.actor.*;
import mrl.ai.*;
import sz.util.*;

public class RespawnAI implements ActionSelector{
	private int counter;

	public String getID(){
		return "Respawn";
	}

	public Action selectAction(Actor who) {
		Debug.enterMethod(this, "selectAction", who);
		Respawner x = (Respawner) who;
		counter++;
		if (Util.chance(5)) {
			Action ret = SFX.getThunder();
			Debug.exitMethod(ret);
			return ret;
		}
		if (x.getFreq() < counter){
			counter = 0;
			//Debug.say("Lets spaen!@");
			Action ret = SpawnMonster.getAction();
			Debug.exitMethod(ret);
			return ret;
    	}
    	Debug.exitMethod("null");
	 	return null;
	}

	public ActionSelector derive(){
 		try {
	 		return (ActionSelector) clone();
	 	} catch (CloneNotSupportedException cnse){
			return null;
	 	}
 	}
}