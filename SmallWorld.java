/*
	SmallWorld -- Little Smalltalk in Java
		runs as application
	Written by Tim Budd, budd@acm.org
	November 2004

	Version 0.9 November 2004
	Version 0.8 November 2002
*/

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.applet.*;
import javax.swing.*;
import java.net.*;

public class SmallWorld extends JApplet {
  static public void main(String[] args) {
    new SmallWorld(args);
  }

  private SmallInterpreter theInterpreter = new SmallInterpreter();

  // used only by applet
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
      public void actionPerformed(ActionEvent e) {
        // maybe later do something more intelligent
        System.exit(0);
      }
    });
    p.add(output);
    return p;
  }

  private JTextField output = new JTextField();

  public SmallWorld() {} // used by applet

  public SmallWorld(String[] args) { // used by application

    JFrame world = new JFrame();
    world.setTitle("Small World");
    world.setSize(200, 150);
    world.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });

    world.getContentPane().add(buildPanel());

    // now read the image
    world.show();
    output.setText("Initializing image: wait ....");
    world.repaint();
    try {
      if (args.length > 0) {
        readImage(new FileInputStream(args[0]));
      } else {
        readImage(new FileInputStream("image"));
      }
    } catch (Exception e) {
      output.setText("caught exception:" + e);
    }
    world.repaint();
  }


  private void readImage(InputStream name) throws Exception {
    ObjectInputStream ois = new ObjectInputStream(name);
    theInterpreter = new SmallInterpreter();
    // theInterpreter = (SmallInterpreter) ois.readObject();
    // now read object by object
    theInterpreter.nilObject = (SmallObject) ois.readObject();
    theInterpreter.trueObject = (SmallObject) ois.readObject();
    theInterpreter.falseObject = (SmallObject) ois.readObject();
    theInterpreter.smallInts = (SmallInt[]) ois.readObject();
    theInterpreter.ArrayClass = (SmallObject) ois.readObject();
    theInterpreter.BlockClass = (SmallObject) ois.readObject();
    theInterpreter.ContextClass = (SmallObject) ois.readObject();
    theInterpreter.IntegerClass = (SmallObject) ois.readObject();
    output.setText("image initialized");
    done = true;
  }

  private boolean done = false;

  private class doItListener implements ActionListener {
    public doItListener(String t) {
      task = t;
    }

    private String task;

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
