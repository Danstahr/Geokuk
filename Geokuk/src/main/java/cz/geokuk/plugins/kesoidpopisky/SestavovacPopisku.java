/**
 * 
 */
package cz.geokuk.plugins.kesoidpopisky;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cz.geokuk.plugins.kesoid.Kes;
import cz.geokuk.plugins.kesoid.Kesoid;
import cz.geokuk.plugins.kesoid.Wpt;
import cz.geokuk.plugins.kesoid.data.EKesoidKind;

/**
 * Třída, která sestavuje popisky.
 * Její stvoření může trvat, ale její provádění nikoli
 * @author veverka
 *
 *
 *
{wpt} - waypoint
{typ1} - označení typu (jedno písmeno)
{velikost} - označení velikosti
{velikost1} - označení velikosti (jedno písmeno)
{obtiznost} - označení obtížnosti
{obtiznost1} - označení obtížnosti (jedno písmeno)
{teren} - označení terenu
{teren1} - označení terenu (jedno písmeno)
{autor} - autor kešky
{nazev} - název kešky
{zalozeno} - datum založení
{nbsp} - mezera (neprovádí se na ní zalomení), jen popisek
{br} - odřádkování, jen popisek
 */
public class SestavovacPopisku {


  private static Map<String, Nahrazovac> sNahrazovace = new TreeMap<String, Nahrazovac>();
  private final Nahrazovac[] nahrazky;
  private static final NahrBr NAHRBR = new NahrBr();

  private final int pocetRadku;

  /**
   * 
   */
  public SestavovacPopisku(String pattern) {
    List<Nahrazovac> nahrazovace = new ArrayList<Nahrazovac>();
    vytvorNahrazovace(nahrazovace, pattern);
    int n = 1;
    for (Nahrazovac nahr : nahrazovace) {
      if (nahr == NAHRBR) {
        n++;
      }
    }
    pocetRadku = n;
    nahrazky = nahrazovace.toArray(new Nahrazovac[nahrazovace.size()]);
  }

  private void vytvorNahrazovace (List<Nahrazovac> nahrazovace, String vzorek) {
    if (vzorek.length() == 0) return;
    for (Map.Entry<String, Nahrazovac> entry : sNahrazovace.entrySet()) {
      int delka = entry.getKey().length();
      int poz = vzorek.indexOf(entry.getKey());
      if (poz >= 0) { // našli jsme některý
        vytvorNahrazovace(nahrazovace, vzorek.substring(0, poz));
        nahrazovace.add(entry.getValue());
        vytvorNahrazovace(nahrazovace, vzorek.substring(poz + delka));
        return; // jednu jsme našli a vyřešili, jiné se najdou jinde
      }
    }
    nahrazovace.add(new NahrKonstantni(vzorek));
  }

  public String[] sestavPopisek(Wpt wpt) {
    int n = 0;
    Context ctx = new Context(wpt);
    String[] popisky = new String[pocetRadku];
    StringBuilder sb = new StringBuilder();
    for (Nahrazovac nahr : nahrazky) {
      nahr.pridej(sb, ctx);
      if (nahr == NAHRBR) {
        popisky[n] = sb.toString();
        n++;
        sb.setLength(0);
      }
    }
    popisky[n] = sb.toString();
    return popisky;
  }

  private static interface Nahrazovac {
    void pridej(StringBuilder sb, Context ctx);

  }

  private static class NahrKonstantni implements Nahrazovac {

    private final String konstatna;

    /**
     * 
     */
    public NahrKonstantni(String konstatna) {
      this.konstatna = konstatna;
    }

    @Override
    public void pridej(StringBuilder sb, Context ctx) {
      sb.append(konstatna);
    }

  }

  /**
   * Oddělovač řádků. Lze ho najít
   * @author veverka
   *
   */
  private static class NahrBr implements Nahrazovac {

    @Override
    public void pridej(StringBuilder sb, Context ctx) {
    }

  }

