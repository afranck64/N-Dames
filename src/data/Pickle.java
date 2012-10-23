package data;

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
import java.util.ResourceBundle;
import java.io.*;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.*;
import util.*;

public class Pickle
{
    // instance variables - replace the example below with your own
    static ResourceBundle res;
    static
    {
    	res = ResourceBundle.getBundle(Globals.resourceName);
    }
    public Pickle()
    {
    }
    
    static int getInt(BufferedReader br) throws IOException
    {
        int nbChars = 0;
        int []vals = new int[10];
        int i=9,j=0;
        int res;
        int c;
        int exp;
        try
        {
            c = br.read();
            Globals.debug("First Value : <" + c + ">");
            while (c==10 || c==13 || c==32)
            {
                Globals.debug("Get BadValue: <" + (char)c + ">");
                c = br.read();
            }
            while (c<48 && c>57)
            {
                Globals.debug("Get BadValue: <" + (char)c + ">");
                c = br.read();
            }
            while (c>=48 && c<=57)
            {
                vals[i] = c-48;
                Globals.debug("Get GoodValue: <" + (char)c + ">");
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
        Globals.debug("NB-Chars = " + nbChars);
        if (nbChars == 0)
        {
            IOException Expt = new IOException();
            throw Expt;
        }
        for (i=0; i<nbChars; i++)
        {
            exp = 1;
            Globals.debug("Val[" + (9-i) + "] = " + vals[9-i]);
            for (j=0; j<(nbChars-i-1); j++)
                exp *= 10;
            res += exp * vals[9-i];
        }
        if (res<0)
        {
            IOException Expt = new IOException();
            throw Expt;
        }
       Globals.debug("\nResult getInt: " + res);
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
        	JFileChooser chooser = new JFileChooser();
        	FileNameExtensionFilter filter = new FileNameExtensionFilter(
        	        "Game extension", res.getString("extensionGame"));
        	chooser.setFileFilter(filter);
        	int choice = chooser.showSaveDialog(Globals.game);
        	if (choice!=JFileChooser.APPROVE_OPTION) {
        		return false;
        	}
        	String filename = chooser.getSelectedFile().getAbsolutePath();
        	if (!filename.endsWith(res.getString("extensionGame")))
        		filename += "." + res.getString("extensionGame");
            FileWriter fw = new FileWriter(filename);
            PrintWriter output = new PrintWriter(fw);
            output.print(Globals.dimension + " ");
            output.print(Globals.game.getCurrentPlayer() + " ");
            output.println(Globals.game.getTime());   //Globals.game.time
            for (int i=0; i<2; i++)
            {   
                pl = Globals.game.getPlayers()[i];
                if (pl.isCPU())
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
            output.println(Globals.game.getLstVariations().size());
            for (int i=0; i<Globals.game.getLstVariations().size(); i++)
            {
                var = Globals.game.getLstVariations().get(i);
                if (var.getLstDeleted()==null)
                    nbPions = 0;
                else
                    nbPions = var.getLstDeleted().size();
                if (var.isToSuper())
                    toDame = 1;
                else
                    toDame = 0;
                output.println(var.getCurrentPlayer() + " " + var.getStartPos().x + " " + var.getStartPos().y + " " + var.getEndPos().x + " " + var.getEndPos().y + " " + toDame + " " + nbPions);
                for (int j=0; j<nbPions; j++)
                {
                    p = var.getLstDeleted().get(j);
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
        Globals.game.setMatrice(new Pion [Globals.dimension][Globals.dimension]);
        Globals.game.setLstObligate(new LinkedList<LinkedList<Position>>());
        Globals.game.setPlayers(new Player[2]);
        try
        {
        	JFileChooser chooser = new JFileChooser();
        	FileNameExtensionFilter filter = new FileNameExtensionFilter(
        	        "Game extension", res.getString("extensionGame"));
        	chooser.setFileFilter(filter);
        	int choice = chooser.showSaveDialog(Globals.game);
        	if (choice!=JFileChooser.APPROVE_OPTION) {
        		return false;
        	}
        	String filename = chooser.getSelectedFile().getAbsolutePath();
            FileReader fr = new FileReader(filename);
            BufferedReader input = new BufferedReader(fr);
            Globals.dimension = getInt(input);
            Globals.game.setCurrentPlayer(getInt(input));
            Globals.game.setTime(getInt(input));
            Globals.debug("cPlayer = " + Globals.game.getCurrentPlayer());
            for (int i = 0; i<2; i++)
            {
                isAuto = getInt(input);
                if (isAuto==0)
                    isCPU = false;
                else
                    isCPU = true;
                pl = new Player(isCPU);
                Globals.game.getPlayers()[i] = pl;
                nbPions = getInt(input);
                Globals.debug("nbPions Player: " + i + " = " + nbPions);
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
                    Globals.debug(" X, Y = " + x + "," + y);
                    Globals.game.getMatrice()[x][y] = p;
                    pl.addPion(p);
                }
                Globals.debug("Pions - " + i + " : " + pl.lst);
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
                    if (var.getCurrentPlayer()==0)
                        color = TColor.black;
                    else
                        color = TColor.white;
                    p = new Pion(x, y, isSuper, color);
                    var.addPion(p);
                }
                var.setToSuper(toSuper);
                Globals.game.getLstVariations().add(var);
            }
        }
        catch (Exception err)
        {
            System.out.println("Exception " + err);
            Globals.dimension = 10;
            return false;
        }
        return true;
    }
}
