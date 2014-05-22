package smallworld;

class SmallObject {
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

  public SmallObject copy(@SuppressWarnings("unused") SmallObject cl) {
    return this;
  }
}
