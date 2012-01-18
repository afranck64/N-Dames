
/**
 * Write a description of class AboutBox here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.Component;

public class AboutBox extends JDialog implements ActionListener
{
    

    /**
     * Constructor for objects of class AboutBox
     */
    ImagePanel dZone;
    JButton okButton;
    public AboutBox(JFrame parent, String title, String msg)
    {
        super(parent, title, true);
        Resource res = new Resource();
        dZone = new ImagePanel("iLogo");
        JTextArea txt = new JTextArea();
        //txt.setColumns(48);
        //txt.setEditable(false);
        txt.setFont(new Font("monospaced", Font.PLAIN, 14));
        txt.setText(msg);
        setSize(400,280);
        setResizable(false);
        okButton = new JButton(res.getString("okLabel"));
        okButton.addActionListener(new ActionListener(){
            public void actionPerformed (ActionEvent evt){
                dispose();
            }
        });
        Container contenu = getContentPane();
        contenu.setLayout (new GridLayout());
        /*/Surface s = new Surface();
        contenu.add(s);
        s.setSize(240,200);
        Pion [][]ps = new Pion[10][10];
        s.setPions(ps);
        */
        contenu.add(dZone, BorderLayout.NORTH);
        contenu.add(txt, BorderLayout.EAST);
        //contenu.add(okButton);        
        dZone.updateUI();
    }
    
    public void actionPerformed(ActionEvent evt)
    {
    }
}
