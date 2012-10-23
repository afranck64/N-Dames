package util;


import java.awt.Color;
import java.awt.Graphics;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import model.Pion;
import model.TColor;

public class Surface extends JPanel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7282467879300554702L;
	Color clight;
    Color cdark;
    Color ceater;
    Color ceatable;
    ImageIcon iblack;
    ImageIcon iwhite;
    ImageIcon isuperblack;
    ImageIcon isuperwhite;
    int currentPlayer;
    Pion [][] pions;
    Position pos2play;
    ResourceBundle res;
    
    public Surface ()
    {
        //this.setSize(680, 600);
        res = ResourceBundle.getBundle(Globals.resourceName);
        clight = Color.yellow; //La couleur pour les cases claires
        cdark = Color.green; //La couleur pour les cases sombres;
        ceatable = Color.red;
        ceater = Color.blue;
        
        iblack = new ImageIcon(res.getString("iBlack"));
        iwhite = new ImageIcon(res.getString("iWhite"));
        isuperblack = new ImageIcon(res.getString("iSuperblack"));
        isuperwhite = new ImageIcon(res.getString("iSuperwhite"));
        //addMouseListener(this); //La Surface sera son propre �couteur d'�v�nements souris
    }
    
    public void paintComponent(Graphics g)
    {
        
        super.paintComponent(g) ;
        int i=0, j=0 ;
        int dimension = Globals.dimension;
        int size = Globals.size;
        Pion p;
        this.setSize(680, 600);
        /* rectangle a coins arrondis de couleur jaune, de bordure noire */
        for (i = 0; i<dimension; i++)
        {
            for (j = 0; j<dimension; j++)
            {
                if ((i+j)%2==0)
                    g.setColor(cdark);
                else
                    g.setColor(clight);
                g.fillRoundRect(i*size,j*size,size,size,0,0);
            }
        }
        /* Modification de la couleur de fond des pions pouvant manger */
        if (Globals.game.getPosStart()==null)
        {
            for (i=0; i<Globals.game.getLstObligate().size(); i++)
            {
                Position pos = Globals.game.getLstObligate().get(i).get(0);
                g.setColor(ceater);
                g.fillRoundRect(pos.x*size, pos.y*size, size, size, 0, 0);
            }
        }
        else
        {
            for (i=0; i<Globals.game.getLstObligate().size(); i++)
            {
                Position ps, pe;
                ps = Globals.game.getLstObligate().get(i).getFirst();
                if (ps.equals(Globals.game.getPosStart()))
                {
                    g.setColor(ceater);
                    g.fillRoundRect(ps.x*size, ps.y*size, size, size, 0, 0);
                    g.setColor(ceatable);
                    pe = Globals.game.getLstObligate().get(i).getLast();
                    g.fillRoundRect(pe.x*size, pe.y*size, size, size, 0, 0);
                }
            }
        }
        for (i = 0; i<dimension; i++)   //Future ==> It�ration sur les pions de chaque joueur (moins couteuse)!
        {
            for (j = 0; j<dimension; j++)
            {
                p = pions[i][j];
                if (p!=null)
                {
                    if ((i+j)%2==0)
                    {
                        if (p.color == TColor.white)
                            if (p.isSuper)
                                g.drawImage(isuperwhite.getImage(),i*size,j*size,null);
                            else
                                g.drawImage(iwhite.getImage(),i*size,j*size,null);
                        else if (p.color == TColor.black)
                            if (p.isSuper)
                                g.drawImage(isuperblack.getImage(),i*size,j*size,null);
                            else
                                g.drawImage(iblack.getImage(),i*size,j*size,null);
                    }
                }
            }
        }
        g.setColor(Color.black);
        if (currentPlayer==0)
        {
            g.drawString(res.getString("currentPlayerLabel"), size*11, 10); //g.drawString("Joueur courant: ", size*11, 10);
            g.drawImage(iwhite.getImage(),(size*11), 20, null);
        }
        else if (currentPlayer==1)
        {
            g.drawString(res.getString("currentPlayerLabel"), size*11, 10); //g.drawString("Joueur courant: ", size*11, 10);
            g.drawImage(iblack.getImage(),(size*11), 20, null);
        }
        g.setColor(Color.black);
    }
        
    public void setPions(Pion [][] matPions)    //D�finit la matrice d'affichage.
    {
        pions = matPions;
    }    
   
    public void setS(int nSize)
    {
    	//TODO to edit and verify
        nSize = 2;
    }
    
    public void setCurrentPlayer(int player)    //D�finit le joueur actuel: 0-1
    {
        currentPlayer = player;
    }
    
    public void endOfGame(int winner) {
    	System.out.println("The winner is : " + winner);
    }
}