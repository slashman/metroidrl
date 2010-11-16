package mrl.action;

import mrl.actor.Actor;
import mrl.player.MPlayer;

public class AutoFire extends Fire{
	
	public String getID() {
		return "AUTOFIRE";
	}
	public boolean needsPosition(){
		return false;
	}
	
	public int getCost(){
		return (int)Math.round(player.getAttackCost() / 2.0D);
	}
	
	public void execute() {
		targetPosition = player.getLockedMonster().getPosition();
		super.execute();
	}
	public boolean canPerform(Actor a){
        player = null;
		try {
			player = (MPlayer) a;
		} catch (ClassCastException cce){
			return false;
		}
		if (player.getLockedMonster() == null){
			invalidationMessage = "You don't have a locked monster.";
			return false;
		}
		return true;
	}
}
