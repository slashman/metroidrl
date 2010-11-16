package mrl.action.monster;

import mrl.action.Action;
import mrl.game.SFXManager;
import mrl.level.Cell;
import mrl.level.MLevel;
import mrl.monster.Monster;
import mrl.player.MPlayer;
import sz.util.Debug;
import sz.util.Position;

public class MonsterCharge extends Action{
	private int range;
	private String message;
	private String effectWav;

	private int damage;
	public String getID(){
		return "MONSTER_CHARGE";
	}
	
	public boolean needsDirection(){
		return true;
	}

	public void set(int pRange, String pMessage, int pDamage, String pEffectWav){
		message = pMessage;
		range = pRange;
		damage = pDamage;
		effectWav = pEffectWav;
	}
	
	public void execute(){
		Debug.doAssert(performer instanceof Monster, "Someone not a monster tried to JumpOver");
		Monster aMonster = (Monster) performer;
		targetDirection = aMonster.starePlayer();
        Position var = directionToVariation(targetDirection);
        MLevel aLevel = performer.getLevel();
        MPlayer aPlayer = aLevel.getPlayer();
        //aLevel.addMessage("The "+aMonster.getDescription()+" "+message+".");
        aLevel.addMessage("The "+aMonster.getDescription()+" "+message+".");
        Cell currentCell = aLevel.getMapCell(performer.getPosition());
        Position destinationPoint = null;
        if (effectWav != null)
        	SFXManager.play(effectWav);
        for (int i=0; i<range; i++){
			destinationPoint = Position.add(aMonster.getPosition(), var);
			Position deepPoint  = aLevel.getDeepPosition(destinationPoint);
			if (deepPoint == null){
				aLevel.addMessage("The "+aMonster.getDescription()+ " falls into an endless pit! at "+destinationPoint);
				performer.die();
				performer.getLevel().removeMonster(aMonster);
				break;
			}
			Cell destinationCell = aLevel.getMapCell(deepPoint);
			if (!aMonster.isEthereal() &&
					( destinationCell.isSolid() || destinationCell.getHeight() > currentCell.getHeight()+aMonster.getLeaping() +1
					)
				) {
				aLevel.addMessage("The "+aMonster.getDescription()+ " bumps into the "+destinationCell.getShortDescription());
				break;
			}

			if (!aMonster.isEthereal() && !aMonster.canSwim() && destinationCell.isWater()){
				aLevel.addMessage("The "+aMonster.getDescription()+ " falls into the "+ destinationCell.getShortDescription() + "!");
				performer.die();
				performer.getLevel().removeMonster(aMonster);
				break;
			}
			if (aPlayer.getPosition().equals(destinationPoint)){
				if (damage == 0)
					aPlayer.damage(aMonster, aMonster.getAttack());
				else
					aPlayer.damage(aMonster, damage);
				
			}
			aMonster.setPosition(destinationPoint);
		}
		
	}

	public String getPromptDirection(){
		return "Where do you want to whip?";
	}

	public int getCost(){
		Monster m = (Monster) performer;
		if (m.getAttackCost() == 0){
			Debug.say("quickie monster");
			return 10;
		}
		return m.getAttackCost();
	}
	
}