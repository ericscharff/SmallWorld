package smallworld;

class SmallException extends Exception {
  public SmallObject context;

  SmallException(String gripe, SmallObject c) {
    super(gripe);
    context = c;
  }
}
