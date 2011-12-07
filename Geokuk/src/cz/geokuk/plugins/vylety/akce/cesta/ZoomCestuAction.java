package cz.geokuk.plugins.vylety.akce.cesta;


import cz.geokuk.core.coordinates.MouRect;
import cz.geokuk.plugins.vylety.data.Bod;
import cz.geokuk.plugins.vylety.data.Cesta;

public class ZoomCestuAction extends CestaAction0 {

  private static final long serialVersionUID = 1L;

  public ZoomCestuAction(Cesta cesta) {
    super(cesta);
    //putValue(NAME,  "<html>Odstraň cestu <i>" + jCestaMenu.getNazev() + "</i> " + (jCestaMenu.getMouDelkaCesta() + " mou"));
    putValue(NAME,  "Zoom cestu");
    putValue(SHORT_DESCRIPTION, "Vybranou cestu nazoomuje do mapy tak, aby byla celá vidět.");
    //putValue(MNEMONIC_KEY, KeyEvent.VK_V);
    //putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("F3"));
    //putValue(SMALL_ICON, ImageLoader.seekResIcon("x16/vylet/vyletAno.png"));
  }


  @Override
  protected boolean mamPovolitProCestu(Cesta cesta) {
    return ! cesta.isEmpty();
  }

  @Override
  protected void nastavJmenoAkce(Cesta cesta, boolean aZKontextovehoMenu) {
    putValue(NAME, "<html>Zoom cestu" + cesta.getNazevADalkaHtml());
  }

  @Override
  protected void provedProCestu(Cesta cesta) {
    MouRect mourect = new MouRect();
    for (Bod bod : cesta.getBody()) {
      mourect.add(bod.getMou());
    }
    mourect.resize(1.2);
    vyrezModel.zoomTo(mourect);
  }



}
