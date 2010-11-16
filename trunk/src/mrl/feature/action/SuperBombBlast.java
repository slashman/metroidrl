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

public class SuperBombBlast extends Action{
	
	public String getID(){
		return "SUPERBOMBBLAST";
	}
		
	public void execute(){
		MLevel aLevel = performer.getLevel();
		int damage = 5;
		Position blastPosition = targetPosition;
		
		//aLevel.addEffect(new SplashEffect(blastPosition, "Oo,.", Appearance.CYAN));
		aLevel.addEffect(EffectFactory.getSingleton().createLocatedEffect(blastPosition, "SFX_SUPERBOMB_BLAST"));
		
		for (int x = blastPosition.x -10; x <= blastPosition.x+10; x++)
			for (int y = blastPosition.y -10; y <= blastPosition.y+10; y++){
				Position destinationPoint = new Position(x,y);
				Monster targetMonster = performer.getLevel().getMonsterAt(destinationPoint);
				if (targetMonster != null){
					if (targetMonster.wasSeen())
						aLevel.addMessage("The "+targetMonster.getDescription()+" is hit by the light blast!");
					targetMonster.damage(damage);
				}
				Feature targetFeature = performer.getLevel().getFeatureAt(destinationPoint);
				if (targetFeature != null){
					
				}
				Cell targetMapCell = performer.getLevel().getMapCell(destinationPoint);
				if (targetMapCell != null && targetMapCell.getID().equals("POWERBOMB_DOOR")){
					//aLevel.addMessage("The wall breaks apart!");
					try {
						performer.getLevel().getCells()[destinationPoint.z][destinationPoint.x][destinationPoint.y] = MapCellFactory.getMapCellFactory().getMapCell("METALLIC_FLOOR");
					} catch (CRLException crle){
						
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
