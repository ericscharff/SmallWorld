package smallworld.ui.noop;

import smallworld.core.SmallObject;
import smallworld.ui.BorderedPanel;
import smallworld.ui.Button;
import smallworld.ui.GridPanel;
import smallworld.ui.HasText;
import smallworld.ui.Label;
import smallworld.ui.ListWidget;
import smallworld.ui.Menu;
import smallworld.ui.MenuItem;
import smallworld.ui.Picture;
import smallworld.ui.Slider;
import smallworld.ui.UIFactory;
import smallworld.ui.Widget;
import smallworld.ui.Window;

public class NoOpUIFactory implements UIFactory {

  private static void log(String message) {
    System.out.println("UI: " + message);
  }

  @Override
  public Button makeButton(String label) {
    return new NoOpButton(label);
  }

  @Override
  public Label makeLabel(String labelText) {
    return new NoOpLabel(labelText);
  }

  @Override
  public ListWidget makeListWidget(SmallObject[] data) {
    return new NoOpListWidget(data);
  }

  @Override
  public Picture makePicture(int width, int height) {
    return new NoOpPicture(width, height);
  }

  @Override
  public Picture makePicture(String fileName) {
    log("makePicture(" + fileName + ")");
    return makePicture(1, 1);
  }

  @Override
  public Slider makeSlider(boolean orientVertically, int min, int max) {
    return new NoOpSlider(orientVertically, min, max);
  }

  @Override
  public HasText makeTextArea() {
    return new NoOpTextArea();
  }

  @Override
  public HasText makeTextField() {
    return new NoOpTextField();
  }

  @Override
  public Window makeWindow() {
    return new NoOpWindow();
  }

  @Override
  public GridPanel makeGridPanel(int rows, int columns) {
    return new NoOpGridPanel(rows, columns);
  }

  @Override
  public BorderedPanel makeBorderedPanel() {
    return new NoOpBorderedPanel();
  }

  @Override
  public Menu makeMenu(String label) {
    return new NoOpMenu(label);
  }

  @Override
  public MenuItem makeMenuItem(String label) {
    return new NoOpMenuItem(label);
  }

  private abstract static class NoOpWidget implements Widget {

    @Override
    public void addMouseDownListener(final MouseListener listener) {
      log("addMouseDownListener");
    }

    @Override
    public void addMouseUpListener(final MouseListener listener) {
      log("addMouseUpListener");
    }

    @Override
    public void addMouseMoveListener(final MouseListener listener) {
      log("addMouseMoveListener");
    }
  }

  private static class NoOpButton extends NoOpWidget implements Button {

    public NoOpButton(String label) {
      log("NoOpButton(" + label + ")");
    }

    @Override
    public void addButtonListener(final ButtonListener listener) {
      log("button addButtonListener");
    }
  }

  private static class NoOpLabel extends NoOpWidget implements Label {

    public NoOpLabel(String labelText) {
      log("NoOpLabel(" + labelText + ")");
    }

    @Override
    public void setPicture(Picture picture) {
      log("label setPicture");
    }
  }

  private static class NoOpListWidget extends NoOpWidget implements ListWidget {

    public NoOpListWidget(SmallObject[] data) {
      log("NoOpListWidget()");
      for (SmallObject o : data) {
        log("  data: " + o);
      }
    }

    @Override
    public int getSelectedIndex() {
      log("listWidget getSelectedIndex");
      return 1;
    }

    @Override
    public void setData(SmallObject[] data) {
      log("listWidget setData()");
      for (SmallObject o : data) {
        log("  data: " + o);
      }
    }

    @Override
    public void addSelectionListener(final Listener listener) {
      log("listWidget addSelectionListener");
    }
  }

  private static class NoOpSlider extends NoOpWidget implements Slider {

    public NoOpSlider(boolean orientVertically, int min, int max) {
      log("NoOpSlider(" + orientVertically + ", " + min + ", " + max + ")");
    }

    @Override
    public void addValueAdjustedListener(final ValueAdjustedListener listener) {
      log("slider addValueAdjustedListener");
    }

