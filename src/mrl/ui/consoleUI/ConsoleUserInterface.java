package mrl.ui.consoleUI;

import java.util.*;

import mrl.action.*;
import mrl.actor.*;
import mrl.ai.*;
import mrl.feature.*;
import mrl.item.Item;
import mrl.level.*;
import mrl.monster.*;
import mrl.player.*;
import mrl.ui.*;
import mrl.ui.consoleUI.effects.CharEffect;
import mrl.ui.effects.*;

import sz.csi.CharKey;
import sz.csi.ConsoleSystemInterface;
import sz.csi.textcomponents.BasicListItem;
import sz.csi.textcomponents.ListBox;
import sz.csi.textcomponents.MenuBox;
import sz.csi.textcomponents.TextInformBox;
import sz.util.*;

//import crl.action.vkiller.Whip;

/** 
 *  Shows the level using characters.
 *  Informs the Actions and Commands of the player.
 * 	Must be listening to a System Interface
 */

public class ConsoleUserInterface extends UserInterface implements CommandListener, Runnable{
	private static UISelector selector;
	
	//Attributes
	private int xrange = 25;
	private int yrange = 9;
	
	//Components
	private TextInformBox messageBox;
	private ListBox idList;
	
	private boolean eraseOnArrival; // Erase the buffer upon the arrival of a new msg
	private String lastMessage; 
    private StringBuffer messageBuffer = new StringBuffer();
	private int msgCounter;
	
	private Hashtable /*BasicListItem*/ sightListItems = new Hashtable();
	// Relations

	//private Monster lockedMonster;
	//private Vector visibleMonsters = new Vector();
	
 	private transient ConsoleSystemInterface si;

	// Setters
	/** Sets the object which will be informed of the player commands.
     * this corresponds to the Game object */
	
	//Getters

    // Smart Getters
    public Position getAbsolutePosition(Position insideLevel){
    	Position relative = Position.subs(insideLevel, player.getPosition());
		return Position.add(PC_POS, relative);
	}

	public final Position
				VP_START = new Position(1,3),
				VP_END = new Position (51,21),
				PC_POS = new Position (26,12);

    //private final int WEAPONCODE = CharKey.SPACE;

    private boolean [][] FOVMask;
    //Interactive Methods
    public void doLook(){
		Position offset = new Position (0,0);
		messageBox.setForeColor(ConsoleSystemInterface.LEMON);
		si.saveBuffer();
		Monster lookedMonster  = null;
		while (true){
			Position browser = Position.add(player.getPosition(), offset);
			String looked = "";
			si.restore();
			if (FOVMask[PC_POS.x + offset.x][PC_POS.y + offset.y]){
				Cell choosen = level.getMapCell(browser);
				Feature feat = level.getFeatureAt(browser);
				ArrayList<Item> items = level.getItemsAt(browser);
				
				Actor actor = level.getActorAt(browser);
				lookedMonster = level.getMonsterAt(browser);
				if (choosen != null)
					looked += choosen.getDescription();
				if (level.getBloodAt(browser) != null)
				    looked += "{bloody}";
				if (feat != null)
					looked += ", "+ feat.getDescription();
				if (actor != null)
					looked += ", "+ actor.getDescription() + "(s to scan)";
				if (items != null && items.size() > 0){
					looked += ", "+ items.get(0).getDescription();
					if (items.size()>1){
						looked += " and something else";
					}
				}
			}
			messageBox.setText(looked);
			messageBox.draw();
			si.print(PC_POS.x + offset.x, PC_POS.y + offset.y, '_', ConsoleSystemInterface.LEMON);
			si.refresh();
			CharKey x = new CharKey(CharKey.NONE);
			while (x.code != CharKey.SPACE && x.code != CharKey.s &&
				   ! x.isArrow())
				x = si.inkey();
			if (x.code == CharKey.SPACE || x.code == CharKey.ESC){
				si.restore();
				break;
			}
			if (x.code == CharKey.s){
				if (lookedMonster != null)
					Display.thus.showScan(lookedMonster, player);
			} 

			if (x.isArrow())
				offset.add(Action.directionToVariation(Action.toIntDirection(x)));

			if (offset.x >= xrange) offset.x = xrange;
			if (offset.x <= -xrange) offset.x = -xrange;
			if (offset.y >= yrange) offset.y = yrange;
			if (offset.y <= -yrange) offset.y = -yrange;
     	}
		messageBox.setText("Look mode off");
		refresh();
	}

    
    // Drawing Methods
	public void drawEffect(Effect what){
		//Debug.enterMethod(this, "drawEffect", what);
		if (what == null)
			return;
		//drawLevel();
		if (insideViewPort(getAbsolutePosition(what.getPosition()))){
			si.refresh();
			si.setAutoRefresh(true);
			((CharEffect)what).drawEffect(this, si);
			si.setAutoRefresh(false);
		}
		//Debug.exitMethod();
	}
	
	public boolean isOnFOVMask(int x, int y){
		return FOVMask[x][y];
	}

	private Cell[] hrowCells = new Cell[11];
	private Cell[] vrowCells = new Cell[11];
	private Monster[] hrowMons = new Monster[11];
	private Monster[] vrowMons = new Monster[11];
     
