package mrl.player;

import java.awt.Image;

import mrl.item.Item;
import mrl.ui.consoleUI.CharAppearance;

import sz.csi.textcomponents.MenuItem;

public class Equipment implements MenuItem{
	private Item item;
	private int quantity;

	public Equipment (Item pItem, int pQuantity){
		item = pItem;
		quantity = pQuantity;
 	}

 	public boolean isEmpty(){
 		return quantity == 0;
    }
 	
 	public static boolean eqMode = false;
 	public static boolean menuDetail = false;

 	public String getMenuDescription(){
 		if (quantity == 1){
 			return item.getAttributesDescription();
 		} else {
 			return item.getAttributesDescription() +" x"+quantity;
 		}
// 		if (eqMode)
// 			return item.getAttributesDescription() +" x"+quantity+ " ["+item.getDefinition().getMenuDescription()+"]";
 	}

 	/*Unsafe, Coupled*/
	public char getMenuChar() {
		return ((CharAppearance)item.getAppearance()).getChar();
	}
	
	/*Unsafe, Coupled*/
	public int getMenuColor() {
		return ((CharAppearance)item.getAppearance()).getColor();
	}


	public Item getItem() {
		return item;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int value) {
		quantity = value;
	}

	public void increaseQuantity (){
		quantity ++;
	}

	public void reduceQuantity(){
		quantity --;
	}

}