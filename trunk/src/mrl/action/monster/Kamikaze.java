package mrl.action.monster;

import mrl.action.Action;
import mrl.feature.Feature;
import mrl.game.CRLException;
import mrl.level.Cell;
import mrl.level.MLevel;
import mrl.level.MapCellFactory;
import mrl.monster.Monster;
import mrl.monster.MonsterFactory;
import sz.util.Debug;
import sz.util.Position;
import sz.util.Util;

public class Kamikaze extends Action{
	
	private String actionMessage;
	private int damage;
	
	public void set(String pActionMessage, int damage){
		this.damage = damage;
		actionMessage = pActionMessage;
	}
	
	public String getID(){
		return "KAMIKAZE";
	}
	
	public void execute(){
		Debug.doAssert(performer instanceof Monster, "Someone not a monster tried to throw a bone");
		Monster aMonster = (Monster) performer;
        MLevel aLevel = performer.getLevel();
        aLevel.addMessage("The "+aMonster.getDescription()+" "+actionMessage+".");
        Position blastPosition = aMonster.getPosition();
        for (int x = blastPosition.x -1; x <= blastPosition.x+1; x++)
			for (int y = blastPosition.y -1; y <= blastPosition.y+1; y++){
				Position destinationPoint = new Position(x,y);
				Monster targetMonster = performer.getLevel().getMonsterAt(destinationPoint);
				if (targetMonster != null){
					if (targetMonster.wasSeen())
						aLevel.addMessage("The "+targetMonster.getDescription()+" is hit by the bomb!");
					targetMonster.damage(damage);
				}
				if (aLevel.getPlayer().getPosition().equals(blastPosition)){
					aLevel.getPlayer().damage(aMonster, damage);
				}
			}
	}

}