package cz.geokuk.plugins.kesoid.mvc;

import cz.geokuk.framework.Action0;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class OpenFileAction extends Action0 {

  private final Logger log = org.apache.logging.log4j.LogManager.getLogger(OpenFileAction.class.getSimpleName());

  private final File fileToOpen;

  public OpenFileAction(File file) {
    super("Otevřít v defaultním programu");
    fileToOpen = file;
  }
  @Override
  public void actionPerformed(ActionEvent e) {
    if (!Desktop.isDesktopSupported()) {
      log.error("Unable to retrieve Desktop environment. not supported on current platform!");
    }
    try {
      Desktop.getDesktop().open(fileToOpen);
    } catch (IOException exception) {
      log.error("Unable to open file " + fileToOpen, exception);
    }
  }
}
