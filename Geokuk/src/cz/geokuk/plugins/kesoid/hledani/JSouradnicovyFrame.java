/*
 * Copyright (c) 1995 - 2008 Sun Microsystems, Inc.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sun Microsystems nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package cz.geokuk.plugins.kesoid.hledani;

/*
 * TextFieldDemo.java requires one additional file:
 * content.txt
 */


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import cz.geokuk.core.coord.PoziceModel;
import cz.geokuk.core.coord.VyrezChangedEvent;
import cz.geokuk.core.coord.VyrezModel;
import cz.geokuk.core.coordinates.CoordinateConversionOriginal;
import cz.geokuk.core.coordinates.Wgs;
import cz.geokuk.core.coordinates.WgsParser;
import cz.geokuk.framework.AfterEventReceiverRegistrationInit;
import cz.geokuk.framework.JMyDialog0;
import cz.geokuk.plugins.refbody.ReferencniBodSeZmenilEvent;

public class JSouradnicovyFrame extends JMyDialog0 implements AfterEventReceiverRegistrationInit, DocumentListener {

  private static final Double SPATNY_FORMAT = Double.NEGATIVE_INFINITY;

  private static final long serialVersionUID = 7087453419069194768L;

  private static final double SIRKA_MIN = 30;
  private static final double SIRKA_MAX = 70;
  private static final double DELKA_MIN =  0;
  private static final double DELKA_MAX = 40;


  private JTextField jSouEdit;
  private JLabel jSouEditLabel;
  private JButton jButtonCentruj;
  private JLabel jHotovaSirka;
  private JLabel jHotovaDelka;
  private JLabel jUtm;

  final static Color  HILIT_COLOR = Color.LIGHT_GRAY;
  final static Color  ERROR_COLOR = Color.PINK;
  final static String CANCEL_ACTION = "cancel-search";

  private Color entryBg;

  private Wgs souradniceEditovane;
  private Wgs souradniceReferencni;

  private final CoordinateConversionOriginal konvertor = new CoordinateConversionOriginal();

  private PoziceModel poziceModel;

  private VyrezModel vyrezModel;

  private boolean souradniceNastavenyRukama;


  public JSouradnicovyFrame() {
    setTitle("Zadání souřadnic");
    init();
    jSouEdit.getDocument().addDocumentListener(this);

    registerEvents();


  }

