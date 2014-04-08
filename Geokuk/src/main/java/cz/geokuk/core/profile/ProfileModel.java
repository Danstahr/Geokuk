package cz.geokuk.core.profile;

import java.io.File;
import java.util.concurrent.ExecutionException;

import cz.geokuk.core.program.FConst;
import cz.geokuk.framework.Dlg;
import cz.geokuk.framework.Model0;
import cz.geokuk.framework.MySwingWorker0;

public class ProfileModel extends Model0 {

  //TODO Naplnit profile model

  private ProfileUkladacSwingWorker profileUkladacSwingWorker;

  /**
   * @return
   */
  public boolean isUkladatDoSouboru() {
    return FPreferencesInNearFile.isUkladatDoSouboru();
  }

  /**
   * @return
   */
  public boolean isUkladaniDoSouboruMozne() {
    return FConst.JAR_DIR_EXISTUJE;
  }

  /**
   * 
   */
  public void zrusUkladaciSouborAVypniUkladani() {
    FPreferencesInNearFile.deleteAndSwitchOff();
    fire(new NastaveniUkladatDoSouboruEvent());
  }

  @Override
  protected void initAndFire() {
    // Není potřeba nic dělat, neboť unačítání ze souborů se musí provést už při startu apliakce, takže pouze event
    fire(new NastaveniUkladatDoSouboruEvent());
  }


  public void ulozJenKdyzJeulozPreferenceDoSouboruJenKdyzSeUklaatMaji() {
    FPreferencesInNearFile.saveNearToProgramIfShould();

  }

  public void spustUlozeniDoSouboru() {
    if (profileUkladacSwingWorker == null) {
      profileUkladacSwingWorker = new ProfileUkladacSwingWorker();
      profileUkladacSwingWorker.execute();
    }

  }

  private class ProfileUkladacSwingWorker extends MySwingWorker0<File, Void> {


    /* (non-Javadoc)
     * @see javax.swing.SwingWorker#doInBackground()
     */
    @Override
    public File doInBackground() throws Exception {
      File file = FPreferencesInNearFile.saveNearToProgramAndSwitchOn();
      return file;
    }

    /* (non-Javadoc)
     * @see javax.swing.SwingWorker#done()
     */
    @Override
    protected void donex() throws InterruptedException, ExecutionException {
      profileUkladacSwingWorker = null;
      if (isCancelled()) return;
      File file = get();
      fire(new NastaveniUkladatDoSouboruEvent());
      Dlg.info("Nastavení byla uložena do souboru \"" + file + "\",\n"
          + "při příštím spuštění budou těmito nastaveními nahrazeny nastavení v Java preferences,\n" +
          "pokud budou nastavení v souboru novější a naopak bude soubor automaticky aktualizován,\n" +
          "pokud budou novější data v Java preferences", "Oznámení");
    }

  }
}
