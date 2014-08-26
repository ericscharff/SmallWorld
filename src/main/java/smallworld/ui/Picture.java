package smallworld.ui;

public interface Picture {
  /**
   * Draw the supplied image on top of this image at the specified location.
   * @param picture picture to draw
   * @param x destination x coordinate, in the coordinates of this image.
   * @param y destination y coordinate, in the coordinates of this image.
   */
  void drawImage(Picture picture, int x, int y);

  void drawText(String text, int x, int y);

  void setColor(int rgb);

  void drawOval(int x, int y, int width, int height);

  void fillOval(int x, int y, int width, int height);

  void drawRect(int x, int y, int width, int height);

  void fillRect(int x, int y, int width, int height);

  void drawLine(int x1, int y1, int x2, int y2);
}
