package cz.geokuk.core.render;

public class PapirovaMetrika {

  // strany A0 papíru
  final double odmocnina2 = Math.sqrt(2);
  final double A0u = Math.sqrt(odmocnina2); // delsi strana
  final double A0v = 1 / A0u;              // kratsi strana
  final double logOdmocniny2 = Math.log(odmocnina2);

  // velikosti jsou v metrech
  final double xsize;
  final double ysize;
  final double okraj;

  final int format;
  final boolean naSirku;
  final boolean naVysku;

  public double getKratsiStrana() {
    double result = A0v / Math.pow(odmocnina2, format);
    return result;
  }

  public double getDelsiStrana() {
    double result = A0u / Math.pow(odmocnina2, format);
    return result;
  }


  public PapirovaMetrika(double xsize, double ysize, double okraj) {
    this.xsize = xsize;
    this.ysize = ysize;
    this.okraj = okraj;

    // formáty, když se to dá na šířku i na výšku
    int foSirka = (int) Math.floor(Math.min(
        Math.log(A0u / (xsize + 2*okraj)) / logOdmocniny2,
        Math.log(A0v / (ysize + 2*okraj)) / logOdmocniny2
    ));
    int foVyska = (int) Math.floor(Math.min(
        Math.log(A0u / (ysize + 2*okraj)) / logOdmocniny2,
        Math.log(A0v / (xsize + 2*okraj)) / logOdmocniny2
    ));
    // vzít menší z formátů
    format = Math.max(foSirka, foVyska);
    naSirku = format == foSirka;
    naVysku = format == foVyska;
    //System.out.printf("sirka:%.0f vyska:%.0f foSirka:%d foVyska:%d\n", xsize *1000, ysize *1000, foSirka, foVyska);

  }

}
