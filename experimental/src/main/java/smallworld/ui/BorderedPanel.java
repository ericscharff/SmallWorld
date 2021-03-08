package smallworld.ui;

public interface BorderedPanel extends Panel {
  void addToCenter(Widget widget);

  void addToNorth(Widget widget);

  void addToSouth(Widget widget);

  void addToEast(Widget widget);

  void addToWest(Widget widget);
}
