/**
 * Write a description of class MaFenetre here.
 * 
 * @author Franck Awounang Nekdem
 * @version v 0.1 - (c) 2011
 */

import java.lang.Math;
import javax.swing.* ;
import java.awt.* ;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

class MaFenetre extends JFrame implements MouseListener ,ActionListener
{
    Resource res;
    Pion [][] mat;  //La matrice de traitement, représentant le damier
    Player players[];   //Tableau contenant les 2 deux adversaires
    int decX;
    int decY;
    int size;   //La dimension d'une case du damier
    int currentPlayer;  //Le joueur courant: 0-1
    int time;   //Hmmm, pour plus tard.
    boolean hasPopup;   //Si la fenêtre a un popup ou non!
    boolean isPlaying;  //Si une partie est en cours.
    Position posStart;  //La position de départ, pour les déplacements
    Position posEnd;    //La position d'arrivée, pour les déplacements
    LinkedList<LinkedList<Position>> lstObligate; //La liste des déplacements obligé pour le joueur courant
    LinkedList<Position> lstEated;  //La liste des pions qu'ont été mangés
    LinkedList<Variation> lstVariations;    //La liste des différentes évolutions de la partie.
    //Menu Items
    JMenuBar allMenus;  //La barre contenant tous les menus
    JMenu game, options, help;    //Les différents menu
    JMenuItem newGame, contact, about, pause, exit, save, load, open, scores, undo, clear; //Les différentes options des menus
    AboutBox aBox;
    
    /**
    * Constructor for objects of class Damier
    */
    public Surface pan ;
    MaFenetre ()
    {
        res = new Resource();   //Pour les paramètres.
        size = 50;
        decX = 4;
        decY = 50;
        isPlaying = false;
        mat = new Pion[Globals.dimension][Globals.dimension];
        players = new Player[2];
        setTitle ("Hello world!") ; setSize (650, 650) ;
        //setBounds (4, 30, 600, 600);
        pan = new Surface() ;
        pan.setPions(mat);
        //pan.setS(size);
        setLayout(new GridLayout());
        getContentPane().add(pan) ;
        addMouseListener(this); //La Surface sera son propre écouteur d'évènements souris
        lstObligate = new LinkedList<LinkedList<Position>> ();
        lstEated = new LinkedList<Position>();
        lstVariations = new LinkedList<Variation>();
        for (int i=0; i<2; i++)
            players[i] = new Player();
        
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
        Globals.game = this;
    }
    
    void addPion(int x, int y)  //Ajoute le pion à son joueur
    {
        if (mat[x][y]!=null)
        {
            players[mat[x][y].player].addPion(mat[x][y]);
        }
    }
    
    void addPion(Pion p)    //Ajoute le pion à son joueur
    {
        players[p.player].addPion(p);
    }
    
    void deletePion (int x, int y)  //Supprime le pion à la position (x,y)
    {
        if (mat[x][y]!=null)
        {
            players[mat[x][y].player].removePion(mat[x][y]);
            mat[x][y] = null;
        }
    }
    
    void deletePion(Pion p) //Supprime le pion passé en paramètre
    {
        players[p.player].removePion(p);
        mat[p.x][p.y] = null;
    }
    
