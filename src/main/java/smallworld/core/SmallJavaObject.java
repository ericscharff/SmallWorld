package smallworld.core;

class SmallJavaObject extends SmallObject {
  public Object value;

  public SmallJavaObject(SmallObject cls, Object v) {
    super(cls, 0);
    value = v;
  }
}
