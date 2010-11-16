package mrl.action;

import mrl.actor.Actor;
import mrl.feature.Feature;
import mrl.level.Cell;
import mrl.level.MLevel;
import mrl.monster.Monster;
import mrl.player.MPlayer;
import sz.util.Debug;
import sz.util.Position;
import sz.util.Util;

public class Walk extends Action{
	private MPlayer aPlayer;
	
	public String getID(){
		return "Walk";
	}
	
	public boolean needsDirection(){
		return true;
	}

	public void execute(){
		Debug.doAssert(performer instanceof MPlayer, "An actor different from the player tried to execute Walk action");
		Debug.enterMethod(this, "execute");
		aPlayer = (MPlayer) performer;
		if (targetDirection == Action.SELF){
			aPlayer.getLevel().addMessage("You walk around yourself");
			return;
		}
        Position var = directionToVariation(targetDirection);
        Position destinationPoint = Position.add(performer.getPosition(), var);
        MLevel aLevel = performer.getLevel();
        Cell destinationCell = aLevel.getMapCell(destinationPoint);
        Feature destinationFeature = aLevel.getFeatureAt(destinationPoint);
        Cell currentCell = aLevel.getMapCell(performer.getPosition());

        if (destinationCell == null){
        	if (!aLevel.isValidCoordinate(destinationPoint)){
        		aPlayer.land();
        		Debug.exitMethod();
            	return;
        	}
        	
        	destinationPoint = aLevel.getDeepPosition(destinationPoint);
        	if (destinationPoint == null){
        		aPlayer.land();
        		Debug.exitMethod();
            	return;
        	} else {
        		aLevel.addMessage("You fall!");
        		destinationCell = aLevel.getMapCell(destinationPoint);
        		currentCell = aLevel.getMapCell(destinationPoint);
        	}
        }

       	if (destinationCell.getHeight() > aPlayer.getHeight() && !(aPlayer.isBallMorphed() && aPlayer.isHasSpiderBall()))
       		aLevel.addMessage("You bump into the "+destinationCell.getShortDescription());
		else {
			if (destinationCell.isSolid() || (destinationCell.getID().equals("METALLIC_HOLE") && !aPlayer.isBallMorphed())) //Test pending
				aLevel.addMessage("You bump into the "+destinationCell.getShortDescription());
			else
				if (destinationFeature != null && destinationFeature.isSolid())
					aLevel.addMessage("You bump into the "+destinationFeature.getDescription());
				else
					if (!aLevel.isWalkable(destinationPoint))
						aLevel.addMessage("Your way is blocked");
					else {
						Actor aActor = aLevel.getActorAt(destinationPoint);
						if (aActor != null){
							if (aActor instanceof Monster){
								Monster aMonster = (Monster) aActor;
								if (aPlayer.isScewAttacking()){
									aMonster.damage(aPlayer.getScrewAttackDamage());
									aLevel.addMessage("You break through the "+aMonster.getDescription()+"!");
								} else {
									aPlayer.damage(aMonster, aMonster.getAttack());
									aLevel.addMessage("You bump with the "+aMonster.getDescription()+"!");
								}
							}
						}
						
						if (aLevel.getBloodAt(aPlayer.getPosition()) != null)
							if (Util.chance(30))
								aLevel.addBlood(destinationPoint, Util.rand(0,1));
						if (aPlayer.isJumping()){
							aPlayer.setPosition(destinationPoint);
						} else {
							aPlayer.landOn(destinationPoint);
						}
					}
		}
       	Debug.exitMethod();
	}

	public int getCost(){
		return aPlayer.getWalkCost();
	}
}