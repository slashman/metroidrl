package mrl.levelgen;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import mrl.action.Action;
import mrl.action.ActionFactory;
import mrl.action.monster.MonsterCharge;
import mrl.action.monster.MonsterMissile;
import mrl.action.monster.SummonMonster;
import mrl.ai.ActionSelector;
import mrl.ai.BasicMonsterAI;
import mrl.ai.SelectorFactory;
import mrl.ai.monster.RangedAI;
import mrl.ai.monster.WanderToPlayerAI;
import mrl.conf.console.data.CharAppearances;
import mrl.data.Cells;
import mrl.data.Features;
import mrl.data.MonsterLoader;
import mrl.data.SmartFeatures;
import mrl.feature.CountDown;
import mrl.feature.Feature;
import mrl.feature.FeatureFactory;
import mrl.feature.SmartFeatureFactory;
import mrl.feature.selector.BombSelector;
import mrl.feature.selector.SuperBombSelector;
import mrl.game.CRLException;
import mrl.level.Dispatcher;
import mrl.level.MLevel;
import mrl.level.MapCellFactory;
import mrl.level.RespawnAI;
import mrl.level.Respawner;
import mrl.levelgen.cave.LavaCaveLevelGenerator;
import mrl.levelgen.patterns.Barrier;
import mrl.levelgen.patterns.BaseEntrance;
import mrl.levelgen.patterns.BonyRoom;
import mrl.levelgen.patterns.Decks;
import mrl.levelgen.patterns.LandingSite;
import mrl.levelgen.patterns.Last;
import mrl.levelgen.patterns.StaticPattern;
import mrl.levelgen.patterns.TestingArea;
import mrl.levelgen.patterns.Upgrade;
import mrl.monster.Monster;
import mrl.monster.MonsterFactory;
import mrl.ui.Appearance;
import mrl.ui.AppearanceFactory;
import sz.util.Debug;
import sz.util.Position;
import sz.util.Util;

