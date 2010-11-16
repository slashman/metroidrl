package mrl.action;

import java.awt.dnd.Autoscroll;

import mrl.actor.Actor;
import mrl.feature.Feature;
import mrl.level.Cell;
import mrl.level.MLevel;
import mrl.monster.Monster;
import mrl.player.MPlayer;
import mrl.ui.effects.Effect;
import mrl.ui.effects.EffectFactory;
import sz.util.Line;
import sz.util.Position;

public class Fire extends Action{
	protected MPlayer player;
	
	public String getID(){
		return "Fire";
	}
	
	public boolean needsPosition(){
		return true;
	}

	private Position resolvePositionFromDirection(){
		return Position.add(player.getPosition(), Action.directionToVariation(targetDirection));
	}
	
	public void execute(){
        player = null;
		try {
			player = (MPlayer) performer;
		} catch (ClassCastException cce){
			return;
		}
		
		MLevel aLevel = performer.getLevel();
		
		if (targetPosition == null)
			targetPosition = resolvePositionFromDirection();
		
		if (targetPosition.equals(performer.getPosition())){
			aLevel.addMessage("You shot upside");
			return;	
        }

		if (player.isChargingBeam()){
			aLevel.addMessage("You are still charging your beam");
			return;
		}
		
		String beamType = player.getBeamType();
		
		if (!player.isPlasmaBeamActivated()){
			int i = 0;
			Line path = new Line(performer.getPosition(), targetPosition);
			for (i=0; i<20; i++){
				Position destinationPoint = path.next();
				if (!player.isWaveBeamActivated()){
					Cell destinationCell = aLevel.getMapCell(destinationPoint);
					if (destinationCell == null || destinationCell.isSolid())
						break;
		        	Feature destinationFeature = aLevel.getFeatureAt(destinationPoint);
	   	 			if (destinationFeature != null && destinationFeature.isDestroyable())
	   	 				break;
				}
				Cell destinationCell = aLevel.getMapCell(destinationPoint);
				if (destinationCell == null || destinationCell.isSolid())
					break;
				Monster targetMonster = performer.getLevel().getMonsterAt(destinationPoint);
				if (
					targetMonster != null &&
					!(targetMonster.isInWater() && targetMonster.canSwim()) &&
					destinationCell.getHeight() == player.getHeight() 
				)
					break;
				
			}
			Effect me = EffectFactory.getSingleton().createDirectedEffect(performer.getPosition(), targetPosition, beamType, i);
			aLevel.addEffect(me);
		} else {
			Effect me = EffectFactory.getSingleton().createDirectedEffect(performer.getPosition(), targetPosition, beamType, 20);
			aLevel.addEffect(me);
		}
				
		boolean hitsSomebody = false;
		Line path = new Line(performer.getPosition(), targetPosition);
		for (int i=0; i<20; i++){
			Position destinationPoint = path.next();
        	Cell destinationCell = aLevel.getMapCell(destinationPoint);
        	/*aLevel.addMessage("You hit the "+destinationCell.getID());*/

			String message = "";
        	Feature destinationFeature = aLevel.getFeatureAt(destinationPoint);
        	if (destinationFeature != null && destinationFeature.isDestroyable()){
        		hitsSomebody = true;
	        	message = "You hit the "+destinationFeature.getDescription();
				/*Feature prize = destinationFeature.damage(player, player.getBeamAttack());
	        	if (prize != null){
		        	message += ", and destroy it!";
				}*/
				aLevel.addMessage(message);
			}

			Monster targetMonster = performer.getLevel().getMonsterAt(destinationPoint);
			message = "";

			if (
				targetMonster != null &&
				!(targetMonster.isInWater() && targetMonster.canSwim()) &&
				destinationCell.getHeight() == player.getHeight() 
			){
				hitsSomebody = true;
				//int penalty = (int)(Position.distance(targetMonster.getPosition(), player.getPosition())/4);
				int penalty = 0;
				int attack = player.getBeamAttack();
				attack -= penalty;
				if (attack < 1)
					attack = 1;
				message = "You hit the "+targetMonster.getDescription();
				if (targetMonster.wasSeen())
					aLevel.addMessage(message);
				targetMonster.damageWithWeapon(attack);
			}
			
			Cell targetMapCell = aLevel.getMapCell(destinationPoint);
			if (targetMapCell != null && targetMapCell.isSolid()){
				//aLevel.addMessage("You hit the "+targetMapCell.getShortDescription());
				hitsSomebody = true;
         	}
			
			if (hitsSomebody && !player.isWaveBeamActivated())
				break;
		}
/*		if (!hitsSomebody)
			aLevel.addMessage("You swing at the air!");*/
		if (player.isChargedBeam()){
			player.dischargeBeam();
		}
	}

	public String getPromptPosition(){
		return "Aim your beam.";
	}

	public Position getPosition(){
		return targetPosition;
	}


	public String getSFX(){
		return "wav/shoot.wav";
	}

	public int getCost(){
		if (player.isAutoFire())
			return (int)Math.round(player.getAttackCost() / 2.0D);
		else
			return player.getAttackCost();
	}
	
	public boolean canPerform(Actor a){
        player = null;
		try {
			player = (MPlayer) a;
		} catch (ClassCastException cce){
			return false;
		}
		if (player.isChargingBeam()){
			invalidationMessage = "Your beam is still charging.";
			return false;
		}
		if (player.isBallMorphed()){
			invalidationMessage = "You cannot fire as a ball.";
			return false;
		}
		return true;
	}
}