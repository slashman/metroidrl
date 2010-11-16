package mrl.action.monster;

import mrl.action.Action;
import mrl.level.MLevel;
import mrl.monster.Monster;
import mrl.monster.MonsterFactory;
import sz.util.Debug;
import sz.util.Position;
import sz.util.Util;

public class SummonMonster extends Action{
	private String monsterId;
	private String actionMessage;
	
	public void set(String pMonsterId, String pActionMessage){
		monsterId = pMonsterId;
		actionMessage = pActionMessage;
	}
	
	public String getID(){
		return "SUMMON_MONSTER";
	}
	
	public void execute(){
		Debug.doAssert(performer instanceof Monster, "Someone not a monster tried to throw a bone");
		Monster aMonster = (Monster) performer;
        MLevel aLevel = performer.getLevel();
        aLevel.addMessage("The "+aMonster.getDescription()+" "+actionMessage+".");
        int monsters = Util.rand(5,10);
        for (int i=0; i<monsters; i++){
        	int xvar = Util.rand(-8,8);
        	int yvar = Util.rand(-8,8);
			Position destinationPoint = Position.add(performer.getPosition(),
				new Position(xvar, yvar));
			if (aLevel.isWalkable(destinationPoint)){
				Monster m = MonsterFactory.getFactory().buildMonster(monsterId);
				m.setPosition(destinationPoint);
				aLevel.addMonster(m);
			}
		}
	}

}