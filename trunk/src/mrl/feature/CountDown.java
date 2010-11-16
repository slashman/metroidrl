package mrl.feature;

import mrl.action.*;
import mrl.actor.*;
import mrl.ai.*;

public class CountDown implements ActionSelector, Cloneable{
	private int turnsToDie;

	public void setTurns(int turns){
		turnsToDie = turns;
	}

	public String getID(){
	     return "COUNTDOWN";
	}

	public Action selectAction(Actor who){
		//Debug.say("cpuntdown " + turnsToDie);
		turnsToDie--;
		if (turnsToDie == 0){
			who.die();
			who.getLevel().removeSmartFeature((SmartFeature)who);
		}
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