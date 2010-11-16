package mrl.data;


import java.util.*;
import java.io.*;

import mrl.ai.ActionSelector;
import mrl.ai.BasicMonsterAI;
import mrl.ai.MonsterAI;
import mrl.ai.RangedAttack;
import mrl.ai.monster.RangedAI;
import mrl.ai.monster.StationaryAI;
import mrl.ai.monster.WanderToPlayerAI;
import mrl.game.CRLException;
import mrl.monster.*;
import mrl.ui.AppearanceFactory;
import mrl.ui.consoleUI.CharImage;

import org.xml.sax.AttributeList;
import org.xml.sax.ContentHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import sz.util.*;
import sz.crypt.*;
import sz.csi.ConsoleSystemInterface;
import uk.co.wilson.xml.MinML;


public class MonsterLoader {
	
	public static MonsterDefinition[] getMonsterDefinitions(String monsterFile) throws CRLException{
		try{
	        MonsterDocumentHandler handler = new MonsterDocumentHandler();
	        //XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
	        MinML parser = new MinML();
	        
	        DESEncrypter encrypter = new DESEncrypter("If you can see this, go ahead and get the monsters info");
	        //parser.setContentHandler(handler);
	        parser.setDocumentHandler(handler);
	        parser.parse(new InputSource(encrypter.decrypt(new FileInputStream(monsterFile))));
	        Vector monsters = handler.getMonsterDefinitions();
	        MonsterDefinition[] ret = new MonsterDefinition[monsters.size()];
	        for (int i = 0; i < monsters.size(); i++)
	        	ret[i] = (MonsterDefinition) monsters.elementAt(i);
	        return ret;
		} catch (IOException ioe){
			throw new CRLException("Error while loading data from monster file");
		} catch (SAXException sax){
			sax.printStackTrace();
			throw new CRLException("Error while loading data from monster file");
		}
    }

}



class MonsterDocumentHandler implements DocumentHandler{
    private Vector defVector = new Vector (10);
   
    private MonsterDefinition currentMD;
    private ActionSelector currentSelector;
    private Vector currentRangedAttacks;
    private CharImage currentCharImage;
	private ArrayList currentCharDataArrayList = new ArrayList();
	private ArrayList currentColorDataArrayList = new ArrayList();
	
	
    
    public Vector getMonsterDefinitions (){
        return defVector;
    }
    
    public void startDocument() throws org.xml.sax.SAXException {}
    
    public void startElement(String localName, AttributeList at) throws org.xml.sax.SAXException {
        if (localName.equals("monster")){
        	currentMD = new MonsterDefinition(at.getValue("id"));
        	currentMD.setAppearance(AppearanceFactory.getAppearanceFactory().getAppearance(at.getValue("appearance")));
        	currentMD.setDescription(at.getValue("description"));
        	if (at.getValue("longDescription") != null)
        		currentMD.setLongDescription(at.getValue("longDescription"));
        	else
        		currentMD.setLongDescription("No detailed info found.");
        	
        	if (at.getValue("effectWav")!= null)
        		currentMD.setWavOnHit(at.getValue("effectWav"));
        	if (at.getValue("hits") != null)
        		currentMD.setMaxHits(inte(at.getValue("hits")));
        	if (at.getValue("attack") != null)
        		currentMD.setAttack(inte(at.getValue("attack")));
        	if (at.getValue("minLevel") != null)
        		currentMD.setMinLevel(inte(at.getValue("minLevel")));
        	if (at.getValue("maxLevel") != null)
        		 currentMD.setMaxLevel(inte(at.getValue("maxLevel")));
        	if (at.getValue("sightRange") != null)
        		currentMD.setSightRange(inte(at.getValue("sightRange")));
        	if (at.getValue("ethereal") != null)
        		currentMD.setEthereal(at.getValue("ethereal").equals("true"));
        	if (at.getValue("canSwim") != null)
        		currentMD.setCanSwim(at.getValue("canSwim").equals("true"));
        	if (at.getValue("walkCost") != null){
        		currentMD.setWalkCost(inte(at.getValue("walkCost")));
        		currentMD.setAttackCost(inte(at.getValue("walkCost")));
        	}
        	if (at.getValue("evadeChance")!=null){
        		currentMD.setEvadeChance(inte(at.getValue("evadeChance")));
        		currentMD.setEvadeMessage(at.getValue("evadeMessage"));
        	}
        	if (at.getValue("autorespawns")!=null){
        		currentMD.setAutorespawnCount(inte(at.getValue("autorespawns")));
        	}
        	
        } else
        if (localName.equals("sel_wander")){
        	currentSelector = new WanderToPlayerAI();
        } else
        
    	if (localName.equals("sel_stationary")){
        	currentSelector = new StationaryAI();
        }else
    	if (localName.equals("sel_basic")){
        	currentSelector = new BasicMonsterAI();
        	if (at.getValue("stationary") != null)
        		((BasicMonsterAI)currentSelector).setStationary(at.getValue("stationary").equals("true"));
        	if (at.getValue("approachLimit") != null)
        		((BasicMonsterAI)currentSelector).setApproachLimit(inte(at.getValue("approachLimit")));
        	if (at.getValue("waitPlayerRange") != null)
        		((BasicMonsterAI)currentSelector).setWaitPlayerRange(inte(at.getValue("waitPlayerRange")));
        	if (at.getValue("patrolRange") != null)
        		((BasicMonsterAI)currentSelector).setPatrolRange(inte(at.getValue("patrolRange")));
        }else
        if (localName.equals("sel_ranged")){
        	currentSelector = new RangedAI();
        	((RangedAI)currentSelector).setApproachLimit(inte(at.getValue("approachLimit")));
        }else
    	if (localName.equals("rangedAttacks")){
    		currentRangedAttacks = new Vector(10);
    	} else 
		if (localName.equals("rangedAttack")){
			int damage = 0;
			try {
				damage = Integer.parseInt(at.getValue("damage")); 
			} catch (NumberFormatException nfe){
				
			}
				
			RangedAttack ra = new RangedAttack(
					at.getValue("id"),
					at.getValue("type"),
					at.getValue("status_effect"),
					Integer.parseInt(at.getValue("range")), 
					Integer.parseInt(at.getValue("frequency")),
					at.getValue("message"),
					at.getValue("effectType"),
					at.getValue("effectID"),
					damage
					
					//color
					);
			if (at.getValue("effectWav") != null)
				ra.setEffectWav(at.getValue("effectWav"));
			if (at.getValue("summonMonsterId") != null)
				ra.setSummonMonsterId(at.getValue("summonMonsterId"));
			if (at.getValue("charge") != null)
				ra.setChargeCounter(inte(at.getValue("charge")));
			
			currentRangedAttacks.add(ra);
		}else if (localName.equals("charImage")){
			currentCharImage = new CharImage();
			currentMD.setPortrait(currentCharImage);
		}else if (localName.equals("charsData")){
			currentCharDataArrayList.clear();
		}else if (localName.equals("colorData")){
			currentColorDataArrayList.clear();
		}else if (localName.equals("line")){
			currentCharDataArrayList.add(at.getValue("l"));
		}else if (localName.equals("colorLine")){
			currentColorDataArrayList.add(at.getValue("l"));
		}
    }
    
