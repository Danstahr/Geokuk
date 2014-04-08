package cz.geokuk.util.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cz.geokuk.util.gui.SelectionModel.Item;

public class JMvRadioPanel<T> extends JPanel {

  private static final long serialVersionUID = -4181105935856103641L;

  private SelectionModel<T> model = new SelectionModel<T>();
  private final List<JRadioButton> buttons = new ArrayList<JRadioButton>();

  public JMvRadioPanel(String title) {
    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    //setBorder(BorderFactory.createRaisedBevelBorder());
    setBorder(BorderFactory.createTitledBorder(title));
  }

  public void setSelectionModel(final SelectionModel<T> model) {
    this.model = model;
    List<Item<T>> items = model.items;
    ButtonGroup bg = new ButtonGroup();
    for (final Item<T> item : items) {
      final JRadioButton rb = new JRadioButton(item.displayText);
      buttons.add(rb);
      bg.add(rb);
      add(rb);
      rb.addChangeListener(new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
          if (rb.isSelected()) {
            model.setSelectedItem(item);
          }
        }
      });
    }
    model.addListener(new SelectionListener<T>() {
      @Override
      public void selectionChanged(SelectionEvent<T> event) {
        buttons.get(event.item.poradi).setSelected(true);
      }
    });

  }


  public SelectionModel<T> getSelectionModel() {
    return model;
  }

}
