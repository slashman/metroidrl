package mrl.ui.consoleUI;

import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import mrl.Main;
import mrl.conf.console.data.CharCuts;
import mrl.game.Game;
import mrl.game.GameFiles;
import mrl.game.STMusicManager;
import mrl.game.STMusicManagerNew;
import mrl.player.GameSessionInfo;
import mrl.player.HiScore;
import mrl.player.MPlayer;
import mrl.ui.Appearance;
import mrl.ui.AppearanceFactory;
import mrl.ui.CommandListener;
import mrl.ui.Display;
import mrl.ui.UserAction;
import mrl.ui.UserCommand;
import mrl.ui.UserInterface;
import mrl.ui.consoleUI.cuts.CharChat;

import sz.csi.CharKey;
import sz.csi.ConsoleSystemInterface;
import sz.csi.textcomponents.TextBox;
import sz.util.Position;
import sz.util.ScriptUtil;
import sz.util.Util;

public class CharDisplay extends Display{
	private ConsoleSystemInterface si;
	
	public CharDisplay(ConsoleSystemInterface si){
		this.si = si;
	}
	
	public int showTitleScreen(){
		si.cls();
		si.print(10,5, "M E T R O I D   R L", ConsoleSystemInterface.LEMON);
		si.print(11,8, "a. ./startMission", ConsoleSystemInterface.WHITE);
		si.print(12,9, "b. ./retrieveCharacter", ConsoleSystemInterface.WHITE);
		si.print(13,10, "c. ./showLogFiles", ConsoleSystemInterface.WHITE);
		si.print(14,11, "d. ./quit", ConsoleSystemInterface.WHITE);
		si.print(10,20, "MetroidRL "+Game.getVersion()+", Developed by Santiago Zapata 2006-2010", ConsoleSystemInterface.GREEN);
		si.print(10,21, "'Metroid' is a trademark of Nintendo", ConsoleSystemInterface.GREEN);


		si.refresh();
    	STMusicManagerNew.thus.playKey("TITLE");
    	CharKey x = new CharKey(CharKey.NONE);
		while (x.code != CharKey.A && x.code != CharKey.a &&
				x.code != CharKey.B && x.code != CharKey.b &&
				x.code != CharKey.C && x.code != CharKey.c &&
				x.code != CharKey.D && x.code != CharKey.d 
				)
			x = si.inkey();
		si.cls();
		switch (x.code){
		case CharKey.A: case CharKey.a:
			return 0;
		case CharKey.B: case CharKey.b:
			return 1;
		case CharKey.C: case CharKey.c:
			return 2;
		case CharKey.D: case CharKey.d:
			return 3;
		}
		return 0;
		
	}
	
	public void showIntro(MPlayer player){
		si.cls();
		si.print(1,1, "Planet X86, (Janos P)", ConsoleSystemInterface.LEMON);
		TextBox tb1 = new TextBox(si);
		tb1.setPosition(1,3);
		tb1.setHeight(20);
		tb1.setWidth(78);
		tb1.setForeColor(ConsoleSystemInterface.GREEN);
		tb1.setText("Contact with Galactic Federation Weaponry research facility has been lost 3 days ago. "+
				"Three Scout ships sent to investigate the problem have been shot down by federation fire "+
				"and a disruption field has covered the area surrouding the base, preventing long range "
				+"scanners to detect activity.");
		tb1.draw();
		tb1.setPosition(1,8);
		tb1.clear();
		tb1.setText("Galactic Commander Adam Becgim, appointed leader of the base, has been preliminary judged "+
		"as a rebel to the federation.");
		tb1.draw();
		tb1.setPosition(1,11);
		tb1.clear();
		tb1.setText("The base was built over a plasma shaft that goes deep into the planet; spontaneous bursts "+
		"of plasma energy have been detected on the stratos of the planet, indicating possible activity "+
		"from the base. Also, a class V unidentified space craft landed on the base 1 day ago.");
		tb1.draw();
		tb1.setPosition(1,16);
		tb1.clear();
		tb1.setText("Bounty Hunter Samus Aran, you have been assigned a mission: to land safely on X86 planet, "+
		"infiltrate the base and recover sensitive weapon research data. You have been allowed to neutralize any resistance found.");
		tb1.draw();
		
		tb1.setPosition(1,20);
		tb1.clear();
		tb1.setText("You board your gunship and begin your journey.");
		tb1.draw();
		
		si.print(2,22, "[Press Space]", ConsoleSystemInterface.LEMON);
		si.refresh();
		si.waitKey(CharKey.SPACE);
		si.cls();
	}
	
