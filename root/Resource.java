
/**
 * Write a description of class Resource here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

import java.net.URL;
import java.util.*;
public class Resource
{
    static ResourceBundle res;
    static
    {
        try
        {
            res = ResourceBundle.getBundle("resources.NDames",Locale.getDefault());
        }
        catch (MissingResourceException mre)
        {
            System.out.println("Ressource non disponible!");
            System.exit(1);
        }
    }
    
    
    public static void main()
    {
        System.out.println(" ... < " + res + ">");
        System.out.println("" + res.getString("Title"));
        System.out.println("" + getString("iBlack"));
        System.out.println("" + getString("iSuperblack"));
    }
    
    public static String getString(String key)
    {
        //return (URL) res.getString(key);
        String str;
        try
        {
            str = res.getString(key);
        }
        catch (MissingResourceException mre)
        {
            str = null;
        }
        return str;
    }
    
    public String getResource(String key)
    {
        String name = getString(key);
        return name;
    }
    
    public static URL getURL (String key)
    {
        String name = getString(key);
        if (name !=null)
        {
            URL url = getURL(name);
            return url;
        }
        return null;
    }
    
    public URL getResourceURL (String key)
    {
        String name = getString(key);
        if (name !=null)
        {
            URL url = this.getClass().getResource(name);
            return url;
        }
        return null;
    }
    
}
