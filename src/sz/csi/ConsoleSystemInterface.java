package sz.csi;

import sz.util.*;

public interface ConsoleSystemInterface {
	public void print (int x, int y, char what, int color);
	public void safeprint (int x, int y, char what, int color);
	public void print (int x, int y, String what, int color);
	public void print (int x, int y, String what);

	/**
	 * Prints a character on the console, ignoring ¥ characters
	 * @param x
	 * @param y
	 * @param what The character to be printed
	 * @param color The color, one of the ConsoleSystemInterface constants
	 */
	public void printx (int x, int y, String what, int color);
	
	public char peekChar(int x, int y);
	public int peekColor(int x, int y);

	public CharKey inkey();
    /**  Waits until a key is pressed and returns it */
    public void locateCaret (int x, int y);
    public String input();
    public String input(int length);
    public boolean isInsideBounds(Position e);

    public void cls();
    public void refresh();
    public void refresh(Thread t);
    public void flash(int color);

    public void setAutoRefresh(boolean value);
    
	public void waitKey (int keyCode);

	public void saveBuffer();
	public void restore();
	
	public static final int
    BLACK = 0,
	DARK_BLUE = 1,
	GREEN = 2,
	TEAL = 3,
	DARK_RED = 4,
	PURPLE = 5,
	BROWN = 6,
	LIGHT_GRAY = 7,
    GRAY = 8,
	BLUE = 9,
	LEMON = 10,
	CYAN = 11,
	RED = 12,
	MAGENTA = 13,
	YELLOW = 14,
	WHITE = 15;
	
//	public int getColor(String colorName);
}