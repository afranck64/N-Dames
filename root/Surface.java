
/**
 * Write a description of class Surface here.
 * 
 * @author Franck Awounang Nekdem
 * @version v 0.1 - (c) 2011
 */
import javax.swing.* ;
import java.awt.* ;
import java.awt.event.* ;   // pour MouseEvent et MouseListener
import java.util.Iterator;
import java.util.LinkedList;

class Surface extends JPanel
{
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
    
    Surface ()
    {
        Resource res = new Resource();
        clight = Color.yellow; //La couleur pour les cases claires
        cdark = Color.green; //La couleur pour les cases sombres;
        //ceater = new Color(120,232,165);
        //ceatable = new Color(123, 45, 222);
        ceatable = Color.red;
        ceater = Color.blue;
        iblack = new ImageIcon(res.getResourceURL("iBlack"));//("data/images/black.gif");
        iwhite = new ImageIcon(res.getResourceURL("iWhite"));//"data/images/white.gif");
        isuperblack = new ImageIcon(res.getResourceURL("iSuperblack"));
        isuperwhite = new ImageIcon(res.getResourceURL("iSuperwhite"));
        //pions[0][0].isSuper = true;
        //addMouseListener(this); //La Surface sera son propre écouteur d'évènements souris
    }
    
    public void paintComponent(Graphics g)
    {
        
        //super.paintComponent(g) ;
        int larg = 20, haut = 20, i=0, j=0 ;
        int dimension = Globals.dimension;
        int size = Globals.size;
        Pion p;
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
        if (Globals.game.posStart==null)
        {
            for (i=0; i<Globals.game.lstObligate.size(); i++)
            {
                Position pos = Globals.game.lstObligate.get(i).get(0);
                g.setColor(ceater);
                g.fillRoundRect(pos.x*size, pos.y*size, size, size, 0, 0);
            }
        }
        else
        {
            for (i=0; i<Globals.game.lstObligate.size(); i++)
            {
                Position ps, pe;
                ps = Globals.game.lstObligate.get(i).getFirst();
                if (ps.equals(Globals.game.posStart))
                {
                    g.setColor(ceater);
                    g.fillRoundRect(ps.x*size, ps.y*size, size, size, 0, 0);
                    g.setColor(ceatable);
                    pe = Globals.game.lstObligate.get(i).getLast();
                    g.fillRoundRect(pe.x*size, pe.y*size, size, size, 0, 0);
                }
            }
        }
        for (i = 0; i<dimension; i++)   //Future ==> Itération sur les pions de chaque joueur (moins couteuse)!
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
        g.drawString(Resource.getString("currentPlayerLabel"), size*11, 10); //g.drawString("Joueur courant: ", size*11, 10);
        if (currentPlayer==0)
            g.drawImage(iwhite.getImage(),(size*11), 20, null);
        else
            g.drawImage(iblack.getImage(),(size*11), 20, null);
        g.setColor(Color.black);
    }
        
    public void setPions(Pion [][] matPions)    //Définit la matrice d'affichage.
    {
        pions = matPions;
    }    
   
    public void setS(int nSize)
    {
        nSize = nSize;
    }
    
    public void setCurrentPlayer(int player)    //Définit le joueur actuel: 0-1
    {
        currentPlayer = player;
    }
}