	private void drawLevel(){
		Debug.enterMethod(this, "drawLevel");
		for (int i = 0; i < hrowCells.length; i++) {
			hrowCells[i] = null;
			vrowCells[i] = null;
			hrowMons[i] = null;
			vrowMons[i] = null;
		}
		//Cell[] [] cells = level.getCellsAround(player.getPosition().x,player.getPosition().y, player.getPosition().z, range);
		Cell[] [] rcells = level.getMemoryCellsAround(player.getPosition().x,player.getPosition().y, player.getPosition().z, xrange,yrange);
		Cell[] [] vcells = level.getVisibleCellsAround(player.getPosition().x,player.getPosition().y, player.getPosition().z, xrange,yrange);
		
		Position runner = new Position(player.getPosition().x - xrange, player.getPosition().y-yrange, player.getPosition().z);
		
		for (int x = 0; x < rcells.length; x++){
			for (int y=0; y<rcells[0].length; y++){
				if (rcells[x][y] != null){
					CharAppearance app = (CharAppearance)rcells[x][y].getAppearance(); 
					char cellChar = app.getChar();
					if (level.getFrostAt(runner) != 0){
						cellChar = '#';
					}
					//if (!level.isVisible(runner.x, runner.y))
					if (vcells[x][y] == null)
						si.print(PC_POS.x-xrange+x,PC_POS.y-yrange+y, cellChar, ConsoleSystemInterface.GRAY);
				} else
					si.print(PC_POS.x-xrange+x,PC_POS.y-yrange+y, CharAppearance.getVoidAppearance().getChar(), CharAppearance.BLACK);
				runner.y++;
			}
			runner.y = player.getPosition().y-yrange;
			runner.x ++;
		}
		
		
		runner.x = player.getPosition().x - xrange;
		runner.y = player.getPosition().y-yrange;
		
		monstersOnSight.removeAllElements();
		featuresOnSight.removeAllElements();
		itemsOnSight.removeAllElements();
		player.resetMonstersOnSight();
		
		for (int x = 0; x < vcells.length; x++){
			for (int y=0; y<vcells[0].length; y++){
				FOVMask[PC_POS.x-xrange+x][PC_POS.y-yrange+y] = false;
				if (vcells[x][y] != null){
					FOVMask[PC_POS.x-xrange+x][PC_POS.y-yrange+y] = true;
					String bloodLevel = level.getBloodAt(runner);
					CharAppearance cellApp = (CharAppearance)vcells[x][y].getAppearance();
					int cellColor = cellApp.getColor();
					if (bloodLevel != null){
						switch (Integer.parseInt(bloodLevel)){
							case 0:
								cellColor = ConsoleSystemInterface.RED;
								break;
							case 1:
								cellColor = ConsoleSystemInterface.DARK_RED;
								break;
							case 8:
								cellColor = ConsoleSystemInterface.LEMON;
								break;
						}
					}
					char cellChar = cellApp.getChar();
					if (level.getFrostAt(runner) != 0){
						cellChar = '#';
						cellColor = ConsoleSystemInterface.CYAN;
					}
					if (level.getDepthFromPlayer(player.getPosition().x - xrange + x, player.getPosition().y - yrange + y) != 0 ){
						cellColor = ConsoleSystemInterface.TEAL;
					}

					if (x!=xrange || y != yrange)
						si.print(PC_POS.x-xrange+x,PC_POS.y-yrange+y, cellChar, cellColor);
					
					if (x == xrange){
						//walking thru x-axis
						if (y>=yrange-5 && y <=yrange+5){
							hrowCells[y-yrange+5] = vcells[x][y];
							if (level.getMonsterAt(runner) != null){
								hrowMons[y-yrange+5] = level.getMonsterAt(runner);
							}
						}
					}
					if (y == yrange){
						//walking thru y-axis
						if (x>=xrange-5 && x <=xrange+5){
							vrowCells[x-xrange+5] = vcells[x][y];
							if (level.getMonsterAt(runner) != null){
								vrowMons[x-xrange+5] = level.getMonsterAt(runner);
							}
						}
					}
					Feature feat = level.getFeatureAt(runner);
					if (feat != null){
						if (feat.isVisible()) {
							BasicListItem li = (BasicListItem)sightListItems.get(feat.getID());
							if (li == null){
								Debug.say("Adding "+feat.getID()+" to the hashtable");
								sightListItems.put(feat.getID(), new BasicListItem(((CharAppearance)feat.getAppearance()).getChar(), ((CharAppearance)feat.getAppearance()).getColor(), feat.getDescription()));
								li = (BasicListItem)sightListItems.get(feat.getID());
							}
							if (feat.isRelevant() && !featuresOnSight.contains(li))
								featuresOnSight.add(li);
							CharAppearance featApp = (CharAppearance)feat.getAppearance();
							si.print(PC_POS.x-xrange+x,PC_POS.y-yrange+y, featApp.getChar(), featApp.getColor());
						}
					}
					
					SmartFeature sfeat = level.getSmartFeature(runner);
					if (sfeat != null){
						if (sfeat.isVisible()){
							CharAppearance featApp = 
								(CharAppearance)sfeat.getAppearance();
							si.print(PC_POS.x-xrange+x,PC_POS.y-yrange+y, featApp.getChar(), featApp.getColor());
						}
					}
					
					ArrayList<Item> items = level.getItemsAt(runner);
					if (items != null && items.size() > 0){
						CharAppearance itemApp = (CharAppearance) items.get(0).getAppearance();
						si.print(PC_POS.x-xrange+x,PC_POS.y-yrange+y, itemApp.getChar(), itemApp.getColor());
						for (Item item : items) {
							CharAppearance itemApp2 = (CharAppearance)item.getAppearance();
							BasicListItem li = (BasicListItem)sightListItems.get(item.getID());
							if (li == null){
								sightListItems.put(item.getID(), new BasicListItem(itemApp2.getChar(), itemApp2.getColor(), item.getDescription()));
								li = (BasicListItem)sightListItems.get(item.getID());
							}
							if (!itemsOnSight.contains(li))
								itemsOnSight.add(li);
						}
						
					}
					
					Monster monster = level.getMonsterAt(runner);
					if (monster != null && monster.isVisible()){
						BasicListItem li = null;
						li = (BasicListItem)sightListItems.get(monster.getID());
						if (li == null){
							CharAppearance monsterApp = (CharAppearance)monster.getAppearance();
							sightListItems.put(monster.getID(), new BasicListItem(monsterApp.getChar(), monsterApp.getColor(), monster.getDescription()));
							li = (BasicListItem)sightListItems.get(monster.getID());
						}
						
						if (!monstersOnSight.contains(li))
							monstersOnSight.add(li);
						player.addMonsterOnSight(monster);
						CharAppearance monsterApp = (CharAppearance) monster.getAppearance();
						if (monster.canSwim() && level.getMapCell(runner)!= null && level.getMapCell(runner).isWater())
							si.print(PC_POS.x-xrange+x,PC_POS.y-yrange+y, '~', monsterApp.getColor());
						else
						if (monster.isFrozen())
							si.print(PC_POS.x-xrange+x,PC_POS.y-yrange+y, monsterApp.getChar(), ConsoleSystemInterface.CYAN);
						else 
						if (monster == player.getLockedMonster())
							si.print(PC_POS.x-xrange+x,PC_POS.y-yrange+y, monsterApp.getChar(), ConsoleSystemInterface.LEMON);
						else
							si.print(PC_POS.x-xrange+x,PC_POS.y-yrange+y, monsterApp.getChar(), monsterApp.getColor());
					}
					
					si.print(PC_POS.x,PC_POS.y, ((CharAppearance)player.getAppearance()).getChar(), ((CharAppearance)player.getAppearance()).getColor());
				}
				runner.y++;
			}
			runner.y = player.getPosition().y-yrange;
			runner.x ++;
		}
		
		/*monstersList.clear();
		itemsList.clear();*/
		idList.clear(); 

		idList.addElements(monstersOnSight);
		idList.addElements(itemsOnSight);
		idList.addElements(featuresOnSight);
		
		String t1 = "";
		String t2 = "";
		String t3 = "";
		String t4 = "";
		String t5 = "";
		for (int i = 0; i < hrowCells.length; i++) {
			Cell cell = hrowCells[i];
			if (cell != null){
				if (cell.isSolid()){ 
					t1+="#";
					t2+="#";
					t3+="#";
					t4+="-";
					t5+=" ";
				} else if (cell.getHeight() == 0){ 
					t1+=".";
					t2+=".";
					t3+=".";
					t4+="-";
					t5+=" ";
				} else if (cell.getHeight() == 1){ 
					t1+=".";
					t2+=".";
					t3+="#";
					t4+="-";
					t5+=" ";
				} else if (cell.getHeight() == 2){ 
					t1+=".";
					t2+="#";
					t3+="#";
					t4+="-";
					t5+=" ";
				} 
			} else {
				t1+=" ";
				t2+=" ";
				t3+=" ";
				t4+=" ";
				t5+=" ";
			}
		} 
		si.print(52,17,t1);
		si.print(52,18,t2);
		si.print(52,19,t3);
		si.print(52,20,t4);
		si.print(52,21,t5);
		
		t1 = "";
		t2 = "";
		t3 = "";
		t4 = "";
		t5 = "";
		for (int i = 0; i < vrowCells.length; i++) {
			Cell cell = vrowCells[i];
			if (cell != null){
				if (cell.isSolid()){ 
					t1+="#";
					t2+="#";
					t3+="#";
					t4+="-";
					t5+=" ";
				} else if (cell.getHeight() == 0){ 
					t1+=".";
					t2+=".";
					t3+=".";
					t4+="-";
					t5+=" ";
				} else if (cell.getHeight() == 1){ 
					t1+=".";
					t2+=".";
					t3+="#";
					t4+="-";
					t5+=" ";
				} else if (cell.getHeight() == 2){ 
					t1+=".";
					t2+="#";
					t3+="#";
					t4+="-";
					t5+=" ";
				}
			} else {
				t1+=" ";
				t2+=" ";
				t3+=" ";
				t4+=" ";
				t5+=" ";
			}
		} 
		si.print(64,17,t1);
		si.print(64,18,t2);
		si.print(64,19,t3);
		si.print(64,20,t4);
		si.print(64,21,t5);
		
		for (int i = 0; i < hrowMons.length; i++) {
			if (hrowMons[i] != null){
				CharAppearance app = (CharAppearance)hrowMons[i].getAppearance();
				si.print(52+i,19,app.getChar(), app.getColor());
			}
		}
		
		for (int i = 0; i < vrowMons.length; i++) {
			if (vrowMons[i] != null){
				CharAppearance app = (CharAppearance)vrowMons[i].getAppearance();
				si.print(64+i,19,app.getChar(), app.getColor());
			}
		}
		CharAppearance app = (CharAppearance)player.getAppearance();
		si.print(57,19,app.getChar(), app.getColor());
		si.print(69,19,app.getChar(), app.getColor());
		
		si.print(57,20,"Y");
		si.print(69,20,"X");
		
		Debug.exitMethod();
	}
	
