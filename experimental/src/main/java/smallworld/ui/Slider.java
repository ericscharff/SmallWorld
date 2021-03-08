package smallworld.ui;

public interface Slider extends Widget {
  void addValueAdjustedListener(ValueAdjustedListener listener);

  int getValue();

  interface ValueAdjustedListener {
    void valueAdjusted(int newValue);
  }
}
