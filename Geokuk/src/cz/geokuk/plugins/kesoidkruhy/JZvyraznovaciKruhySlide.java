package cz.geokuk.plugins.kesoidkruhy;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;

import cz.geokuk.core.coord.JSingleSlide0;
import cz.geokuk.core.coordinates.Mou;
import cz.geokuk.core.program.FConst;
import cz.geokuk.plugins.kesoid.Wpt;
import cz.geokuk.plugins.kesoid.mvc.KeskyVyfiltrovanyEvent;
import cz.geokuk.plugins.mapy.ZmenaMapNastalaEvent;
import cz.geokuk.plugins.mapy.kachle.EKaType;
import cz.geokuk.util.index2d.FlatVisitor;
import cz.geokuk.util.index2d.Indexator;
import cz.geokuk.util.index2d.Sheet;


public class JZvyraznovaciKruhySlide extends JSingleSlide0 {
  private static final long serialVersionUID = -5858146658366237217L;
  private static final int MINIMALNI_JEDNOTKOVY_KRUH = 25;

  private Indexator<Wpt> iIndexator;


  private EKaType podklad;
  private KruhySettings kruhy;


  public JZvyraznovaciKruhySlide() {
    setOpaque(false);
    setCursor(null);
    initComponents();
  }

  /**
   * 
   */
  private void initComponents() {
    provazModely();
  }


  public void onEvent(KeskyVyfiltrovanyEvent event) {
    iIndexator = event.getFiltrovane().getIndexator();
    repaint();
  }

  public void onEvent(KruhyPreferencesChangeEvent event) {
    setVisible(event.kruhy.isOnoff());
    kruhy = event.kruhy;
    repaint();
  }

  public void onEvent(ZmenaMapNastalaEvent event) {
    podklad = event.getKaSet().getPodklad();
    provazModely();
  }

  private void provazModely() {
    if (podklad != null) {
      //      barvaAVElikostKruhuModel = Settings.barvyAVelikostiKruhu.get(podklad);
    }
  }

  @Override
  public void paintComponent(Graphics aG) {
    if (iIndexator == null) return;
    final boolean prekrocenLimit = iIndexator.count(getSoord().getBoundingRect()) > FConst.MAX_POC_WPT_NA_MAPE;
    if (prekrocenLimit) return;

    final Graphics2D g = (Graphics2D) aG;
    final int r;
    final boolean jednotkove = kruhy.isJednotkovaVelikost();
    if (jednotkove) {
      r = vypoctiJednotkovyPolomer();
    } else {
      r = kruhy.getVelikost();
    }
    final int d = 2 * r;
    final Color barva = kruhy.getBarva();
    final Stroke prerus = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1,
        new float[]{5.0f,5.0f}, 0);
    g.setColor(barva);
    iIndexator.visit(getSoord().getBoundingRect(), new FlatVisitor<Wpt>() {

      @Override
      public void visit(Sheet<Wpt> aSheet) {
        Mou mou = new Mou(aSheet.getXx(), aSheet.getYy());
        Point p = getSoord().transform(mou);
        g.setStroke(prerus);
        g.setColor(barva);
        g.fillOval(p.x -r, p.y - r, d, d);
        if (jednotkove) {
          g.setColor(Color.WHITE);
          g.drawOval(p.x -r, p.y - r, d, d);

        }
      }
    });

  }

  private int vypoctiJednotkovyPolomer() {
    double pixluNaMetr = getSoord().getPixluNaMetr();
    int metru = 1;
    int pixlu;
    do {
      metru = metru * 10;
      pixlu = (int) (metru * pixluNaMetr);
    } while (pixlu < MINIMALNI_JEDNOTKOVY_KRUH);
    return pixlu;
  }


  @Override
  public JSingleSlide0 createRenderableSlide() {
    return new JZvyraznovaciKruhySlide();
  }


}