	private Vector messageHistory = new Vector(20,10);
	public void addMessage(Message message){
		Debug.enterMethod(this, "addMessage", message);
		if (eraseOnArrival){
	 		messageBox.clear();
	 		messageBox.setForeColor(ConsoleSystemInterface.LEMON);
	 		eraseOnArrival = false;
		}
		if ((player != null && player.getPosition() != null && message.getLocation().z != player.getPosition().z) || (message.getLocation() != null && !insideViewPort(getAbsolutePosition(message.getLocation())))){
			Debug.exitMethod();
			return;
		}
		messageHistory.add(message.getText());
		if (messageHistory.size()>100)
			messageHistory.removeElementAt(0);
		messageBox.addText(message.getText());
		
		messageBox.draw();
		Debug.exitMethod();
		
	}

	
    private void drawPlayerStatus(){
	    Debug.enterMethod(this, "drawPlayerStatus");
	    
	    int rest1 = player.getEnergyTanks()>10 ? player.getEnergyTanks()-10:0; 
	    int rest1x = player.getEnergyTanksCapacity()>10 ? player.getEnergyTanksCapacity()-10:0;
	    int rest2 = player.getEnergyTanks() > 10 ? 10 : player.getEnergyTanks();
	    int rest2x = player.getEnergyTanksCapacity() > 10 ? 10 : player.getEnergyTanksCapacity();
	    
    	for (int i = 0; i < 10; i++)
   			if (i+1 <= rest1x)
   				if (i+1 <= rest1)
   					si.print(i+2,1,'O', ConsoleSystemInterface.PURPLE);
   				else
   					si.print(i+2,1,'O', ConsoleSystemInterface.WHITE);
   			else 
   				si.print(i+2,1,' ', ConsoleSystemInterface.WHITE);
   		for (int i = 0; i < 10; i++)
   			if (i+1 <= rest2x)
   				if (i+1 <= rest2)
   					si.print(i+2,2,'O', ConsoleSystemInterface.PURPLE);
   				else
   					si.print(i+2,2,'O', ConsoleSystemInterface.WHITE);
   			else 
   				si.print(i+2,2,' ', ConsoleSystemInterface.WHITE);
   		
   		if (player.getTankEnergy()<10){
   			si.print(13,1,"ENERGY  0"+player.getTankEnergy(), ConsoleSystemInterface.LEMON);
   		} else {
   			si.print(13,1,"ENERGY  "+player.getTankEnergy(), ConsoleSystemInterface.LEMON);
   		}
   		if (player.getReserveEnergy()<10){
   			si.print(13,2,"RESERVE 0"+player.getReserveEnergy(), ConsoleSystemInterface.YELLOW);
   		}else {
   			si.print(13,2,"RESERVE "+player.getReserveEnergy(), ConsoleSystemInterface.YELLOW);
   		}
   		
   		String weaponString = "";
   		String weaponString2 = "";
   		if (player.isHasMorphBall()){
   			weaponString+=  "1---\\ ";
   			weaponString2+= "Ball  ";
   		}else{
   			weaponString+= "      ";
   			weaponString2+= "      ";
   		}
   		if (player.isHasChargeBeam()){
   			weaponString+= "2---\\ ";
   			weaponString2+= "Charg ";
   		}else{
   			weaponString+= "      ";
   			weaponString2+= "      ";
   		}
   		if (player.isHasEnergyBomb()){
   			weaponString+= "3---\\ ";
   			weaponString2+= "Bomb  ";
   		}else{
   			weaponString+= "      ";
   			weaponString2+= "      ";
   		}
   		
   		
   		if (player.getMissilesCapacity()>0){
   			if (player.getMissiles()>10){
   				weaponString+= "4-"+player.getMissiles()+"\\ ";
   			} else {
   				weaponString+= "4-0"+player.getMissiles()+"\\ ";
   			}
   			weaponString2+= "Missl ";
   		}else{
   			weaponString+= "      ";
   			weaponString2+= "      ";
   		}
   		
   		if (player.getSuperMissilesCapacity()>0){
   			if (player.getSuperMissiles()>10){
   				weaponString+= "5-"+player.getSuperMissiles()+"\\ ";
   			} else {
   				weaponString+= "5-0"+player.getSuperMissiles()+"\\ ";
   			}
   			weaponString2+= "SMiss ";
   		}else{
   			weaponString+= "      ";
   			weaponString2+= "      ";
   		}
   		
   		if (player.getPowerBombsCapacity()>0){
   			if (player.getPowerBombs()>10){
   				weaponString+= "6-"+player.getPowerBombs()+"\\ ";
   			} else {
   				weaponString+= "6-0"+player.getPowerBombs()+"\\ ";
   			}
   			weaponString2+= "PBomb ";
   		}else{
   			weaponString+= "      ";
   			weaponString2+= "      ";
   		}
   		
   		si.print (25,1,weaponString, ConsoleSystemInterface.LEMON);
   		si.print (25,2,weaponString2, ConsoleSystemInterface.WHITE);
   		
   		si.print (64,1,"               ");
   		si.print (64,2,"               ");
   		
   		si.print (64,1,"Cay Ley Base", ConsoleSystemInterface.RED);
   		si.print (64,2,level.getDescription());
   		
   		si.print (56,4,"                      ");
   		si.print (56,5,"                      ");
   		//si.print (56,6,player.getAdapterDescription());
   		
   		si.print (56,4,player.getArmorDescription());
   		si.print (56,5,player.getBeamDescription());
   		//si.print (56,6,player.getAdapterDescription());
   		
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
		
		si.printx(52,4," o ",visorColor);
		si.printx(52,5," T ",visorColor);
		si.printx(52,6,"   ",visorColor);
		
		si.printx(52,4,"еее",visorColorH);
		si.printx(52,5,"/е\\",visorColorH);
		si.printx(52,6,"/е\\",visorColorH);
		
   		
    	si.print (52,16,"                    ");
    	si.print (52,17,"Alt "+player.getHeight()+ (player.isJumping() ? " Jump" : "")+ (player.isScewAttacking() ? "Scr" : ""));
    	
    	Debug.exitMethod();
    }

