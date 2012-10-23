package util;

public class Position implements Comparable<Position>
{
    // instance variables - replace the example below with your own
    public int x;   //L'abscisse de la position
    public int y;   //L'ordonnée de la position

    /**
     * Constructor for objects of class Position
     */
    public Position()   //Constructeur sans argument
    {
        // initialise instance variables
        x = 0;
        y = 0;
    }
    public Position(int nx, int ny) //Constructeur à 2 arguments
    {
        x = nx;
        y = ny;
    }
    public int getX()   //Retourne l'abscisse de la position
    {
        return x;
    }
    public int getY()   //Retourne l'ordonnée de la position
    {
        return y;
    }
    public void setX(int newX)  //Définit l'abscisse de la position
    {
        x = newX;
    }
    public void setY(int newY)  //Définit la coordonnée de la position
    {
        y = newY;
    }
    public String toString()    //Affiche la <position> sous forme de String
    {
        String res = new String("Pos<");
        res = res + x + "," + y + ">";
        return res;
    }
    
    public int compareTo(Position pp) //Compare deux positions données
    {
        Position p = (Position) pp;
        if (x==p.x && y==p.y)
            return 0;
        else if (y<p.y)
            return -1;
        else
            return 1;
    }
    
    public boolean equals (Object pp)   //Retourne vrai si deux positions sont égaux, et faux sinon
    {
        Position p = (Position) pp;
        if (this.x==p.x && this.y==p.y)
            return true;
        return false;
    }
}
