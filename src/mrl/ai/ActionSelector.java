package mrl.ai;

import mrl.action.Action;
import mrl.actor.Actor;

public interface ActionSelector extends Cloneable, java.io.Serializable{
	public Action selectAction(Actor who);
	public String getID();

	public ActionSelector derive();

}