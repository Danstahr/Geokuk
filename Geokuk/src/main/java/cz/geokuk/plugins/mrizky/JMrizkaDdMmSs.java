package cz.geokuk.plugins.mrizky;


import java.awt.Color;

import cz.geokuk.core.coord.JSingleSlide0;
import cz.geokuk.core.coordinates.Wgs;


public class JMrizkaDdMmSs extends JMrizkaWgs {

  /**
   * 
   */
  private static final long serialVersionUID = -4045265652174560904L;

  @Override
  public String getTextY(double y) {
    return Wgs.toDdMmSsFormat(y);
  }

  @Override
  public String getTextX(double x) {
    return Wgs.toDdMmSsFormat(x);
  }

  /* (non-Javadoc)
   * @see mrizka.JMrizka0#initPainting(mrizka.JMrizka0.Vykreslovac)
   */
  @Override
  public void initPainting(Vykreslovac v) {
    v.setColor(Color.CYAN);
    v.rastr(VTERINA * 1 , 1);
    v.rastr(VTERINA * 2 , 5);
    v.rastr(VTERINA * 5 , 2);
    v.rastr(VTERINA * 10, 1);
    v.rastr(VTERINA * 20, 3);
    v.rastr(VTERINA * 30, 2);
    v.rastr(MINUTA * 1 , 1);
    v.rastr(MINUTA * 2 , 5);
    v.rastr(MINUTA * 5 , 2);
    v.rastr(MINUTA * 10, 1);
    v.rastr(MINUTA * 20, 3);
    v.rastr(MINUTA * 30, 2);
    v.rastr(1, 1);
    v.rastr(5, 1);
    v.rastr(10, 1);
    v.rastr(15, 1);
    v.rastr(30, 1);
    v.rastr(45, 1);
    v.rastr(60, 1);
    v.rastr(90, 1);
    v.rastr(180, 1);
    v.rastr(360, 1);
  }

  @Override
  public JSingleSlide0 createRenderableSlide() {
    return new JMrizkaDdMmSs();
  }

}
