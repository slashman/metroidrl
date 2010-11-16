package mrl.action;

import mrl.actor.Actor;
import mrl.level.MLevel;
import mrl.player.MPlayer;

public class SuperBomb extends Action{
	public String getID(){
		return "SUPERBOMB";
	}
	
	public void execute(){
        MLevel aLevel = performer.getLevel();
        MPlayer aPlayer = (MPlayer) performer;
        if (aPlayer.getPowerBombs() > 0){
	        aLevel.addMessage("You drop a power bomb!");
	        aPlayer.setPowerBombs(aPlayer.getPowerBombs()-1);
			aLevel.addSmartFeature("SUPERBOMB", performer.getPosition());
        }
	}
	
	public int getCost(){
		MPlayer p = (MPlayer) performer;
		return p.getAttackCost();
	}
	
	public String getSFX(){
		return "wav/dropBomb.wav";
	}
	
	public boolean canPerform(Actor a){
		MPlayer player = (MPlayer) a;
		if (player.getPowerBombsCapacity() == 0){
			invalidationMessage = "[/suit.powerBomb {Suit Functionality Unavailable}]";
			return false;
		} else if (player.getPowerBombs() <= 0){
			invalidationMessage = "You are short of power bomb energy.";
			return false;
		}
		if (!player.isBallMorphed()){
			invalidationMessage = "You can only drop power bombs as a ball.";
			return false;
		}

		return true;
	}
}