public class LevelPlotter {
	private static Dispatcher currentDispatcher;
	private static boolean firstCave = true;
	public static MLevel createLevel(String levelID, String previousLevelID, int targetLevelNumber, LevelMetaData md) throws CRLException{
		//JOptionPane.showMessageDialog(null, "createLevel "+levelID+" with former "+previousLevelID);
		Debug.enterStaticMethod("LevelMaster", "createLevel");
		Debug.say("levelID "+levelID);
		boolean overrideLevelNumber = false;
		MLevel ret = null;
		PatternGenerator.getGenerator().resetFeatures();
		Respawner x = new Respawner(30, 80);
		x.setSelector(new RespawnAI());
		boolean isStatic = false;
		StaticPattern pattern = null;
		/*if (levelID.startsWith("TOWN")){
			isStatic = true;
			pattern = new BigTown();
		} else*/
		if (levelID.startsWith("LANDING")){ //LANDING
			isStatic = true;
			pattern = new LandingSite();
		}else if (levelID.startsWith("BASE1")){
			isStatic = true;
			pattern = new BaseEntrance();
		}else if (levelID.startsWith("DECKS")){
			isStatic = true;
			pattern = new Decks();
		}else if (levelID.startsWith("UPGRADE")){
			isStatic = true;
			String upgrade = levelID.substring(levelID.indexOf("_")+1);
			/*System.out.println("md "+md);
			System.out.println("md.exits "+md.exits);*/
			pattern = new Upgrade((String)md.exits.get(0), upgrade);
		}else if (levelID.startsWith("BARRIER")){
			isStatic = true;
			String upgrade = levelID.substring(levelID.indexOf("_")+1);
			pattern = new Barrier((String)md.exits.get(0), (String)md.exits.get(1), upgrade);
		}else if (levelID.startsWith("LAST")){
			isStatic = true;
			String upgrade = levelID.substring(levelID.indexOf("_")+1);
			pattern = new Last((String)md.exits.get(0), upgrade);
		}
		else if (levelID.startsWith("TEST")){
			isStatic = true;
			pattern = new TestingArea();
		}
		else if (levelID.startsWith("BONY")){
			isStatic = true;
			pattern = new BonyRoom((String)md.exits.get(0));
		}
		
		
		if (isStatic){
			pattern.setup(StaticGenerator.getGenerator());
			ret = StaticGenerator.getGenerator().createLevel();
			if (pattern.isHaunted()){
				ret.setHaunted(true);
				ret.setNightRespawner(x);
				ret.savePop();
				ret.getMonsters().removeAll();
				ret.getDispatcher().removeAll();
				
			}
			ret.setRespawner(x);
			ret.setInhabitants(pattern.getSpawnInfo());
			ret.setDwellerIDs(pattern.getDwellers());
			ret.setDescription(pattern.getDescription());
			ret.setHostageSafe(pattern.isHostageSafe());
			ret.setMusicKeyMorning(pattern.getMusicKeyMorning());
			ret.setMusicKeyNoon(pattern.getMusicKeyNoon());
			
			if (pattern.getBoss() != null){
				Monster monsBoss = MonsterFactory.getFactory().buildMonster(pattern.getBoss());
				monsBoss.setPosition(pattern.getBossPosition());
				ret.setBoss(monsBoss);
			}
			if (pattern.getUnleashers() != null){
				ret.setUnleashers(pattern.getUnleashers());
			}
			ret.setMapLocationKey(pattern.getMapKey());
			if (levelID.startsWith("BARRIER")){
				String upgrade = levelID.substring(levelID.indexOf("_")+1);
				if (upgrade.equals("VARIA_SUIT")){
					ret.setCold(true);
				}
			}
		} 
		
		if (levelID.startsWith("SURFACE")){
			LavaCaveLevelGenerator clg = new LavaCaveLevelGenerator();
			clg.init("ROCK2", "ROCK", "ROCK1");
			ret = clg.generateLevel(Util.rand(60,80),Util.rand(20,40), false);
			ret.setDwellerIDs(new String[]{"GALACTIC_TROOPER", "RED_ANT", "DRIVEL", "ZOOMER", "CACATAC", "ZEELA"});
			ret.setDispatcher(new Dispatcher());
			ret.setRespawner(x);
			ret.setDescription("Janos Surface");
			ret.setMusicKeyMorning("SURFACE");
			ret.setMusicKeyNoon(null);
			ret.setMapLocationKey("SURFACE");
			Position next = ret.getExitFor("_NEXT");
			Position back = ret.getExitFor("_BACK");
			ret.removeExit("_NEXT");
			ret.removeExit("_BACK");
			ret.addExit(next,"BASE1");
			ret.addExit(back,"LANDING");
		} else if (levelID.startsWith("_BASE")){
			BaseGenerator bg = new BaseGenerator();
			bg.init("METALLIC_WALL", "METALLIC_FLOOR", "METALLIC_FLOOR2", "METALLIC_FLOOR3", "BLUE_DOOR", md.exits, md.exitDoors);
			ret = bg.generateLevel(Util.rand(60,80),Util.rand(60,80));
			//ret = bg.generateLevel(20,20);
			//ret.setInhabitants(Ruins.getInhabitants(0));
			ret.setDispatcher(new Dispatcher());
			ret.setRespawner(x);
			//ret.setDescription("Janos Base");
			ret.setDescription(md.levelDescription);
			ret.setDwellerIDs(md.dwellers);
			ret.setMapLocationKey("BASE");
			ret.setDwellerIDs(new String[]{"GALACTIC_TROOPER", "GALACTIC_SOLDIER", "AUTOTOAD", "SIDEHOOPER"});
			ret.setMusicKeyMorning(md.musicKey);
		} 
		 
		
		ret.setID(levelID);
		if (!overrideLevelNumber)
			ret.setLevelNumber(targetLevelNumber);
		Debug.exitMethod(ret);
		return ret;

	}

	public static Dispatcher getCurrentDispatcher() {
		return currentDispatcher;
	}