	public void init(ConsoleSystemInterface psi, UserAction[] gameActions, UserCommand[] gameCommands,
		Action advance, Action target, Action attack){
		Debug.enterMethod(this, "init");
		super.init(gameActions, gameCommands, advance, target, attack);
		selector = new UISelector();
		messageBox = new TextInformBox(psi);
		idList = new ListBox(psi);
		
		messageBox.setPosition(1,22);
		messageBox.setWidth(78);
		messageBox.setHeight(3);
		messageBox.setForeColor(ConsoleSystemInterface.LEMON);
		
		/*monstersList.setPosition(2, 4);
		monstersList.setWidth(27);
		monstersList.setHeight(10);*/
		
		idList.setPosition(52,7);
		idList.setWidth(27);
		idList.setHeight(9);
		si = psi;
		FOVMask = new boolean[80][25];
		Debug.exitMethod();
	}

	/** 
	 * Checks if the point, relative to the console coordinates, is inside the
	 * ViewPort 
	 */
	public boolean insideViewPort(int x, int y){
    	return (x>=VP_START.x && x <= VP_END.x && y >= VP_START.y && y <= VP_END.y && (x>=0 && x < FOVMask.length && y >= 0 && y < FOVMask[0].length) && FOVMask[x][y]);
		//return (x>=0 && x < FOVMask.length && y >= 0 && y < FOVMask[0].length) && FOVMask[x][y];
    }

	public boolean insideViewPort(Position what){
    	return insideViewPort(what.x, what.y);
    }

	public boolean isDisplaying(Actor who){
    	return insideViewPort(getAbsolutePosition(who.getPosition()));
    }

