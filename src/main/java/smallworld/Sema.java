package smallworld;

class Sema {
  private boolean hasBeenSet = false;
  private SmallObject value;

  public synchronized SmallObject get() {
    if (!hasBeenSet) {
      try {
        wait();
      } catch (Exception e) {
        System.out.println("Sema got exception " + e);
      }
    }
    return value;
  }

  public synchronized void set(SmallObject v) {
    value = v;
    hasBeenSet = true;
    notifyAll();
  }
}
