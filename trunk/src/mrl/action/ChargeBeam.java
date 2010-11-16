package mrl.action;

import mrl.actor.Actor;
import mrl.level.MLevel;
import mrl.player.MPlayer;

public class ChargeBeam extends Action{
	public String getID(){
		return "CHARGE";
	}
	
	public void execute(){
        MLevel aLevel = performer.getLevel();
        MPlayer aPlayer = (MPlayer) performer;
        aLevel.addMessage("You charge your power shot!");
        aPlayer.initBeamCharge();
	}
	
	public int getCost(){
		MPlayer p = (MPlayer) performer;
		return p.getAttackCost();
	}
	
	public String getSFX(){
		return "wav/charge.wav";
	}
	
	public boolean canPerform(Actor a){
		MPlayer player = (MPlayer) a;
		if (!player.isChargeBeamActivated()){
			invalidationMessage = "[/beam.chargeBeam {Suit Functionality Unavailable}]";
			return false;
		} 
		if (player.isChargingBeam()){
			invalidationMessage = "You are already charging your beam.";
			return false;
		}
		if (player.isChargedBeam()){
			invalidationMessage = "Your beam is already charged!";
			return false;
		}
		
		if (player.isBallMorphed()){
			invalidationMessage = "You cannot charge as a ball.";
			return false;
		}
		return true;
	}
}