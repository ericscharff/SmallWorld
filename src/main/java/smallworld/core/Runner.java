package smallworld.core;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
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

  public Runner(InputStream imageStream) {
    UIFactory factory = new NoOpUIFactory();
    interpreter = new SmallInterpreter(factory);

    try {
      readImage(imageStream);
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

  public Object doIt(String task) {
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
      SmallObject ctx = interpreter.buildContext(
        interpreter.nilObject,
        args,
        doItMethod
      );
      try {
        return interpreter.execute(ctx, null, null);
      } catch (Exception ex) {
        ex.printStackTrace();
      } finally {
        out("Task complete");
      }
    }
    return null;
  }

  private static void out(Object o) {
    System.out.println(o);
  }

  private static void prompt() {
    System.out.print("SmallWorld> ");
    System.out.flush();
  }

  public static void main(String[] args) throws Exception {
    Runner runner = new Runner(args.length > 0 ? args[0] : null);
    BufferedReader reader = new BufferedReader(
      new InputStreamReader(System.in)
    );
    String line;

    prompt();
    while ((line = reader.readLine()) != null) {
      out(runner.doIt(line));
      prompt();
    }
  }
}