  static {
    def("{wpt}", new Nahrazovac() {
      @Override
      public void pridej(StringBuilder sb, Context ctx) {
        sb.append(ctx.wpt.getName());
      }
    });

    def("{typ1}", new Nahrazovac() {
      @Override
      public void pridej(StringBuilder sb, Context ctx) {
        if (ctx.isKes()) {
          sb.append(ctx.kes.getOneLetterType());
        }
      }
    });

    def("{velikost}", new Nahrazovac() {
      @Override
      public void pridej(StringBuilder sb, Context ctx) {
        if (ctx.isKes()) {
          sb.append(ctx.kes.getSize());
        }
      }
    });

    def("{velikost1}", new Nahrazovac() {
      @Override
      public void pridej(StringBuilder sb, Context ctx) {
        if (ctx.isKes()) {
          sb.append(ctx.kes.getOneLetterSize());
        }
      }
    });

    def("{obtiznost}", new Nahrazovac() {
      @Override
      public void pridej(StringBuilder sb, Context ctx) {
        if (ctx.isKes()) {
          sb.append(ctx.kes.getDifficulty());
        }
      }
    });

    def("{obtiznost1}", new Nahrazovac() {
      @Override
      public void pridej(StringBuilder sb, Context ctx) {
        if (ctx.isKes()) {
          sb.append(ctx.kes.getOneLetterDifficulty());
        }
      }
    });

    def("{teren}", new Nahrazovac() {
      @Override
      public void pridej(StringBuilder sb, Context ctx) {
        if (ctx.isKes()) {
          sb.append(ctx.kes.getTerrain());
        }
      }
    });

    def("{teren1}", new Nahrazovac() {
      @Override
      public void pridej(StringBuilder sb, Context ctx) {
        if (ctx.isKes()) {
          sb.append(ctx.kes.getOneLetterTerrain());
        }
      }
    });

    def("{autor}", new Nahrazovac() {
      @Override
      public void pridej(StringBuilder sb, Context ctx) {
        sb.append(ctx.getKesoid().getAuthor());
      }
    });

    def("{nazev}",  new Nahrazovac() {
      @Override
      public void pridej(StringBuilder sb, Context ctx) {
        sb.append(ctx.wpt.getNazev());
      }
    });

    def("{zalozeno}", new Nahrazovac() {
      @Override
      public void pridej(StringBuilder sb, Context ctx) {
        sb.append(ctx.getKesoid().getHidden());
      }
    });
    def("{nbsp}", new NahrKonstantni(" "));

    def("{br}", NAHRBR);

    def("{puvodnipotvora}",  new Nahrazovac() {
      @Override
      public void pridej(StringBuilder sb, Context ctx) {
        sb.append(computeByvalyPopisek(ctx.wpt));
      }
    });


    // a nové, které nejsou na geocaching.cz

    def("{info}",  new Nahrazovac() {
      @Override
      public void pridej(StringBuilder sb, Context ctx) {
        if (ctx.isKes()) {
          sb.append(ctx.kes.getInfo());
        }
      }
    });

  }

  private static class Context {
    private final Wpt wpt;

    private boolean kesoidTypeResolved;

    public Kesoid kesoid;
    private Kes kes;

    Context(Wpt wpt) {
      this.wpt = wpt;
    }

    /**
     * @return
     */
    public Kesoid getKesoid() {
      resolveKesoidType();
      return kesoid;
    }

    /**
     * @return
     */
    public boolean isKes() {
      resolveKesoidType();
      return kes != null;
    }

    /**
     * 
     */
    private void resolveKesoidType() {
      if (kesoidTypeResolved) return;
      kesoid = wpt.getKesoid();
      if (kesoid instanceof Kes) {
        kes = (Kes) kesoid;
      }
      kesoidTypeResolved = true;
    }

  }
  /**
   * @param string
   * @param xXX2
   */
  private static void def(String key, Nahrazovac nahrazovac) {
    sNahrazovace.put(key, nahrazovac);
  }

  public static String computeByvalyPopisek(Wpt wpt) {
    Kesoid kesoid = wpt.getKesoid();
    String nazev = (kesoid.getKesoidKind() == EKesoidKind.CGP)
    ? kesoid.getCode()
        : kesoid.getNazev();
    return nazev;

  }

  public static String getNahrazovaceDisplay() {
    return sNahrazovace.keySet().toString();

  }
}