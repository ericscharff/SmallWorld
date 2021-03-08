package smallworld.ui;

import smallworld.core.SmallObject;

public interface UIFactory {
  Button makeButton(String label);

  Label makeLabel(String labelText);

  ListWidget makeListWidget(SmallObject[] data);

  Picture makePicture(int width, int height);

  Picture makePicture(String fileName);

  Slider makeSlider(boolean orientVertically, int min, int max);

  HasText makeTextArea();

  HasText makeTextField();

  Window makeWindow();

  GridPanel makeGridPanel(int rows, int columns);

  BorderedPanel makeBorderedPanel();

  Menu makeMenu(String label);

  MenuItem makeMenuItem(String label);
}
