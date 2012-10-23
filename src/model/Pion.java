package model;

public class Pion
{
    // instance variables - replace the example below with your own
    public int x;   //L'abscisse du pion
    public int y;   //L'ordonnée du pion
    public boolean isSuper; //Booléen: vrai si le pion est une dame
    public TColor color;    //La couleur du pion
    public int player;  //Le posséseur du pion: 0-1
    public int id;  //L'identifiant du pion    

    /**
     * Constructor for objects of class Pion
     */
    public Pion()
    {
        // initialise instance variables
        x = 0;
        y = 0;
        isSuper = false;
        color = TColor.black;
        player = 0;
    }
    
    public Pion(int nX, int nY, TColor nColor)
    {
        x = nX;
        y = nY;
        isSuper = false;
        color = nColor;
        if (color == TColor.white)
            player = 0;
        else
            player = 1;
    }
    
    public Pion(int nX, int nY, boolean isDame, TColor nColor)
    {
        x = nX;
        y = nY;
        isSuper = isDame;
        color = nColor;
        if (color == TColor.white)
            player = 0;
        else
            player = 1;
    }
    
    public void setSuper(boolean isDame) //Définit si le pion est une dame ou pas
    {
        isSuper = isDame;
    }
    
    public void setColor(TColor nColor) //Définit la couleur du pion
    {
        color = nColor;
        if (nColor==TColor.white)
            player = 0;
        else
            player = 1;
    }
    public String toString()    //Retourne le texte correspondant au pion
    {
        String res = new String("Pion<");
        res = res + x + "," + y + " - " + isSuper + " - " + color + ">";
        return res;
    }
}
