package smallworld;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SmallWorld extends JApplet {
  private boolean done = false;
  private final JTextField output = new JTextField();
  private SmallInterpreter theInterpreter = new SmallInterpreter();

  public static void main(String[] args) {
    new SmallWorld(args);
  }

  public SmallWorld() {} // used by applet

  public SmallWorld(String[] args) { // used by application

    JFrame world = new JFrame();
    world.setTitle("Small World");
    world.setSize(200, 150);
    world.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });

    world.getContentPane().add(buildPanel());

    // now read the image
    world.setVisible(true);
    output.setText("Initializing image: wait ....");
    world.repaint();
    try {
      if (args.length > 0) {
        readImage(new FileInputStream(args[0]));
      } else {
        readImage(getClass().getResourceAsStream("/image"));
      }
    } catch (Exception e) {
      output.setText("caught exception:" + e);
    }
    world.repaint();
  }


  private JPanel buildPanel() {
    JPanel p = new JPanel();
    p.setLayout(new GridLayout(4, 1));
    JButton browserButton = new JButton("class browser");
    browserButton.addActionListener(new doItListener("Class browser"));
    p.add(browserButton);
    JButton saveButton = new JButton("save image");
    saveButton.addActionListener(new doItListener("File saveImage: 'image'"));
    p.add(saveButton);
    JButton quitButton = new JButton("quit");
    p.add(quitButton);
    quitButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // maybe later do something more intelligent
        System.exit(0);
      }
    });
    p.add(output);
    return p;
  }

  // used only by applet
  @Override
  public void init() {
    setContentPane(buildPanel());
    // now read the image
    output.setText("Initializing image: wait ....");
    try {
      InputStream fin = new URL(getCodeBase(), "image").openStream();
      readImage(fin);
    } catch (Exception e) {
      output.setText("Applet exception " + e);
    }
    repaint();
  }

  private void readImage(InputStream name) throws Exception {
    theInterpreter = new SmallInterpreter();
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

  private class doItListener implements ActionListener {
    private final String task;

    public doItListener(String t) {
      task = t;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
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
          output.setText("exception: " + ex);
          repaint();
        }
      }
    }
  }
}
