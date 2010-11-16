package mrl.game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import mrl.monster.MonsterDefinition;
import mrl.player.GameSessionInfo;
import mrl.player.HiScore;
import mrl.player.MonsterDeath;
import mrl.player.MPlayer;
import mrl.player.Skill;
import mrl.ui.Appearance;
import mrl.ui.UserInterface;

import sz.csi.ConsoleSystemInterface;
import sz.util.Debug;
import sz.util.FileUtil;

public class GameFiles {
	public static HiScore[] loadScores(){
		Debug.enterStaticMethod("GameFiles", "loadScores");
		HiScore[] ret = new HiScore[10];
        try{
            BufferedReader lectorArchivo = FileUtil.getReader("hiscore.tbl");
            for (int i = 0; i < 10; i++) {
            	String line = lectorArchivo.readLine();
            	String [] regs = line.split(";");
            	HiScore x = new HiScore();
            	x.setName(regs[0]);
            	x.setScore(Integer.parseInt(regs[1]));
            	x.setDate(regs[2]);
            	x.setTurns(regs[3]);
            	x.setDeathString(regs[4]);
            	x.setDeathLevel(regs[5]);
            	ret[i] = x;
            }
            Debug.exitMethod(ret);
            return ret;
        }catch(IOException ioe){
        	Game.crash("Invalid or corrupt hiscore table", ioe);
    	}
    	return null;
	}
	
	public static void saveHiScore (MPlayer player){
		Debug.enterStaticMethod("GameFiles", "saveHiscore");
		int score = player.getRating();
		String name = player.getName();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String now = sdf.format(new Date());

		HiScore [] scores = loadScores();

		try{
			BufferedWriter fileWriter = FileUtil.getWriter("hiscore.tbl");
			for (int i = 0; i < 10; i++){
				if (score > scores[i].getScore()){
           			fileWriter.write(name+";"+score+";"+now+";"+player.getGameSessionInfo().getTurns()+";"+player.getGameSessionInfo().getShortDeathString()+";"+player.getGameSessionInfo().getDeathLevelDescription());
           			fileWriter.newLine();
            		score = -1;
            		if (i == 9)
	            		break;
            	}
            	fileWriter.write(scores[i].getName()+";"+scores[i].getScore()+";"+scores[i].getDate()+";"+scores[i].getTurns()+";"+scores[i].getDeathString()+";"+scores[i].getDeathLevel());
            	fileWriter.newLine();
            }
            fileWriter.close();
            Debug.exitMethod();
        }catch(IOException ioe){
        	ioe.printStackTrace(System.out);
			Game.crash("Invalid or corrupt hiscore table", ioe);
        }
	}

