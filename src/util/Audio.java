package util;

import java.applet.AudioClip;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Audio extends Object
{
    public static AudioClip sound;
    static ResourceBundle res = ResourceBundle.getBundle(Globals.resourceName);
    
    /*public static AudioClip smove = new sun.applet.AppletAudioClip(res.getResourceURL("sMove"));
    public static AudioClip seat = new sun.applet.AppletAudioClip(res.getResourceURL("sEat"));
    public static AudioClip sdame = new sun.applet.AppletAudioClip(res.getResourceURL("sDame"));
    */
    public Audio(String filename) {
    }
    
    public static void play(String title)
    {
    	Clip clickClip;
    	AudioInputStream ais;
		try {
			/*
			clickClip = AudioSystem.getClip();
			ais = AudioSystem.getAudioInputStream(new FileInputStream(res.getString(title)));
	    	clickClip.open(ais);
	    	clickClip.loop(1);
	    	clickClip.start();*/
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	/**
        if (title == "seat" || title=="eat")
            seat.play();
        else if (title=="smove" || title=="move")
            smove.play();
        else if (title == "sdame" || title == "dame")
            sdame.play();
            */
    }
}
