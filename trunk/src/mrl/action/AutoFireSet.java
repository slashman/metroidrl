package mrl.action;

import sz.util.Position;
import mrl.actor.Actor;
import mrl.level.MLevel;
import mrl.monster.Monster;
import mrl.player.MPlayer;

public class AutoFireSet extends Action{
	public String getID(){
		return "AUTOFIRE_SET";
	}
	
	public void execute(){
        MPlayer aPlayer = (MPlayer) performer;
        MLevel aLevel = aPlayer.getLevel();
        if (aPlayer.isAutoFire()){
        	aPlayer.setAutoFire(false);
        	aLevel.addMessage("Turn autofire off");
        	aPlayer.setLockedMonster(null);
        } else {
        	if (aPlayer.getLockedMonster() == null){
        		Monster m = aPlayer.getNearestMonsterOnSight();
            	if (m != null){
                	aPlayer.setLockedMonster(m);
            	} else {
                	aLevel.addMessage("No targets found for autofire");
                	return;
            	}
        	}
        	aPlayer.setAutoFire(true);
        	aLevel.addMessage("Turn autofire on");
        }
	}
	
	public int getCost(){
		return 0;
	}
	
	public boolean canPerform(Actor a){
		MPlayer aPlayer = (MPlayer) a;
		if (aPlayer.isChargingBeam()){
			invalidationMessage = "Your beam is still charging.";
			return false;
		}
		if (aPlayer.isBallMorphed()){
			invalidationMessage = "You cannot fire as a ball.";
			return false;
		}
		
        if (aPlayer.isAutoFire()){
        	return true;
        } else {
        	if (aPlayer.getLockedMonster() == null){
        		Monster m = aPlayer.getNearestMonsterOnSight();
            	if (m != null){
            		return true;
            	} else {
                	invalidationMessage = "No targets found for autofire";
                	return false;
            	}
        	}
        	return true;
        }
	}
	
	public String getSFX(){
		return "wav/dropBomb.wav";
	}
}