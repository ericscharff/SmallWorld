package smallworld.ui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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

public class SwingUIFactory implements UIFactory {

  @Override
  public Button makeButton(String label) {
    return new SwingButton(label);
  }

  @Override
  public Label makeLabel(String labelText) {
    return new SwingLabel(labelText);
  }

  @Override
  public ListWidget makeListWidget(SmallObject[] data) {
    return new SwingListWidget(data);
  }

  @Override
  public Picture makePicture(int width, int height) {
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    SwingPicture swingPicture = new SwingPicture(image);
    swingPicture.clear();
    return swingPicture;
  }

  @Override
  public Picture makePicture(String fileName) {
    try (FileInputStream inFile = new FileInputStream(fileName)) {
      BufferedImage img = ImageIO.read(inFile);
      return new SwingPicture(img);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return makePicture(1, 1);
  }

  @Override
  public Slider makeSlider(boolean orientVertically, int min, int max) {
    return new SwingSlider(orientVertically, min, max);
  }

  @Override
  public HasText makeTextArea() {
    return new SwingTextArea();
  }

  @Override
  public HasText makeTextField() {
    return new SwingTextField();
  }

  @Override
  public Window makeWindow() {
    return new SwingWindow();
  }

  @Override
  public GridPanel makeGridPanel(int rows, int columns) {
    return new SwingGridPanel(rows, columns);
  }

  @Override
  public BorderedPanel makeBorderedPanel() {
    return new SwingBorderedPanel();
  }

  @Override
  public Menu makeMenu(String label) {
    return new SwingMenu(label);
  }

  @Override
  public MenuItem makeMenuItem(String label ) {
    return new SwingMenuItem(label);
  }

  private abstract static class SwingWidget implements Widget {
    protected abstract JComponent getComponent();
    protected abstract JComponent getComponentOrScrollPane();

    @Override
    public void addMouseDownListener(final MouseListener listener) {
      getComponent().addMouseListener(new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
          listener.mouseEvent(e.getX(), e.getY());
        }
      });
    }

    @Override
    public void addMouseUpListener(final MouseListener listener) {
      getComponent().addMouseListener(new MouseAdapter() {
        @Override
        public void mouseReleased(MouseEvent e) {
          listener.mouseEvent(e.getX(), e.getY());
        }
      });
    }

