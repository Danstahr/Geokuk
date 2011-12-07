package cz.geokuk.plugins.cesty.akce;


import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import cz.geokuk.img.ImageLoader;


public class VyletPresClipboardDoGeogetuAction extends VyletAction0 {

  private static final long serialVersionUID = -7547868179813232769L;

  public VyletPresClipboardDoGeogetuAction() {
    super("Výlet do Geogetu", ImageLoader.seekResIcon("x16/geoget.png"));
    putValue(SHORT_DESCRIPTION, "Všechny keše ve výletu přes clipboard předá do otevřeného geogetu. V clipboardu nakonec zůstane URL poslední přidané keše.");
    putValue(MNEMONIC_KEY, KeyEvent.VK_G);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    cestyModel.nasypVyletDoGeogetu();
  }

  @Override
  protected void vyletChanged() {
    super.vyletChanged();
    setEnabled(cestyModel.getPocetWaypointuVeVyletu() > 0);
  }


}
