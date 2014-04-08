/**
 * 
 */
package cz.geokuk.util.exception;

import java.lang.Thread.UncaughtExceptionHandler;

import javax.swing.JOptionPane;



/**
 * @author veverka
 *
 */
public class MyExceptionHandler  implements UncaughtExceptionHandler {

  @SuppressWarnings("unused") // to je jen špunt, aby se daly zobrazot okna
  private static byte[] spunt = new byte[500000];

  /* (non-Javadoc)
   * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
   */
  @Override
  public void uncaughtException(Thread vlakno, Throwable t) {
    try {
      if (t instanceof OutOfMemoryError) {
        zpracujMaloPameti((OutOfMemoryError) t);
      }
      AExcId excId = FExceptionDumper.dump(t, EExceptionSeverity.DISPLAY, "Výjimka vypadla až z vlákna " + vlakno);
      System.err.println("Exception: " + excId);
    } catch (Throwable tt) {
      // Tak když výjimku nešlo ani vypsat
      t.printStackTrace();
      tt.printStackTrace();
    }
  }

  private void zpracujMaloPameti(OutOfMemoryError oome) {
    System.err.println("Málo paměti!");
    Runtime runtime = Runtime.getRuntime();
    long freeMemory =  (runtime.freeMemory() / 1024);
    long totalMemory = (runtime.totalMemory() / 1024);
    spunt = null; // uvolníme špunt, čímž umožníme ještě zobrazit okno a ukončit program
    AExcId excId = FExceptionDumper.dump(oome, EExceptionSeverity.DISPLAY, "Málo paměti odchyceno.");
    System.err.println("Exception: " + excId);
    JOptionPane.showMessageDialog(null, excId + ": došla paměť, total=" +
        totalMemory + " KiB, free=" + freeMemory +" KiB, proces bude ukončen, zkus: \"java -Xmx256m -jar geokuk.jar\"; " + oome.toString(), "Chyba", JOptionPane.ERROR_MESSAGE);
    System.exit(1);
  }

}
