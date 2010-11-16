package mrl.level;

import mrl.action.*;
import mrl.monster.*;
import sz.util.*;

public class EmergeMonster extends Action{
	private static EmergeMonster singleton = new EmergeMonster();
	
	public String getID(){
		return "EmergeMonster";
	}
	
	public void execute(){
		MLevel level = performer.getLevel();
		Emerger em = (Emerger) performer;
		Monster monster = em.getMonster();
		monster.setPosition(new Position(em.getPoint()));
		level.addMonster(monster);
	}

	public static EmergeMonster getAction(){
		return singleton;
	}
}