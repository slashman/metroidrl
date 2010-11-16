package mrl.levelgen;

import java.util.ArrayList;

import mrl.game.CRLException;
import mrl.game.Prize;
import mrl.level.Dispatcher;
import mrl.level.MLevel;
import mrl.level.RespawnAI;
import mrl.level.Respawner;
import mrl.levelgen.cave.CaveLevelGenerator;
import mrl.levelgen.cave.LavaCaveLevelGenerator;
import mrl.levelgen.featureCarve.BarrierFeature;
import mrl.levelgen.featureCarve.ColumnsRoom;
import mrl.levelgen.featureCarve.ExitRoom;
import mrl.levelgen.featureCarve.FeatureCarveGenerator;
import mrl.levelgen.featureCarve.PrizeRoom;
import mrl.levelgen.featureCarve.RoomFeature;
import mrl.levelgen.mapData.SpecialRoomFactory;
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
import sz.util.Debug;
import sz.util.Position;
import sz.util.Util;


public class LevelMaster {
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
			ret.setPopulationAverage(pattern.getPopulationAverage());
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
			ret.setPopulationAverage(5);
		} else if (levelID.startsWith("_OLEBASE")){
			BaseGenerator bg = new BaseGenerator();
			bg.init("METALLIC_WALL", "METALLIC_FLOOR", "METALLIC_FLOOR2", "METALLIC_FLOOR3", "BLUE_DOOR", md.exits, md.exitDoors);
			ret = bg.generateLevel(Util.rand(30,50),Util.rand(30,50));
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
			ret.setPopulationAverage((int)Math.round((ret.getWidth()*ret.getHeight())/100.0));
		} else if (levelID.startsWith("_BASE")){
			FeatureCarveGenerator fcg = new FeatureCarveGenerator();
			ArrayList rooms = getRooms(md);
			ArrayList prizeRooms = getPrizeRooms(md);
			ArrayList exitRooms = getExitRooms(md);
			fcg.initialize(rooms, prizeRooms, exitRooms,
					md.defaultWallCell, 
					Util.rand(80,100),
					Util.rand(80,100),
					md.defaultFloorCell, md);
			ret = fcg.generateLevel();
			ret.setDispatcher(new Dispatcher());
			ret.setRespawner(x);
			ret.setDescription(md.levelDescription);
			ret.setMusicKeyMorning(md.musicKey);
			ret.setMapLocationKey("BASE");
			ret.setDwellerIDs(md.dwellers);
			ret.setPopulationAverage((int)Math.round((ret.getWidth()*ret.getHeight())/50.0));
			ret.populate();
			//ret.setRutinary(true);
		} else if (levelID.startsWith("_SQUARES")){
			RuinLevelGenerator rlg = new RuinLevelGenerator();
			ArrayList rooms = getRooms(md);
			ArrayList prizeRooms = getPrizeRooms(md);
			//ArrayList exitRooms = getExitRooms(md);
			rooms.addAll(prizeRooms);
			rlg.init(md.defaultWallCell, md.defaultFloorCell, md.defaultFloorCell, "TELEPOD", rooms );
			ret = rlg.generateLevel(Util.rand(40,80), Util.rand(40,80), md);
			ret.setDispatcher(new Dispatcher());
			ret.setRespawner(x);
			ret.setDescription(md.levelDescription);
			ret.setMusicKeyMorning(md.musicKey);
			ret.setMapLocationKey("BASE");
			ret.setDwellerIDs(md.dwellers);
			ret.setPopulationAverage((int)Math.round((ret.getWidth()*ret.getHeight())/100.0));
			ret.populate();
		} else if (levelID.startsWith("_CAVE")){
			/*CaveLevelGenerator clg = new CaveLevelGenerator();
			ArrayList rooms = getRooms(md);
			ArrayList prizeRooms = getPrizeRooms(md);
			//ArrayList exitRooms = getExitRooms(md);
			rooms.addAll(prizeRooms);
			clg.init(md.defaultWallCell, md.defaultFloorCell, md.defaultFloorCell, "TELEPOD", rooms);
			ret = rlg.generateLevel(Util.rand(40,80), Util.rand(40,80), md);
			ret.setDispatcher(new Dispatcher());
			ret.setRespawner(x);
			ret.setDescription(md.levelDescription);
			ret.setMusicKeyMorning(md.musicKey);
			ret.setMapLocationKey("BASE");
			ret.setDwellerIDs(md.dwellers);
			ret.setPopulationAverage((int)Math.round((ret.getWidth()*ret.getHeight())/100.0));
			ret.populate();*/
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

	private static ArrayList getRooms(LevelMetaData md){
		int rooms = Util.rand(4,6);
		mrl.levelgen.featureCarve.Feature room = null;
		
		ArrayList ret = new ArrayList();
		String floor = md.defaultFloorCell;
		String column = md.defaultWallCell;
		for (int i = 0; i < rooms; i++){
			switch (Util.rand(1,3)){
			case 1:
				//room = new RoomFeature(Util.rand(5,12), Util.rand(5,12), floor);
				room = getSpecialRoom(md.facets);
				if (room == null){
					room = new RoomFeature(Util.rand(5,12), Util.rand(5,12), floor);
				}
				break;
			case 2:
				room = new RoomFeature(Util.rand(5,12), Util.rand(5,12), floor);
				break;
			case 3:
				room = new ColumnsRoom(Util.rand(5,12), Util.rand(5,12), floor, column);
				break;
			}
			ret.add(room);
		}
		return ret;
	}
	
	private static mrl.levelgen.featureCarve.Feature getSpecialRoom(String facets){
		return SpecialRoomFactory.thus.buildFeatureByFacet(facets);
	}
	
	private static ArrayList getPrizeRooms(LevelMetaData md){ 
		ArrayList ret = new ArrayList();
		for (int i = 0; i < md.prizes.size(); i++){
			Prize p = (Prize) md.prizes.get(i);
			int roomSize = Util.rand(8,12);
			PrizeRoom room = new PrizeRoom(roomSize, roomSize, md.defaultFloorCell, p.getPriceId());
			ret.add(room);
		}
		return ret;
	}
	
	private static ArrayList getExitRooms(LevelMetaData md){
		ArrayList ret = new ArrayList();
		for (int i = 0; i < md.exits.size(); i++){
			String e = (String) md.exits.get(i);
			String ed = (String) md.exitDoors.get(i);
			int roomSize = Util.rand(8,12);
			ExitRoom room = new ExitRoom(roomSize, roomSize, md.defaultFloorCell, e, ed);
			ret.add(room);
		}
		return ret;
	}
}