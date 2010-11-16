package mrl.action;

import mrl.actor.Actor;
import mrl.feature.Feature;
import mrl.game.CRLException;
import mrl.level.Cell;
import mrl.level.MLevel;
import mrl.level.MapCellFactory;
import mrl.monster.Monster;
import mrl.player.MPlayer;
import mrl.ui.effects.Effect;
import mrl.ui.effects.EffectFactory;
import sz.util.Line;
import sz.util.Position;

public class FireSuperMissile  extends Action{
	private MPlayer player;
	
	public String getID(){
		return "FireSuperMissile";
	}
	
	public boolean needsPosition(){
		return true;
	}

	public void execute(){
        player = null;
		try {
			player = (MPlayer) performer;
		} catch (ClassCastException cce){
			return;
		}
		
		MLevel aLevel = performer.getLevel();
		
		
		if (targetPosition.equals(performer.getPosition())){
			aLevel.addMessage("You shot you super missile upside");
			return;
        }

		if (player.isChargingBeam()){
			player.dischargeBeam();
		}
		
		player.setSuperMissiles(player.getSuperMissiles()-1);
		
		int i = 0;
		Line path = new Line(performer.getPosition(), targetPosition);
		for (i=0; i<20; i++){
			Position destinationPoint = path.next();
			Cell destinationCell = aLevel.getMapCell(destinationPoint);
        	Feature destinationFeature = aLevel.getFeatureAt(destinationPoint);
 			if (destinationFeature != null){
 				if (destinationFeature.isSolid())
 					break;
 			}
			Monster targetMonster = performer.getLevel().getMonsterAt(destinationPoint);
			if (
				targetMonster != null &&
				!(targetMonster.isInWater() && targetMonster.canSwim()) &&
				destinationCell.getHeight() == player.getHeight() 
			)
				break;
		}
		Effect me = EffectFactory.getSingleton().createDirectedEffect(performer.getPosition(), targetPosition, "SUPERMISSILE", i);
		aLevel.addEffect(me);
		
				
		boolean hitsSomebody = false;
		path = new Line(performer.getPosition(), targetPosition);
		for (i=0; i<20; i++){
			Position destinationPoint = path.next();
        	Cell destinationCell = aLevel.getMapCell(destinationPoint);
        	/*aLevel.addMessage("You hit the "+destinationCell.getID());*/

			String message = "";
        	Feature destinationFeature = aLevel.getFeatureAt(destinationPoint);
        	if (destinationFeature != null){
        		hitsSomebody = true;
 				if (destinationFeature.isSolid())
 					break;
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
				int attack = player.getSuperMissileAttack();
				attack -= penalty;
				if (attack < 1)
					attack = 1;
				message = "Your super missile hits the "+targetMonster.getDescription();
				if (targetMonster.wasSeen())
					aLevel.addMessage(message);
				targetMonster.damageWithWeapon(attack);
			}
			
			Cell targetMapCell = aLevel.getMapCell(destinationPoint);
			if (targetMapCell != null && targetMapCell.getID().equals("SUPERMISSILE_DOOR")){
				//aLevel.addMessage("The wall breaks apart!");
				try {
					performer.getLevel().getCells()[destinationPoint.z][destinationPoint.x][destinationPoint.y] = MapCellFactory.getMapCellFactory().getMapCell("METALLIC_FLOOR");
				} catch (CRLException crle){
					
				}
			}
			if (targetMapCell != null && targetMapCell.isSolid()){
				//aLevel.addMessage("You hit the "+targetMapCell.getShortDescription());
				hitsSomebody = true;
         	}
			
			if (hitsSomebody)
				break;
		}
		
	}

	public String getPromptPosition(){
		return "Aim your supermissile.";
	}

	public Position getPosition(){
		return targetPosition;
	}


	public String getSFX(){
		return "wav/superMissile.wav";
	}

	public int getCost(){
		return player.getAttackCost();
	}
	
	public boolean canPerform(Actor a){
        player = null;
		try {
			player = (MPlayer) a;
		} catch (ClassCastException cce){
			return false;
		}
		if (player.getSuperMissilesCapacity() == 0){
			invalidationMessage = "[/missile.energySuperMissile {Suit Functionality Unavailable}]";
			return false;
		}
		if (player.getSuperMissiles() <1){
			invalidationMessage = "You are short of super missile energy.";
			return false;
		}
		if (player.isBallMorphed()){
			invalidationMessage = "You cannot fire as a ball.";
			return false;
		}
		return true;
	}
}