
/**
 * Beschreiben Sie hier die Klasse Variation.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
import java.util.LinkedList;

public class Variation
{
    int currentPlayer;
    LinkedList<Pion> lstDeleted;
    Position start;
    Position end;
    boolean toSuper;
    public Variation(int player, int x,int y, int a, int b)
    {
        currentPlayer = player;
        start = new Position(x,y);
        end = new Position(a,b);
    }
    
    public void setToSuper(boolean goSuper)
    {
       toSuper = goSuper;
    }
    
    public void addPion(Pion p)
    {
        if (lstDeleted == null)
            lstDeleted = new LinkedList<Pion>();
        lstDeleted.add(p);
    }
    
    public Pion getPion()
    {
        return lstDeleted.pollLast();
    }
    
    public boolean hasPion()
    {
        if (lstDeleted == null)
            return false;
        else if (lstDeleted.size()!=0)
            return true;
        else
            return false;
    }
    
    public String toString()
    {
        String str = new String("Var : ("+ start.x + ","+start.y + ") ==> (" + end.x + "," + end.y + ")" );
        return str;
    }
}
