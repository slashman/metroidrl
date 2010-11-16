package mrl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Enumeration;
import java.util.Properties;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

import mrl.action.Action;
import mrl.action.ActionFactory;
import mrl.action.AutoFire;
import mrl.action.AutoFireSet;
import mrl.action.BallMorph;
import mrl.action.Bomb;
import mrl.action.ChargeBeam;
import mrl.action.Fire;
import mrl.action.FireMissile;
import mrl.action.FireSuperMissile;
import mrl.action.Get;
import mrl.action.Jump;
import mrl.action.SuperBomb;
import mrl.action.Walk;
import mrl.action.monster.Kamikaze;
import mrl.action.monster.MonsterCharge;
import mrl.action.monster.MonsterMissile;
import mrl.action.monster.MonsterWalk;
import mrl.action.monster.SummonMonster;
import mrl.ai.ActionSelector;
import mrl.ai.BasicMonsterAI;
import mrl.ai.SelectorFactory;
import mrl.ai.monster.RangedAI;
import mrl.ai.monster.WanderToPlayerAI;
import mrl.conf.console.data.CharAppearances;
import mrl.conf.console.data.CharCuts;
import mrl.conf.console.data.CharEffects;
import mrl.data.AmmoTypes;
import mrl.data.Cells;
import mrl.data.Features;
import mrl.data.MonsterLoader;
import mrl.data.RoomsLoader;
import mrl.data.SmartFeatures;
import mrl.data.WeaponTypes;
import mrl.feature.CountDown;
import mrl.feature.FeatureFactory;
import mrl.feature.SmartFeatureFactory;
import mrl.feature.selector.BombSelector;
import mrl.feature.selector.SuperBombSelector;
import mrl.game.CRLException;
import mrl.game.Game;
import mrl.game.GameFiles;
import mrl.game.PlayerGenerator;
import mrl.game.SFXManager;
import mrl.game.STMusicManagerNew;
import mrl.item.ItemFactory;
import mrl.level.MapCellFactory;
import mrl.levelgen.mapData.SpecialRoomFactory;
import mrl.monster.MonsterFactory;
import mrl.ui.Appearance;
import mrl.ui.AppearanceFactory;
import mrl.ui.CommandListener;
import mrl.ui.Display;
import mrl.ui.UserAction;
import mrl.ui.UserCommand;
import mrl.ui.UserInterface;
import mrl.ui.consoleUI.CharDisplay;
import mrl.ui.consoleUI.CharPlayerGenerator;
import mrl.ui.consoleUI.ConsoleUserInterface;
import mrl.ui.consoleUI.effects.CharEffectFactory;
import mrl.ui.effects.EffectFactory;
import sz.csi.CharKey;
import sz.csi.ConsoleSystemInterface;
import sz.csi.jcurses.JCursesConsoleInterface;
import sz.csi.wswing.WSwingConsoleInterface;
import sz.midi.STMidiPlayer;

public class Main {
	//private static SystemInterface si;
	private final static int JCURSES_CONSOLE = 0, SWING_GFX = 1, SWING_CONSOLE = 2;
	private static String uiFile;
	
	private static UserInterface ui;
	
	private static Game currentGame;
	private static boolean createNew = true;
	private static int mode;

