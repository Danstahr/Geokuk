/**
 * 
 */
package cz.geokuk.plugins.kesoid.mvc;

import cz.geokuk.plugins.kesoid.mapicon.ASada;

/**
 * @author veverka
 *
 */
public class JmenoAktualniSadyIkonChangeEvent extends KesoidModelEvent0 {
  private final ASada jmenoSadyIkon;

  /**
   * @param jmenoSadyIkon
   */
  public JmenoAktualniSadyIkonChangeEvent(ASada jmenoSadyIkon) {
    super();
    this.jmenoSadyIkon = jmenoSadyIkon;
  }

  /**
   * @return the jmenoSadyIkon
   */
  public ASada getJmenoSadyIkon() {
    return jmenoSadyIkon;
  }

}