    private Position pickPosition(String prompt) throws ActionCancelException{
    	Debug.enterMethod(this, "pickPosition");
    	messageBox.setForeColor(ConsoleSystemInterface.LEMON);
    	messageBox.setText(prompt);
		messageBox.draw();
		si.refresh();
		si.saveBuffer();
		Position offset = new Position (0,0);
		Monster lockedMonster = player.getLockedMonster();
		if (lockedMonster != null){
			if (!player.sees(lockedMonster)  || lockedMonster.isDead())
				player.setLockedMonster(null);
			else
				defaultTarget = new Position(lockedMonster.getPosition());
		}
		
		int monsterIndex = -1;
		while (true){
			si.restore();
			Position browser = null;
			if (defaultTarget == null){
				//offset = null; //CRASH!
				browser = Position.add(player.getPosition(), offset);
			}else{
				browser = defaultTarget;
				offset = new Position(defaultTarget.x - player.getPosition().x, defaultTarget.y - player.getPosition().y);
				defaultTarget = null;
			}
			String looked = "";
			if (PC_POS.x + offset.x < 0 || PC_POS.x + offset.x >= FOVMask.length || PC_POS.y + offset.y < 0 || PC_POS.y + offset.y >=FOVMask[0].length){
				offset = new Position (0,0);
				browser = Position.add(player.getPosition(), offset);
			}
				
			if (FOVMask[PC_POS.x + offset.x][PC_POS.y + offset.y]){
				Cell choosen = level.getMapCell(browser);
				Feature feat = level.getFeatureAt(browser);
				ArrayList<Item> items = level.getItemsAt(browser);
				
				Actor actor = level.getActorAt(browser);
				si.restore();
				
				if (choosen != null)
					looked += choosen.getDescription();
				if (level.getBloodAt(browser) != null)
				    looked += "{bloody}";
				if (feat != null)
					looked += ", "+ feat.getDescription();
				if (actor != null)
					looked += ", "+ actor.getDescription();
				if (items != null && items.size() > 0){
					looked += ", "+ items.get(0).getDescription();
					if (items.size()>1){
						looked += " and something else";
					}
				}
			}
			messageBox.setText(prompt+" "+looked);
			messageBox.draw();
			//si.print(PC_POS.x + offset.x, PC_POS.y + offset.y, '_', ConsoleSystemInterface.RED);
			drawLineTo(PC_POS.x + offset.x, PC_POS.y + offset.y, '*', ConsoleSystemInterface.GREEN);
			si.print(PC_POS.x + offset.x, PC_POS.y + offset.y, 'O', ConsoleSystemInterface.LEMON);
			si.refresh();
			CharKey x = new CharKey(CharKey.NONE);
			while (x.code != CharKey.SPACE && x.code != CharKey.ESC && x.code != CharKey.z &&
				   ! x.isArrow())
				x = si.inkey();
			if (x.code == CharKey.ESC){
				si.restore();
				throw new ActionCancelException();
			}
			if (x.code == CharKey.z && player.getMonstersOnSight().size() > 0){
				monsterIndex++;
				if (monsterIndex == player.getMonstersOnSight().size())
					monsterIndex = 0;
				
				defaultTarget = new Position(((Monster)player.getMonstersOnSight().get(monsterIndex)).getPosition());
				continue;
			} 
			if (x.code == CharKey.SPACE){
				si.restore();
				if (level.getMonsterAt(browser) != null)
					player.setLockedMonster(level.getMonsterAt(browser));
				return browser;
			}
			offset.add(Action.directionToVariation(Action.toIntDirection(x)));

			if (offset.x >= xrange) offset.x = xrange;
			if (offset.x <= -xrange) offset.x = -xrange;
			if (offset.y >= yrange) offset.y = yrange;
			if (offset.y <= -yrange) offset.y = -yrange;
     	}
		
		
    }

	private Item pickUnderlyingItem(String prompt) throws ActionCancelException{
		Debug.enterMethod(this, "pickUnderlyingItem");
  		ArrayList items = level.getItemsAt(player.getPosition());
  		if (items == null)
  			return null;
  		if (items.size() == 1)
  			return (Item) items.get(0);
  		MenuBox menuBox = new MenuBox(si);
  		menuBox.setBounds(10,3,60,18);
  		menuBox.setPromptSize(2);
  		menuBox.setMenuItems(new Vector(items));
  		menuBox.setPrompt(prompt);
  		menuBox.setForeColor(ConsoleSystemInterface.RED);
  		menuBox.setBorder(true);
  		si.saveBuffer();
  		menuBox.draw();
		Item item = (Item)menuBox.getSelection();
		si.restore();
		if (item == null){
			ActionCancelException ret = new ActionCancelException();
			Debug.exitExceptionally(ret);
			throw ret;
		}
		return item;
	}
	
	private int pickDirection(String prompt) throws ActionCancelException{
		Debug.enterMethod(this, "pickDirection");
		//refresh();
		messageBox.setText(prompt);
		messageBox.draw();
		si.refresh();
		//refresh();

		CharKey x = new CharKey(CharKey.NONE);
		while (x.code == CharKey.NONE)
			x = si.inkey();
		if (x.isArrow() || x.code == CharKey.N5){
			int ret = Action.toIntDirection(x);
        	Debug.exitMethod(ret);
        	return ret;
		} else {
			ActionCancelException ret = new ActionCancelException(); 
			Debug.exitExceptionally(ret);
			si.refresh();
        	throw ret; 
		}
	}

	
	public void processQuit(){
		messageBox.setForeColor(ConsoleSystemInterface.LEMON);
		messageBox.setText(quitMessages[Util.rand(0, quitMessages.length-1)]+" (y/n)");
		messageBox.draw();
		si.refresh();
		if (prompt()){
			messageBox.setText("Go away, and let the world flood in darkness... [Press Space to continue]");
			messageBox.draw();
			si.refresh();
			si.waitKey(CharKey.SPACE);
			player.getGameSessionInfo().setDeathCause(GameSessionInfo.QUIT);
			player.getGameSessionInfo().setDeathLevelDescription(level.getDescription());
			informPlayerCommand(CommandListener.QUIT);
		}
		messageBox.draw();
		messageBox.clear();
		si.refresh();
	}
	
