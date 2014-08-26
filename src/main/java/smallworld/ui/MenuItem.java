package smallworld.ui;

public interface MenuItem {
  void addItemListener(MenuItemListener listener);

  interface MenuItemListener {
    void itemClicked();
  }
}
