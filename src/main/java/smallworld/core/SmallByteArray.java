package smallworld.core;

class SmallByteArray extends SmallObject {
  public byte[] values;

  public SmallByteArray(SmallObject cl, int size) {
    super(cl, 0);
    values = new byte[size];
  }

  public SmallByteArray(SmallObject cl, String text) {
    super(cl, 0);
    int size = text.length();
    values = new byte[size];
    for (int i = 0; i < size; i++) values[i] = (byte) text.charAt(i);
  }

  @Override
  public SmallObject copy(SmallObject cl) {
    SmallByteArray newObj = new SmallByteArray(cl, values.length);
    for (int i = 0; i < values.length; i++) {
      newObj.values[i] = values[i];
    }
    return newObj;
  }

  @Override
  public String toString() {
    // we assume its a string, tho not always true...
    return new String(values);
  }
}
