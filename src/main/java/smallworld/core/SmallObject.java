package smallworld.core;

public class SmallObject {
  public SmallObject objClass;
  public SmallObject[] data;

  public SmallObject() {
    objClass = null;
    data = null;
  }

  public SmallObject(SmallObject cl, int size) {
    objClass = cl;
    data = new SmallObject[size];
  }

  public SmallObject copy(SmallObject cl) {
    return this;
  }
}
