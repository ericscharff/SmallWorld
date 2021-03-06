package smallworld.core;

import java.io.FileInputStream;
import java.io.InputStream;
import smallworld.ui.UIFactory;
import smallworld.ui.noop.NoOpUIFactory;

public class Runner {
  private final SmallInterpreter interpreter;

  public Runner(String imageName) {
    UIFactory factory = new NoOpUIFactory();
    interpreter = new SmallInterpreter(factory);

    try {
      if (imageName != null) {
        readImage(new FileInputStream(imageName));
      } else {
        readImage(Runner.class.getResourceAsStream("/image"));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void readImage(InputStream s) throws Exception {
    ImageReader ir = new ImageReader(s);
    interpreter.nilObject = ir.readObject();
    interpreter.trueObject = ir.readObject();
    interpreter.falseObject = ir.readObject();
    interpreter.smallInts = ir.readSmallInts();
    interpreter.ArrayClass = ir.readObject();
    interpreter.BlockClass = ir.readObject();
    interpreter.ContextClass = ir.readObject();
    interpreter.IntegerClass = ir.readObject();
    out("image initialized");
  }

  private void doIt(String task) {
    out("Running task: " + task);

    // start from the basics
    SmallObject TrueClass = interpreter.trueObject.objClass;
    SmallObject name = TrueClass.data[0]; // a known string
    SmallObject StringClass = name.objClass;
    // now look for the method
    SmallObject methods = StringClass.data[2];
    SmallObject doItMethod = null;
    for (int i = 0; i < methods.data.length; i++) {
      SmallObject aMethod = methods.data[i];
      if ("doIt".equals(aMethod.data[0].toString())) {
        doItMethod = aMethod;
      }
    }
    if (doItMethod == null) {
      out("can't find do it!!");
    } else {
      SmallByteArray rec = new SmallByteArray(StringClass, task);
      SmallObject args = new SmallObject(interpreter.ArrayClass, 1);
      args.data[0] = rec;
      SmallObject ctx = interpreter.buildContext(interpreter.nilObject, args, doItMethod);
      try {
        out(interpreter.execute(ctx, null, null));
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    out("Task complete");
  }

  private static void out(Object o) {
    System.out.println(o);
  }

  public static void main(String[] args) {
    Runner runner = new Runner(args.length > 0 ? args[0] : null);
    runner.doIt("((1 / 3) + (9 / 8)) printString");
    runner.doIt( "(25 * 25 * 25 * 25 * 25 * 25 * 25) printString");
  }
}
