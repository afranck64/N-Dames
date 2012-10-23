package util;

import java.util.LinkedList;
import model.*;

public class Variation
{
    private int currentPlayer;
    private LinkedList<Pion> lstDeleted;
    private Position start;
    private Position end;
    private boolean toSuper;
    public Variation(int player, int x,int y, int a, int b)
    {
        setCurrentPlayer(player);
        setStartPos(new Position(x,y));
        setEndPos(new Position(a,b));
    }
    
    public void setToSuper(boolean goSuper)
    {
       toSuper = goSuper;
    }
    
    public void addPion(Pion p)
    {
        if (getLstDeleted() == null)
            setLstDeleted(new LinkedList<Pion>());
        getLstDeleted().add(p);
    }
    
    public Pion getPion()
    {
        return getLstDeleted().pollLast();
    }
    
    public boolean hasPion()
    {
        if (getLstDeleted() == null)
            return false;
        else if (getLstDeleted().size()!=0)
            return true;
        else
            return false;
    }
    
    public String toString()
    {
        String str = new String("Var : ("+ getStartPos().x + ","+getStartPos().y + ") ==> (" + getEndPos().x + "," + getEndPos().y + ")" );
        return str;
    }

	public Position getStartPos() {
		return start;
	}

	public void setStartPos(Position start) {
		this.start = start;
	}

	public Position getEndPos() {
		return end;
	}

	public void setEndPos(Position end) {
		this.end = end;
	}

	public int getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(int currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public boolean isToSuper() {
		return toSuper;
	}

	public LinkedList<Pion> getLstDeleted() {
		return lstDeleted;
	}

	public void setLstDeleted(LinkedList<Pion> lstDeleted) {
		this.lstDeleted = lstDeleted;
	}
}
