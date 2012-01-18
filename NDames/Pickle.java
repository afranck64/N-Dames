
/**
 * Write a description of class Pickle here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
/*File description:
 * dimension currentPlayer time
 * player.isCPU player.nbPions
 * (pion.x pion.y pion.isSuper)*
 * player.isCPU player.nbPions
 * (pion.x pion.y pion.isSuper)*
 * nbVariations
 * (currentPlayer toSuper start.x start.y end.x end.y toSuper nbPions 
 *      (pion.x pion.y pion.isSuper)*)*
 */

import java.util.LinkedList;
import java.io.*;

public class Pickle
{
    // instance variables - replace the example below with your own
    static Resource res;
    static
    {
        res = new Resource();
    }
    public Pickle()
    {
    }
    
    static int getInt(BufferedReader br) throws IOException
    {
        if (Globals.debug) System.out.println("!!!!!!!Inside getInt!!!!!!!!");
        int nbChars = 0;
        int []vals = new int[10];
        int i=9,j=0;
        int res;
        int c;
        int exp;
        try
        {
            c = br.read();
            if (Globals.debug) System.out.println("First Value : <" + c + ">");
            while (c==10 || c==13 || c==32)
            {
                if (Globals.debug) System.out.println("Get BadValue: <" + (char)c + ">");
                c = br.read();
            }
            while (c<48 && c>57)
            {
                if (Globals.debug) System.out.println("Get BadValue: <" + (char)c + ">");
                c = br.read();
            }
            while (c>=48 && c<=57)
            {
                vals[i] = c-48;
                if (Globals.debug) System.out.println("Get GoodValue: <" + (char)c + ">");
                c = br.read();
                i--;
                nbChars++;
            }
        }
        catch(IOException err)
        {
            throw err;
        }
        res = 0;
        if (Globals.debug) System.out.println("NB-Chars = " + nbChars);
        if (nbChars == 0)
        {
            IOException Expt = new IOException();
            throw Expt;
        }
        for (i=0; i<nbChars; i++)
        {
            exp = 1;
            if (Globals.debug) System.out.print("Val[" + (9-i) + "] = " + vals[9-i]);
            for (j=0; j<(nbChars-i-1); j++)
                exp *= 10;
            res += exp * vals[9-i];
        }
        if (res<0)
        {
            IOException Expt = new IOException();
            throw Expt;
        }
        if (Globals.debug) System.out.println("\nResult getInt: " + res);
        return res;
    }
    
    static void savePion(PrintWriter output, Pion p)
    {
        
    }
    
    public static boolean save()
    {
        Player pl;
        Pion p;
        int isDame;
        int isCPU;
        int nbPions;
        int toDame;
        Variation var;
        try
        {
            FileWriter fw = new FileWriter("save."+res.getString("extensionGame"));
            PrintWriter output = new PrintWriter(fw);
            output.print(Globals.dimension + " ");
            output.print(Globals.game.currentPlayer + " ");
            output.println(Globals.game.time);   //Globals.game.time
            for (int i=0; i<2; i++)
            {   
                pl = Globals.game.players[i];
                if (pl.isCPU)
                    isCPU = 1;
                else
                    isCPU = 0;
                output.println(isCPU);
                output.println(pl.lst.size());
                for (int j=0; j<pl.lst.size(); j++)
                {
                    p = pl.lst.get(j);
                    if (p.isSuper)
                        isDame = 1;
                    else
                        isDame = 0;
                    output.println(p.x + " " + p.y + " " + isDame);
                }
            }
            output.println(Globals.game.lstVariations.size());
            for (int i=0; i<Globals.game.lstVariations.size(); i++)
            {
                var = Globals.game.lstVariations.get(i);
                if (var.lstDeleted==null)
                    nbPions = 0;
                else
                    nbPions = var.lstDeleted.size();
                if (var.toSuper)
                    toDame = 1;
                else
                    toDame = 0;
                output.println(var.currentPlayer + " " + var.start.x + " " + var.start.y + " " + var.end.x + " " + var.end.y + " " + toDame + " " + nbPions);
                for (int j=0; j<nbPions; j++)
                {
                    p = var.lstDeleted.get(j);
                    if (p.isSuper)
                        isDame = 1;
                    else
                        isDame = 0;
                    output.println(p.x + " " + p.y + " " + isDame);
                }
            }
            output.close();
            return true;
        }
        catch(IOException IOEx)
        {
            return false;
        }
    }
    
    public static boolean load()
    {
        Player pl;
        Pion p;
        int nbPions, x, y, a, b;
        int isDame;
        int isAuto;
        int toDame;
        int nbVariations;
        int player;
        boolean isCPU;
        boolean isSuper;
        boolean toSuper;
        TColor color;
        String tab = new String("");
        Globals.game.mat = new Pion [Globals.dimension][Globals.dimension];
        Globals.game.lstObligate = new LinkedList<LinkedList<Position>>();
        Globals.game.players = new Player[2];
        try
        {
            FileReader fr = new FileReader("save."+res.getString("extensionGame"));
            BufferedReader input = new BufferedReader(fr);
            Globals.dimension = getInt(input);
            Globals.game.currentPlayer = getInt(input);
            Globals.game.time = getInt(input);
            if (Globals.debug) System.out.println("cPlayer = " + Globals.game.currentPlayer);
            for (int i = 0; i<2; i++)
            {
                isAuto = getInt(input);
                if (isAuto==0)
                    isCPU = false;
                else
                    isCPU = true;
                pl = new Player(isCPU);
                Globals.game.players[i] = pl;
                nbPions = getInt(input);
                if (Globals.debug) System.out.println("nbPions Player: " + i + " = " + nbPions);
                if (i==0)
                    color = TColor.white;
                else
                    color = TColor.black;
                for (int j=0; j<nbPions; j++)
                {
                    x = getInt(input);
                    y = getInt(input);
                    isDame = getInt(input);
                    if (isDame == 0)
                        isSuper = false;
                    else
                        isSuper = true;
                    p = new Pion(x, y, isSuper, color);
                    if (Globals.debug) System.out.println(" X, Y = " + x + "," + y);
                    Globals.game.mat[x][y] = p;
                    pl.addPion(p);
                }
                if (Globals.debug) System.out.println("Pions - " + i + " : " + pl.lst);
            }
            nbVariations = getInt(input);
            for (int i=0; i<nbVariations; i++)
            {
                player = getInt(input);
                x = getInt(input);
                y = getInt(input);
                a = getInt(input);
                b = getInt(input);
                Variation var = new Variation(player, x, y, a, b);
                toDame = getInt(input);
                if (toDame==0)
                    toSuper = false;
                else
                    toSuper = true;
                nbPions = getInt(input);
                for (int j=0; j<nbPions; j++)
                {
                    x = getInt(input);
                    y = getInt(input);
                    isDame = getInt(input);
                    if (isDame==0)
                        isSuper = false;
                    else
                        isSuper = true;
                    if (var.currentPlayer==0)
                        color = TColor.black;
                    else
                        color = TColor.white;
                    p = new Pion(x, y, isSuper, color);
                    var.addPion(p);
                }
                var.setToSuper(toSuper);
                Globals.game.lstVariations.add(var);
            }
        }
        catch (Exception Expt)
        {
            System.out.println("Exception " + Expt);
            Globals.dimension = 10;
            return false;
        }
        return true;
    }
}
