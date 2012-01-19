
/**
 * Write a description of class Player here.
 * 
 * @author Franck Awounang Nekdem
 * @version v 0.1 - (c) 2011
 */
import java.util.LinkedList;

public class Player
{
    int nbPions;    //Le nombre de pions restant au joueur
    boolean isCPU;  //Booléen, vrai si le pion est piloté par l'ordinateur
    String name;    //Le nom du joueur
    LinkedList<Pion> lst;    //La liste des pions que possède le joueur

    /**
     * Constructor for objects of class Player
     */
    public Player()
    {
        lst = new LinkedList<Pion>();
        nbPions = 0;
    }
    public Player(boolean boolCPU)
    {
        lst = new LinkedList<Pion>();
        nbPions = 0;
        isCPU = boolCPU;
    }
    
    public Player(String nName, boolean boolCPU)
    {
        lst = new LinkedList<Pion>();
        nbPions = 0;
        isCPU = boolCPU;
        name = nName;
    }
    
    public void addPion(Pion p) //Ajoute le pion <p> au joueur
    {
        p.id = nbPions;
        if (!lst.contains(p))
        {
            lst.add(p);
            nbPions ++;
        }
    }
    
    public void removePion(Pion p)  //Supprimer le pion <p> au joueur
    {
        if (lst.contains(p))
        { 
            lst.remove(p);
            nbPions --;
        }
    }
    
    public void reset()
    {
        nbPions = 0;
        lst.clear();
        lst = new LinkedList<Pion> ();
        isCPU = false;
    }
    
    public void setCPU(boolean boolCPU)
    {
        isCPU = boolCPU;
    }
    
    public void setName(String nName)
    {
        name = nName;
    }
    
    public String toString()
    {
        String str = new String("Player<" + isCPU + ">");
        return str;
    }
}
