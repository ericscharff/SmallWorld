package smallworld.ui;

public interface Button extends Widget {
  void addButtonListener(ButtonListener listener);

  interface ButtonListener {
    void buttonClicked();
  }
}
