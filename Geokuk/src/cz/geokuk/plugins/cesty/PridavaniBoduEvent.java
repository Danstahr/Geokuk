package cz.geokuk.plugins.cesty;

import cz.geokuk.framework.Event0;

public class PridavaniBoduEvent extends Event0<VyletModel> {

  public final boolean probihaPridavani;


  public PridavaniBoduEvent(boolean probihaPridavani) {
    this.probihaPridavani = probihaPridavani;
  }


}