	public void processSave(){
		messageBox.setForeColor(ConsoleSystemInterface.LEMON);
		messageBox.setText("Save your game? (y/n)");
		messageBox.draw();
		si.refresh();
		if (prompt()){
			messageBox.setText("Saving... I will await your return.. [Press Space to continue]");
			messageBox.draw();
			si.refresh();
			si.waitKey(CharKey.SPACE);
			informPlayerCommand(CommandListener.SAVE);
		}
		messageBox.draw();
		messageBox.clear();
		si.refresh();
	}

	public boolean prompt (){
		CharKey x = new CharKey(CharKey.NONE);
		while (x.code != CharKey.Y && x.code != CharKey.y && x.code != CharKey.N && x.code != CharKey.n)
			x = si.inkey();
		return (x.code == CharKey.Y || x.code == CharKey.y);
	}

	public void refresh(){
		//cleanViewPort();
		drawPlayerStatus();
	 	drawLevel();
		idList.draw();
		si.refresh();
		messageBox.draw();
	  	messageBox.setForeColor(ConsoleSystemInterface.GREEN);
	 	eraseOnArrival = true;
	  	
    }

	private void setTargets(Action a) throws ActionCancelException{
		if (a.needsDirection()){
			a.setDirection(pickDirection(a.getPromptDirection()));
		}
		if (a.needsPosition()){
			a.setPosition(pickPosition(a.getPromptPosition()));
		}
		if (a.needsUnderlyingItem()){
			a.setItem(pickUnderlyingItem(a.getPrompUnderlyingItem()));
		}
		
	}
	

 	/**
     * Shows a message inmediately; useful for system
     * messages.
     *  
     * @param x the message to be shown
     */
	public void showMessage(String x){
		messageBox.setForeColor(ConsoleSystemInterface.LEMON);
		messageBox.addText(x);
		messageBox.draw();
		si.refresh();
	}
	
	public void showSystemMessage(String x){
		messageBox.setForeColor(ConsoleSystemInterface.LEMON);
		messageBox.setText(x);
		messageBox.draw();
		si.refresh();
		si.waitKey(CharKey.SPACE);
	}
	
	
	private void showMessageHistory(){
		si.saveBuffer();
		si.cls();
		si.print(1, 0, "Message Buffer", CharAppearance.LEMON);
		for (int i = 0; i < 22; i++){
			if (i >= messageHistory.size())
				break;
			si.print(1,i+2, (String)messageHistory.elementAt(messageHistory.size()-1-i), CharAppearance.GREEN);
		}
		
		si.print(55, 24, "[ Space to Continue ]");
		si.waitKey(CharKey.SPACE);
		si.restore();
	}