  private void registerEvents() {

    jButtonCentruj.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        poziceModel.setPozice(souradniceEditovane);
        vyrezModel.vystredovatNaPozici();
      }
    });
  }

  public void onEvent(ReferencniBodSeZmenilEvent aEvent) {
    setSouradniceReferencni(aEvent.wgs);
    vyhodnotEnableCentrovacihoTlacitka();
  }

  public void onEvent(VyrezChangedEvent aEvent) {
    vyhodnotEnableCentrovacihoTlacitka();
  }


  /** This method is called from within the constructor to
   * initialize the form.
   */

  @Override
  protected void initComponents() {
    String tooltip = "Šířku i délku zadáváte jak jedno až tři celá nebo desetinná čísla (stupně, minuty, vteřiny)," +
        " jako oddělovač použijte mezeru nebo odpovídající značky °'\". Jako oddělovač desetin můžete použít tečku nebo čárku. " +
        " Písmena N nebo E můžete uvést na začátku, na knoci nebo je vynechat. (Nelze zadávat jižní šířku, či západní délku.)";
    jSouEdit = new JTextField();
    jSouEdit.setToolTipText(tooltip);

    jSouEditLabel = new JLabel("Souřadnice: ");
    jSouEditLabel.setLabelFor(jSouEdit);

    jButtonCentruj = new JButton("Centruj");
    jButtonCentruj.setToolTipText("Centruje mapu na zadaných souřadnicích.");
    getRootPane().setDefaultButton(jButtonCentruj);

    Font hotovoFont = new Font("Monospaced", Font.BOLD, 20);
    jHotovaSirka = new JLabel();
    jHotovaSirka.setFont(hotovoFont);
    jHotovaSirka.setOpaque(true);
    jHotovaDelka = new JLabel();
    jHotovaDelka.setFont(hotovoFont);
    jHotovaDelka.setOpaque(true);
    jUtm = new JLabel();

    setTitle("Zadání souřadnic");

    JPanel panel = new JPanel();
    GroupLayout layout = new GroupLayout(panel);
    panel.setLayout(layout);
    add(panel);

    jButtonCentruj.setAlignmentY(CENTER_ALIGNMENT);
    jUtm.setText("?");
    //    panel.add(jSirka);
    //    panel.add(jDelka);
    //    panel.add(jButtonCentruj);

    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);
    layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(jSouEditLabel)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jSouEdit)
                    )
            )
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jHotovaSirka)
                    .addComponent(jHotovaDelka)
                    )
                    .addComponent(jButtonCentruj)
                )
                .addComponent(jUtm)
        );

    layout.setVerticalGroup(layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                    .addComponent(jSouEditLabel)
                    .addComponent(jSouEdit)
                    )
                )
            )
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jHotovaSirka)
                    .addComponent(jHotovaDelka)
                    )
                    .addComponent(jButtonCentruj)
                )
                .addComponent(jUtm)
        );
    jSouEdit.setPreferredSize(new Dimension(150, jSouEdit.getPreferredSize().height));
    jSouEdit.setText("");
    entryBg = jSouEdit.getBackground();
    edituj();

  }

  private void setSouradniceReferencni(Wgs wgs) {
    if (wgs.equals(souradniceReferencni)) return;
    souradniceReferencni = wgs;
    if (!souradniceNastavenyRukama) {
      jSouEdit.setText(Wgs.toGeoFormat(wgs.lat) + " ; " + Wgs.toGeoFormat(wgs.lon));
      jSouEdit.selectAll();
      souradniceNastavenyRukama = false; // toto se může zdát zbytečné, ale řádky před tím to změní
    }
    edituj();
  }

  private void edituj() {
    //if (souradnice == null) return;
    boolean ok;
    Wgs wgs = new WgsParser().parsruj(jSouEdit.getText());
    if (wgs == null) { // prizpusobeni puvodni verzi
      wgs = new Wgs(SPATNY_FORMAT, SPATNY_FORMAT);
    }
    boolean okSirka = aplikuj(jHotovaSirka, jSouEdit, wgs.lat, SIRKA_MIN, SIRKA_MAX);
    boolean okDelka = aplikuj(jHotovaDelka, jSouEdit, wgs.lon, DELKA_MIN, DELKA_MAX);
    ok = okSirka && okDelka;
    if (ok) {
      souradniceEditovane = wgs;
      jUtm.setText(konvertor.latLon2UTM(wgs.lat, wgs.lon));
    } else {
      jUtm.setText("UTM = ?");
    }
    vyhodnotEnableCentrovacihoTlacitka();
  }

  /**
   * 
   */
  private void vyhodnotEnableCentrovacihoTlacitka() {
    boolean jsmeNaMiste = jsmeVycentrovaniSeZadanouPozici();

    //jsmeNaMiste = vyrezModel.isPoziceUprostred();
    jButtonCentruj.setEnabled(!jsmeNaMiste);
  }

  /**
   * @return
   */
  private boolean jsmeVycentrovaniSeZadanouPozici() {
    boolean jsmeNaMiste =
        (souradniceEditovane != null && souradniceEditovane.equals(souradniceReferencni))
        && vyrezModel.isPoziceUprostred();
    return jsmeNaMiste;
  }


  private boolean aplikuj(JLabel jHotova, JTextField editacni,  double val, double min, double max) {
    boolean ok;
    if (val == SPATNY_FORMAT) {
      jHotova.setText("Grrrr!");
      jHotova.setBackground(Color.RED);
      editacni.setBackground(ERROR_COLOR);
      ok = false;
    } else {
      jHotova.setText(Wgs.toGeoFormat(val));
      if (val >= min && val <= max) {
        jHotova.setBackground(Color.GREEN);
        editacni.setBackground(Color.WHITE);
        ok = true;
      } else {
        if (val < 10 && val == (long)val) {
          jHotova.setBackground(Color.GRAY);
          jHotova.setText(jHotova.getText().replaceAll("\\d", "?"));
          editacni.setBackground(Color.WHITE);
          ok = false;
        } else {
          jHotova.setBackground(Color.RED);
          editacni.setBackground(ERROR_COLOR);
          ok = false;
        }
      }
    }
    return ok;
  }

  // DocumentListener methods

  @Override
  public void insertUpdate(DocumentEvent ev) {
    souradniceNastavenyRukama = true;
    edituj();
  }

  @Override
  public void removeUpdate(DocumentEvent ev) {
    souradniceNastavenyRukama = true;
    edituj();
  }

  @Override
  public void changedUpdate(DocumentEvent ev) {
    souradniceNastavenyRukama = true;
    edituj();
  }

  //  private final class SpousteniVyhledavace implements ChangeListener {
  //    @Override
  //    public void stateChanged(ChangeEvent e) {
  //      search();
  //    }
  //  }

  class CancelAction extends AbstractAction {
    private static final long serialVersionUID = -480129891208539096L;

    @Override
    public void actionPerformed(ActionEvent ev) {
      //      hilit.removeAllHighlights();
      jSouEdit.setText("");
      jSouEdit.setBackground(entryBg);
    }
  }


  //  public static void main(String args[]) {
  //    //Schedule a job for the event dispatch thread:
  //    //creating and showing this application's GUI.
  //
  //    SwingUtilities.invokeLater(new Runnable() {
  //      public void run() {
  //        //Turn off metal's use of bold fonts
  ////        UIManager.put("swing.boldMetal", Boolean.FALSE);
  //        new JSouradnicovyFrame(null).setVisible(true);
  //      }
  //    });
  //  }


  public void inject(PoziceModel poziceModel) {
    this.poziceModel = poziceModel;
  }

  public void inject(VyrezModel vyrezModel) {
    this.vyrezModel = vyrezModel;
  }

  /* (non-Javadoc)
   * @see cz.geokuk.framework.AfterEventReceiverRegistrationInit#initAfterEventReceiverRegistration()
   */
  @Override
  public void initAfterEventReceiverRegistration() {
    //super.ini
  }

  @Override
  protected String getTemaNapovedyDialogu() {
    return "JintNaSouradnice";
  }
}

