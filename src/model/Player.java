package model;

import java.util.LinkedList;

public class Player
{
    private int nbPions;    //Le nombre de pions restant au joueur
    private boolean isCPU;  //Booléen, vrai si le pion est piloté par l'ordinateur
    String name;    //Le nom du joueur
    public LinkedList<Pion> lst;    //La liste des pions que possède le joueur

    /**
     * Constructor for objects of class Player
     */
    public Player()
    {
        lst = new LinkedList<Pion>();
        setNbPions(0);
    }
    public Player(boolean boolCPU)
    {
        lst = new LinkedList<Pion>();
        setNbPions(0);
        setCPU(boolCPU);
    }
    
    public Player(String nName, boolean boolCPU)
    {
        lst = new LinkedList<Pion>();
        setNbPions(0);
        setCPU(boolCPU);
        name = nName;
    }
    
    public void addPion(Pion p) //Ajoute le pion <p> au joueur
    {
        p.id = getNbPions();
        if (!lst.contains(p))
        {
            lst.add(p);
            setNbPions(getNbPions() + 1);
        }
    }
    
    public void removePion(Pion p)  //Supprimer le pion <p> au joueur
    {
        if (lst.contains(p))
        { 
            lst.remove(p);
            setNbPions(getNbPions() - 1);
        }
    }
    
    public void reset()
    {
        setNbPions(0);
        lst.clear();
        lst = new LinkedList<Pion> ();
        setCPU(false);
    }
    
    public void setCPU(boolean boolCPU)
    {
        isCPU = boolCPU;
    }
    
    public void setName(String nName)
    {
        name = nName;
    }
    
    public void setPions(LinkedList<Pion> nLst) {
    	this.lst = nLst;
    }
    
    public LinkedList<Pion> getPions() {
    	return lst;
    }
    
    public String toString()
    {
        String str = new String("Player<" + isCPU() + ">");
        return str;
    }
	public int getNbPions() {
		return nbPions;
	}
	public void setNbPions(int nbPions) {
		this.nbPions = nbPions;
	}
	public boolean isCPU() {
		return isCPU;
	}
}