	public void showPlayerStats(){
		Display.thus.showScreen("screens/status.xml");
	}
	public void showPlayerStatsX(){
  		si.saveBuffer();
  		si.cls();
  		si.print(0, 0, "            /-VISOR-----\\                                             ", ConsoleSystemInterface.LEMON);
 		si.print(0, 1, "            |           |__                         /-BOMB------\\     ", ConsoleSystemInterface.LEMON);
 		si.print(0, 2, "            |           |  \\                  ______|           |     ", ConsoleSystemInterface.LEMON); 
 		si.print(0, 3, "            \\-----------/   \\               _/      |           |     ", ConsoleSystemInterface.LEMON);
 		si.print(0, 4, "      /-BEAM------\\          \\             /        \\-----------/     ", ConsoleSystemInterface.LEMON);
 		si.print(0, 5, "      |           |__         \\           /                           ", ConsoleSystemInterface.LEMON);
 		si.print(0, 6, "      |           |  \\         \\         /             /-SUIT------\\  ", ConsoleSystemInterface.LEMON);
 		si.print(0, 7, "      |           |   \\         \\       /              |           |  ", ConsoleSystemInterface.LEMON);
 		si.print(0, 8, "      |           |    \\-----------@-------------------|           |  ", ConsoleSystemInterface.LEMON);
 		si.print(0, 9, "      \\-----------/          /          \\              |           |  ", ConsoleSystemInterface.LEMON);
 		si.print(0,10, "                            /    SAMUS   \\             |           |  ", ConsoleSystemInterface.LEMON);
 		si.print(0,11, "                           /              \\            \\-----------/  ", ConsoleSystemInterface.LEMON);
 		si.print(0,12, "        /-MISSILE---\\     |                |                          ", ConsoleSystemInterface.LEMON);
 		si.print(0,13, "        |           |__ _/                 |     /-MISC----------\\    ", ConsoleSystemInterface.LEMON);
 		si.print(0,14, "        |           |                       \\    |               |    ", ConsoleSystemInterface.LEMON);
 		si.print(0,15, "        \\-----------/                        \\___|               |    ", ConsoleSystemInterface.LEMON);
 		si.print(0,16, "                                                 |               |    ", ConsoleSystemInterface.LEMON);
 		si.print(0,17, "     ENERGY              MISS:                   |               |    ", ConsoleSystemInterface.LEMON);
 		si.print(0,18, "     RESERVE             SMISS:                  \\---------------/    ", ConsoleSystemInterface.LEMON);
 		si.print(0,19, "                         BOMBS:                                       ",  ConsoleSystemInterface.LEMON);
 		
 		
 		if (player.isHasXRayVisor())
 			if (player.isXRayVisorActivated())
 				si.print(15,1,"+ X-RAY", ConsoleSystemInterface.RED);
 			else
 				si.print(15,1,"+ X-RAY", ConsoleSystemInterface.WHITE);
 		
 		if (player.isHasThermoVisor())
 			if (player.isThermoVisorActivated())
 				si.print(15,2,"+ THERMO", ConsoleSystemInterface.RED);
 			else
 				si.print(15,2,"+ THERMO", ConsoleSystemInterface.WHITE);
 		if (player.isHasChargeBeam())
 			if (player.isChargeBeamActivated())
 				si.print(9,5,"+ CHARGE", ConsoleSystemInterface.RED);
 			else
 				si.print(9,5,"+ CHARGE", ConsoleSystemInterface.WHITE);
 		
 		if (player.isHasIceBeam())
 			if (player.isIceBeamActivated())
 		 		si.print(9,6,"+ ICE", ConsoleSystemInterface.RED);
 			else
 				si.print(9,6,"+ ICE", ConsoleSystemInterface.WHITE);
 		
 		if (player.isHasWaveBeam())
 			if (player.isWaveBeamActivated())
 		 		si.print(9,7,"+ WAVE", ConsoleSystemInterface.RED);
 			else
 				si.print(9,7,"+ WAVE", ConsoleSystemInterface.WHITE);
 		
 		if (player.isHasPlasmaBeam())
 			if (player.isPlasmaBeamActivated())
 		 		si.print(9,8,"+ PLASMA", ConsoleSystemInterface.RED);
 			else
 				si.print(9,8,"+ PLASMA", ConsoleSystemInterface.WHITE);
 		
 		if (player.getMissilesCapacity()>0)
 			si.print(11,13,"+ ENERGY", ConsoleSystemInterface.RED);
 		
 		if (player.getSuperMissilesCapacity()>0)
 			si.print(11,14,"+ SUPER", ConsoleSystemInterface.RED);
 		
 		if (player.isEnergyBombActivated())
 			si.print(55,2,"+ ENERGY", ConsoleSystemInterface.RED);
 		
 		if (player.getPowerBombsCapacity()>0)
 			si.print(55,3,"+ POWER", ConsoleSystemInterface.RED);
 		
 		si.print(58,7,"+ POWER", ConsoleSystemInterface.WHITE);
 		if (player.isHasVariaSuit())
 			if (player.isVariaSuitActivated())
 				si.print(58,8,"+ VARIA", ConsoleSystemInterface.RED);
 			else
 				si.print(58,8,"+ VARIA", ConsoleSystemInterface.WHITE);
 		if (player.isHasGravitySuit())
 			if (player.isGravitySuitActivated())
 				si.print(58,9,"+ GRAVITY", ConsoleSystemInterface.RED);
 			else
 				si.print(58,9,"+ GRAVITY", ConsoleSystemInterface.WHITE);

 		//si.print(58,10,"+ ZERO", ConsoleSystemInterface.WHITE);

 		if (player.isHasMorphBall())
 			si.print(52,14,"+ MORPHBALL", ConsoleSystemInterface.WHITE);
 		
 		if (player.isHasHighJumpBooster())
 			if (player.isHighJumpBoosterActivated())
 				si.print(52,15,"+ HI-JUMP", ConsoleSystemInterface.RED);
 			else
 				si.print(52,15,"+ HI-JUMP", ConsoleSystemInterface.WHITE);
 		
 		if (player.isHasSpaceJump())
 			if (player.isSpaceJumpActivated())
 				si.print(52,16,"+ SPACE-JUMP", ConsoleSystemInterface.RED);
 			else
 				si.print(52,16,"+ SPACE-JUMP", ConsoleSystemInterface.WHITE);
 		
 		if (player.isHasScrewAttack())
 			if (player.isScrewAttackActivated())
 				si.print(52,17,"+ SCREW", ConsoleSystemInterface.RED);
 			else
 				si.print(52,17,"+ SCREW", ConsoleSystemInterface.WHITE);
 		
 		si.print(15,17,player.getAbsoluteEnergy()+"/"+player.getMaxEnergy(), ConsoleSystemInterface.WHITE);
 		si.print(15,18,player.getReserveEnergy()+"/"+player.getReserveEnergyMax(), ConsoleSystemInterface.WHITE);
 		si.print(35,17,player.getMissiles()+"/"+player.getMissilesCapacity(), ConsoleSystemInterface.WHITE);
 		si.print(35,18,player.getSuperMissiles()+"/"+player.getSuperMissilesCapacity(), ConsoleSystemInterface.WHITE);
 		si.print(35,19,player.getPowerBombs()+"/"+player.getPowerBombsCapacity(), ConsoleSystemInterface.WHITE);
 		si.refresh();
 		si.waitKey(CharKey.SPACE);
 		si.restore();
 	}

    public Action showSkills() throws ActionCancelException {
    	Debug.enterMethod(this, "showSkills");
    	si.saveBuffer();
		Vector skills = player.getAvailableSkills();
  		MenuBox menuBox = new MenuBox(si);
  		menuBox.setHeight(14);
  		menuBox.setWidth(33);
  		menuBox.setBorder(true);
  		menuBox.setForeColor(ConsoleSystemInterface.RED);
  		menuBox.setPosition(24,4);
  		menuBox.setMenuItems(skills);
  		menuBox.setTitle("Skills");
  		menuBox.setPromptSize(0);
  		menuBox.draw();
		si.refresh();
        Skill selectedSkill = (Skill)menuBox.getSelection();
        if (selectedSkill == null){
        	si.restore();
        	Debug.exitMethod("null");
        	return null;
        }
        si.restore();
        if (selectedSkill.isSymbolic()){
        	Debug.exitMethod("null");
        	return null;
        }
        	
		Action selectedAction = selectedSkill.getAction();
		selectedAction.setPerformer(player);
		if (selectedAction.canPerform(player))
			setTargets(selectedAction);
		else
			level.addMessage(selectedAction.getInvalidationMessage());
		
		//si.refresh();
		Debug.exitMethod(selectedAction);
		return selectedAction;
	}

    private final String[] SOUL_OPTIONS = new String[]{
    		"VENUS_SPIRIT",
    		"MERCURY_SPIRIT",
    		"MARS_SPIRIT",
    		"URANUS_SPIRIT",
    		"NEPTUNE_SPIRIT",
    		"JUPITER_SPIRIT"
    };
    
    private Action selectCommand (CharKey input){
		Debug.enterMethod(this, "selectCommand", input);
		int com = getRelatedCommand(input.code);
		informPlayerCommand(com);
		Action ret = actionSelectedByCommand;
		actionSelectedByCommand = null;
		Debug.exitMethod(ret);
		return ret;
	}
	
