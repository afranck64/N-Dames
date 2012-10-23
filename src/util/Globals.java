package util;

import gui.MaFenetre;

public class Globals
{
	public static String  resourceName = "resources.NDames";
    public static int size = 50;     //Size of a bloc : 50 x 50
    public static int nbPlayers = 2;    //Number of players : 2 
    public static int dimension = 10;    //Dimension of the game: 10 x 10
    public static int width = 650;
    public static int height = 600;
    public static boolean _debug = false;    //
    public static MaFenetre game = null;
    
    public static void debug(String msg) {
    	if (_debug) {
    		System.out.println(msg);
    	}
    }
    
    public static void setDebug(boolean debug) {
    	_debug = debug;
    }
    
    public static boolean getDebug() {
    	return _debug;
    }
}