package mrl.feature.action;

import mrl.action.Action;
import mrl.feature.Feature;
import mrl.game.CRLException;
import mrl.level.Cell;
import mrl.level.MLevel;
import mrl.level.MapCellFactory;
import mrl.monster.Monster;
import mrl.ui.effects.EffectFactory;
import sz.util.Position;

public class BombBlast extends Action{
	
	public String getID(){
		return "BOMBBLAST";
	}
		
	public void execute(){
		MLevel aLevel = performer.getLevel();
		int damage = 1;
		Position blastPosition = targetPosition;
		
		//aLevel.addEffect(new SplashEffect(blastPosition, "Oo,.", Appearance.CYAN));
		aLevel.addEffect(EffectFactory.getSingleton().createLocatedEffect(blastPosition, "SFX_BOMB_BLAST"));
		
		for (int x = blastPosition.x -1; x <= blastPosition.x+1; x++)
			for (int y = blastPosition.y -1; y <= blastPosition.y+1; y++){
				Position destinationPoint = new Position(x,y);
				Monster targetMonster = performer.getLevel().getMonsterAt(destinationPoint);
				if (targetMonster != null){
					if (targetMonster.wasSeen())
						aLevel.addMessage("The "+targetMonster.getDescription()+" is hit by the bomb!");
					targetMonster.damage(damage);
				}
				Feature targetFeature = performer.getLevel().getFeatureAt(destinationPoint);
				if (targetFeature != null){
					
				}
				
				Cell targetCell = performer.getLevel().getMapCell(destinationPoint);
				if (targetCell != null){
					if (targetCell.getID().equals("BREAKABLE_WALL")){
						aLevel.addMessage("The wall breaks apart!");
						try {
							performer.getLevel().getCells()[destinationPoint.z][destinationPoint.x][destinationPoint.y] = MapCellFactory.getMapCellFactory().getMapCell("METALLIC_FLOOR");
						} catch (CRLException crle){
							
						}
					}
				}
			}
	}
	
	public String getSFX(){
		return "wav/lazrshot.wav";
	}
	
	public int getCost(){
		return 10;
	}
}
