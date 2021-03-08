package smallworld.core;

class Sema {
  private boolean hasBeenSet = false;
  private SmallObject value;

  public synchronized SmallObject get() throws InterruptedException {
    if (!hasBeenSet) {
      wait();
    }
    return value;
  }

  public synchronized void set(SmallObject v) {
    value = v;
    hasBeenSet = true;
    notifyAll();
  }
}
