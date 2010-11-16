package mrl.action.monster;

import mrl.action.Action;
import mrl.game.SFXManager;
import mrl.level.Cell;
import mrl.level.MLevel;
import mrl.monster.Monster;
import mrl.player.MPlayer;
import mrl.ui.effects.EffectFactory;
import sz.util.Debug;
import sz.util.Line;
import sz.util.Position;

public class MonsterMissile extends Action{
	private int range;
	private String message;
	private String effectType;
	private String effectID;
	private String statusEffect;
	private String effectWav;
	private int damage;
	private String type;
	
	public String getEffectWav(){
		return effectWav;
	}
	public final static String TYPE_AXE = "AXE", TYPE_STRAIGHT = "STRAIGHT", TYPE_DIRECT = "DIRECT";
	
	/*public void set(String pStatusEffect, int pRange, String pMessage, String pEffectType, String pEffectID){
		range = pRange;
		statusEffect = pStatusEffect;
		message = pMessage;
		effectType = pEffectType;
		effectID = pEffectID;
		damage = 0;
	}*/
	
	public void set(String pType, String pStatusEffect, int pRange, String pMessage, String pEffectType, String pEffectID, int damage, String pEffectWav){
		type = pType;
		range = pRange;
		statusEffect = pStatusEffect;
		message = pMessage;
		effectType = pEffectType;
		effectID = pEffectID;
		effectWav =  pEffectWav;
		this.damage = damage;
	}

	
	public String getID(){
		return "MONSTER_MISSILE";
	}
	
	public boolean needsPosition(){
		return true;
	}

	public void execute(){
		Debug.doAssert(performer instanceof Monster, "Someone not a monster tried to throw a bone");
		Monster aMonster = (Monster) performer;
        MLevel aLevel = performer.getLevel();
        MPlayer aPlayer = aLevel.getPlayer();
        Cell playerCell = aLevel.getMapCell(aPlayer.getPosition());
        Cell monsterCell = aLevel.getMapCell(aMonster.getPosition());
        int targetDirection = solveDirection(performer.getPosition(), targetPosition);
        if (effectWav != null){
        	SFXManager.play(effectWav);
        }
        if (effectType.equals("beam")){
        	drawEffect(EffectFactory.getSingleton().createDirectedEffect(aMonster.getPosition(), targetPosition, effectID,range));        
        }else if (effectType.equals("melee")) {
        	drawEffect(EffectFactory.getSingleton().createDirectionalEffect(aMonster.getPosition(), targetDirection, range, effectID));
        }else if (effectType.equals("missile")){
        	drawEffect(EffectFactory.getSingleton().createDirectedEffect(aMonster.getPosition(), targetPosition, effectID, range));
        }else if (effectType.equals("directionalmissile")){
        	drawEffect(EffectFactory.getSingleton().createDirectedEffect(aMonster.getPosition(), targetPosition, effectID , range));
        }
        Line line = new Line(aMonster.getPosition(), targetPosition);
        boolean hits = false;
        for (int i=0; i<range; i++){
			Position destinationPoint = line.next();
			if (aPlayer.getPosition().equals(destinationPoint)){
				int penalty = (int)(Position.distance(aMonster.getPosition(), aPlayer.getPosition())/4);
				int attack = aMonster.getAttack();
				attack -= penalty;
				if (attack < 1)
					attack = 1;
				if (type.equals(TYPE_DIRECT)){
					hits = true;
				} else if (type.equals(TYPE_STRAIGHT)){
					if (monsterCell.getHeight() == playerCell.getHeight()){
						hits = true;
					} else {
						hits = false;
					}
				} else if (type.equals(TYPE_AXE)){
					if (i > (int)(range/4) && i < (int)(3*(range/4))){
						if (playerCell.getHeight() == monsterCell.getHeight()+2 || playerCell.getHeight() == monsterCell.getHeight()+3){
							hits = true;
						} else {
							hits = false;
						}
					} else {
						if (playerCell.getHeight() == monsterCell.getHeight()){
							hits = true;
						} else {
							hits = false;
						}
					}
				}
				if (hits){
					//aLevel.addBlood(destinationPoint, 1);
					if (damage == 0)
						aPlayer.damage(aMonster, aMonster.getAttack());
					else
						aPlayer.damage(aMonster, damage);
					
				}
				break;
			}
		}
		aLevel.addMessage("The "+aMonster.getDescription()+" "+message+".");
	}

	public String getPromptPosition(){
		return "";
	}
	
	private int solveDirection(Position old, Position newP){
		if (newP.x() == old.x()){
			if (newP.y() > old.y()){
				return Action.DOWN;
			} else {
                 return Action.UP;
			}
		} else
		if (newP.y() == old.y()){
			if (newP.x() > old.x()){
				return Action.RIGHT;
			} else {
				return Action.LEFT;
			}
		} else
		if (newP.x() < old.x()){
			if (newP.y() > old.y())
				return Action.DOWNLEFT;
			else
				return Action.UPLEFT;
		} else {
            if (newP.y() > old.y())
				return Action.DOWNRIGHT;
			else
				return Action.UPRIGHT;
		}
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