
/**
 * Beschreiben Sie hier die Klasse ImagePanel.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */

import javax.swing.* ;
import java.awt.* ;
import java.net.URL;
//import java.awt.event.* ;   // pour MouseEvent et MouseListener

public class ImagePanel extends JPanel
{
    ImageIcon img;
    Resource res;
    public ImagePanel(URL url)
    {
       img = new ImageIcon(url);
       setBackground(Color.black);
    }
    
    public ImagePanel(String key)
    {
        res = new Resource();
        URL url = res.getResourceURL(key);
        img = new ImageIcon(url);
    }
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(img.getImage(), 0, 0, null);
    }
}