    public void endElement(String localName) throws org.xml.sax.SAXException {
        if (localName.equals("rangedAttacks")){
        	((MonsterAI)currentSelector).setRangedAttacks(currentRangedAttacks);
        }
        else
        if (localName.equals("selector")){
            currentMD.setDefaultSelector(currentSelector);
        }
        else
        if (localName.equals("monster")){
            defVector.add(currentMD);
        }else if (localName.equals("charsData")){
        	String[] charDataStr = (String[]) currentCharDataArrayList.toArray(new String[currentCharDataArrayList.size()]);
        	char[][] charData = new char[charDataStr.length][];
        	for (int i = 0; i < charData.length; i++){
        		charData[i] = charDataStr[i].toCharArray(); 
        	}
			currentCharImage.setImageData(charData);
		}else if (localName.equals("colorData")){
			String[] colorDataStr = (String[]) currentColorDataArrayList.toArray(new String[currentColorDataArrayList.size()]);
        	int[][] colorData = new int[colorDataStr.length][];
        	for (int i = 0; i < colorData.length; i++){
        		colorData[i] = new int [colorDataStr[i].length()];
        		for (int j = 0; j < colorDataStr[i].length(); j++){
        			colorData[i][j] = defineColorFor (colorDataStr[i].charAt(j));
        		}
        	}
			currentCharImage.setColorData(colorData);
		}
    }
    
    private int defineColorFor(char val){
    	//System.out.println(val);
    	return Integer.parseInt((String)charColorEquiv.get(val+""));
    }
    
    public final static Hashtable charColorEquiv = new Hashtable();
    static
    {
    	charColorEquiv.put(" ",ConsoleSystemInterface.BLACK+"");
    	charColorEquiv.put("b",ConsoleSystemInterface.BLUE+"");
    	charColorEquiv.put("B",ConsoleSystemInterface.BROWN+"");
    	charColorEquiv.put("c",ConsoleSystemInterface.CYAN+"");
    	charColorEquiv.put("d",ConsoleSystemInterface.DARK_BLUE+"");
    	charColorEquiv.put("D",ConsoleSystemInterface.DARK_RED+"");
    	charColorEquiv.put("g",ConsoleSystemInterface.GRAY+"");
    	charColorEquiv.put("G",ConsoleSystemInterface.GREEN+"");
    	charColorEquiv.put("l",ConsoleSystemInterface.LEMON+"");
    	charColorEquiv.put("g",ConsoleSystemInterface.LIGHT_GRAY+"");
    	charColorEquiv.put("m",ConsoleSystemInterface.MAGENTA+"");
    	charColorEquiv.put("p",ConsoleSystemInterface.PURPLE+"");
    	charColorEquiv.put("r",ConsoleSystemInterface.RED+"");
    	charColorEquiv.put("t",ConsoleSystemInterface.TEAL+"");
    	charColorEquiv.put(".",ConsoleSystemInterface.WHITE+"");
    	charColorEquiv.put("Y",ConsoleSystemInterface.YELLOW+"");
    }
    
    
    public void characters(char[] values, int param, int param2) throws org.xml.sax.SAXException {
    	
    }

    public void endDocument() throws org.xml.sax.SAXException {}

    public void endPrefixMapping(String str) throws org.xml.sax.SAXException {}

    public void ignorableWhitespace(char[] values, int param, int param2) throws org.xml.sax.SAXException {}

    public void processingInstruction(String str, String str1) throws org.xml.sax.SAXException {}

    public void setDocumentLocator(org.xml.sax.Locator locator) {}

    public void skippedEntity(String str) throws org.xml.sax.SAXException {}

    public void startPrefixMapping(String str, String str1) throws org.xml.sax.SAXException {}
    
    private int inte(String s){
    	return Integer.parseInt(s);
    }
}
	