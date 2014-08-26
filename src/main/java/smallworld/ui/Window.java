package smallworld.ui;

public interface Window {
  void setTitle(String title);
  void setVisible(boolean visible);
  void setSize(int width, int height);
  void addChild(Widget child);
  void addMenu(Menu menu);
  void redraw();
  void addCloseListener(CloseListener listener);

  interface CloseListener {
    void windowClosed();
  }
}