	public boolean showResumeScreen(MPlayer player){
		GameSessionInfo gsi = player.getGameSessionInfo();
		si.cls();
		si.print(2,3, "Samus Aran: Mission Record {"+player.getName()+"}", ConsoleSystemInterface.LEMON);
		
		TextBox tb = new TextBox(si);
		tb.setPosition(2,5);
		tb.setHeight(3);
		tb.setWidth(70);
		tb.setForeColor(ConsoleSystemInterface.GREEN);
		tb.setText("Mission Failed: "+gsi.getDeathString()+" on the "+player.getLevel().getDescription()+".");
		tb.draw();
		
		si.print(2,10, "Survived for "+gsi.getTurns()+" turns ", ConsoleSystemInterface.GREEN);

		si.print(2,11, "Exterminate "+gsi.getTotalDeathCount()+" monsters ", ConsoleSystemInterface.GREEN);
		
		si.print(2,14, "Redirect information to persistent file? [Y/N]", ConsoleSystemInterface.LEMON);
		si.refresh();
		return UserInterface.getUI().prompt();
	}

	public void showEndgame(MPlayer player){
		si.cls();
		TextBox tb = new TextBox(si);
		tb.setPosition(2,5);
		tb.setHeight(8);
		tb.setWidth(76);
		tb.setForeColor(ConsoleSystemInterface.LEMON);
		
		tb.setText("The Mission has been completed! The weapons are recovered... but... is this the end?");
		tb.draw();
		si.print(2,16, "Thank you for playing!", ConsoleSystemInterface.GREEN);

		si.print(2,17, "MetroidRL: v"+Game.getVersion(), ConsoleSystemInterface.GREEN);
		si.print(2,18, "Santiago Zapata 2006", ConsoleSystemInterface.GREEN);

		si.print(2,20, "[Press Space]", ConsoleSystemInterface.LEMON);
		si.refresh();
		si.waitKey(CharKey.SPACE);
		si.cls();

	}
	
	public void showHiscores(HiScore[] scores){
		si.cls();

		si.print(1,1, "%%/MetroidRL"+Game.getVersion()+"/list {archives} -briefing", ConsoleSystemInterface.LEMON);

		si.print(13,4, "Rating");
		si.print(25,4, "Date");	
		si.print(36,4, "Turns");
		si.print(43,4, "Death");

		for (int i = 0; i < scores.length; i++){
			si.print(2,5+i, scores[i].getName(), ConsoleSystemInterface.LEMON);
			si.print(13,5+i, ""+scores[i].getScore(), ConsoleSystemInterface.GREEN);
			si.print(25,5+i, ""+scores[i].getDate(), ConsoleSystemInterface.GREEN);
			si.print(36,5+i, ""+scores[i].getTurns(), ConsoleSystemInterface.GREEN);
			si.print(43,5+i, ""+scores[i].getDeathString()+" on "+scores[i].getDeathLevel(), ConsoleSystemInterface.GREEN);

		}
		si.print(2,23, "[Press Space]", ConsoleSystemInterface.LEMON);
		si.refresh();
		si.waitKey(CharKey.SPACE);
		si.cls();
	}
	
