package cz.geokuk.plugins.kesoid.importek;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.Future;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import cz.geokuk.framework.ProgressorInputStream;
import cz.geokuk.plugins.kesoid.KesBag;
import cz.geokuk.plugins.kesoid.mapicon.Genom;
import cz.geokuk.plugins.kesoid.mvc.KesoidModel;
import cz.geokuk.util.exception.EExceptionSeverity;
import cz.geokuk.util.exception.FExceptionDumper;
import cz.geokuk.util.file.DirScaner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author veverka
 */
public class MultiNacitac {

  private static final Logger log = LogManager.getLogger(MultiNacitac.class.getSimpleName());

  private final DirScaner ds;

  private final List<Nacitac0> nacitace = new ArrayList<>();

  private final KesoidModel kesoidModel;

  //private static final String CACHE_SUFFIX = ".cache.serialized";

  public MultiNacitac(KesoidModel kesoidModel) {
    this.kesoidModel = kesoidModel;
    ds = new DirScaner();
    nacitace.add(new NacitacGeokuk());
    nacitace.add(new NacitacGpx());
    nacitace.add(new NacitacImageMetadata());
  }

  public void setDir(File dir, boolean prenacti) {
    ds.setDir(dir, prenacti);
  }

  public KesBag nacti(Future<?> future, Genom genom) throws IOException {
    List<File> list = ds.coMamNacist();
    if (list == null) {
      return null;
    }
    KesoidImportBuilder builder = new KesoidImportBuilder(kesoidModel.getGccomNick(), kesoidModel.getProgressModel());
    builder.init();
    for (File file : list) {
      log.debug("Nacitam: " + file);
      try {
        zpracujJedenFile(file, builder, future);
      } catch (Exception e) {
        FExceptionDumper.dump(e, EExceptionSeverity.DISPLAY, "Problem pri cteni souboru " + file);
        ds.nulujLastScaned(); // ať se načte znovu
      }
    }

    builder.done(genom);

    return builder.getKesBag();
  }

  /**
   * @param file
   * @param builder
   * @param future
   * @throws ZipException
   * @throws IOException
   * @throws FileNotFoundException
   */
  private void zpracujJedenFile(File file, KesoidImportBuilder builder, Future<?> future) throws IOException {
    // TODO : Clean this up, more distinguishing
    if (isZipFile(file)) {
      try (ZipFile zipFile = new ZipFile(file)) {
        for (Enumeration<? extends ZipEntry> en = zipFile.entries(); en.hasMoreElements(); ) {
          ZipEntry entry = en.nextElement();
          if (entry.getName().matches(".*\\.(geokuk|gpx)")) {
            for (Nacitac0 nacitac : nacitace) {
              try (InputStream istm = new BufferedInputStream(
                  new ProgressorInputStream(kesoidModel.getProgressModel(), "Loading: " + file,
                      zipFile.getInputStream(entry))
              )) {
                String jmenoZdroje = file.getName() + "!" + entry.getName();
                boolean nacitat = kesoidModel.maSeNacist(file);
                builder.setCurrentlyLoaded(file, nacitat);
                if (nacitat) {
                  nacitac.nactiKdyzUmisBezVyjimky(istm, jmenoZdroje, builder, future);
                }
              }
            }
          }
        }
      }
    } else {
      for (Nacitac0 nacitac : nacitace) {
        try (InputStream istm = new BufferedInputStream(
            new ProgressorInputStream(kesoidModel.getProgressModel(), "Loading: " + file,
                new FileInputStream(file))
        )) {
          String jmenoZdroje = file.getAbsolutePath();
          boolean nacitat = kesoidModel.maSeNacist(file);
          builder.setCurrentlyLoaded(file, nacitat);
          if (nacitat) {
            nacitac.nactiKdyzUmisBezVyjimky(istm, jmenoZdroje, builder, future);
          }
        }
      }
    }
  }

  /**
   * Checks whether the given file is a ZIP file.
   * Copied from
   * http://www.java2s.com/Code/Java/File-Input-Output/DeterminewhetherafileisaZIPFile.htm
   */
  private static boolean isZipFile(File fileToTest) {
    if (fileToTest.isDirectory()) {
      return false;
    }
    if (fileToTest.length() < 4) {
      return false;
    }
    try (DataInputStream in = new DataInputStream(new BufferedInputStream(
        new FileInputStream(fileToTest)))) {
      int test = in.readInt();
      return test == 0x504b0304;
    } catch (IOException e) {
      throw new IllegalArgumentException("The file " + fileToTest + " cannot be checked!",
          e);
    }
  }

}