    @Override
    public void addMouseMoveListener(final MouseListener listener) {
      getComponent().addMouseMotionListener(new MouseMotionListener() {
        @Override
        public void mouseMoved(MouseEvent e) {
          listener.mouseEvent(e.getX(), e.getY());
        }

        @Override
        public void mouseDragged(MouseEvent e) {
          listener.mouseEvent(e.getX(), e.getY());
        }
      });
    }
  }

  private static class SwingButton extends SwingWidget implements Button {
    private final JButton jButton;

    public SwingButton(String label) {
      jButton = new JButton(label);
    }

    @Override
    public void addButtonListener(final ButtonListener listener) {
      jButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          listener.buttonClicked();
        }
      });
    }

    @Override
    protected JComponent getComponent() {
      return jButton;
    }

    @Override
    protected JComponent getComponentOrScrollPane() {
      return jButton;
    }
  }

  private static class SwingLabel extends SwingWidget implements Label {
    private final JLabel jLabel;
    private final JScrollPane scrollPane;

    public SwingLabel(String labelText) {
      jLabel = new JLabel(labelText);
      scrollPane = new JScrollPane(jLabel);
    }

    @Override
    public void setPicture(Picture picture) {
      BufferedImage img = ((SwingPicture) picture).getImage();
      jLabel.setIcon(new ImageIcon(img));
      jLabel.setHorizontalAlignment(SwingConstants.LEFT);
      jLabel.setVerticalAlignment(SwingConstants.TOP);
      jLabel.repaint();
    }

    @Override
    protected JComponent getComponent() {
      return jLabel;
    }

    @Override
    protected JComponent getComponentOrScrollPane() {
      return scrollPane;
    }
  }

  private static class SwingListWidget extends SwingWidget implements ListWidget {
    private final JList<SmallObject> jList;
    private final JScrollPane scrollPane;

    public SwingListWidget(SmallObject[] data) {
      this.jList = new JList<>(data);
      this.scrollPane = new JScrollPane(jList);
      jList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    }

    @Override
    public int getSelectedIndex() {
      return jList.getSelectedIndex() + 1;
    }

    @Override
    public void setData(SmallObject[] data) {
      jList.setListData(data);
      jList.repaint();
    }

    @Override
    public void addSelectionListener(final Listener listener) {
      jList.addListSelectionListener(new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
          if (!e.getValueIsAdjusting() && (jList.getSelectedIndex() >= 0)) {
            listener.itemSelected(jList.getSelectedIndex() + 1);
          }
        }
      });
    }

    @Override
    protected JComponent getComponent() {
      return jList;
    }

    @Override
    protected JComponent getComponentOrScrollPane() {
      return scrollPane;
    }
  }

  private static class SwingSlider extends SwingWidget implements Slider {
    private final JScrollBar scrollBar;

    public SwingSlider(boolean orientVertically, int min, int max) {
      scrollBar = new JScrollBar(
          (orientVertically ? JScrollBar.VERTICAL : JScrollBar.HORIZONTAL), min, 10, min, max);
    }

    @Override
    public void addValueAdjustedListener(final ValueAdjustedListener listener) {
      scrollBar.addAdjustmentListener(new AdjustmentListener() {
        @Override
        public void adjustmentValueChanged(AdjustmentEvent e) {
          listener.valueAdjusted(e.getValue());
        }
      });
    }

    @Override
    public int getValue() {
      return scrollBar.getValue();
    }

    @Override
    protected JComponent getComponent() {
      return scrollBar;
    }

    @Override
    protected JComponent getComponentOrScrollPane() {
      return scrollBar;
    }
  }

  private static class SwingTextArea extends SwingWidget implements HasText {
    private final JTextArea textArea;
    private final JScrollPane scrollPane;

    public SwingTextArea() {
      this.textArea = new JTextArea();
      this.scrollPane = new JScrollPane(textArea);
    }

    @Override
    public String getSelectedText() {
      return textArea.getSelectedText();
    }

    @Override
    public String getText() {
      return textArea.getText();
    }

    @Override
    public void setText(String text) {
      textArea.setText(text);
    }

    @Override
    protected JComponent getComponent() {
      return textArea;
    }

    @Override
    protected JComponent getComponentOrScrollPane() {
      return scrollPane;
    }
  }

  private static class SwingTextField extends SwingWidget implements HasText {
    private final JTextField textField;

    public SwingTextField() {
      this.textField = new JTextField();
    }

    @Override
    public String getSelectedText() {
      return textField.getSelectedText();
    }

    @Override
    public String getText() {
      return textField.getText();
    }

    @Override
    public void setText(String text) {
      textField.setText(text);
    }

    @Override
    protected JComponent getComponent() {
      return textField;
    }

    @Override
    protected JComponent getComponentOrScrollPane() {
      return textField;
    }
  }

  private static class SwingPicture implements Picture {
    private final BufferedImage image;
    private final Graphics2D graphics;

    public SwingPicture(BufferedImage image) {
      this.image = image;
      this.graphics = image.createGraphics();
    }

    public BufferedImage getImage() {
      return image;
    }

    public void clear() {
      graphics.setColor(Color.WHITE);
      graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
    }

    @Override
    public void drawImage(Picture picture, int x, int y) {
      graphics.drawImage(((SwingPicture) picture).getImage(), x, y, null);
    }

    @Override
    public void drawText(String text, int x, int y) {
      graphics.drawString(text, x, y);
    }

    @Override
    public void setColor(int rgb) {
      graphics.setColor(new Color(rgb));
    }

    @Override
    public void drawOval(int x, int y, int width, int height) {
      graphics.drawOval(x, y, width, height);
    }

    @Override
    public void fillOval(int x, int y, int width, int height) {
      graphics.fillOval(x, y, width, height);
    }

    @Override
    public void drawRect(int x, int y, int width, int height) {
      graphics.drawRect(x, y, width, height);
    }

    @Override
    public void fillRect(int x, int y, int width, int height) {
      graphics.fillRect(x, y, width, height);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
      graphics.drawLine(x1, y1, x2, y2);
    }
  }

  private static class SwingMenu extends SwingWidget implements Menu {
    private final JMenu menu;

    public SwingMenu(String label) {
      menu = new JMenu(label);
    }

    @Override
    public void addItem(MenuItem item) {
      SwingMenuItem swingItem = (SwingMenuItem) item;
      menu.add((JMenuItem) swingItem.getComponent());
    }

    @Override
    protected JComponent getComponent() {
      return menu;
    }

    @Override
    protected JComponent getComponentOrScrollPane() {
      return menu;
    }
  }

  private static class SwingMenuItem extends SwingWidget implements MenuItem {
    private final JMenuItem menuItem;

    public SwingMenuItem(String label) {
      menuItem = new JMenuItem(label);
    }

    @Override
    public void addItemListener(final MenuItemListener listener) {
      menuItem.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          listener.itemClicked();
        }
      });
    }

    @Override
    protected JComponent getComponent() {
      return menuItem;
    }

    @Override
    protected JComponent getComponentOrScrollPane() {
      return menuItem;
    }
  }

  private static class SwingWindow implements Window {
    private final JDialog dialog;

    public SwingWindow() {
      dialog = new JDialog();
      dialog.setVisible(false);
    }

    @Override
    public void setTitle(String title) {
      dialog.setTitle(title);
    }

    @Override
    public void setVisible(boolean visible) {
      dialog.setVisible(visible);
    }

    @Override
    public void setSize(int width, int height) {
      dialog.setSize(width, height);
    }

    @Override
    public void addChild(Widget child) {
      dialog.getContentPane().add(((SwingWidget) child).getComponentOrScrollPane());
    }

    @Override
    public void addMenu(Menu menu) {
      if (dialog.getJMenuBar() == null) {
        dialog.setJMenuBar(new JMenuBar());
      }
      dialog.getJMenuBar().add(((SwingMenu) menu).getComponent());
    }

    @Override
    public void redraw() {
      dialog.repaint();
    }

    @Override
    public void addCloseListener(final CloseListener listener) {
      dialog.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
          listener.windowClosed();
        }
      });
    }
  }

  private static class SwingGridPanel extends SwingWidget implements GridPanel {
    private final JPanel panel;

    public SwingGridPanel(int rows, int columns) {
      panel = new JPanel(new GridLayout(rows, columns));
    }

    @Override
    public void addChild(Widget widget) {
      panel.add(((SwingWidget) widget).getComponentOrScrollPane());
    }

    @Override
    protected JComponent getComponent() {
      return panel;
    }

    @Override
    protected JComponent getComponentOrScrollPane() {
      return panel;
    }
  }

  private static class SwingBorderedPanel extends SwingWidget implements BorderedPanel {
    private final JPanel panel;

    public SwingBorderedPanel() {
      panel = new JPanel(new BorderLayout());
    }

    @Override
    public void addToCenter(Widget widget) {
      panel.add(BorderLayout.CENTER, ((SwingWidget) widget).getComponentOrScrollPane());
    }

    @Override
    public void addToNorth(Widget widget) {
      panel.add(BorderLayout.NORTH, ((SwingWidget) widget).getComponentOrScrollPane());
    }

    @Override
    public void addToSouth(Widget widget) {
      panel.add(BorderLayout.SOUTH, ((SwingWidget) widget).getComponentOrScrollPane());
    }

    @Override
    public void addToEast(Widget widget) {
      panel.add(BorderLayout.EAST, ((SwingWidget) widget).getComponentOrScrollPane());
    }

    @Override
    public void addToWest(Widget widget) {
      panel.add(BorderLayout.WEST, ((SwingWidget) widget).getComponentOrScrollPane());
    }

    @Override
    protected JComponent getComponent() {
      return panel;
    }

    @Override
    protected JComponent getComponentOrScrollPane() {
      return panel;
    }
  }
}
