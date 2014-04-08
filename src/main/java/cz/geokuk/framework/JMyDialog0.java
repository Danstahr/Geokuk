package cz.geokuk.framework;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;



public abstract class JMyDialog0 extends JDialog {

  private static final long serialVersionUID = 1L;
  private static final Object CANCEL_ACTION = "kanclujToVsecko";
  protected Factory factory;
  private EventManager eventManager;
  private final JFrame frame;
  private final NapovedaAction napovedaAction = new NapovedaAction("Dialog/" + getTemaNapovedyDialogu());

  public JMyDialog0() {
    super(Dlg.parentFrame());
    frame = Dlg.parentFrame();
    assert frame != null;
  }

  protected final void init() {
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    JButton jZavri = new JButton("Zavřít");
    JButton jNapoveda = new JButton("Nápověda");
    CancelAction cancelAction = new CancelAction();
    jZavri.setAction(cancelAction);
    jNapoveda.setAction(napovedaAction);
    JPanel jDolniCudly = new JPanel();
    jDolniCudly.add(jZavri);
    if (getTemaNapovedyDialogu() != null) {
      jDolniCudly.add(jNapoveda);
    }
    add(jDolniCudly, BorderLayout.SOUTH);
    initComponents();
    pack();

    final Toolkit toolkit = getToolkit();
    Dimension screenSize = toolkit.getScreenSize();
    Dimension frmSize = frame.getSize();
    Dimension mySize = getSize();
    int xo = (frmSize.width - mySize.width) / 2;
    int yo = (frmSize.height - mySize.height) / 2;
    Point loc = frame.getLocation();
    Point p = new Point(loc.x + xo, loc.y + yo);
    p.x = Math.max(Math.min(p.x, screenSize.width - mySize.width), 0);
    p.y = Math.max(Math.min(p.y, screenSize.height - mySize.height), 0);
    Dimension novy = new Dimension(
        Math.min(mySize.width, screenSize.width - p.x),
        Math.min(mySize.height, screenSize.height - p.y)

    );
    setLocation(p);
    setSize(novy);


    InputMap im = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    ActionMap am = getRootPane().getActionMap();
    im.put(KeyStroke.getKeyStroke("ESCAPE"), CANCEL_ACTION);
    am.put(CANCEL_ACTION, cancelAction);

    getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F1"), "napoveda");
    getRootPane().getActionMap().put("napoveda", napovedaAction);

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosed(WindowEvent aE) {
        if (eventManager != null) { // jen když byl tento dialog registrován a tak má event managera
          eventManager.unregister(this);
          odregistrujUdalosti();
        }
      }
    });

  }


  public void odregistrujUdalosti() {
    odregistrujUdalosti(this);

  }

  private void odregistrujUdalosti(Component comp) {
    eventManager.unregister(this);
    if (comp instanceof Container) {
      Container container = (Container) comp;
      for (Component c : container.getComponents()) {
        odregistrujUdalosti(c);
      }
    }
  }

  protected abstract void initComponents();

  private class CancelAction extends AbstractAction {
    private static final long serialVersionUID = -4843379055570361691L;

    public CancelAction() {
      super("Zavřít");
    }
    @Override
    public void actionPerformed(ActionEvent e) {
      dispose();
    }
  }

  public void inject(Factory factory) {
    this.factory = factory;
    factory.init(napovedaAction);
  }

  public void inject(EventManager eventManager) {
    this.eventManager = eventManager;
  }

  protected abstract String getTemaNapovedyDialogu();
}