	public void showHelp(){
		si.saveBuffer();
		si.cls();
		//printBars();

		
		si.print(2,0, "----------------------------", ConsoleSystemInterface.LEMON);
		si.print(2,1, "Suit Command Reference", ConsoleSystemInterface.LEMON);
		si.print(2,2, "----------------------------", ConsoleSystemInterface.LEMON);
		si.print(2,3,  "Space ./beam: Fires current beam", ConsoleSystemInterface.GREEN);
		si.print(2,4,  "Ctrl  ./jumpDevice: Initiate a rolling jump",ConsoleSystemInterface.GREEN);
		si.print(2,5,  "Enter ./autoFire: Lock onto near target, fire while moving",ConsoleSystemInterface.GREEN);
		si.print(2,6,  "1 ./ballMorph: Transform into a ball / Regain suit shape",ConsoleSystemInterface.GREEN);
		si.print(2,7,  "2 ./beamCharge: Initialize beam charging sequence",ConsoleSystemInterface.GREEN);
		si.print(2,8,  "3 ./energyBomb: Drops a small energy bomb",ConsoleSystemInterface.GREEN);
		si.print(2,9,  "4 ./missile: Shoots an energy missile",ConsoleSystemInterface.GREEN);
		si.print(2,10, "5 ./superMissile: Shoots an energy super missile",ConsoleSystemInterface.GREEN);
		si.print(2,11, "6 ./powerBomb: Drops a power bomb",ConsoleSystemInterface.GREEN);
		si.print(2,12, ", ./grab: Fetch crystals",ConsoleSystemInterface.GREEN);
		si.print(2,13, ". ./wait: Do nothing for a turn",ConsoleSystemInterface.GREEN);
		si.print(2,14, "l ./scanV: Use scan visor to get information on terrain and enemies",ConsoleSystemInterface.GREEN);
		si.print(2,15, "i ./info: Shows the available suit upgrades",ConsoleSystemInterface.GREEN);
		
		si.print(2,17, "m ./mLog: Shows the latest mission log entries.",ConsoleSystemInterface.GREEN);
		si.print(2,18, "S ./store: Stores mission data on persistant file",ConsoleSystemInterface.GREEN);
		si.print(2,19, "Q ./quit: Abort Mission",ConsoleSystemInterface.GREEN);
		si.print(2,20, "? ./man: Bring up suit command reference",ConsoleSystemInterface.GREEN);
		
		si.print(2,22,"[Space to continue]");

				UserCommand[] userCommands = new UserCommand[]{
					new UserCommand(CommandListener.SHOWMESSAGEHISTORY, CharKey.m),
					//new UserCommand(CommandListener.SHOWMAP, CharKey.M),

			};
			
			
		si.refresh();
		si.waitKey(CharKey.SPACE);
		si.restore();
		si.refresh();
	}
	
	public void init(ConsoleSystemInterface syst){
		si = syst;
	}
	
	public int showSavedGames(File[] saveFiles){
		si.cls();
		if (saveFiles.length == 0){
			si.print(3,6, "Empty");
			si.print(4,8, "[Space to Cancel]");
			si.refresh();
			si.waitKey(CharKey.SPACE);
			return -1;
		}
			
		si.print(3,6, "Select file to upload");
		for (int i = 0; i < saveFiles.length; i++){
			si.print(5,7+i, (char)(CharKey.a+i+1)+ " - "+ saveFiles[i].getName());
		}
		si.print(3,9+saveFiles.length, "[Space to Cancel]");
		si.refresh();
		CharKey x = si.inkey();
		while ((x.code < CharKey.a || x.code > CharKey.a+saveFiles.length) && x.code != CharKey.SPACE){
			x = si.inkey();
		}
		si.cls();
		if (x.code == CharKey.SPACE)
			return -1;
		else
			return x.code - CharKey.a;
	}
	
	private int readAlphaToNumber(int numbers){
		while (true){
			CharKey key = si.inkey();
			if (key.code >= CharKey.A && key.code <= CharKey.A + numbers -1){
				return key.code - CharKey.A;
			}
			if (key.code >= CharKey.a && key.code <= CharKey.a + numbers -1){
				return key.code - CharKey.a;
			}
		}
	}
	
