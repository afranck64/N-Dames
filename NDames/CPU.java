
/**
 * Beschreiben Sie hier die Klasse CPU.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */

import java.util.LinkedList;
import java.lang.Math;

public class CPU
{
    int id;
    Player player;
    public CPU(int idPlayer)
    {
        id = idPlayer;
        player = Globals.game.players[idPlayer];
        player.setCPU(true);
    }
        
    public void auto()
    {
        Player pl = player;
        Pion p;
        int dim = Globals.dimension;
        int idPlayer = id;
        int nbPions;
        int cLst, id, idPion;
        int nx, ny, a, b, x2, y2;
        Pion [][] mat = Globals.game.mat;
        //L=Left, R=Right, U=Up, D=Down, F=Free, P=Partner
        boolean PLU=false, PRU=false, PLD=false, PRD=false, FLU=false, FRU=false, FLD=false, FRD=false;
        boolean found = false;
        boolean lost = false;
        LinkedList<Pion> lstSimple = new LinkedList<Pion>();
        LinkedList<Pion> lstL = new LinkedList<Pion>();
        LinkedList<Pion> lstR = new LinkedList<Pion>();
        LinkedList<Pion> lstDame = new LinkedList<Pion>();
        LinkedList<Pion> lstSafeL = new LinkedList<Pion>();
        LinkedList<Pion> lstSafeR = new LinkedList<Pion>();
        LinkedList<Position> lstPos;
        Position pos1, pos2;
        if (idPlayer==1)
        {
            if (Globals.game.lstObligate.size()>0)
            {
                lstPos = Globals.game.lstObligate.getFirst();
                pos1 = lstPos.getFirst();
                pos2 = lstPos.getLast();
                Globals.game.gotoxy(pos1.x, pos1.y, pos2.x, pos2.y);
            }
            else if (pl.nbPions>=0)
            {
                for (int i=0; i<pl.nbPions; i++)
                {
                    p = pl.lst.get(i);
                    if ((p.x-1)>=0 && (p.y-1)>=0 && Globals.game.mat[p.x-1][p.y-1]==null)
                        lstL.add(p);
                    if ((p.x+1)<dim && (p.y-1)>=0 && Globals.game.mat[p.x+1][p.y-1]==null)
                       lstR.add(p);
                    if (p.isSuper)
                        lstDames.add(p);
                }
                for (int i=0; i<lstL.size(); i++)
                {
                    p = lstL.get(i);
                    nx = p.x-1;
                    ny = p.y-1;
                    PLU=false; PRU=false; PLD=false; PRD=false; FLU=false; FRU=false; FLD=false; FRD=false;
                    if ((nx-1)>=0 && (ny-1)>=0)
                    {
                        if(mat[nx-1][ny-1]==null)
                            FLU = true;
                        else if (mat[nx-1][ny-1].color == p.color)
                            PLU = true;
                    }
                    else
                    {
                        PLU = true;
                    }
                    if ((nx+1)<dim && (ny-1)>=0)
                    {
                        if (mat[nx+1][ny-1]==null)
                            FRU = true;
                        else if (mat[nx+1][ny-1].color == p.color)
                            PRU = true;
                    }
                    else
                    {
                        PRU = true;
                    }
                    if ((nx-1)>=0 && (ny+1)<dim)
                    {
                        if (mat[nx-1][ny+1]==null)
                            FLD = true;
                        else if (mat[nx-1][ny+1].color == p.color)
                            PLD = true;
                    }
                    else
                    {
                        PLD = true;
                    }
                    if (((FLU || PLU) && (!FLD && !FRU)) || ((FLU || PLU) && (FLD || PLD) && (FRU || PRU)))
                        lstSafeL.add(p);
                }
                
                for (int i=0; i<lstR.size(); i++)
                {
                    p = lstR.get(i);
                    nx = p.x+1;
                    ny = p.y-1;
                    PLU=false; PRU=false; PLD=false; PRD=false; FLU=false; FRU=false; FLD=false; FRD=false;
                    if ((nx-1)>=0 && (ny-1)>=0)
                    {
                        if(mat[nx-1][ny-1]==null)
                            FLU = true;
                        else if (mat[nx-1][ny-1].color == p.color)
                            PLU = true;
                    }
                    else
                    {
                        PLU = true;
                    }
                    if ((nx+1)<dim && (ny-1)>=0)
                    {
                        if (mat[nx+1][ny-1]==null)
                            FRU = true;
                        else if (mat[nx+1][ny-1].color == p.color)
                            PRU = true;
                    }
                    else
                    {
                        PRU = true;
                    }
                    if ((nx+1)<dim && (ny+1)<dim)
                    {
                        if (mat[nx+1][ny+1]==null)
                            FRD = true;
                        else if (mat[nx+1][ny+1].color == p.color)
                            PRD = true;
                    }
                    else
                    {
                        PRD = true;
                    }
                    if (((FRU || PRU) && (!FLU && !FRD)) || ((FLU || PLU) && (FRU || PRU) && (FRD || PRD)))
                        lstSafeR.add(p);
                }
                cLst = (int) (Math.random() * 10);
                //cLst = 10;
                System.out.println("cLst = " + cLst);
                System.out.println("lstL = " + lstL);
                System.out.println("lstSafeL = " + lstSafeL);
                System.out.println("lstR = " + lstR);
                System.out.println("lstSafeR = " + lstSafeR);
                if (cLst>5)
                {
                    if (lstSafeL.size()>0)
                    {
                        idPion = (int) (lstSafeL.size() * Math.random());
                        p = lstSafeL.get(idPion);
                        Globals.game.gotoxy(p.x, p.y, p.x-1, p.y-1);
                    }
                    else if (lstSafeR.size()>0)
                    {
                        idPion = (int) (lstSafeR.size() * Math.random());
                        p = lstSafeR.get(idPion);
                        Globals.game.gotoxy(p.x, p.y, p.x+1, p.y-1);
                    }
                    else if (lstL.size()>0)
                    {
                        idPion = (int) (lstL.size() * Math.random());
                        p = lstL.get(idPion);
                        Globals.game.gotoxy(p.x, p.y, p.x-1, p.y-1);
                    }
                    else if (lstR.size()>0)
                    {
                        idPion = (int) (lstR.size() * Math.random());
                        p = lstR.get(idPion);
                        Globals.game.gotoxy(p.x, p.y, p.x+1, p.y-1);
                    }
                    else
                        lost = true;
                }
                else
                {
                    if (lstSafeR.size()>0)
                    {
                        idPion = (int) (lstSafeR.size() * Math.random());
                        p = lstSafeR.get(idPion);
                        Globals.game.gotoxy(p.x, p.y, p.x+1, p.y-1);
                    }
                    else if (lstSafeL.size()>0)
                    {
                        idPion = (int) (lstSafeL.size() * Math.random());
                        p = lstSafeL.get(idPion);
                        Globals.game.gotoxy(p.x, p.y, p.x-1, p.y-1);
                    }
                    else if (lstR.size()>0)
                    {
                        idPion = (int) (lstR.size() * Math.random());
                        p = lstR.get(idPion);
                        Globals.game.gotoxy(p.x, p.y, p.x+1, p.y-1);
                    }
                    else if (lstL.size()>0)
                    {
                        idPion = (int) (lstL.size() * Math.random());
                        p = lstL.get(idPion);
                        Globals.game.gotoxy(p.x, p.y, p.x-1, p.y-1);
                    }
                    else
                    lost = true;
                }
                /*
                else if (lstL.size()>0)
                {
                    idPion = (int) (lstL.size() * Math.random());
                    p = lstL.get(idPion);
                    Globals.game.gotoxy(p.x, p.y, p.x-1, p.y-1);
                }
                else if (lstR.size()>0)
                {
                    idPion = (int) (lstR.size() * Math.random());
                    p = lstR.get(idPion);
                    Globals.game.gotoxy(p.x, p.y, p.x+1, p.y-1);
                }   */
                    //System.out.println("Cannot Play!");
            }
        }
        else    //idPlayer==0
        {
            
        }
    }
}