	private static void init(){
		System.out.println("MetroidRL "+Game.getVersion());
		System.out.println("Slash ~ 2006-2010");
		System.out.println("Reading configuration");
    	readConfiguration();
    	
		if (createNew){
			try {
    			
    			switch (mode){
				case SWING_GFX:
					System.out.println("Initializing Graphics Appearances");
					initializeGAppearances();
					break;
				case JCURSES_CONSOLE:
				case SWING_CONSOLE:
					System.out.println("Initializing Char Appearances");
					initializeCAppearances();
					break;
    			}
				System.out.println("Initializing Action Objects");
				initializeActions();
				initializeSelectors();
				System.out.println("Loading Data");
				initializeCells();
				initializeFeatures();
				initializeItems();
				initializeMonsters();
				initializeMapData();
				switch (mode){
				case SWING_GFX:
					/*System.out.println("Initializing Swing GFX System Interface");
					SwingSystemInterface si = new SwingSystemInterface();
					System.out.println("Initializing Swing GFX User Interface");
					UserInterface.setSingleton(new GFXUserInterface());
					//GFXCuts.initializeSingleton();
					Display.thus = new GFXDisplay(si, UIconfiguration);
					PlayerGenerator.thus = new GFXPlayerGenerator(si, UIconfiguration);
					//PlayerGenerator.thus.initSpecialPlayers();
					EffectFactory.setSingleton(new GFXEffectFactory());
					((GFXEffectFactory)EffectFactory.getSingleton()).setEffects(new GFXEffects().getEffects(UIconfiguration));
					ui = UserInterface.getUI();
					initializeUI(si);*/
					break;
				case JCURSES_CONSOLE:
					System.out.println("Initializing JCurses System Interface");
					ConsoleSystemInterface csi = null;
					try{
						csi = new JCursesConsoleInterface();
					}
		            catch (ExceptionInInitializerError eiie){
		            	crash("Fatal Error Initializing JCurses", eiie);
		            	eiie.printStackTrace();
		                System.exit(-1);
		            }
		            System.out.println("Initializing Console User Interface");
					UserInterface.setSingleton(new ConsoleUserInterface());
					CharCuts.initializeSingleton();
					Display.thus = new CharDisplay(csi);
					PlayerGenerator.thus = new CharPlayerGenerator(csi);
					//PlayerGenerator.thus.initSpecialPlayers();
					EffectFactory.setSingleton(new CharEffectFactory());
					((CharEffectFactory)EffectFactory.getSingleton()).setEffects(new CharEffects().getEffects());
					ui = UserInterface.getUI();
					initializeUI(csi);
					break;
				case SWING_CONSOLE:
					System.out.println("Initializing Swing Console System Interface");
					csi = new WSwingConsoleInterface();
		            System.out.println("Initializing Console User Interface");
					UserInterface.setSingleton(new ConsoleUserInterface());
					CharCuts.initializeSingleton();
					Display.thus = new CharDisplay(csi);
					PlayerGenerator.thus = new CharPlayerGenerator(csi);
					//PlayerGenerator.thus.initSpecialPlayers();
					EffectFactory.setSingleton(new CharEffectFactory());
					((CharEffectFactory)EffectFactory.getSingleton()).setEffects(new CharEffects().getEffects());
					ui = UserInterface.getUI();
					initializeUI(csi);
				}
				
            } catch (CRLException crle){
            	crash("Error initializing", crle);
            }
            
            STMusicManagerNew.initManager();
        	if (configuration.getProperty("enableSound") != null && configuration.getProperty("enableSound").equals("true")){ // Sound
        		if (configuration.getProperty("enableMusic") == null || !configuration.getProperty("enableMusic").equals("true")){ // Music
    	    		STMusicManagerNew.thus.setEnabled(false);
    		    } else {
    		    	System.out.println("Initializing Midi Sequencer");
    	    		try {
    	    			STMidiPlayer.sequencer = MidiSystem.getSequencer ();
    	    			STMidiPlayer.sequencer.open();
    	    		} catch(MidiUnavailableException mue) {
    	            	Game.addReport("Midi device unavailable");
    	            	System.out.println("Midi Device Unavailable");
    	            	STMusicManagerNew.thus.setEnabled(false);
    	            	return;
    	            }
    	    		System.out.println("Initializing Music Manager");
    				
    		    	
    	    		Enumeration keys = configuration.keys();
    	    	    while (keys.hasMoreElements()){
    	    	    	String key = (String) keys.nextElement();
    	    	    	if (key.startsWith("mus_")){
    	    	    		String music = key.substring(4);
    	    	    		STMusicManagerNew.thus.addMusic(music, configuration.getProperty(key));
    	    	    	}
    	    	    }
    	    	    STMusicManagerNew.thus.setEnabled(true);
    		    }
    	    	if (configuration.getProperty("enableSFX") == null || !configuration.getProperty("enableSFX").equals("true")){
    		    	SFXManager.setEnabled(false);
    		    } else {
    		    	SFXManager.setEnabled(true);
    		    }
        	}
            createNew = false;
    	}
	}
	private static Properties configuration;
	private static Properties UIconfiguration;
	
	private static void readConfiguration(){
		configuration = new Properties();
	    try {
	    	configuration.load(new FileInputStream("mrl.cfg"));
	    } catch (IOException e) {
	    	System.out.println("Error loading configuration file, please confirm existence of mrl.cfg");
	    	System.exit(-1);
	    }
	    
	    if (mode == SWING_GFX){
		    UIconfiguration = new Properties();
		    try {
		    	UIconfiguration.load(new FileInputStream(uiFile));
		    } catch (IOException e) {
		    	System.out.println("Error loading configuration file, please confirm existence of mrl.cfg");
		    	System.exit(-1);
		    }
	    }
	}
	