	public void showChat(String chatID, Game game){
		si.saveBuffer();
		CharChat chat = CharCuts.thus.get(chatID);
		TextBox tb = new TextBox(si);
		tb.setBounds(3,4,40,10);
		tb.setBorder(true);
		String[] marks = new String[] {"%NAME"};
		String[] replacements = new String[] {game.getPlayer().getName()};
		for (int i = 0; i < chat.getConversations(); i++){
			tb.clear();
			tb.setText(ScriptUtil.replace(marks, replacements, chat.getConversation(i)));
			tb.setTitle(ScriptUtil.replace(marks, replacements, chat.getName(i)));
			tb.draw();
			si.refresh();
			si.waitKey(CharKey.SPACE);
		}
		si.restore();
	}
	/*
	public void showScreen(Object pScreen){
		si.saveBuffer();
		String screenText = (String) pScreen;
		TextBox tb = new TextBox(si);
		tb.setBounds(3,4,50,18);
		tb.setBorder(true);
		tb.setText(screenText);
		tb.draw();
		si.refresh();
		si.waitKey(CharKey.SPACE);
		si.restore();
	}
	*/
	public static void main(String[] args){
		//showScreen("screens/status.xml");
	}
	
	public void showScreen(Object fileName){
		/*try {
			File file = new File(fileName);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			System.out.println("Root element " + doc.getDocumentElement().getNodeName());
			NodeList nodeLst = doc.getElementsByTagName("clayer");
			System.out.println("Screen Info");
			for (int s = 0; s < nodeLst.getLength(); s++) {
				Node fstNode = nodeLst.item(s);
				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
					Element fstElmnt = (Element) fstNode;
					String fstNmElmntLst = fstElmnt.getAttribute("id");
					System.out.println("id "+fstNmElmntLst);
					NodeList fstNm = fstElmnt.getElementsByTagName("l");
					for (int t = 0; t < fstNm.getLength(); t++){
						System.out.println(fstNm.item(t).getTextContent());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		si.saveBuffer();
		try {
			File file = new File((String)fileName);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			NodeList nodeLst = doc.getElementsByTagName("clayer");
			for (int s = 0; s < nodeLst.getLength(); s++) {
				Node fstNode = nodeLst.item(s);
				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
					Element fstElmnt = (Element) fstNode;
					//System.out.println(fstElmnt.getAttribute("id"));
					int layerColor = charColorEquiv.get(fstElmnt.getAttribute("color"));
					String blank = fstElmnt.getAttribute("blank");
					NodeList fstNm = fstElmnt.getElementsByTagName("l");
					for (int t = 0; t < fstNm.getLength(); t++){
						String line = fstNm.item(t).getTextContent();
						line = line.replaceAll(" ", "е");
						if (blank != null && blank.length()>0){
							line = line.replaceAll(blank, " ");
							//System.out.println(line);
						}
						si.printx(0,t, line, layerColor);
					}
					blank = null;
				}
			}
			si.refresh();
			si.waitKey(CharKey.SPACE);
			si.restore();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public final static Hashtable<String, Integer> charColorEquiv = new Hashtable<String, Integer>();
    static
    {
    	charColorEquiv.put("BLACK",ConsoleSystemInterface.BLACK);
    	charColorEquiv.put("BLUE",ConsoleSystemInterface.BLUE);
    	charColorEquiv.put("BROWN",ConsoleSystemInterface.BROWN);
    	charColorEquiv.put("CYAN",ConsoleSystemInterface.CYAN);
    	charColorEquiv.put("DARK_BLUE",ConsoleSystemInterface.DARK_BLUE);
    	charColorEquiv.put("DARK_RED",ConsoleSystemInterface.DARK_RED);
    	charColorEquiv.put("GRAY",ConsoleSystemInterface.GRAY);
    	charColorEquiv.put("GREEN",ConsoleSystemInterface.GREEN);
    	charColorEquiv.put("LEMON",ConsoleSystemInterface.LEMON);
    	charColorEquiv.put("LIGHT_GRAY",ConsoleSystemInterface.LIGHT_GRAY);
    	charColorEquiv.put("MAGENTA",ConsoleSystemInterface.MAGENTA);
    	charColorEquiv.put("PURPLE",ConsoleSystemInterface.PURPLE);
    	charColorEquiv.put("RED",ConsoleSystemInterface.RED);
    	charColorEquiv.put("TEAL",ConsoleSystemInterface.TEAL);
    	charColorEquiv.put("WHITE",ConsoleSystemInterface.WHITE);
    	charColorEquiv.put("YELLOW",ConsoleSystemInterface.YELLOW);
    }
    
	private Hashtable locationKeys;
	{
		locationKeys = new Hashtable();
		locationKeys.put("TOWN", new Position(15,15));
		locationKeys.put("FOREST", new Position(23,15));
		locationKeys.put("BRIDGE", new Position(30,15));
		locationKeys.put("ENTRANCE", new Position(36,15));
		locationKeys.put("HALL", new Position(41,15));
		locationKeys.put("LAB", new Position(39,12));
		locationKeys.put("CHAPEL", new Position(37,9));
		locationKeys.put("RUINS", new Position(45,10));
		locationKeys.put("CAVES", new Position(46,18));
		locationKeys.put("COURTYARD", new Position(52,17));
		locationKeys.put("DUNGEON", new Position(60,18));
		locationKeys.put("STORAGE", new Position(63,12));
		locationKeys.put("CLOCKTOWER", new Position(62,7));
		locationKeys.put("KEEP", new Position(52,5));
	}
	
	String[] mapImage = new String[]{
			"                                                                                ",
			" ''`.--..,''`_,''`.-''----.----..'     '`''''..,'''-'    `_,,'''`--- ./  ,'-.   ",
			" '                                                 /\\                   |,. `.  ",
			"  |                                               /  \\                    `.... ",
			"  |                                              | /-\\|      /'\\              | ",
			"  |                                               \\| |\\    .'   |             | ",
			"  |                            O    /\\             \\-/ \\   . /-\\`             | ",
			"  |                                |  |            `.===``/==| | \\           ,  ",
			" .\"                                |/-\\_              `===== \\-/  `.          \\ ",
			" |                                 `| |==.../-\\       .'      =    |          | ",
			" |                                 /\\-/ ====| ||  ,-.'.       ==   /         /  ",
			" \\.                                | =/-\\   \\-/| |   | '|     /-\\ /           \\ ",
			"  |                               .' =| |=   = | |   '..'     | | |           / ",
			"  |            ,-.     ..--.      |.  \\-/=   = |  |           \\-/  \\         |  ",
			"  |          _/-\\|.  ,/-\\  `./-\\   /-\\  /-\\  =  \\ |            =   |.        <. ",
			"  :|      ,,' | |=====| |====| |===| |==| |  ==  \\ `'\\.        =    `.        | ",
			"  |    _,'    \\-/     \\-/    \\-/   \\-/  \\-/   =   |/-\\|        =     |        | ",
			"   |.-'                                      /-\\ ==| |===  /-\\ =     `.      |  ",
			" .-' ''`''-.    ,'`..,''''''`.               | |== \\-/  ===| |==       ``.   `. ",
			" |.         ---'             `._,..,_        \\-/  ,..      \\-/   _  __   ___. | ",
			"  '                                  `.     .-''''   \\.   _....'' `'  \\''    ./ ",
			"  |                                    `_,.-          '`--'                  `. ",
			"  | ..           _                      .                                     | ",
			" |../'....,'-.../ ``...,''`--....''`..,' `\"-..,''`....,`...-'..''......'`...,-' ",
			"                                                                                "
	};

	
	public void showNav(String text){
		si.saveBuffer();
		TextBox tb = new TextBox(si);
		tb.setBounds(3,4,60,8);
		tb.setBorder(true);
		tb.setTitle("NAV");
		tb.setBorderColor(ConsoleSystemInterface.LEMON);
		tb.setText(text);
		tb.draw();
		si.refresh();
		si.waitKey(CharKey.SPACE);
		si.restore();		
	}
	public void showMap(String locationKey, String locationDescription) {
		si.saveBuffer();
		for (int i = 0; i < 25; i++){
			si.print(0,i, mapImage[i], CharAppearance.BROWN);
		}
		si.print(15, 11, locationDescription);
		Position location = (Position) locationKeys.get(locationKey);
		si.print(location.x, location.y, "X", CharAppearance.RED);
		si.waitKey(CharKey.SPACE);
		si.restore();
	}
	
	public void showScan(mrl.monster.Monster who, MPlayer player) {
		CharAppearance app = (CharAppearance)who.getAppearance();
		//si.cls();
	
		int visorColor = 0;
		int visorColorH = 0;
		if (player.isGravitySuitActivated()){
			visorColor = ConsoleSystemInterface.PURPLE;
			visorColorH = ConsoleSystemInterface.MAGENTA;
		} else if (player.isVariaSuitActivated()) {
			visorColor = ConsoleSystemInterface.RED;
			visorColorH = ConsoleSystemInterface.DARK_RED;
    	} else {
    		visorColor = ConsoleSystemInterface.YELLOW;
    		visorColorH = ConsoleSystemInterface.BROWN;
    	}

		si.printx(0,0, "еееее'---..,-._                                                 _.-,..---`ееееее", visorColor);
		si.printx(0,1, "еееееееееееееее`-........_                           _........-'ееееееееееееееее", visorColor);
		si.printx(0,2, "ееееееееееееееееееееееееее`,_                     _,'еееееееееееееееееееееееееее", visorColor);
		si.printx(0,3, "еееееееееееееееееееееееееееее`-------------------'ееееееееееееееееееееееееееееее", visorColor);
		si.printx(0,4, "еееееееееееееееееееее_,--..еееееееееееееееееееееееее..--,_ееееееееееееееееееееее", visorColorH);
		si.printx(0,5, "еееееееееееееееееее.'      \\......................./      `.ееееееееееееееееееее", visorColorH);
		si.printx(0,6, "ееееееееееееееее,''                                         ``,еееееееееееееееее", visorColorH);
		si.printx(0,7, "еееееееееееееее/                                               \\ееееееееееееееее", visorColorH);
		si.printx(0,8, "ееееееееееееее|'                                               `|еееееееееееееее", visorColorH);
		si.printx(0,9, "еееееееееееееее|                                               |ееееееееееееееее", visorColorH);
		si.printx(0,10,"ееееееееееееееее\\                                             /еееееееееееееееее", visorColorH);
		si.printx(0,11,"еееееееееееееееее`.                                         .'ееееееееееееееееее", visorColorH);
		si.printx(0,12,"еееееееееееееееееее`-.                                   .-'ееееееееееееееееееее", visorColorH);
		si.printx(0,13,"ееееееееееееееееееее,>..-.......---------------.......-..<,еееееееееееееееееееее", visorColorH);
		si.printx(0,14,"еееееееееееееееее,-'                                       `-,ееееееееееееееееее", visorColorH);
		si.printx(0,15,"ееееееееееееееее/                                             \\еееееееееееееееее", visorColorH);
		si.printx(0,16,"ееееееееееееееее|                                             |еееееееееееееееее", visorColorH);
		si.printx(0,17,"ееееееееееееееее|                                             |еееееееееееееееее", visorColorH);
		si.printx(0,18,"ееееееееееееееее|                                             |еееееееееееееееее", visorColorH);
		si.printx(0,19,"ееееееееееееееее\\                                             /еееееееееееееееее", visorColorH);
		si.printx(0,20,"еееееееееееееееее`-.______________           ______________.-'ееееееееееееееееее", visorColorH);
		si.printx(0,21,"ееееееееееее_,---.....ееееееееееее`.........'ееееееееееее.....---,_еееееееееееее", visorColor);
		si.printx(0,22,"ееее__,,-''-'         `'`..еееееееееееееееееееееееее..'`'         `-``-,,__еееее", visorColor);
		
		if (who.getPortrait() != null)
			who.getPortrait().drawOver(si, 21, 6);
		
		int msgWidth = (int)Math.round((39 - who.getDescription().length())/2.0D);
		si.print(21+msgWidth,14, who.getDescription(), ConsoleSystemInterface.LEMON);
		si.print(39,21,app.getChar(),app.getColor());
		
		TextBox tb = new TextBox(si);
		tb.setPosition(19,15);
		tb.setHeight(5);
		tb.setBorder(false);
		tb.setWidth(41);
		tb.setForeColor(ConsoleSystemInterface.WHITE);
		if (who.getLongDescription() != null)
			tb.setText(who.getLongDescription());
		tb.draw();
		//si.print(2,20, "[Press Space]",ConsoleSystemInterface.WHITE);
		si.refresh();
		si.waitKey(CharKey.SPACE);
		si.restore();
		si.refresh();
	}
	
	
}