	public static void saveMemorialFile(MPlayer player){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			String now = sdf.format(new Date());
			BufferedWriter fileWriter = FileUtil.getWriter("memorials/"+player.getName()+"("+now+").life");
			GameSessionInfo gsi = player.getGameSessionInfo();
			gsi.setDeathLevelDescription(player.getLevel().getDescription());
			fileWriter.write(" MetroidRL"+Game.getVersion()+ " Mission Log");fileWriter.newLine();
			fileWriter.newLine();fileWriter.newLine();
			fileWriter.write("Samus Aran: "+gsi.getDeathString()+" on "+gsi.getDeathLevelDescription()+"...");fileWriter.newLine();
			fileWriter.write("Survival time "+gsi.getTurns()+" turns  Rating: "+player.getRating());fileWriter.newLine();
			fileWriter.newLine();
			fileWriter.write(" Skills:");fileWriter.newLine();
			Vector skills = player.getAvailableSkills();
			for (int i = 0; i < skills.size(); i++){
				fileWriter.write(((Skill)skills.elementAt(i)).getMenuDescription());fileWriter.newLine();
			}
			Vector history = gsi.getHistory();
			for (int i = 0; i < history.size(); i++){
				fileWriter.write(" " + history.elementAt(i));
				fileWriter.newLine();
			}
			fileWriter.newLine();
			fileWriter.write("Terminated "+gsi.getTotalDeathCount()+" monsters");fileWriter.newLine();
			
			int i = 0;
			Enumeration monsters = gsi.getDeathCount().elements();
			while (monsters.hasMoreElements()){
				MonsterDeath mons = (MonsterDeath) monsters.nextElement();
				fileWriter.write(mons.getTimes() +" "+mons.getMonsterDescription());fileWriter.newLine();
				
				i++;
			}
			fileWriter.newLine();
			fileWriter.write("-- Available Suit Upgrades --");fileWriter.newLine();fileWriter.newLine();
			
	 		if (player.isHasXRayVisor()){
	 			fileWriter.write("X-RAY");fileWriter.newLine();
	 		}
	 		
	 		if (player.isHasThermoVisor()){
	 			fileWriter.write("THERMO");fileWriter.newLine();
			}
	 		if (player.isHasChargeBeam()){
	 			fileWriter.write("CHARGE");fileWriter.newLine();
	 		}
	 		if (player.isHasIceBeam()){
	 			fileWriter.write("ICE");fileWriter.newLine();
	 		}
	 		if (player.isHasWaveBeam()){
	 			fileWriter.write("WAVE");fileWriter.newLine();
	 		}
	 		if (player.isHasPlasmaBeam()){
	 			fileWriter.write("PLASMA");fileWriter.newLine();
	 		}	
	 		if (player.getMissilesCapacity()>0){
	 			fileWriter.write("ENERGY_MISSILE");fileWriter.newLine();
	 		}
	 		if (player.getSuperMissilesCapacity()>0){
	 			fileWriter.write("SUPER_MISSILE");fileWriter.newLine();
	 		}
	 		if (player.isEnergyBombActivated()){
	 			fileWriter.write("ENERGY_BOMB");fileWriter.newLine();
	 		}
	 		if (player.getPowerBombsCapacity()>0){
	 			fileWriter.write("POWER_BOMB");fileWriter.newLine();
	 		
	 		}
	 		if (player.isHasVariaSuit()){
	 			fileWriter.write("VARIA");fileWriter.newLine();
	 		}	
	 		if (player.isHasGravitySuit()){
	 			fileWriter.write("GRAVITY");fileWriter.newLine();
	 		}	
	 		if (player.isHasMorphBall()){
	 			fileWriter.write("MORPHBALL");fileWriter.newLine();
	 		}
	 		if (player.isHasHighJumpBooster()){
	 			fileWriter.write("HI-JUMP");fileWriter.newLine();
	 		}	
	 		
	 		if (player.isHasSpaceJump()){
	 			fileWriter.write("SPACE-JUMP");fileWriter.newLine();
	 		}
	 		if (player.isHasScrewAttack()){
	 			fileWriter.write("SCREW");fileWriter.newLine();
	 		}
	 			
	 		
	 		fileWriter.write("ENERGY: "+ player.getAbsoluteEnergy()+"/"+player.getMaxEnergy());fileWriter.newLine();
	 		if (player.getReserveEnergyMax() != 0)
	 			fileWriter.write("RESERVE ENERGY: "+player.getReserveEnergy()+"/"+player.getReserveEnergyMax());fileWriter.newLine();
	 		if (player.getMissilesCapacity() != 0)
	 			fileWriter.write("MISSILE ENERGY: "+player.getMissiles()+"/"+player.getMissilesCapacity());fileWriter.newLine();
 			if (player.getSuperMissilesCapacity() != 0)
 				fileWriter.write("SUPER MISSILE ENERGY: "+player.getSuperMissiles()+"/"+player.getSuperMissilesCapacity());fileWriter.newLine();
			if (player.getPowerBombsCapacity() != 0)
				fileWriter.write("POWER BOMB ENERGY: "+player.getPowerBombs()+"/"+player.getPowerBombsCapacity());fileWriter.newLine();
	 		
			
			fileWriter.write("-- Last Messages --");fileWriter.newLine();
			Vector messages = UserInterface.getUI().getMessageBuffer();
			for (int j = 0; j < messages.size(); j++){
				fileWriter.write(messages.elementAt(j).toString());fileWriter.newLine();
			}
			
			fileWriter.close();
		} catch (IOException ioe){
			Game.crash("Error writing the memorial file", ioe);
		}
		
	}
	
	public static void saveGame(Game g, MPlayer p){
		String filename = "savegame/"+p.getName()+".sav";
		try {
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(filename));
			p.setSelector(null);
			os.writeObject(g);
			os.close();
		} catch (IOException ioe){
			Game.crash("Error saving the game", ioe);
		}
	}
	
	public static void saveGame(Game g, MPlayer p, boolean crash){
		String filename = "savegame/"+p.getName()+".sav";
		try {
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(filename));
			p.setSelector(null);
			os.writeObject(g);
			os.close();
		} catch (IOException ioe){
			if (!crash)
				Game.crash("Error saving the game", ioe);
		}
	}
	
	public static void permadeath(MPlayer p){
		String filename = "savegame/"+p.getName()+".sav";
		if (FileUtil.fileExists(filename)) {
			FileUtil.deleteFile(filename);
		}
	}
	
}
