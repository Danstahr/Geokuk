package cz.geokuk.util.gui;

import java.util.ArrayList;
import java.util.List;

import cz.geokuk.util.lang.FObject;

/**
 * GUI model pro výáběr nějakých objektů.
 * @author tatinek
 *
 * @param <T>
 */
public class SelectionModel<T> {
  
  final List<Item<T>> items = new ArrayList<Item<T>>();
  
  private Item<T> selectedItem;

  private final List<SelectionListener<T>> listeners = new ArrayList<SelectionListener<T>>();
  
  
//  public void add(Map<? extends T, String> map) {
//    this.map.putAll(map);
//  }

  public void add(T item, String displayText) {
    items.add(new Item<T>(item, displayText, items.size()));
  }
  
  public void addListener(SelectionListener<T> listener) {
    listeners.add(listener);
  }
  
  private void fire(SelectionEvent<T> event) {
    for (SelectionListener<T> listener : listeners) {
      listener.selectionChanged(event);
    }
    
  }
  
  public void setSelected(T value) {
    for (Item<T> item : items) {
      if (FObject.isEqual(value, item.value)) {
        setSelectedItem(item);
        return;
      }
    }
    setSelectedFirst();
  }
  
  public void setSelectedFirst() {
    if (! items.isEmpty()) {
      setSelectedItem(items.get(0));
    }
  }

  void setSelectedItem(Item<T> item) {
    if (selectedItem != item) { // mohu přímo porovnávat, protože je to jeden z našeho
      selectedItem = item;
      fire(new SelectionEvent<T>(this, selectedItem));
    }
  }
  
  static class Item<T> {
    T value;
    String displayText;
    int poradi;

    public Item(T value, String displayText, int poradi) {
      this.value = value;
      this.displayText = displayText;
      this.poradi = poradi;
    }
    
    @Override
    public String toString() {
      return displayText;
    }
    
    
  }
}
