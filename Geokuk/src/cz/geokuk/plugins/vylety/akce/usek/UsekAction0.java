package cz.geokuk.plugins.vylety.akce.usek;

import java.awt.event.ActionEvent;

import cz.geokuk.core.coordinates.Mou;
import cz.geokuk.framework.AfterInjectInit;
import cz.geokuk.plugins.vylety.akce.BousekAction0;
import cz.geokuk.plugins.vylety.cesty.Usek;

public abstract class UsekAction0 extends BousekAction0 implements AfterInjectInit {

  private static final long serialVersionUID = 1L;

  private final Usek usek;
  private final Mou mou;

  public UsekAction0(Usek usek, Mou mouMysi) {
    this.usek = usek;
    mou = usek.getNejblizsiBodKPrimce(mouMysi);
  }

  protected abstract boolean mamPovolitProUsek(Usek usek, Mou mou);

  protected abstract void nastavJmenoAkce(Usek usek, Mou mou);

  protected abstract void provedProUsek(Usek usek, Mou mou);


  @Override
  protected final void vyletChanged() {
    vyhodnotitPovolenost();
  }

  private void vyhodnotitPovolenost() {
    if (mamPovolitProUsek(usek, mou)) {
      setEnabled(true);
      nastavJmenoAkce(usek, mou);
    } else {
      setEnabled(false);
    }
  }

  @Override
  public final void actionPerformed(ActionEvent aE) {
    provedProUsek(usek, mou);
  }




}
