package smallworld.ui;

public interface Button extends Widget {

  interface ButtonListener {
    void buttonClicked();
  }
  void addButtonListener(ButtonListener listener);
}
