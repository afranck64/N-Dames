
/**
 * Beschreiben Sie hier die Klasse Audio.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */

import java.applet.AudioClip;
import java.net.URL;
import javax.sound.sampled.AudioSystem;

public class Audio extends Object
{
    static Resource res = new Resource();
    int duration;
    public static AudioClip sound;
    public static AudioClip smove = new sun.applet.AppletAudioClip(res.getResourceURL("sMove"));
    public static AudioClip seat = new sun.applet.AppletAudioClip(res.getResourceURL("sEat"));
    public static AudioClip sdame = new sun.applet.AppletAudioClip(res.getResourceURL("sDame"));
    
    public static void play(String title)
    {
        if (title == "seat" || title=="eat")
            seat.play();
        else if (title=="smove" || title=="move")
            smove.play();
        else if (title == "sdame" || title == "dame")
            sdame.play();
    }
}
