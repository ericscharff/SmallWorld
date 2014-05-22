package smallworld;

class SmallInt extends SmallObject {
  public int value;

  public SmallInt(SmallObject IntegerClass, int v) {
    super(IntegerClass, 0);
    value = v;
  }

  @Override
  public String toString() {
    return "SmallInt: " + value;
  }
}
