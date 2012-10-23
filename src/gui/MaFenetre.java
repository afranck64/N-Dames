package gui;

import ia.CPU;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import model.Pion;
import model.Player;
import model.TColor;
import util.Audio;
import util.Globals;
import util.Position;
import util.Surface;
import util.Variation;
import data.Pickle;

public class MaFenetre extends JFrame implements MouseListener ,ActionListener
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3718822294864897498L;
	ResourceBundle res;
    private Pion [][] mat;  //La matrice de traitement, repr�sentant le damier
    private Player players[];   //Tableau contenant les 2 deux adversaires
    int decX;
    int decY;
    int size;   //La dimension d'une case du damier
    private int currentPlayer;  //Le joueur courant: 0-1
    private int time;   //Hmmm, pour plus tard.
    boolean hasPopup;   //Si la fen�tre a un popup ou non!
    boolean isPlaying;  //Si une partie est en cours.
    private Position posStart;  //La position de d�part, pour les d�placements
    Position posEnd;    //La position d'arriv�e, pour les d�placements
    private LinkedList<LinkedList<Position>> lstObligate; //La liste des d�placements oblig� pour le joueur courant
    LinkedList<Position> lstEated;  //La liste des pions qu'ont �t� mang�s
    private LinkedList<Variation> lstVariations;    //La liste des diff�rentes �volutions de la partie.
    //Menu Items
    JMenuBar allMenus;  //La barre contenant tous les menus
    JMenu game, options, help;    //Les diff�rents menu
    JMenuItem newGame, contact, about, pause, exit, save, load, open, scores, undo, clear; //Les diff�rentes options des menus
    AboutBox aBox;
    
    /**
    * Constructor for objects of class Damier
    */
    Surface pan ;
    public MaFenetre ()
    {  	
        res = ResourceBundle.getBundle(Globals.resourceName);
        //Pour les param�tres.
        size = 50;
        decX = 4; //decX=4
        decY = 50; //decY=50
        isPlaying = false;
        setMatrice(new Pion[Globals.dimension][Globals.dimension]);
        setPlayers(new Player[2]);
        setTitle ("Hello world!") ;
        pan = new Surface() ;
        pan.setPions(getMatrice());
        //pan.setBounds(0, 0, Globals.size*Globals.dimension, Globals.size*Globals.dimension);
        //pan.setS(size);
        setLayout(new GridLayout());
        getContentPane().add(pan) ;
        addMouseListener(this); //La Surface sera son propre �couteur d'�v�nements souris
        setLstObligate(new LinkedList<LinkedList<Position>> ());
        lstEated = new LinkedList<Position>();
        setLstVariations(new LinkedList<Variation>());
        for (int i=0; i<2; i++)
            getPlayers()[i] = new Player();
        
        
        //Edition des menus
        allMenus = new JMenuBar();
        setJMenuBar (allMenus);
        game = new JMenu(res.getString("gameLabel"));
        allMenus.add(game);
        newGame = new JMenuItem(res.getString("newLabel"));
        newGame.addActionListener(this);
        game.add(newGame);
        load = new JMenuItem(res.getString("loadLabel"));
        load.addActionListener(this);
        game.add(load);
        save = new JMenuItem(res.getString("saveLabel"));
        save.addActionListener(this);
        game.add(save);
        game.add(new JSeparator());
        
        exit = new JMenuItem(res.getString("exitLabel"));
        exit.addActionListener(this);
        game.add(exit);
        
        options = new JMenu(res.getString("optionsLabel"));
        allMenus.add(options);
        undo = new JMenuItem(res.getString("undoLabel"));
        undo.addActionListener(this);
        options.add(undo);
        clear = new JMenuItem("Clear-Map");
        clear.addActionListener(this);
        options.add(clear);
        pause = new JMenuItem(res.getString("pauseLabel"));
        pause.addActionListener(this);
        options.add(pause);
        scores = new JMenuItem(res.getString("scoresLabel"));
        scores.addActionListener(this);
        options.add(scores);
        
        help = new JMenu(res.getString("helpLabel"));
        allMenus.add(help);
        about = new JMenuItem(res.getString("aboutLabel"));
        about.addActionListener(this);
        help.add(about);
        contact = new JMenuItem(res.getString("contactLabel"));
        contact.addActionListener(this);
        help.add(contact);
        
        //Linkage vers Globals/Globals
        currentPlayer = -1;
        Globals.game = this;
        setSize(Globals.width, Globals.height);
    	setResizable(false);
    }
    
    void addPion(int x, int y)  //Ajoute le pion � son joueur
    {
        if (getMatrice()[x][y]!=null)
        {
            getPlayers()[getMatrice()[x][y].player].addPion(getMatrice()[x][y]);
        }
    }
    
    void addPion(Pion p)    //Ajoute le pion � son joueur
    {
        getPlayers()[p.player].addPion(p);
    }
    
    void deletePion (int x, int y)  //Supprime le pion � la position (x,y)
    {
        if (getMatrice()[x][y]!=null)
        {
            getPlayers()[getMatrice()[x][y].player].removePion(getMatrice()[x][y]);
            getMatrice()[x][y] = null;
        }
    }
    
    void deletePion(Pion p) //Supprime le pion pass� en param�tre
    {
        getPlayers()[p.player].removePion(p);
        getMatrice()[p.x][p.y] = null;
    }
    
    //M�thodes de binding de keys:
    public void mouseClicked (MouseEvent evt)   //Prend en charge les clics souris
    {
        String btn = new String("");
        if ((evt.getModifiers() & InputEvent.BUTTON3_MASK) != 0)
            btn = btn + "BTN3";
        else if ((evt.getModifiers() & InputEvent.BUTTON1_MASK) != 0)
            btn = btn + "BTN1";
        int x = evt.getX();
        int y = evt.getY();
        x = (x-decX)/Globals.size;
        y = (y-decY)/Globals.size;        
         if (x>=0 && x<Globals.dimension && y>=0 && y<Globals.dimension && Globals.getDebug())
        {   
            if (getMatrice()[x][y]==null && (x+y)%2==0)
            {
                getMatrice()[x][y] = new Pion();
                getMatrice()[x][y].x = x;
                getMatrice()[x][y].y = y;
                if ((evt.getModifiers() & InputEvent.BUTTON3_MASK) != 0)
                {
                    getMatrice()[x][y].setColor(TColor.black);
                    getPlayers()[1].addPion(getMatrice()[x][y]);
                }
                else if ((evt.getModifiers() & InputEvent.BUTTON1_MASK) != 0)
                {
                    getMatrice()[x][y].setColor(TColor.white);
                    getPlayers()[0].addPion(getMatrice()[x][y]);
                }
            }
            else
            {
                if (getMatrice()[x][y]!=null)
                {
                    if (getMatrice()[x][y].isSuper)
                    {
                        getPlayers()[getMatrice()[x][y].player].removePion(getMatrice()[x][y]);
                        getMatrice()[x][y] = null;
                    }
                    else
                        getMatrice()[x][y].isSuper = true;
                }
            }
            pan.updateUI();
        }
    }
    public void mousePressed (MouseEvent evt)   //Prend en charge l'appui sur les bouttons souris
    {
        String btn = new String("");
        if ((evt.getModifiers() & InputEvent.BUTTON3_MASK) != 0)
            btn = btn + "BTN3";
        else if ((evt.getModifiers() & InputEvent.BUTTON1_MASK) != 0)
            btn = btn + "BTN1";
        int x = evt.getX();
        int y = evt.getY();
        x = (x-decX)/Globals.size;
        y = (y-decY)/Globals.size;
        setPosStart(new Position());
        getPosStart().x = x;
        getPosStart().y = y;
         if (x>=0 && x<Globals.dimension && y>=0 && y<Globals.dimension)
        {   
            pan.updateUI();
        }
    }
    public void mouseReleased (MouseEvent evt)  //Prend en charge la rel�che des bouttons souris
    {
        String btn = new String("");
        if ((evt.getModifiers() & InputEvent.BUTTON3_MASK) != 0)
            btn = btn + "BTN3";
        else if ((evt.getModifiers() & InputEvent.BUTTON1_MASK) != 0)
            btn = btn + "BTN1";
        int x = evt.getX();
        int y = evt.getY();
        x = (x-decX)/Globals.size;
        y = (y-decY)/Globals.size;  
        if (x>=0 && x<Globals.dimension && y>=0 && y<Globals.dimension)
        {   
            if (getPosStart()!=null && getPosStart().x>=0 && getPosStart().x<Globals.dimension && getPosStart().y>=0 && getPosStart().y<Globals.dimension && x>=0
            && x<Globals.dimension && y>=0 && y<Globals.dimension)
            {
                gotoxy(getPosStart().x,getPosStart().y,x,y);
            }
            pan.updateUI();
        }
        setPosStart(null);
        pan.updateUI();
            
    }
    public void mouseEntered (MouseEvent evt){}
    public void mouseExited (MouseEvent evt)
    {
        setPosStart(null);
        pan.updateUI();
    }
    
    public void actionPerformed(ActionEvent evt)    //S'occupe des clics sur les menus
    {
        Object source = evt.getSource();
        if (source==newGame)    //Start a new game!
        {
            this.newGame();
            isPlaying = true;
        }
        else if (source==load)
        {
            if (Pickle.load())
            {
                Globals.debug("Players" + getPlayers()[0] + "\n" + getPlayers()[1]);
                pan.setPions(getMatrice());
                pan.setCurrentPlayer(getCurrentPlayer());
                findPaths();
                Globals.debug("To EAT: " + getLstObligate());
            }
            else
            {
                if (Globals.getDebug())
                {
                    setMatrice(new Pion[Globals.dimension][Globals.dimension]);
                    pan.setPions(getMatrice());
                    Globals.debug("No Saved Data found!");
                }
            }
        }
        else if (source==save)
        {
            if (isPlaying)
                Pickle.save();
            else
            {
                Globals.debug("Aucune partie en cours!");
            }
        }
        else if (source==undo)
        {
            undoLast();   
        }
        else if (source==clear)
        {
            clearMap();
        }
        else if (source==pause)
        {
            //Pause!
        }
        else if (source==exit)
        {
            //Exit game!
            dispose();
        }
        else if (source==about)
        {
            //About!
            aBox = new AboutBox(this,res.getString("aboutLabel"), res.getString("aboutText"));
            aBox.setVisible(true);
        }
        else if (source==contact)
        {
            aBox =  new AboutBox(this, res.getString("contactLabel"), res.getString("contactText"));
            aBox.setVisible(true);
            //Contact!
        }
        
        //EDIT - R�glages � venir: CPU, ...
        pan.updateUI();
    }
    
    public void findPaths()
    {
        LinkedList<Position> al;
        Pion pb;
        Player pl;
        al = new LinkedList<Position>();
        pl = getPlayers()[getCurrentPlayer()];
        if (pl == null)
            Globals.debug("ERROOOOOOOOOOOOOOOOOOOORRRRRRrrr!!!!!!!!!!!!! player == null!");
        getLstObligate().clear();
        for (int i=0; i<pl.getNbPions(); i++)
        {
            pb = pl.lst.get(i);
            lstEated.clear();
            paths(pb, pb.x, pb.y, al, getLstObligate(), lstEated, 0);
            al.clear();
        }
    }
    //M�thodes sp�ciales:
    
   
    public void gotoxy(int x, int y, int a, int b)  //D�place le pion (x,y) vers (a,b) si possible
   {
       Pion p;
       pan.updateUI();
       if (getMatrice()[x][y]!=null)
       {
            getMatrice()[x][y].x = x;
            getMatrice()[x][y].y = y;
       }
       if (getMatrice()[x][y]!=null && tgotoxy(getMatrice()[x][y], a, b))   //On v�rifie que le d�placement est possible
       {
           p = getMatrice()[a][b];
           getMatrice()[a][b] = getMatrice()[x][y];
           getMatrice()[x][y] = p;
           getMatrice()[a][b].x = a;
           getMatrice()[a][b].y = b;
           pan.updateUI();
           Audio.play("move");
           setCurrentPlayer((getCurrentPlayer() + 1) % 2);
           pan.setCurrentPlayer(getCurrentPlayer());
           new LinkedList<Position>();
           //Traitement: on passe la main au joueur suivant;
           findPaths();
           //Jeu si ordinateur actif!
           if (getCurrentPlayer()==1)
           {
               try
               {
                   Thread.sleep(300);
               }
               catch(Exception expt)
               {}
               CPU cpu = new CPU( 1);
               cpu.auto();
           }
           Globals.debug("TO EAT : "+getLstObligate());
       }
       else
       {
               //Le d�placement n'a pas �t� effectu�: on rappelle la m�thode des�lection des positions
       }
       pan.updateUI();
   }
       
    public  boolean tgotoxy(Pion p, int a, int b)   //Retourne vrai si le pion <p> peut �tre d�palc� vers (a,b)
    {
       int dx, dy;             //Le vecteur d�placement
       int ux, uy;             //Un vecteur unitaire parall�le au vecteur de d�placement
       int dist;       //La distance de d�placement
       int i,j;                //Pour les boucles
       Pion pb;        //Pion pour les traitements temporaires.
       Pion p2;
       dx = a-p.x;
       dy = b-p.y;
       if (getLstObligate().size()==0)   //Il n'y a pas de pions � manger
       {
           if(getMatrice()[a][b]==null)  //V�rification que la case d'arriv�e est vide (sinon, impossible!)
           {
               if((p.player==getCurrentPlayer()) && (dx!=0) && (dy!=0) && (dx<Globals.dimension) && (dy<Globals.dimension) && (Math.abs(dx) == Math.abs(dy)))
               {
                   dist = Math.abs(dx);
                   ux = dx/dist;
                   uy = dy/dist;
                   if (p.isSuper)        //Traitement dans le cas d'un pion de type super! (Une dame)
                   {
                       i = 1;
                       while (i<dist)
                       {
                           pb = getMatrice()[p.x+ux*i][p.y+uy*i];
                           if (pb!=null)
                           {
                               if (pb.player==getCurrentPlayer())
                                   return false;
                               else if (getMatrice()[p.x+(i+1)*ux][p.y+(i+1)*uy]!=null)
                                   return false;
                               break;
                           }
                           i++;
                       }
                   }
                   else //Traitement d'un simple pion
                   {
                        if (dist==1)    //distance == 1, simple d�placement
                        {
                            Globals.debug("Dist == 1");
                            if (p.color==TColor.white && uy!=1)     //Les blancs descendent uniquement
                                return false;
                            else if (p.color==TColor.black && uy!=-1)
                                 return false;
                            //Traitement: d�placement d'une case (normal)
                        }
                        else    //Sinon, impossible
                            return false;
                   }
               }
               else
                    return false;
           }
           else
               return false;
           //Traitement: suppressions des �l�ments dans le cas d'une �limination.
           //Traitement2: mise � jour desliste de repas obligatoires.
           Variation var = new Variation(getCurrentPlayer(), p.x, p.y, a, b);    //Sauvegarde le mouvement pour annulation possible
           if (p.color == TColor.white && b==(Globals.dimension-1))
           {
               p.isSuper = true;
               var.setToSuper(true);
               Audio.play("dame");
           }
           else if (p.color == TColor.black && b==0)
           {
               p.isSuper = true;
               var.setToSuper(true);
               Audio.play("dame");
           }
           getLstVariations().add(var);
           return true;
       }
       else //Il y a des pions � manger
       {
           LinkedList<Position> mylst, lst;
           Variation var = new Variation(getCurrentPlayer(), p.x, p.y, a, b);    //Sauvegarde le mouvement pour annulation possible
           mylst = null;
           Position pos1,pos2;
           i = 0;
           while (i<getLstObligate().size()) //Recherche d'une chaine de postions correspondant � (<p.x,p.y> , <a,b>)
           {
               lst = getLstObligate().get(i);
               if (lst.get(0).x==p.x && lst.get(0).y==p.y && lst.get(lst.size()-1).x==a && lst.get(lst.size()-1).y==b)
               {
                   mylst = lst;
                   break;
               }
               i++;
           }
           if (mylst==null) //Si les coordonn�es ne coincident avec aucune des sous liste, Impossible
                return false;
           else //Sinon, on a des pions � supprimer
           {
               pos1 = mylst.get(0);
               for (i=1; i<mylst.size(); i++)
               {
                   pos2 = mylst.get(i);
                   dist = Math.abs(pos2.x-pos1.x);
                   ux = (pos2.x-pos1.x)/dist;
                   uy = (pos2.y-pos1.y)/dist;
                   j = 1;
                   p2 = null;
                   while (j<dist && p2==null)
                   {
                       p2 = getMatrice()[pos1.x+(ux)*j][pos1.y+(uy)*j];
                       j++;
                    }
                   if (p2!=null)   //Cas des d�lplacement de dames, il peut y avoir des positions vides.
                   {
                       var.addPion(p2);     //Ajoute le pion � la liste des pion supprim�s ce tour.
                       deletePion(p2);
                       try
                       {
                           Thread.sleep(150);
                       }
                       catch (Exception expt)
                       {}
                       Audio.play("eat");
                       try
                       {
                           Thread.sleep(150);
                       }
                       catch (Exception expt)
                       {}
                       pan.updateUI();
                   }
                   pos1 = pos2;
               }
           }
           if (!p.isSuper && p.color == TColor.white && b==(Globals.dimension-1))  //Si le pion n'est pas une dame et atteint sa limite ==> devient Dame
           {
               p.isSuper = true;
               var.setToSuper(true);
               Audio.play("dame");
           }
           else if (!p.isSuper && p.color == TColor.black && b==0)  //Si le pion n'est pas une dame et atteint sa limite ==> devient Dame
           {
               p.isSuper = true;
               var.setToSuper(true);
               Audio.play("dame");
           }
           getLstVariations().add(var);
       }
       return true;
    }
    
    public void updateList(LinkedList<Position> l, LinkedList<LinkedList<Position>> lst)   //Met � jour la liste
    //lst, � partir de l'�l�ment <l>
    {
        int i;
        if (l.size()>1)
        {
            if (lst.size()==0)  //La liste est vide, on ajoute juste la nouvelle sous-liste
            {
                lst.add(l);
            }
            else if (lst.size()>0)  //La liste n'est pas vide!
            {
                if (lst.get(0).size()==l.size() && !lst.contains(l))    //La liste contient des �l�ments de m�me Globals.dimension
                {
                    for (i=0; i<lst.size(); i++)    //On v�rifie qu'il n'y a pas d�j� de m�me (d�part, fin)
                    if (lst.get(i).getFirst()==l.getFirst() && lst.get(i).getLast()==l.getLast())
                    {
                        return;
                    }
                    lst.add(l);
                }
                else if (lst.get(0).size() < l.size())  //La liste contient des sous-listes plus petites que <l>
                {
                    lst.clear();
                    lst.add(l);
                }
            }
        }
    }
       
    public void paths (Pion p, int x, int y, LinkedList<Position> l, LinkedList<LinkedList<Position>> lst, LinkedList<Position> eated, int pVec)
    //Recherche toutes les prises possibles du pion <p>, � partir de la positions (x,y), de son d�placement pr�c�dent <pVec>, de la liste
    //de ses positions pr�c�dentes <l> et des prises <eated>
    {
        Position pos = new Position();
		new Position();
		Position tpos = new Position();
        Pion p1,p2;
        LinkedList<Position> nl;
        LinkedList<Position> nEated,nlEated;
        int nx,ny,i;
        int lmin = 1, lmax = Globals.dimension-2;
        int linf=1, lsup=Globals.dimension-2;
        int min = 0, max = Globals.dimension-1;
        int cVec = 0;
        pos.x = x;
        pos.y = y;
        i = 0;
        nlEated = new LinkedList<Position>(eated);
        nEated = new LinkedList<Position>();
        if (l.contains(pos))
        {
            updateList(l,lst);
            return;
        }
        else
        {
            l.add(pos);
            updateList(l,lst);
        }
        if (p.isSuper)  //Le pion <p> est une dame
        {
            Globals.debug("Super Pion - Dame : Profondeur = " + l.size() );
            if (x>linf && y>linf && pVec!=4)    //D�placement (haut, gauche)
            {
                cVec = 1;
                i=0;
                while ((x-i-1)>lmin && (y-i-1)>lmin && getMatrice()[x-i-1][y-i-1]==null)
                    i++;
                Globals.debug("X = " + x + ", Y = " + y + " I = " + i);
                p1 = getMatrice()[x-i-1][y-i-1];
                p2 = getMatrice()[x-i-2][y-i-2];
                tpos = new Position(x-i-1, y-i-1);
                Globals.debug("P1 == " + p1 + " && P2 == " + p2);
                if (p1!=null && p1.player!=p.player && p2==null && !nlEated.contains(tpos))
                {
                    nlEated.add(tpos);
                    nEated = new LinkedList<Position>(nlEated);
                    i+=2;
                    while ((x-i)>=min && (y-i)>=min && getMatrice()[x-i][y-i]==null)
                    {
                        Globals.debug("Recall Paths at (" + (x-i) + "," + (y-i));
                        nl = new LinkedList<Position> (l);
                        paths(p,x-i,y-i,nl,lst,nEated,cVec);
                        i++;
                    }
                }
            }
            if (x<lsup && y>linf && pVec!=3)    //D�placement (haut, droite)
            {
                cVec = 2;
                i=0;
                while ((x+i+1)<lmax && (y-i-1)>lmin && getMatrice()[x+i+1][y-i-1]==null)
                    i++;
                Globals.debug("X = " + x + ", Y = " + y + " I = " + i);
                p1 = getMatrice()[x+i+1][y-i-1];
                p2 = getMatrice()[x+i+2][y-i-2];
                tpos = new Position(x+i+1, y-i-1);
                Globals.debug("P1 == " + p1 + " && P2 == " + p2);
                if (p1!=null && p1.player!=p.player && p2==null  && !nlEated.contains(tpos))
                {
                    nlEated.add(tpos);
                    nEated = new LinkedList<Position>(nlEated);
                    i+=2;
                    while ((x+i)<=max && (y-i)>=min && getMatrice()[x+i][y-i]==null)
                    {
                        nl = new LinkedList<Position> (l);
                        Globals.debug("Recall Paths at (" + (x+i) + "," + (y-i));
                        paths(p,x+i,y-i,nl,lst,nEated,cVec);
                        i++;
                    }
                }
            }
            if (x>linf && y<lsup && pVec!=2)    //D�placement (bas, gauche)
            {
                cVec = 3;
                i=0;
                while ((x-i-1)>lmin && (y+i+1)<lmax && getMatrice()[x-i-1][y+i+1]==null)
                    i++;
                Globals.debug("X = " + x + ", Y = " + y + " I = " + i);
                p1 = getMatrice()[x-i-1][y+i+1];
                p2 = getMatrice()[x-i-2][y+i+2];
                tpos = new Position(x-i-1, y+i+1);
                Globals.debug("P1 == " + p1 + " && P2 == " + p2);
                if (p1!=null && p1.player!=p.player && p2==null && !nlEated.contains(tpos))
                {
                    nlEated.add(tpos);
                    nEated = new LinkedList<Position>(nlEated);
                    i+=2;
                    while ((x-i)>=min && (y+i)<=max && getMatrice()[x-i][y+i]==null)
                    {
                        Globals.debug("Recall Paths at (" + (x-i) + "," + (y+i) );
                        nl = new LinkedList<Position> (l);
                        paths(p,x-i,y+i,nl,lst,nEated,cVec);
                        i++;
                    }
                }
            }
            
            if (x<lsup && y<lsup && pVec!=1)    //D�placement (bas, droite)
            {
                cVec = 4;
                i=0;
                while ((x+i+1)<lmax && (y+i+1)<lmax && getMatrice()[x+i+1][y+i+1]==null)
                    i++;
                Globals.debug("X = " + x + ", Y = " + y + " I = " + i);
                p1 = getMatrice()[x+i+1][y+i+1];
                p2 = getMatrice()[x+i+2][y+i+2];
                tpos = new Position(x+i+1, y+i+1);
                Globals.debug("P1 == " + p1 + " && P2 == " + p2);
                if (p1!=null && p1.player!=p.player && p2==null && !nlEated.contains(tpos))
                {
                    nlEated.add(tpos);
                    nEated = new LinkedList<Position>(nlEated);
                    i+=2;
                    while ((x+i)<=max && (y+i)<=max && getMatrice()[x+i][y+i]==null)
                    {
                        Globals.debug("Recall Paths at (" + (x+i) + "," + (y+i));
                        nl = new LinkedList<Position> (l);
                        paths(p,x+i,y+i,nl,lst,nEated,cVec);
                        i++;
                    }
                }
            }
            return;
        }
        else    //Le pion n'est pas une Dame! - OK
        {
            Globals.debug("Simple pion");
            nx = x-2;
            ny = y-2;
            if (nx>=0 && ny>=0 && getMatrice()[nx][ny]==null)// && directions[p.player]==-1)
            {
                p2 = getMatrice()[x-1][y-1];
                Globals.debug("P2 = "+p2);
                if (p2!=null && p2.player!=p.player)
                {
                    nl = new LinkedList<Position> (l);
                    paths(p,nx,ny,nl,lst,nEated,cVec);
                }
            }
            nx = x-2;
            ny = y+2;
            if (nx>=0 && ny<Globals.dimension && getMatrice()[nx][ny]==null)// && directions[p.player]==1)
            {
                p2 = getMatrice()[x-1][y+1];
                Globals.debug("P2 = "+p2);
                if (p2!=null && p2.player!=p.player)
                {
                    nl = new LinkedList<Position> (l);
                    paths(p,nx,ny,nl,lst,nEated,cVec);
                }
            }
            nx = x+2;
            ny = y-2;
            if (nx<Globals.dimension && ny>=0 && getMatrice()[nx][ny]==null)// && directions[p.player]==-1)
            {
                p2 = getMatrice()[x+1][y-1];
                Globals.debug("P2 = "+p2);
                if (p2!=null && p2.player!=p.player)
                {
                    nl = new LinkedList<Position> (l);
                    paths(p,nx,ny,nl,lst,nEated,cVec);
                }
            }
            nx = x+2;
            ny = y+2;
            if (nx<Globals.dimension && ny<Globals.dimension && getMatrice()[nx][ny]==null)// && directions[p.player]==1)
            {
                p2 = getMatrice()[x+1][y+1];
                Globals.debug("P2 = "+p2);
                if (p2!=null && p2.player!=p.player)
                {
                    nl = new LinkedList<Position> (l);
                    paths(p,nx,ny,nl,lst,nEated,cVec);
                }
            }
            
        }
    }
    
    void isGameEnd() {
    	if (this.players[this.currentPlayer].getPions().size()==0) {
    		pan.endOfGame((currentPlayer + 1)%2);
    	}
    		
    	
    }
    void newGame()  //D�marre une nouvelle partie.
    {
        setLstObligate(new LinkedList<LinkedList<Position>>());
        lstEated = new LinkedList<Position>();
        setLstVariations(new LinkedList<Variation>());
        setCurrentPlayer(0);
        setPlayers(new Player[2]);
        setMatrice(new Pion[Globals.dimension][Globals.dimension]);
        for (int i=0; i<2; i++)
            getPlayers()[i] = new Player();
        for (int i=0; i<Globals.dimension; i++)
        {
            for (int j=0; j<Globals.dimension; j++)
            {
                if ((i+j)%2==0 && getMatrice()[i][j]!=null)
                {
                    getMatrice()[i][j] = null;
                }
            }
        }
        int nbLines = (Globals.dimension - 1)/2;
        for (int i=0; i<Globals.dimension; i++)
        {
            for (int j=0; j<nbLines; j++)
            {
                if ((i+j)%2 == 0)
                {
                    getMatrice()[i][j] = new Pion(i,j,TColor.white);
                    this.addPion(getMatrice()[i][j]);
                }
            }
            for (int j=(Globals.dimension+2)/2; j<Globals.dimension; j++)
            {
                
                if ((i+j)%2 == 0)
                {
                    getMatrice()[i][j] = new Pion(i,j,TColor.black);
                    this.addPion(getMatrice()[i][j]);
                }
            }
        }
        pan.setCurrentPlayer(getCurrentPlayer());
        pan.setPions(getMatrice());
    }
    
    void undoLast()
    {
        Pion p;
        Globals.debug("\nMat before:");
        for (int i = 0; i<Globals.dimension; i++)
        {
            for (int j=0; j<Globals.dimension; j++)
            {
                if (getMatrice()[i][j]==null)
                    Globals.debug("0 ");
                else
                    Globals.debug("1 ");
            }
            Globals.debug("");
        }
        if (getLstVariations().size()>0)
        {
            Variation var = getLstVariations().pollLast();
            p = getMatrice()[var.getEndPos().x][var.getEndPos().y];
            p.x = var.getStartPos().x;
            p.y = var.getStartPos().y;
            getMatrice()[var.getStartPos().x][var.getStartPos().y] = p;
            getMatrice()[var.getEndPos().x][var.getEndPos().y] = null;/*
            mat[var.start.x][var.start.y] = mat[var.end.x][var.end.y];
            mat[var.start.x][var.start.y].x = var.start.x;
            mat[var.start.x][var.start.y].y = var.start.y;
            mat[var.end.x][var.end.y] = null; */
            if (var.isToSuper())
                p.setSuper(false);
            setCurrentPlayer(var.getCurrentPlayer());
            pan.setCurrentPlayer(getCurrentPlayer());
            while (var.hasPion())
            {
                p = var.getPion();
                getMatrice()[p.x][p.y] = p;
                addPion(p);
            }
            findPaths();
            Globals.debug("Modif! " + var);
            Globals.debug("To EAT : " + getLstObligate());
            Globals.debug("\nMat after:");
            for (int i = 0; i<Globals.dimension; i++)
            {
                for (int j=0; j<Globals.dimension; j++)
                {
                    if (getMatrice()[i][j]==null)
                        Globals.debug("0 ");
                    else
                        Globals.debug("1 ");
                }
                Globals.debug("");
            }
        }
    }
    
    public void clearMap()
    {
        setMatrice(new Pion[Globals.dimension][Globals.dimension]);
        pan.setPions(getMatrice());
        getLstObligate().clear();
        getLstVariations().clear();
        lstEated.clear();
        for (int i=0; i<Globals.nbPlayers; i++)
            getPlayers()[i].reset();
    }

	public LinkedList<LinkedList<Position>> getLstObligate() {
		return lstObligate;
	}

	public void setLstObligate(LinkedList<LinkedList<Position>> lstObligate) {
		this.lstObligate = lstObligate;
	}

	public Position getPosStart() {
		return posStart;
	}

	public void setPosStart(Position posStart) {
		this.posStart = posStart;
	}

	public Pion [][] getMatrice() {
		return mat;
	}

	public void setMatrice(Pion [][] mat) {
		this.mat = mat;
	}

	public Player[] getPlayers() {
		return players;
	}

	public void setPlayers(Player players[]) {
		this.players = players;
	}

	public int getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(int currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public LinkedList<Variation> getLstVariations() {
		return lstVariations;
	}

	public void setLstVariations(LinkedList<Variation> lstVariations) {
		this.lstVariations = lstVariations;
	}
}