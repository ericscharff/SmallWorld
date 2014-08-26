package smallworld.core;

import java.io.FileInputStream;
import java.io.InputStream;

import smallworld.ui.Button;
import smallworld.ui.GridPanel;
import smallworld.ui.HasText;
import smallworld.ui.UIFactory;
import smallworld.ui.Window;
import smallworld.ui.swing.SwingUIFactory;

public class SmallWorld {
  private boolean done = false;
  private final SmallInterpreter theInterpreter;
  private final HasText output;

  public static void main(String[] args) {
    new SmallWorld(args);
  }

  private SmallWorld(String[] args) {
    UIFactory factory = new SwingUIFactory();

    theInterpreter = new SmallInterpreter(factory);

    output = factory.makeTextField();

    Window world = factory.makeWindow();
    world.setTitle("Small World");
    world.setSize(200, 150);
    world.addCloseListener(new Window.CloseListener() {
      @Override
      public void windowClosed() {
        System.exit(0);
      }
    });
    world.addChild(buildPanel(factory));

    // now read the image
    world.setVisible(true);
    output.setText("Initializing image: wait ....");
    world.redraw();
    try {
      if (args.length > 0) {
        readImage(new FileInputStream(args[0]));
      } else {
        readImage(getClass().getResourceAsStream("/image"));
      }
    } catch (Exception e) {
      output.setText("caught exception:" + e);
    }
    world.redraw();
  }


  private GridPanel buildPanel(UIFactory factory) {
    GridPanel p = factory.makeGridPanel(4, 1);
    Button browserButton = factory.makeButton("class browser");
    browserButton.addButtonListener(new doItListener("Class browser"));
    p.addChild(browserButton);
    Button saveButton = factory.makeButton("save image");
    saveButton.addButtonListener(new doItListener("File saveImage: 'image'"));
    p.addChild(saveButton);
    Button quitButton = factory.makeButton("quit");
    p.addChild(quitButton);
    quitButton.addButtonListener(new Button.ButtonListener() {
      @Override
      public void buttonClicked() {
        // maybe later do something more intelligent
        System.exit(0);
      }
    });
    p.addChild(output);
    return p;
  }

  private void readImage(InputStream name) throws Exception {
    ImageReader ir = new ImageReader(name);
    theInterpreter.nilObject = ir.readObject();
    theInterpreter.trueObject = ir.readObject();
    theInterpreter.falseObject = ir.readObject();
    theInterpreter.smallInts = ir.readSmallInts();
    theInterpreter.ArrayClass = ir.readObject();
    theInterpreter.BlockClass = ir.readObject();
    theInterpreter.ContextClass = ir.readObject();
    theInterpreter.IntegerClass = ir.readObject();
    output.setText("image initialized");
    done = true;
  }

  private class doItListener implements Button.ButtonListener {
    private final String task;

    public doItListener(String t) {
      task = t;
    }

    @Override
    public void buttonClicked() {
      if (!done) {
        return; // not ready yet
      }
      output.setText(task);
      // start from the basics
      SmallObject TrueClass = theInterpreter.trueObject.objClass;
      SmallObject name = TrueClass.data[0]; // a known string
      SmallObject StringClass = name.objClass;
      // now look for the method
      SmallObject methods = StringClass.data[2];
      SmallObject doItMeth = null;
      for (int i = 0; i < methods.data.length; i++) {
        SmallObject aMethod = methods.data[i];
        if ("doIt".equals(aMethod.data[0].toString())) {
          doItMeth = aMethod;
        }
      }
      if (doItMeth == null) {
        System.out.println("can't find do it!!");
      } else {
        SmallByteArray rec = new SmallByteArray(StringClass, task);
        SmallObject args = new SmallObject(theInterpreter.ArrayClass, 1);
        args.data[0] = rec;
        SmallObject ctx = theInterpreter.buildContext(theInterpreter.nilObject, args, doItMeth);
        try {
          theInterpreter.execute(ctx, null, null);
        } catch (Exception ex) {
          ex.printStackTrace();
          output.setText("exception: " + ex);
        }
      }
    }
  }
}