	public void commandSelected (int commandCode){
		switch (commandCode){
			case CommandListener.PROMPTQUIT:
				processQuit();
				break;
			case CommandListener.PROMPTSAVE:
				processSave();
				break;
			case CommandListener.HELP:
				si.saveBuffer();
				Display.thus.showHelp();
				si.restore();
				break;
			case CommandListener.LOOK:
				doLook();
				break;
			case CommandListener.SHOWSTATS:
				showPlayerStats();
				break;
			case CommandListener.SHOWSKILLS:
				try {
					actionSelectedByCommand = showSkills();
				} catch (ActionCancelException ace){

				}
				break;
			case CommandListener.SHOWMESSAGEHISTORY:
				showMessageHistory();
				break;
			case CommandListener.SHOWMAP:
				Display.thus.showMap(level.getMapLocationKey(), level.getDescription());
				break;
			case CommandListener.REMOVELOCK:
				level.addMessage("[/visor.removeLock {Command Complete}]");
				player.setLockedMonster(null);
				refresh();
				break;
		}
	}
/*
	private boolean cheatConsole(CharKey input){
		switch (input.code){
		case CharKey.F2:
			player.addHearts(5);
			break;
		case CharKey.F3:
			player.setInvincible(250);
			break;
		case CharKey.F4:
			player.informPlayerEvent(Player.EVT_NEXT_LEVEL);
			break;
		case CharKey.F5:
			player.heal();
			break;
		case CharKey.F6:
			if (player.getLevel().getBoss() != null)
				player.getLevel().getBoss().damage(15);
			break;
		case CharKey.F7:
			player.getLevel().setIsDay(!player.getLevel().isDay());
			break;
		case CharKey.F8:
			player.informPlayerEvent(Player.EVT_BACK_LEVEL);
			break;
		default:
			return false;
		}
		return true;
	}*/
	
//	Runnable interface
	public void run (){}
	
//	IO Utility    
	public void waitKey (){
		CharKey x = new CharKey(CharKey.NONE);
		while (x.code == CharKey.NONE)
			x = si.inkey();
	}

	private void cleanViewPort(){
	    Debug.enterMethod(this, "cleanViewPort");
    	String spaces = "";
    	for (int i= 0; i<= VP_END.x - VP_START.x; i++){
    		spaces+=" ";
    	}
    	for (int i= VP_START.y; i<= VP_END.y; i++){
    		si.print(VP_START.x, i,spaces);
    	}
    	Debug.exitMethod();
	}

	private void drawLineTo(int x, int y, char icon, int color){
		Position target = new Position(x,y);
		Line line = new Line(PC_POS, target);
		Position tmp = line.next();
		while (!tmp.equals(target)){
			tmp = line.next();
			si.print(tmp.x, tmp.y, icon, color);
		}
		
	}
	
	private Position getNearestMonsterPosition(){
		VMonster monsters = level.getMonsters();
		Monster nearMonster = null;
		int minDist = 150;
		int maxDist = 15;
		for (int i = 0; i < monsters.size(); i++){
			Monster monster = (Monster) monsters.elementAt(i);
			if (monster.getPosition().z() != level.getPlayer().getPosition().z())
				continue;
			if (monster.getHeight() != player.getHeight())
				continue;
			int distance = Position.flatDistance(level.getPlayer().getPosition(), monster.getPosition());
			if (distance < maxDist && distance< minDist && player.sees(monster)){
				minDist = distance;
				nearMonster = monster;
			}
		}
		if (nearMonster != null)
			return nearMonster.getPosition();
		else
			return null;
	}

	public Vector getMessageBuffer() {
		if (messageHistory.size() < 19)
			return messageHistory;
		else
			return new Vector(messageHistory.subList(0,21));
	}
	public ActionSelector getSelector(){
		return selector;
	}
	
	class UISelector implements ActionSelector {
		/** 
		 * Returns the Action that the player wants to perform.
	     * It may also forward a command instead
	     * 
	     */
		public Action selectAction(Actor who){
	    	Debug.enterMethod(this, "selectAction", who);
		    CharKey input = null;
		    Action ret = null;
		    while (ret == null){
		    	if (gameOver)
		    		return null;
				input = si.inkey();
				ret = selectCommand(input);
				if (ret != null)
					return ret;
				if (input.code == CharKey.DOT) {
					Debug.exitMethod("null");
					return null;
				}
				/*if (cheatConsole(input)){
					continue;
				}*/
				if (input.isArrow()){
					int direction = Action.toIntDirection(input);
					Monster vMonster = player.getLevel().getMonsterAt(Position.add(player.getPosition(), Action.directionToVariation(direction)));
					if (vMonster != null && !player.isScewAttacking()){
						attack.setPosition(Position.add(player.getPosition(), Action.directionToVariation(direction)));
						Debug.exitMethod(attack);
						return attack;
					}
					advance.setDirection(direction);
					Debug.exitMethod(advance);
					return advance;
				} else
				
	            	ret = getRelatedAction(input.code);
	            	/*if (ret == target){
	            		defaultTarget = player.getNearestMonsterPosition();
	            	}*/
	            	try {
	            		Position nearest =getNearestMonsterPosition(); 
	                	if (ret!= null && ret.needsPosition() && nearest != null){
	                		defaultTarget = nearest;
	                	}
		            	if (ret != null){
		            		ret.setPerformer(player);
		            		if (ret.canPerform(player))
		            			setTargets(ret);
		            		else
		            			level.addMessage(ret.getInvalidationMessage());
	                     	Debug.exitMethod(ret);
	                    	return ret;
						}

					}
					catch (ActionCancelException ace){
						//si.cls();
		 				//refresh();
		 				addMessage(new Message("Cancelled", player.getPosition()));
						ret = null;
					}
					//refresh();
				
			}
			Debug.exitMethod("null");
			return null;
		}
		
		public String getID(){
			return "UI";
		}
	    
		public ActionSelector derive(){
	 		return null;
	 	}
	}
}