/**
 * 
 */
package cz.geokuk.plugins.vylety.akce.bod;


import cz.geokuk.plugins.vylety.data.Bod;

/**
 * Jde na vybranou pozici
 * @author veverka
 *
 */
public class PresunoutSemVychoziBodUzavreneCesty extends BodAction0 {

  private static final long serialVersionUID = -2882817111560336824L;

  //  private Pozice pozice;
  /**
   * @param aBoard
   */
  public PresunoutSemVychoziBodUzavreneCesty(Bod bod) {
    super(bod);
    putValue(NAME, "Přesunout výchozí bod");
    putValue(SHORT_DESCRIPTION, "Přesune sem výchozí bod uzavřené cesty.");
    //putValue(MNEMONIC_KEY, KeyEvent.VK_P);
    //putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("RIGHT"));
  }

  @Override
  protected boolean mamPovolitProBod(Bod bod) {
    return bod.getCesta().isKruh() && ! bod.isKrajovy();
  }

  @Override
  protected void nastavJmenoAkce(Bod bod, boolean aZKontextovehoMenu) {
    putValue(NAME, "<html>Přesunout výchozí bod" + bod.getCesta().getNazevHtml() + " " + bod.dalkaCestaRozdelenoHtml(null));
  }

  @Override
  protected void provedProBod(Bod bod) {
    vyletModel.presunoutVyhoziBod(bod);
  }

}
