package mrl.action.monster;

import mrl.action.Action;
import mrl.ai.BasicMonsterAI;
import mrl.feature.SmartFeature;
import mrl.game.SFXManager;
import mrl.level.Cell;
import mrl.level.MLevel;
import mrl.monster.Monster;
import sz.util.Debug;
import sz.util.Position;

public class MonsterWalk extends Action{
	public String getID(){
		return "MonsterWalk";
	}
	
	public boolean needsDirection(){
		return true;
	}

	public void execute(){
		Debug.doAssert(performer instanceof Monster, "The player tried to MonsterWalk...");
		Monster aMonster = (Monster) performer;
        Position var = directionToVariation(targetDirection);
        Position destinationPoint = Position.add(performer.getPosition(), var);
        MLevel aLevel = performer.getLevel();
        if (!aLevel.isValidCoordinate(destinationPoint))
        	return;
        Cell destinationCell = aLevel.getMapCell(destinationPoint);
        Cell currentCell = aLevel.getMapCell(performer.getPosition());
        Monster destinationMonster = aLevel.getMonsterAt(destinationPoint);

        SmartFeature standing = aLevel.getSmartFeature(performer.getPosition());
        if (standing != null)
        	if (standing.getEffectOnStep() != null){
		        String[] effects = standing.getEffectOnStep().split(" ");
		        if (effects[0].equals("TRAP") && aMonster != aLevel.getBoss()){
			        aLevel.addMessage("The "+aMonster.getDescription()+" is trapped!");
		        	return;
		        }
		    }

        if (destinationCell == null || destinationCell.isSolid())
        	if (aMonster.getSelector() instanceof BasicMonsterAI)
        		if (((BasicMonsterAI)aMonster.getSelector()).getPatrolRange() > 0)
        			((BasicMonsterAI)aMonster.getSelector()).setChangeDirection(true);
        if (destinationCell != null){
        	
			if (aMonster.isEthereal() || !destinationCell.isSolid())
				if (destinationMonster == null)
					if (aMonster.isEthereal() || currentCell == null || destinationCell.getHeight() <= currentCell.getHeight()+aMonster.getLeaping() +1)
						if (destinationCell.isWater())
							if (aMonster.canSwim() || aMonster.isEthereal())
								performer.setPosition(destinationPoint);
							else
								;
						else
							if (aLevel.getPlayer().getPosition().equals(destinationPoint)){
								if (aMonster.getWavOnHit() != null)
									SFXManager.play(aMonster.getWavOnHit());
								aLevel.getPlayer().damage(aMonster, aMonster.getAttack());
								if (aLevel.getPlayer().getPosition().equals(destinationPoint)){
								//The player wasnt bounced back,
									
									aLevel.addMessage("You are hit by the "+aMonster.getDescription()+"!");
								} else {
									aLevel.addMessage("You are bounced back by the "+aMonster.getDescription()+"!");
									performer.setPosition(destinationPoint);
								}

							} else {
								performer.setPosition(destinationPoint);
								if (aLevel.getSmartFeature(destinationPoint) != null){
									SmartFeature sf = aLevel.getSmartFeature(destinationPoint);
									if (sf.getDamageOnStep() > 0) {
										aLevel.addMessage("The "+aMonster.getDescription()+" is injured by the " + sf.getDescription());
										aMonster.damage(sf.getDamageOnStep());
									}
								}
							}
        }

	}

	public int getCost(){
		Monster m = (Monster) performer;
		if (m.getWalkCost() == 0){
			Debug.say("quickie monster");
			return 10;
		}
		return m.getWalkCost();
	}
}