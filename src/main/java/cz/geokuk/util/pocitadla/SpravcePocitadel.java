/**
 * 
 */
package cz.geokuk.util.pocitadla;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;


/**
 * @author veverka
 *
 */
public class SpravcePocitadel {

  private static final Map<Pocitadlo, Integer> pocitadla = Collections.synchronizedMap(new WeakHashMap<Pocitadlo, Integer>());
  
  public static Collection<Pocitadlo> getPocitadla() {
    return pocitadla.keySet();
    //return new ArrayList<Pocitadlo>();
  }
  
  public static void register(Pocitadlo pocitadlo) {
    pocitadla.put(pocitadlo, 0);
  }
}