	protected int placeKeys(MLevel ret){
		Debug.enterMethod(this, "placeKeys");
		//Place the magic Keys
		int keys = Util.rand(1,4);
		Position tempPosition = new Position(0,0);
		for (int i = 0; i < keys; i++){
			int keyx = Util.rand(1,ret.getWidth()-1);
			int keyy = Util.rand(1,ret.getHeight()-1);
			int keyz = Util.rand(0, ret.getDepth()-1);
			tempPosition.x = keyx;
			tempPosition.y = keyy;
			tempPosition.z = keyz;
			if (ret.isWalkable(tempPosition)){
				Feature keyf = FeatureFactory.getFactory().buildFeature("KEY");
				keyf.setPosition(tempPosition.x, tempPosition.y, tempPosition.z);
				ret.addFeature(keyf);
			} else {
				i--;
			}
		}
		Debug.exitMethod(keys);
		return keys;
		
	}
	
	
	public static void main(String args[]){
		try {
			Appearance[] definitions = new CharAppearances().getAppearances();
			for (int i=0; i<definitions.length; i++){
				AppearanceFactory.getAppearanceFactory().addDefinition(definitions[i]);
			}
			System.out.println("Initializing Action Objects");
			initializeActions();
			initializeSelectors();
			System.out.println("Loading Data");
			MapCellFactory.getMapCellFactory().init(Cells.getCellDefinitions(AppearanceFactory.getAppearanceFactory()));
			initializeMonsters();
			FeatureFactory.getFactory().init(Features.getFeatureDefinitions(AppearanceFactory.getAppearanceFactory()));
			initializeSmartFeatures();
			initializeSelectors();
			
			LevelMetaData test = new LevelMetaData("_BASE3");
			MLevel level = createLevel("_BASE3", "FOREST", 3, test);
			JFrame frmX = new JFrame();
			frmX.setBounds(0,0,800,600);
			frmX.getContentPane().add(new PanelLevel(level));
			frmX.setVisible(true);
			frmX.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} catch (CRLException e){
			e.printStackTrace();
		}
	}
	
	private static void initializeActions(){
		ActionFactory af = ActionFactory.getActionFactory();
		Action[] definitions = new Action[]{
				
				
				new MonsterCharge(),
				new MonsterMissile(),
				new SummonMonster(),
				

		};
		for (int i = 0; i < definitions.length; i++)
			af.addDefinition(definitions[i]);
	}
	

	
	private static void initializeSelectors(){
		ActionSelector [] definitions = getSelectorDefinitions();
		for (int i=0; i<definitions.length; i++){
        	SelectorFactory.getSelectorFactory().addDefinition(definitions[i]);
		}
	}
	
	private static ActionSelector [] getSelectorDefinitions(){
		ActionSelector [] ret = new ActionSelector[]{
				new WanderToPlayerAI(),
				new RangedAI(),
				new CountDown(),
				new BasicMonsterAI(),
				new BombSelector(),
				new SuperBombSelector()
		};
		return ret;
	}
	
	private static void initializeMonsters() throws CRLException{
		
		MonsterFactory.getFactory().init(MonsterLoader.getMonsterDefinitions("data/monsters.exml"));
	}

	private static void initializeSmartFeatures (){
		SmartFeatureFactory.getFactory().init(SmartFeatures.getSmartFeatures(SelectorFactory.getSelectorFactory()));
	}
	
	static class PanelLevel extends JPanel{
		private MLevel level;
		public PanelLevel(MLevel l){
			level = l;
		}
		public void paintComponent(Graphics g){
			for (int x = 0; x < level.getWidth(); x++)
				for (int y = 0; y < level.getHeight(); y++){
					if (level.getMapCell(x,y,0)== null || level.getMapCell(x,y,0).isSolid()){
						g.setColor(Color.BLACK);
					} else {
						g.setColor(Color.WHITE);
					}
					g.fillRect(x*5,y*5,5,5);
				}
		}
	}
}