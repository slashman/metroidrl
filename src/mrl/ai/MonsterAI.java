package mrl.ai;

import java.util.Vector;

import mrl.action.Action;
import mrl.actor.Actor;

import sz.util.Debug;

public abstract class MonsterAI  implements ActionSelector, Cloneable{
	protected Vector rangedAttacks;
	
	public void setRangedAttacks(Vector pRangedAttacks){
   	 rangedAttacks = pRangedAttacks;
    }
	
	public abstract Action selectAction(Actor who);
	public abstract String getID();

	public ActionSelector derive(){
		try {
			return (ActionSelector) clone();
		} catch (Exception e) {
			Debug.byebye("Failed to clone MonsterAI "+getID());
			return null;
		}
	}
}