    @Override
    public int getValue() {
      log("slider getValue");
      return 1;
    }
  }

  private static class NoOpTextArea extends NoOpWidget implements HasText {

    public NoOpTextArea() {
      log("NoOpTextArea()");
    }

    @Override
    public String getSelectedText() {
      log("textArea getSelectedText");
      return "";
    }

    @Override
    public String getText() {
      log("textArea getText");
      return "";
    }

    @Override
    public void setText(String text) {
      log("textArea setText(" + text + ")");
    }
  }

  private static class NoOpTextField extends NoOpWidget implements HasText {

    public NoOpTextField() {
      log("NoOpTextField()");
    }

    @Override
    public String getSelectedText() {
      log("textField getSelectedText");
      return "";
    }

    @Override
    public String getText() {
      log("textField getText");
      return "";
    }

    @Override
    public void setText(String text) {
      log("textField setText(" + text + ")");
    }
  }

  private static class NoOpPicture implements Picture {

    public NoOpPicture(int width, int height) {
      log("NoOpPicture(" + width + ", " + height + ")");
    }

    @Override
    public void drawImage(Picture picture, int x, int y) {
      log("picture drawImage");
    }

    @Override
    public void drawText(String text, int x, int y) {
      log("picture drawText");
    }

    @Override
    public void setColor(int rgb) {
      log("picture setColor");
    }

    @Override
    public void drawOval(int x, int y, int width, int height) {
      log("picture drawOval");
    }

    @Override
    public void fillOval(int x, int y, int width, int height) {
      log("picture fillOval");
    }

    @Override
    public void drawRect(int x, int y, int width, int height) {
      log("picture drawRect");
    }

    @Override
    public void fillRect(int x, int y, int width, int height) {
      log("picture fillRect");
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
      log("picture drawLine");
    }
  }

  private static class NoOpMenu extends NoOpWidget implements Menu {

    public NoOpMenu(String label) {
      log("NoOpMenu(" + label + ")");
    }

    @Override
    public void addItem(MenuItem item) {
      log("menu addItem");
    }
  }

  private static class NoOpMenuItem extends NoOpWidget implements MenuItem {

    public NoOpMenuItem(String label) {
      log("NoOpMenuItem(" + label + ")");
    }

    @Override
    public void addItemListener(final MenuItemListener listener) {
      log("menuItem addItemListener");
    }
  }

  private static class NoOpWindow implements Window {

    public NoOpWindow() {
      log("NoOpWindow()");
    }

    @Override
    public void setTitle(String title) {
      log("window setTitle(" + title + ")");
    }

    @Override
    public void setVisible(boolean visible) {
      log("window setVisible(" + visible + ")");
    }

    @Override
    public void setSize(int width, int height) {
      log("window setSize(" + width + ", " + height + ")");
    }

    @Override
    public void addChild(Widget child) {
      log("window addChild");
    }

    @Override
    public void addMenu(Menu menu) {
      log("window addMenu");
    }

    @Override
    public void redraw() {
      log("window redraw");
    }

    @Override
    public void addCloseListener(final CloseListener listener) {
      log("window addCloseListener");
    }
  }

  private static class NoOpGridPanel extends NoOpWidget implements GridPanel {

    public NoOpGridPanel(int rows, int columns) {
      log("NoOpGridPanel(" + rows + ", " + columns + ")");
    }

    @Override
    public void addChild(Widget widget) {
      log("gridPanel addChild");
    }
  }

  private static class NoOpBorderedPanel extends NoOpWidget implements BorderedPanel {

    public NoOpBorderedPanel() {
      log("NoOpBorderedPanel()");
    }

    @Override
    public void addToCenter(Widget widget) {
      log("borderedPanel addToCenter");
    }

    @Override
    public void addToNorth(Widget widget) {
      log("borderedPanel addToNorth");
    }

    @Override
    public void addToSouth(Widget widget) {
      log("borderedPanel addToSouth");
    }

    @Override
    public void addToEast(Widget widget) {
      log("borderedPanel addToEast");
    }

    @Override
    public void addToWest(Widget widget) {
      log("borderedPanel addToWest");
    }
  }
}
