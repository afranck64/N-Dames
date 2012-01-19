
/**
 * Write a description of class Paneau here.
 * 
 * @author Franck Awounang Nekdem
 * @version v 0.1 - (c) 2011
 */

public class Paneau
{ 
/**public static void main (String args[])
  { 
      Surface fen = new Surface() ;
      fen.setVisible(true) ;
  }
 **/
  public static void main (String args[])
 {
     MaFenetre fen = new MaFenetre() ;
     fen.setDefaultCloseOperation(fen.DISPOSE_ON_CLOSE);
     fen.setVisible(true) ;
     //fen.newGame();
     //Pickle.save(fen);
     //fen.pan.updateBox(fen.getGraphics(),0,0,TColor.white,true);
 }
 
 public static void test()
 {
     MaFenetre fen = new MaFenetre();
     Globals.debug = false;
     fen.setDefaultCloseOperation(fen.DISPOSE_ON_CLOSE);
     fen.setVisible(true) ;
 }
 
 public static void debug()
 {
     MaFenetre fen = new MaFenetre();
     Globals.debug = true;
     fen.setDefaultCloseOperation(fen.DISPOSE_ON_CLOSE);
     fen.setVisible(true) ;
 }
}