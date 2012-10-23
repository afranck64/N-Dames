package main;

import gui.MaFenetre;

import javax.swing.WindowConstants;


import util.*;
public class NDame
{
	public static void main (String args[])
	{
		MaFenetre fen = new MaFenetre() ;
		fen.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		fen.setVisible(true) ;
		Globals.setDebug(false);
	}
}
