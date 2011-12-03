/**
 * 
 */
package cz.geokuk.core.napoveda;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import cz.geokuk.framework.Action0;


/**
 * @author veverka
 *
 */
public class ZkontrolovatAktualizaceAction extends Action0 {

  private static final long serialVersionUID = -2882817111560336824L;
  private NapovedaModel napovedaModel;
  /**
   * @param aBoard
   */
  public ZkontrolovatAktualizaceAction() {
    super("Zkontrolovat aktualizace...");
    putValue(SHORT_DESCRIPTION, "Připojením na WEB zkontroluje, zda nejsou k dispozici nové verze programu.");
    putValue(MNEMONIC_KEY, KeyEvent.VK_A);
  }
  /* (non-Javadoc)
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  @Override
  public void actionPerformed(ActionEvent aE) {
    napovedaModel.zkontrolujNoveAktualizace(true);
  }
  
  public void onEvent(NapovedaModelChangedEvent event) { 
    setEnabled(napovedaModel.isOnlineMode());
  }

  public void inject(NapovedaModel napovedaModel) {
    this.napovedaModel = napovedaModel;
  }

  
}