    //Méthodes de binding de keys:
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
         if (x>=0 && x<Globals.dimension && y>=0 && y<Globals.dimension && Globals.debug)
        {   
            if (mat[x][y]==null && (x+y)%2==0)
            {
                mat[x][y] = new Pion();
                mat[x][y].x = x;
                mat[x][y].y = y;
                if ((evt.getModifiers() & InputEvent.BUTTON3_MASK) != 0)
                {
                    mat[x][y].setColor(TColor.black);
                    players[1].addPion(mat[x][y]);
                }
                else if ((evt.getModifiers() & InputEvent.BUTTON1_MASK) != 0)
                {
                    mat[x][y].setColor(TColor.white);
                    players[0].addPion(mat[x][y]);
                }
            }
            else
            {
                if (mat[x][y]!=null)
                {
                    if (mat[x][y].isSuper)
                    {
                        players[mat[x][y].player].removePion(mat[x][y]);
                        mat[x][y] = null;
                    }
                    else
                        mat[x][y].isSuper = true;
                }
            }
            pan.updateUI();
        }
    }
    public void mousePressed (MouseEvent evt)   //Prend en charge l'appui sur les bouttons souris
    {
        Pion p;
        String btn = new String("");
        if ((evt.getModifiers() & InputEvent.BUTTON3_MASK) != 0)
            btn = btn + "BTN3";
        else if ((evt.getModifiers() & InputEvent.BUTTON1_MASK) != 0)
            btn = btn + "BTN1";
        int x = evt.getX();
        int y = evt.getY();
        x = (x-decX)/Globals.size;
        y = (y-decY)/Globals.size;
        posStart = new Position();
        posStart.x = x;
        posStart.y = y;
         if (x>=0 && x<Globals.dimension && y>=0 && y<Globals.dimension)
        {   
            pan.updateUI();
        }
    }
    public void mouseReleased (MouseEvent evt)  //Prend en charge la relâche des bouttons souris
    {
        Pion p;
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
            if (posStart!=null && posStart.x>=0 && posStart.x<Globals.dimension && posStart.y>=0 && posStart.y<Globals.dimension && x>=0
            && x<Globals.dimension && y>=0 && y<Globals.dimension)
            {
                gotoxy(posStart.x,posStart.y,x,y);
            }
            pan.updateUI();
        }
        posStart = null;
        pan.updateUI();
            
    }
    public void mouseEntered (MouseEvent evt){}
    public void mouseExited (MouseEvent evt)
    {
        posStart = null;
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
            boolean loaded;
            if (Pickle.load())
            {
                System.out.println("Players" + players[0] + "\n" + players[1]);
                pan.setPions(mat);
                pan.setCurrentPlayer(currentPlayer);
                findPaths();
                if (Globals.debug)
                    System.out.println("To EAT: " + lstObligate);
            }
            else
            {
                if (Globals.debug)
                {
                    mat = new Pion[Globals.dimension][Globals.dimension];
                    pan.setPions(mat);
                    Player []players = new Player[Globals.nbPlayers];
                    String msg = new String("No Saved Data found!");
                    System.out.println(msg);
                }
            }
        }
        else if (source==save)
        {
            if (isPlaying)
                Pickle.save();
            else
            {
                String msg = new String("Aucune partie en cours!");
                if (Globals.debug)System.out.println(msg);
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
        
        //EDIT - Réglages à venir: CPU, ...
        pan.updateUI();
    }
    
    public void findPaths()
    {
        LinkedList<Position> al;
        Pion p, pb;
        Player pl;
        al = new LinkedList<Position>();
        pl = players[currentPlayer];
        if (pl == null)
            System.out.println("ERROOOOOOOOOOOOOOOOOOOORRRRRRrrr!!!!!!!!!!!!! player == null!");
        lstObligate.clear();
        for (int i=0; i<pl.nbPions; i++)
        {
            pb = pl.lst.get(i);
            lstEated.clear();
            paths(pb, pb.x, pb.y, al, lstObligate, lstEated, 0);
            al.clear();
        }
    }
    //Méthodes spéciales:
    
   
    public void gotoxy(int x, int y, int a, int b)  //Déplace le pion (x,y) vers (a,b) si possible
   {
       Pion p,pb;
       Player pl;
       LinkedList<Position> al;
       pan.updateUI();
       if (mat[x][y]!=null)
       {
            mat[x][y].x = x;
            mat[x][y].y = y;
       }
       if (mat[x][y]!=null && tgotoxy(mat[x][y], a, b))   //On vérifie que le déplacement est possible
       {
           p = mat[a][b];
           mat[a][b] = mat[x][y];
           mat[x][y] = p;
           mat[a][b].x = a;
           mat[a][b].y = b;
           pan.updateUI();
           Audio.play("move");
           currentPlayer = (currentPlayer + 1) % 2;
           pan.setCurrentPlayer(currentPlayer);
           al = new LinkedList<Position>();
           //Traitement: on passe la main au joueur suivant;
           findPaths();
           //Jeu si ordinateur actif!
           if (currentPlayer==1)
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
           if (Globals.debug) System.out.println("TO EAT : "+lstObligate);
       }
       else
       {
               //Le déplacement n'a pas été effectué: on rappelle la méthode desélection des positions
       }
       pan.updateUI();
   }
       
    public  boolean tgotoxy(Pion p, int a, int b)   //Retourne vrai si le pion <p> peut être dépalcé vers (a,b)
    {
       int dx, dy;             //Le vecteur déplacement
       int ux, uy;             //Un vecteur unitaire parallèle au vecteur de déplacement
       int dist;       //La distance de déplacement
       int nbPions = 0;        //peut être temporaire
       int i,j;                //Pour les boucles
       Pion pb;        //Pion pour les traitements temporaires.
       Pion p2;
       dx = a-p.x;
       dy = b-p.y;
       if (lstObligate.size()==0)   //Il n'y a pas de pions à manger
       {
           if(mat[a][b]==null)  //Vérification que la case d'arrivée est vide (sinon, impossible!)
           {
               if((p.player==currentPlayer) && (dx!=0) && (dy!=0) && (dx<Globals.dimension) && (dy<Globals.dimension) && (Math.abs(dx) == Math.abs(dy)))
               {
                   dist = Math.abs(dx);
                   ux = dx/dist;
                   uy = dy/dist;
                   if (p.isSuper)        //Traitement dans le cas d'un pion de type super! (Une dame)
                   {
                       i = 1;
                       while (i<dist)
                       {
                           pb = mat[p.x+ux*i][p.y+uy*i];
                           if (pb!=null)
                           {
                               nbPions ++;
                               if (pb.player==currentPlayer)
                                   return false;
                               else if (mat[p.x+(i+1)*ux][p.y+(i+1)*uy]!=null)
                                   return false;
                               break;
                           }
                           i++;
                       }
                   }
                   else //Traitement d'un simple pion
                   {
                        if (dist==1)    //distance == 1, simple déplacement
                        {
                            if (Globals.debug)System.out.println("Dist == 1");
                            if (p.color==TColor.white && uy!=1)     //Les blancs descendent uniquement
                                return false;
                            else if (p.color==TColor.black && uy!=-1)
                                 return false;
                            //Traitement: déplacement d'une case (normal)
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
           //Traitement: suppressions des éléments dans le cas d'une élimination.
           //Traitement2: mise à jour desliste de repas obligatoires.
           Variation var = new Variation(currentPlayer, p.x, p.y, a, b);    //Sauvegarde le mouvement pour annulation possible
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
           lstVariations.add(var);
           return true;
       }
       else //Il y a des pions à manger
       {
           LinkedList<Position> mylst, lst;
           Variation var = new Variation(currentPlayer, p.x, p.y, a, b);    //Sauvegarde le mouvement pour annulation possible
           int idPlayer2 = (currentPlayer + 1)%2, px, py;
           mylst = null;
           Position pos1,pos2;
           boolean found = false;
           i = 0;
           while (i<lstObligate.size()) //Recherche d'une chaine de postions correspondant à (<p.x,p.y> , <a,b>)
           {
               lst = lstObligate.get(i);
               if (lst.get(0).x==p.x && lst.get(0).y==p.y && lst.get(lst.size()-1).x==a && lst.get(lst.size()-1).y==b)
               {
                   mylst = lst;
                   break;
               }
               i++;
           }
           if (mylst==null) //Si les coordonnées ne coincident avec aucune des sous liste, Impossible
                return false;
           else //Sinon, on a des pions à supprimer
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
                       p2 = mat[pos1.x+(ux)*j][pos1.y+(uy)*j];
                       j++;
                    }
                   if (p2!=null)   //Cas des délplacement de dames, il peut y avoir des positions vides.
                   {
                       var.addPion(p2);     //Ajoute le pion à la liste des pion supprimés ce tour.
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
                       
                       /*players[p2.player].removePion(p2);
                       mat[p2.x][p2.y] = null;*/
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
           lstVariations.add(var);
       }
       return true;
    }
    
    public void updateList(LinkedList<Position> l, LinkedList<LinkedList<Position>> lst)   //Met à jour la liste
    //lst, à partir de l'élément <l>
    {
        int i;
        boolean already = false;    //Vrai si la liste <lst> contient déjà une liste de même couple (start,end) que <l>
        if (l.size()>1)
        {
            if (lst.size()==0)  //La liste est vide, on ajoute juste la nouvelle sous-liste
            {
                lst.add(l);
            }
            else if (lst.size()>0)  //La liste n'est pas vide!
            {
                if (lst.get(0).size()==l.size() && !lst.contains(l))    //La liste contient des éléments de même Globals.dimension
                {
                    for (i=0; i<lst.size(); i++)    //On vérifie qu'il n'y a pas déjà de même (départ, fin)
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
    //Recherche toutes les prises possibles du pion <p>, à partir de la positions (x,y), de son déplacement précédent <pVec>, de la liste
    //de ses positions précédentes <l> et des prises <eated>
    {
        Position pos = new Position(), pos2 = new Position(), tpos = new Position();
        Pion p1,p2;
        LinkedList<Position> nl;
        LinkedList<Position> nEated,nlEated;
        int nx,ny,state,i;
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
            if (Globals.debug) System.out.println("Super Pion - Dame : Profondeur = " + l.size() );
            if (x>linf && y>linf && pVec!=4)    //Déplacement (haut, gauche)
            {
                cVec = 1;
                i=0;
                while ((x-i-1)>lmin && (y-i-1)>lmin && mat[x-i-1][y-i-1]==null)
                    i++;
                if (Globals.debug) System.out.println("X = " + x + ", Y = " + y + " I = " + i);
                p1 = mat[x-i-1][y-i-1];
                p2 = mat[x-i-2][y-i-2];
                tpos = new Position(x-i-1, y-i-1);
                if (Globals.debug) System.out.println("P1 == " + p1 + " && P2 == " + p2);
                if (p1!=null && p1.player!=p.player && p2==null && !nlEated.contains(tpos))
                {
                    nlEated.add(tpos);
                    nEated = new LinkedList<Position>(nlEated);
                    i+=2;
                    while ((x-i)>=min && (y-i)>=min && mat[x-i][y-i]==null)
                    {
                        if (Globals.debug) System.out.println("Recall Paths at (" + (x-i) + "," + (y-i));
                        nl = new LinkedList<Position> (l);
                        paths(p,x-i,y-i,nl,lst,nEated,cVec);
                        i++;
                    }
                }
            }
            if (x<lsup && y>linf && pVec!=3)    //Déplacement (haut, droite)
            {
                cVec = 2;
                i=0;
                while ((x+i+1)<lmax && (y-i-1)>lmin && mat[x+i+1][y-i-1]==null)
                    i++;
                if (Globals.debug) System.out.println("X = " + x + ", Y = " + y + " I = " + i);
                p1 = mat[x+i+1][y-i-1];
                p2 = mat[x+i+2][y-i-2];
                tpos = new Position(x+i+1, y-i-1);
                if (Globals.debug) System.out.println("P1 == " + p1 + " && P2 == " + p2);
                if (p1!=null && p1.player!=p.player && p2==null  && !nlEated.contains(tpos))
                {
                    nlEated.add(tpos);
                    nEated = new LinkedList<Position>(nlEated);
                    i+=2;
                    while ((x+i)<=max && (y-i)>=min && mat[x+i][y-i]==null)
                    {
                        nl = new LinkedList<Position> (l);
                        if (Globals.debug) System.out.println("Recall Paths at (" + (x+i) + "," + (y-i));
                        paths(p,x+i,y-i,nl,lst,nEated,cVec);
                        i++;
                    }
                }
            }
            if (x>linf && y<lsup && pVec!=2)    //Déplacement (bas, gauche)
            {
                cVec = 3;
                i=0;
                while ((x-i-1)>lmin && (y+i+1)<lmax && mat[x-i-1][y+i+1]==null)
                    i++;
                if (Globals.debug) System.out.println("X = " + x + ", Y = " + y + " I = " + i);
                p1 = mat[x-i-1][y+i+1];
                p2 = mat[x-i-2][y+i+2];
                tpos = new Position(x-i-1, y+i+1);
                if (Globals.debug) System.out.println("P1 == " + p1 + " && P2 == " + p2);
                if (p1!=null && p1.player!=p.player && p2==null && !nlEated.contains(tpos))
                {
                    nlEated.add(tpos);
                    nEated = new LinkedList<Position>(nlEated);
                    i+=2;
                    while ((x-i)>=min && (y+i)<=max && mat[x-i][y+i]==null)
                    {
                        if (Globals.debug) System.out.println("Recall Paths at (" + (x-i) + "," + (y+i) );
                        nl = new LinkedList<Position> (l);
                        paths(p,x-i,y+i,nl,lst,nEated,cVec);
                        i++;
                    }
                }
            }
            
            if (x<lsup && y<lsup && pVec!=1)    //Déplacement (bas, droite)
            {
                cVec = 4;
                i=0;
                while ((x+i+1)<lmax && (y+i+1)<lmax && mat[x+i+1][y+i+1]==null)
                    i++;
                if (Globals.debug) System.out.println("X = " + x + ", Y = " + y + " I = " + i);
                p1 = mat[x+i+1][y+i+1];
                p2 = mat[x+i+2][y+i+2];
                tpos = new Position(x+i+1, y+i+1);
                if (Globals.debug) System.out.println("P1 == " + p1 + " && P2 == " + p2);
                if (p1!=null && p1.player!=p.player && p2==null && !nlEated.contains(tpos))
                {
                    nlEated.add(tpos);
                    nEated = new LinkedList<Position>(nlEated);
                    i+=2;
                    while ((x+i)<=max && (y+i)<=max && mat[x+i][y+i]==null)
                    {
                        if (Globals.debug) System.out.println("Recall Paths at (" + (x+i) + "," + (y+i));
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
            if (Globals.debug) System.out.println("Simple pion");
            nx = x-2;
            ny = y-2;
            if (nx>=0 && ny>=0 && mat[nx][ny]==null)// && directions[p.player]==-1)
            {
                p2 = mat[x-1][y-1];
                if (Globals.debug) System.out.println("P2 = "+p2);
                state = 0;
                if (p2!=null && p2.player!=p.player)
                {
                    nl = new LinkedList<Position> (l);
                    paths(p,nx,ny,nl,lst,nEated,cVec);
                }
            }
            nx = x-2;
            ny = y+2;
            if (nx>=0 && ny<Globals.dimension && mat[nx][ny]==null)// && directions[p.player]==1)
            {
                p2 = mat[x-1][y+1];
                if (Globals.debug) System.out.println("P2 = "+p2);
                state = 0;
                if (p2!=null && p2.player!=p.player)
                {
                    nl = new LinkedList<Position> (l);
                    paths(p,nx,ny,nl,lst,nEated,cVec);
                }
            }
            nx = x+2;
            ny = y-2;
            if (nx<Globals.dimension && ny>=0 && mat[nx][ny]==null)// && directions[p.player]==-1)
            {
                p2 = mat[x+1][y-1];
                if (Globals.debug) System.out.println("P2 = "+p2);
                state = 0;
                if (p2!=null && p2.player!=p.player)
                {
                    nl = new LinkedList<Position> (l);
                    paths(p,nx,ny,nl,lst,nEated,cVec);
                }
            }
            nx = x+2;
            ny = y+2;
            if (nx<Globals.dimension && ny<Globals.dimension && mat[nx][ny]==null)// && directions[p.player]==1)
            {
                p2 = mat[x+1][y+1];
                if (Globals.debug) System.out.println("P2 = "+p2);
                state = 0;
                if (p2!=null && p2.player!=p.player)
                {
                    nl = new LinkedList<Position> (l);
                    paths(p,nx,ny,nl,lst,nEated,cVec);
                }
            }
            
        }
    }
    
    void newGame()  //Démarre une nouvelle partie.
    {
        lstObligate = new LinkedList<LinkedList<Position>>();
        lstEated = new LinkedList<Position>();
        lstVariations = new LinkedList<Variation>();
        currentPlayer = 0;
        players = new Player[2];
        mat = new Pion[Globals.dimension][Globals.dimension];
        for (int i=0; i<2; i++)
            players[i] = new Player();
        for (int i=0; i<Globals.dimension; i++)
        {
            for (int j=0; j<Globals.dimension; j++)
            {
                if ((i+j)%2==0 && mat[i][j]!=null)
                {
                    mat[i][j] = null;
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
                    mat[i][j] = new Pion(i,j,TColor.white);
                    this.addPion(mat[i][j]);
                }
            }
            for (int j=(Globals.dimension+2)/2; j<Globals.dimension; j++)
            {
                
                if ((i+j)%2 == 0)
                {
                    mat[i][j] = new Pion(i,j,TColor.black);
                    this.addPion(mat[i][j]);
                }
            }
        }
        pan.setCurrentPlayer(currentPlayer);
        pan.setPions(mat);
    }
    
    void undoLast()
    {
        Pion p,p2;
        System.out.println("\nMat before:");
        for (int i = 0; i<Globals.dimension; i++)
        {
            for (int j=0; j<Globals.dimension; j++)
            {
                if (mat[i][j]==null)
                    System.out.print("0 ");
                else
                    System.out.print("1 ");
            }
            System.out.println("");
        }
        if (lstVariations.size()>0)
        {
            Variation var = lstVariations.pollLast();
            p = mat[var.end.x][var.end.y];
            p.x = var.start.x;
            p.y = var.start.y;
            mat[var.start.x][var.start.y] = p;
            mat[var.end.x][var.end.y] = null;/*
            mat[var.start.x][var.start.y] = mat[var.end.x][var.end.y];
            mat[var.start.x][var.start.y].x = var.start.x;
            mat[var.start.x][var.start.y].y = var.start.y;
            mat[var.end.x][var.end.y] = null; */
            if (var.toSuper)
                p.setSuper(false);
            currentPlayer = var.currentPlayer;
            pan.setCurrentPlayer(currentPlayer);
            while (var.hasPion())
            {
                p = var.getPion();
                mat[p.x][p.y] = p;
                addPion(p);
            }
            Globals.debug = false;
            findPaths();
            Globals.debug = true;
            System.out.println("Modif! " + var);
            if (Globals.debug)
            {
                System.out.println("To EAT : " + lstObligate);
                //System.out.println("Pions player: " + players[currentPlayer].lst);
            }
            
            System.out.println("\nMat after:");
            for (int i = 0; i<Globals.dimension; i++)
            {
                for (int j=0; j<Globals.dimension; j++)
                {
                    if (mat[i][j]==null)
                        System.out.print("0 ");
                    else
                        System.out.print("1 ");
                }
                System.out.println("");
            }
        }
    }
    
    public void clearMap()
    {
        mat = new Pion[Globals.dimension][Globals.dimension];
        pan.setPions(mat);
        lstObligate.clear();
        lstVariations.clear();
        lstEated.clear();
        for (int i=0; i<Globals.nbPlayers; i++)
            players[i].reset();
    }
}