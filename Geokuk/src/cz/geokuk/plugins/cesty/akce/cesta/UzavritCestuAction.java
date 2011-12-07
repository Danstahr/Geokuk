/**
 * 
 */
package cz.geokuk.plugins.cesty.akce.cesta;


import cz.geokuk.plugins.cesty.data.Cesta;

/**
 * Jde na vybranou pozici
 * @author veverka
 *
 */
public class UzavritCestuAction extends CestaAction0 {

  private static final long serialVersionUID = -2882817111560336824L;

  //  private Pozice pozice;
  /**
   * @param aBoard
   */
  public UzavritCestuAction(Cesta cesta) {
    super(cesta);
    putValue(NAME, "Uzavřít cestu");
    putValue(SHORT_DESCRIPTION, "Rozdělí cestu na dvě cesty kratší.");
    //putValue(MNEMONIC_KEY, KeyEvent.VK_P);
    //putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("RIGHT"));
  }

  @Override
  protected boolean mamPovolitProCestu(Cesta cesta) {
    return ! cesta.isKruh();
  }

  @Override
  protected void nastavJmenoAkce(Cesta cesta, boolean aZKontextovehoMenu) {
    putValue(NAME, "<html>Uzavřít cestu" + cesta.getNazevHtml() + " "  + cesta.dalkaHtml() + " +" + cesta.dalkaStartuACileHtml());
  }

  @Override
  protected void provedProCestu(Cesta cesta) {
    vyletModel.uzavriCestu(cesta);
  }

}