	private static void	title() {
		STMusicManagerNew.thus.playKey("TITLE");
		int choice = Display.thus.showTitleScreen();
		switch (choice){
		case 0:
			newGame();
			break;
		case 1:
			loadGame();
			break;
		case 2:
			Display.thus.showHiscores(GameFiles.loadScores());
			title();
			break;
		case 3:
			System.out.println("MetroidRL v"+Game.getVersion()+", clean Exit");
			System.out.println("Thank you for playing!");
			System.exit(0);
			break;
		
		}
	}
	
	private static void loadGame(){
		File saveDirectory = new File("saveGame");
		File[] saves = saveDirectory.listFiles(new SaveGameFilenameFilter() );
		
		int index = Display.thus.showSavedGames(saves);
		if (index == -1)
			title();
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saves[index]));
			currentGame = (Game) ois.readObject();
			ois.close();
		} catch (IOException ioe){
 
			ioe.printStackTrace();
		} catch (ClassNotFoundException cnfe){
			crash("Invalid savefile or wrong version", new CRLException("Invalid savefile or wrong version"));
		}
		currentGame.setInterfaces(ui);
		if (currentGame.getPlayer().getLevel() == null){
			crash("Player wasnt loaded", new Exception("Player wasnt loaded"));
		}
		currentGame.setPlayer(currentGame.getPlayer());
		ui.setPlayer(currentGame.getPlayer());
		
		currentGame.resume();
		
		title();
	}
	
	private static void newGame(){
		if (currentGame != null){
			ui.removeCommandListener(currentGame);
			//currentGame.resetUniqueRegister();
		}
		currentGame = new Game();
		//currentGame.setCanSave(true);
		currentGame.setInterfaces(ui);
		currentGame.newGame();
		
		title();
	}

	private static void initializeUI(Object si){
		Action walkAction = new Walk();
		Action jump = new Jump();
		Action bomb = new Bomb();
		Action fire = new Fire();
		Action autofireset = new AutoFireSet();
		Action missile = new FireMissile();
		Action superMissile = new FireSuperMissile();
		Action superBomb = new SuperBomb();
		Action ball = new BallMorph();
		Action charge = new ChargeBeam();
		Action get = new Get();
		
		UserAction[] userActions = new UserAction[] {
	        new UserAction(fire, CharKey.SPACE),
	        new UserAction(jump, CharKey.CTRL),
	        new UserAction(autofireset, CharKey.ENTER),
	        new UserAction(ball, CharKey.NUMBER1),
	        new UserAction(charge, CharKey.NUMBER2),
	        new UserAction(bomb, CharKey.NUMBER3),
	        new UserAction(missile, CharKey.NUMBER4),
	        new UserAction(superMissile, CharKey.NUMBER5),
	        new UserAction(superBomb, CharKey.NUMBER6),
	        new UserAction(get, CharKey.COMMA),
		};
		

		UserCommand[] userCommands = new UserCommand[]{
				new UserCommand(CommandListener.PROMPTQUIT, CharKey.Q),
				new UserCommand(CommandListener.HELP, CharKey.F1),
				new UserCommand(CommandListener.LOOK, CharKey.l),
				new UserCommand(CommandListener.PROMPTSAVE, CharKey.S),
				new UserCommand(CommandListener.REMOVELOCK, CharKey.r),
				//new UserCommand(CommandListener.SHOWSKILLS, CharKey.s),
				new UserCommand(CommandListener.HELP, CharKey.QUESTION),
				new UserCommand(CommandListener.SHOWSTATS, CharKey.i),
				new UserCommand(CommandListener.SHOWMESSAGEHISTORY, CharKey.m),
				//new UserCommand(CommandListener.SHOWMAP, CharKey.M),

		};
		switch (mode){
		case SWING_GFX:
			//((GFXUserInterface)ui).init((SwingSystemInterface)si, userActions, userCommands, walkAction, target, attack, UIconfiguration);
			break;
		case JCURSES_CONSOLE:
			((ConsoleUserInterface)ui).init((ConsoleSystemInterface)si, userActions, userCommands, walkAction, fire, fire);
			break;
		case SWING_CONSOLE:
			((ConsoleUserInterface)ui).init((WSwingConsoleInterface)si, userActions, userCommands, walkAction, fire, fire);
			break;
		}
		
		
	}
	
	public static void main(String args[]){
		
		mode = JCURSES_CONSOLE;
		uiFile = "slash-retrovga.ui";
		if (args!= null && args.length > 0){
			if (args[0].equalsIgnoreCase("sgfx")){
				mode = SWING_GFX;
				if (args.length > 1)
					uiFile = args[1];
				else
					uiFile = "slash-retrovga.ui";
			}
			else if (args[0].equalsIgnoreCase("jc"))
				mode = JCURSES_CONSOLE;
			else if (args[0].equalsIgnoreCase("sc"))
				mode = SWING_CONSOLE;
		}
		
		init();
		System.out.println("Launching game");
		try {
			title();
		} catch (Exception e){
			Game.crash("Unrecoverable Exception [Press Space]",e);
			//si.waitKey(CharKey.SPACE);
		}
	}

	private static void initializeGAppearances(){
		/*String tileSet = UIconfiguration.getProperty("TILESET");
		String tileSet_dark = UIconfiguration.getProperty("TILESET_DARK");
		Appearance[] definitions = new GFXAppearances(tileSet, tileSet_dark, Integer.parseInt(UIconfiguration.getProperty("TILESIZE"))).getAppearances();
		for (int i=0; i<definitions.length; i++){
			AppearanceFactory.getAppearanceFactory().addDefinition(definitions[i]);
		}*/
	}
	
	private static void initializeCAppearances(){
		Appearance[] definitions = new CharAppearances().getAppearances();
		for (int i=0; i<definitions.length; i++){
			AppearanceFactory.getAppearanceFactory().addDefinition(definitions[i]);
		}
	}
	
	private static void initializeActions(){
		ActionFactory af = ActionFactory.getActionFactory();
		Action[] definitions = new Action[]{
				new Kamikaze(),
				new MonsterWalk(),
				new MonsterCharge(),
				new MonsterMissile(),
				new SummonMonster(),
				new AutoFireSet(),
				new AutoFire() 
		};
		for (int i = 0; i < definitions.length; i++)
			af.addDefinition(definitions[i]);
	}
	
	private static void initializeCells(){
		MapCellFactory.getMapCellFactory().init(Cells.getCellDefinitions(AppearanceFactory.getAppearanceFactory()));
	}
	
	private static void initializeFeatures(){
		FeatureFactory.getFactory().init(Features.getFeatureDefinitions(AppearanceFactory.getAppearanceFactory()));
		SmartFeatureFactory.getFactory().init(SmartFeatures.getSmartFeatures(SelectorFactory.getSelectorFactory()));
	}

	

	private static void initializeSelectors(){
		ActionSelector [] definitions = getSelectorDefinitions();
		for (int i=0; i<definitions.length; i++){
        	SelectorFactory.getSelectorFactory().addDefinition(definitions[i]);
		}
	}
	
	private static void initializeItems(){
		ItemFactory.thus.setAmmoDefs(AmmoTypes.getAmmoTypes());
		ItemFactory.thus.setWeaponDefs(WeaponTypes.getWeaponTypes());
	}

	private static void initializeMonsters() throws CRLException{
		MonsterFactory.getFactory().init(MonsterLoader.getMonsterDefinitions("data/monsters.exml"));
	}

	private static void initializeMapData() throws CRLException{
		SpecialRoomFactory.thus.initializeMapData(RoomsLoader.getMapData("data/mapData.xml"));
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

    public static void crash(String message, Throwable exception){
    	System.out.println("MetroidRL "+Game.getVersion()+": Error");
        System.out.println("");
        System.out.println("NAV: I am sorry to inform an unrecoverable error has been produced.");
        System.out.println("Crash information will be written to crash.txt, you may want to report this bug at the game official website");
        System.out.println("");
        System.out.println("Unrecoverable error: "+message);
        exception.printStackTrace();
        if (currentGame != null){
        	System.out.println("Trying to save game");
        	try {
        		GameFiles.saveGame(currentGame, currentGame.getPlayer(), true);
        	} catch (Exception e){
        		e.printStackTrace();
        		System.out.println("Couldn't save game");
        	}
        }
        System.exit(-1);
    }
}

class SaveGameFilenameFilter implements FilenameFilter {

	public boolean accept(File arg0, String arg1) {
		//if (arg0.getName().endsWith(".sav"))
		if (arg1.endsWith(".sav"))
			return true;
		else
			return false;
	}
	
}