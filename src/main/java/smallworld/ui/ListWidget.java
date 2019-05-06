package smallworld.ui;

import smallworld.core.SmallObject;

public interface ListWidget extends Widget {
  /**
   * Get the index of the currently selected item. Note that this is one-based, to be consistent
   * with the underlying smalltalk code.
   *
   * @return the selected item, where 1 is the first item, 2 is the second, etc.
   */
  int getSelectedIndex();

  void setData(SmallObject[] data);

  void addSelectionListener(Listener listener);

  interface Listener {
    /**
     * Called when an item was selected from the underlying list. This index is one-based, not zero
     * based! (Because this is consistent with the smalltalk code that calls this). Thus, the item
     * that was selected, in java, is data[selectedIndex - 1].
     *
     * @param selectedIndex the selected item, where 1 is the first item, 2 is the second, etc.
     */
    void itemSelected(int selectedIndex);
  }
}
