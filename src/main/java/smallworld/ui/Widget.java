package smallworld.ui;

public interface Widget {
  void addMouseDownListener(MouseListener listener);
  void addMouseUpListener(MouseListener listener);
  void addMouseMoveListener(MouseListener listener);

  interface MouseListener {
    void mouseEvent(int x, int y);
  }
}
