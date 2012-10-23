package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import util.Globals;
//import java.awt.event.* ;   // pour MouseEvent et MouseListener


public class ImagePanel extends JPanel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -283415907384595121L;
	ImageIcon img;
    ResourceBundle res;
    public ImagePanel(URL url)
    {
       img = new ImageIcon(url);
       setBackground(Color.black);
    }
    
    public ImagePanel(String key)
    {
    	res = ResourceBundle.getBundle(Globals.resourceName);
        img = new ImageIcon(res.getString(key));
    }
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(img.getImage(), 0, 0, null);
    }
}

