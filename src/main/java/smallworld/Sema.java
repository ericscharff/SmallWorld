package smallworld;

class Sema {
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

  private SmallObject value;
  private boolean hasBeenSet = false;
}
