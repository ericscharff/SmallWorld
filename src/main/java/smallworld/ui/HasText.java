package smallworld.ui;

public interface HasText extends Widget {
  String getSelectedText();
  String getText();
  void setText(String text);
}
