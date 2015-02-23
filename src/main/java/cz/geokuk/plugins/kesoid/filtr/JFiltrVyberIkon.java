package cz.geokuk.plugins.kesoid.filtr;


import java.util.HashSet;
import java.util.Set;

import cz.geokuk.plugins.kesoid.KesBag;
import cz.geokuk.plugins.kesoid.mapicon.Alela;
import cz.geokuk.plugins.kesoid.mapicon.Gen;
import cz.geokuk.plugins.kesoid.mapicon.IkonBag;
import cz.geokuk.plugins.kesoid.mapicon.JVyberIkon0;
import cz.geokuk.plugins.kesoid.mvc.KesoidModel;

public class JFiltrVyberIkon extends JVyberIkon0 {

  private static final long serialVersionUID = -6496737194139718970L;

  private KesBag kesBag;

  private KesoidModel kesoidModel;

  /**
   * @param aJskelneikony
   */
  public JFiltrVyberIkon() {
    super(false, true);
  }


  public void resetBag(IkonBag bag, KesBag kesBag, Set<String> aJmenaVybranychAlel) {
    this.kesBag = kesBag;
    refresh(bag, aJmenaVybranychAlel, kesBag.getPoctyAlel());
  }

  @Override
  protected void zmenaVyberu(Set<Alela> aAlely) {
    System.out.println("Vyber alel, které se Filtrují pryč: " + aAlely);
    kesoidModel.setJmenaNechtenychAlel(Alela.alelyToNames(aAlely));
  }


  /* (non-Javadoc)
   * @see geokuk.mapicon.JVyberIkon0#shouldRender(geokuk.mapicon.Alela)
   */
  @Override
  protected boolean shouldRender(Alela alela) {
    return kesBag.getPouziteAlely().contains(alela);
  }


  /* (non-Javadoc)
   * @see geokuk.mapicon.JVyberIkon0#shouldRender(geokuk.mapicon.Gen)
   */
  @Override
  protected boolean shouldRender(Gen gen) {
    Set<Alela> alely = new HashSet<>(gen.getAlely());
    alely.retainAll(kesBag.getPouziteAlely());
    return alely.size() > 1;
  }


  @Override
  protected boolean shouldEnable(Alela alela) {
    return true;
  }

  public void inject(KesoidModel kesoidModel) {
    this.kesoidModel = kesoidModel;
  }

}
