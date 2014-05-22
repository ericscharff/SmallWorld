package smallworld;

class SmallException extends Exception {
  SmallException(String gripe, SmallObject c) {
    super(gripe);
    context = c;
  }

  public SmallObject context;
}
