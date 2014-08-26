package smallworld.core;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import smallworld.ui.BorderedPanel;
import smallworld.ui.Button;
import smallworld.ui.GridPanel;
import smallworld.ui.HasText;
import smallworld.ui.Label;
import smallworld.ui.ListWidget;
import smallworld.ui.Menu;
import smallworld.ui.MenuItem;
import smallworld.ui.Picture;
import smallworld.ui.Slider;
import smallworld.ui.UIFactory;
import smallworld.ui.Widget;
import smallworld.ui.Window;

/**
 * Little Smalltalk Interpreter written in Java.
 *
 *  Written by Tim Budd, budd@acm.org
 *
 * Version 0.8 (November 2002)
 */
class SmallInterpreter {
  // global constants
  public SmallObject ArrayClass;
  public SmallObject BlockClass;
  public SmallObject ContextClass;
  public SmallObject falseObject;
  public SmallObject IntegerClass;
  public SmallObject nilObject;
  public SmallInt[] smallInts;
  public SmallObject trueObject;

  private final UIFactory uiFactory;

  public SmallInterpreter(UIFactory factory) {
    this.uiFactory = factory;
  }

  private Widget asWidget(SmallObject o) {
    return (Widget) ((SmallJavaObject) o).value;
  }

  SmallObject buildContext(SmallObject oldContext, SmallObject arguments, SmallObject method) {
    SmallObject context = new SmallObject(ContextClass, 7);
    context.data[0] = method;
    context.data[1] = arguments;
    // allocate temporaries
    int max = ((SmallInt) (method.data[4])).value;
    if (max > 0) {
      context.data[2] = new SmallObject(ArrayClass, max);
      while (max > 0)
        // iniailize to nil
        context.data[2].data[--max] = nilObject;
    }
    // allocate stack
    max = ((SmallInt) (method.data[3])).value;
    context.data[3] = new SmallObject(ArrayClass, max);
    context.data[4] = smallInts[0]; // byte pointer
    context.data[5] = smallInts[0]; // stacktop
    context.data[6] = oldContext;
    return context;
  }

  // execution method
  SmallObject execute(SmallObject context, final Thread myThread, final Thread parentThread)
      throws SmallException {
    SmallObject[] selectorCache = new SmallObject[197];
    SmallObject[] classCache = new SmallObject[197];
    SmallObject[] methodCache = new SmallObject[197];
    int lookup = 0;
    int cached = 0;

    SmallObject[] contextData = context.data;

    outerLoop: while (true) {
      boolean debug = false;
      SmallObject method = contextData[0]; // method in context
      byte[] code = ((SmallByteArray) method.data[1]).values; // code pointer
      int bytePointer = ((SmallInt) contextData[4]).value;
      SmallObject[] stack = contextData[3].data;
      int stackTop = ((SmallInt) contextData[5]).value;
      SmallObject returnedValue = null;
      SmallObject[] tempa;

      // everything else can be null for now
      SmallObject[] temporaries = null;
      SmallObject[] instanceVariables = null;
      SmallObject arguments = null;
      SmallObject[] literals = null;

      innerLoop: while (true) {
        int high = code[bytePointer++];
        int low = high & 0x0F;
        high = (high >>= 4) & 0x0F;
        if (high == 0) {
          high = low;
          // convert to positive int
          low = code[bytePointer++] & 0x0FF;
        }

        switch (high) {

          case 1: // PushInstance
            if (arguments == null) {
              arguments = contextData[1];
            }
            if (instanceVariables == null) {
              instanceVariables = arguments.data[0].data;
            }
            stack[stackTop++] = instanceVariables[low];
            break;

          case 2: // PushArgument
            if (arguments == null) {
              arguments = contextData[1];
            }
            stack[stackTop++] = arguments.data[low];
            break;

          case 3: // PushTemporary
            if (temporaries == null) {
              temporaries = contextData[2].data;
            }
            stack[stackTop++] = temporaries[low];
            break;

          case 4: // PushLiteral
            if (literals == null) {
              literals = method.data[2].data;
            }
            stack[stackTop++] = literals[low];
            break;

          case 5: // PushConstant
            switch (low) {
              case 0:
              case 1:
              case 2:
              case 3:
              case 4:
              case 5:
              case 6:
              case 7:
              case 8:
              case 9:
                stack[stackTop++] = smallInts[low];
                break;
              case 10:
                stack[stackTop++] = nilObject;
                break;
              case 11:
                stack[stackTop++] = trueObject;
                break;
              case 12:
                stack[stackTop++] = falseObject;
                break;
              default:
                throw new SmallException("Unknown constant " + low, context);
            }
            break;

          case 12: // PushBlock
            // low is argument location
            // next byte is goto value
            high = code[bytePointer++] & 0x0FF;
            returnedValue = new SmallObject(BlockClass, 10);
            tempa = returnedValue.data;
            tempa[0] = contextData[0]; // share method
            tempa[1] = contextData[1]; // share arguments
            tempa[2] = contextData[2]; // share temporaries
            tempa[3] = contextData[3]; // stack (later replaced)
            tempa[4] = newInteger(bytePointer); // current byte pointer
            tempa[5] = smallInts[0]; // stacktop
            tempa[6] = contextData[6]; // previous context
            tempa[7] = newInteger(low); // argument location
            tempa[8] = context; // creating context
            tempa[9] = newInteger(bytePointer); // current byte pointer
            stack[stackTop++] = returnedValue;
            bytePointer = high;
            break;

          case 14: // PushClassVariable
            if (arguments == null) {
              arguments = contextData[1];
            }
            if (instanceVariables == null) {
              instanceVariables = arguments.data[0].data;
            }
            stack[stackTop++] = arguments.data[0].objClass.data[low + 5];
            break;

          case 6: // AssignInstance
            if (arguments == null) {
              arguments = contextData[1];
            }
            if (instanceVariables == null) {
              instanceVariables = arguments.data[0].data;
            }
            // leave result on stack
            instanceVariables[low] = stack[stackTop - 1];
            break;

          case 7: // AssignTemporary
            if (temporaries == null) {
              temporaries = contextData[2].data;
            }
            temporaries[low] = stack[stackTop - 1];
            break;

          case 8: // MarkArguments
            SmallObject newArguments = new SmallObject(ArrayClass, low);
            tempa = newArguments.data; // direct access to array
            while (low > 0)
              tempa[--low] = stack[--stackTop];
            stack[stackTop++] = newArguments;
            break;

          case 9: // SendMessage
            // save old context
            arguments = stack[--stackTop];
            // expand newInteger in line
            // contextData[5] = newInteger(stackTop);
            contextData[5] =
                (stackTop < 10) ? smallInts[stackTop] : new SmallInt(IntegerClass, stackTop);
            // contextData[4] = newInteger(bytePointer);
            contextData[4] = (bytePointer < 10) ? smallInts[bytePointer]
                : new SmallInt(IntegerClass, bytePointer);
            // now build new context
            if (literals == null) {
              literals = method.data[2].data;
            }
            returnedValue = literals[low]; // message selector
            // System.out.println("Sending " + returnedValue);
            // System.out.println("Arguments " + arguments);
            // System.out.println("Arguments receiver " + arguments.data[0]);
            // System.out.println("Arguments class " + arguments.data[0].objClass);
            high = Math.abs(arguments.data[0].objClass.hashCode() + returnedValue.hashCode()) % 197;
            if ((selectorCache[high] != null) && (selectorCache[high] == returnedValue)
                && (classCache[high] == arguments.data[0].objClass)) {
              method = methodCache[high];
              cached++;
            } else {
              method = methodLookup(arguments.data[0].objClass, (SmallByteArray) literals[low],
                  context, arguments);
              lookup++;
              selectorCache[high] = returnedValue;
              classCache[high] = arguments.data[0].objClass;
              methodCache[high] = method;
            }
            context = buildContext(context, arguments, method);
            contextData = context.data;
            // load information from context
            continue outerLoop;

          case 10: // SendUnary
            if (low == 0) { // isNil
              SmallObject arg = stack[--stackTop];
              stack[stackTop++] = (arg == nilObject) ? trueObject : falseObject;
            } else if (low == 1) { // notNil
              SmallObject arg = stack[--stackTop];
              stack[stackTop++] = (arg != nilObject) ? trueObject : falseObject;
            } else {
              throw new SmallException("Illegal SendUnary " + low, context);
            }
            break;

          case 11: {// SendBinary
            if ((stack[stackTop - 1] instanceof SmallInt)
                && (stack[stackTop - 2] instanceof SmallInt)) {
              int j = ((SmallInt) stack[--stackTop]).value;
              int i = ((SmallInt) stack[--stackTop]).value;
              boolean done = true;
              switch (low) {
                case 0: // <
                  returnedValue = (i < j) ? trueObject : falseObject;
                  break;
                case 1: // <=
                  returnedValue = (i <= j) ? trueObject : falseObject;
                  break;
                case 2: // +
                  long li = i + (long) j;
                  if (li != (i + j)) {
                    done = false; // overflow
                  }
                  returnedValue = newInteger(i + j);
                  break;
              }
              if (done) {
                stack[stackTop++] = returnedValue;
                break;
              } else {
                stackTop += 2; // overflow, send message
              }
            }
            // non optimized binary message
            arguments = new SmallObject(ArrayClass, 2);
            arguments.data[1] = stack[--stackTop];
            arguments.data[0] = stack[--stackTop];
            contextData[5] = newInteger(stackTop);
            contextData[4] = newInteger(bytePointer);
            SmallByteArray msg = null;
            switch (low) {
              case 0:
                msg = new SmallByteArray(null, "<");
                break;
              case 1:
                msg = new SmallByteArray(null, "<=");
                break;
              case 2:
                msg = new SmallByteArray(null, "+");
                break;
            }
            method = methodLookup(arguments.data[0].objClass, msg, context, arguments);
            context = buildContext(context, arguments, method);
            contextData = context.data;
            continue outerLoop;
          }

          case 13: // Do Primitive, low is arg count, next byte is number
            high = code[bytePointer++] & 0x0FF;
            switch (high) {

              case 1: // object identity
                returnedValue = stack[--stackTop];
                if (returnedValue == stack[--stackTop]) {
                  returnedValue = trueObject;
                } else {
                  returnedValue = falseObject;
                }
                break;

              case 2: // object class
                returnedValue = stack[--stackTop].objClass;
                break;

              case 4: // object size
                returnedValue = stack[--stackTop];
                if (returnedValue instanceof SmallByteArray) {
                  low = ((SmallByteArray) returnedValue).values.length;
                } else {
                  low = returnedValue.data.length;
                }
                returnedValue = newInteger(low);
                break;

              case 5: // object at put
                low = ((SmallInt) stack[--stackTop]).value;
                returnedValue = stack[--stackTop];
                returnedValue.data[low - 1] = stack[--stackTop];
                break;

              case 6: // new context execute
                returnedValue = execute(stack[--stackTop], myThread, parentThread);
                break;

              case 7: // new object allocation
                low = ((SmallInt) stack[--stackTop]).value;
                returnedValue = new SmallObject(stack[--stackTop], low);
                while (low > 0)
                  returnedValue.data[--low] = nilObject;
                break;

              case 8: { // block invocation
                returnedValue = stack[--stackTop]; // the block
                high = ((SmallInt) returnedValue.data[7]).value; // arg location
                low -= 2;
                if (low >= 0) {
                  temporaries = returnedValue.data[2].data;
                  while (low >= 0) {
                    temporaries[high + low--] = stack[--stackTop];
                  }
                }
                contextData[5] = newInteger(stackTop);
                contextData[4] = newInteger(bytePointer);
                SmallObject newContext = new SmallObject(ContextClass, 10);
                for (int i = 0; i < 10; i++)
                  newContext.data[i] = returnedValue.data[i];
                newContext.data[6] = contextData[6];
                newContext.data[5] = smallInts[0]; // stack top
                newContext.data[4] = returnedValue.data[9]; // starting addr
                low = newContext.data[3].data.length; // stack size
                newContext.data[3] = new SmallObject(ArrayClass, low); // new stack
                context = newContext;
                contextData = context.data;
                continue outerLoop;
              }

              case 9: // read a char from input
                try {
                  returnedValue = newInteger(System.in.read());
                } catch (IOException e) {
                  returnedValue = nilObject;
                }
                break;

              case 10: { // small integer addition need to handle ovflow
                low = ((SmallInt) stack[--stackTop]).value;
                high = ((SmallInt) stack[--stackTop]).value;
                long lhigh = ((long) high) + (long) low;
                high += low;
                if (lhigh == high) {
                  returnedValue = newInteger(high);
                } else {
                  returnedValue = nilObject;
                }
              }
                break;

              case 11: // small integer quotient
                low = ((SmallInt) stack[--stackTop]).value;
                high = ((SmallInt) stack[--stackTop]).value;
                high /= low;
                returnedValue = newInteger(high);
                break;

              case 12: // small integer remainder
                low = ((SmallInt) stack[--stackTop]).value;
                high = ((SmallInt) stack[--stackTop]).value;
                high %= low;
                returnedValue = newInteger(high);
                break;

              case 14: // small int equality
                low = ((SmallInt) stack[--stackTop]).value;
                high = ((SmallInt) stack[--stackTop]).value;
                returnedValue = (low == high) ? trueObject : falseObject;
                break;

              case 15: { // small integer multiplication
                low = ((SmallInt) stack[--stackTop]).value;
                high = ((SmallInt) stack[--stackTop]).value;
                long lhigh = ((long) high) * (long) low;
                high *= low;
                if (lhigh == high) {
                  returnedValue = newInteger(high);
                } else {
                  returnedValue = nilObject;
                }
              }
                break;

              case 16: { // small integer subtraction
                low = ((SmallInt) stack[--stackTop]).value;
                high = ((SmallInt) stack[--stackTop]).value;
                long lhigh = ((long) high) - (long) low;
                high -= low;
                if (lhigh == high) {
                  returnedValue = newInteger(high);
                } else {
                  returnedValue = nilObject;
                }
              }
                break;

              case 17: // small integer as string
                low = ((SmallInt) stack[--stackTop]).value;
                returnedValue = new SmallByteArray(stack[--stackTop], String.valueOf(low));
                break;

              case 18: // debugg -- dummy for now
                returnedValue = stack[--stackTop];
                System.out.println(
                    "Debug " + returnedValue + " class " + returnedValue.objClass.data[0]);
                break;

              case 19: // block fork
                returnedValue = stack[--stackTop];
                new ActionThread(returnedValue, myThread).start();
                break;

              case 20: // byte array allocation
                low = ((SmallInt) stack[--stackTop]).value;
                returnedValue = new SmallByteArray(stack[--stackTop], low);
                break;

              case 21: // string at
                low = ((SmallInt) stack[--stackTop]).value;
                returnedValue = stack[--stackTop];
                SmallByteArray baa = (SmallByteArray) returnedValue;
                low = baa.values[low - 1] & 0x0FF;
                returnedValue = newInteger(low);
                break;

              case 22: // string at put
                low = ((SmallInt) stack[--stackTop]).value;
                SmallByteArray ba = (SmallByteArray) stack[--stackTop];
                high = ((SmallInt) stack[--stackTop]).value;
                ba.values[low - 1] = (byte) high;
                returnedValue = ba;
                break;

              case 23: // string copy
                returnedValue = stack[--stackTop];
                returnedValue = stack[--stackTop].copy(returnedValue);
                break;

              case 24: { // string append
                SmallByteArray a = (SmallByteArray) stack[--stackTop];
                SmallByteArray b = (SmallByteArray) stack[--stackTop];
                low = a.values.length + b.values.length;
                SmallByteArray n = new SmallByteArray(a.objClass, low);
                high = 0;
                for (int i = 0; i < a.values.length; i++)
                  n.values[high++] = a.values[i];
                for (int i = 0; i < b.values.length; i++)
                  n.values[high++] = b.values[i];
                returnedValue = n;
              }
                break;

              case 26: { // string compare
                SmallByteArray a = (SmallByteArray) stack[--stackTop];
                SmallByteArray b = (SmallByteArray) stack[--stackTop];
                low = a.values.length;
                high = b.values.length;
                int s = (low < high) ? low : high;
                int r = 0;
                for (int i = 0; i < s; i++)
                  if (a.values[i] < b.values[i]) {
                    r = 1;
                    break;
                  } else if (b.values[i] < a.values[i]) {
                    r = -1;
                    break;
                  }
                if (r == 0) {
                  if (low < high) {
                    r = 1;
                  } else if (low > high) {
                    r = -1;
                  }
                }
                returnedValue = newInteger(r);
              }
                break;

              case 29: { // image save
                SmallByteArray a = (SmallByteArray) stack[--stackTop];
                String name = a.toString();
                try {
                  ImageWriter iw = new ImageWriter(new FileOutputStream(name));
                  iw.writeObject(nilObject);
                  iw.writeObject(trueObject);
                  iw.writeObject(falseObject);
                  iw.writeObject(smallInts);
                  iw.writeObject(ArrayClass);
                  iw.writeObject(BlockClass);
                  iw.writeObject(ContextClass);
                  iw.writeObject(IntegerClass);
                  iw.finish();
                } catch (Exception e) {
                  throw new SmallException("got I/O Exception " + e, context);
                }
                returnedValue = a;
              }
                break;

              case 30: // array at
                low = ((SmallInt) stack[--stackTop]).value;
                returnedValue = stack[--stackTop];
                returnedValue = returnedValue.data[low - 1];
                break;

              case 31: {// array with: (add new item)
                SmallObject oldar = stack[--stackTop];
                low = oldar.data.length;
                returnedValue = new SmallObject(oldar.objClass, low + 1);
                for (int i = 0; i < low; i++)
                  returnedValue.data[i] = oldar.data[i];
                returnedValue.data[low] = stack[--stackTop];
              }
                break;

              case 32: { // object add: increase object size
                returnedValue = stack[--stackTop];
                low = returnedValue.data.length;
                SmallObject na[] = new SmallObject[low + 1];
                for (int i = 0; i < low; i++)
                  na[i] = returnedValue.data[i];
                na[low] = stack[--stackTop];
                returnedValue.data = na;
              }
                break;

              case 33: {// Sleep for a bit
                low = ((SmallInt) stack[--stackTop]).value;
                try {
                  Thread.sleep(low);
                } catch (Exception a) {
                }
              }
                break;

              case 34: { // thread kill
                if (parentThread != null) {
                  parentThread.interrupt();
                }
                if (myThread != null) {
                  myThread.interrupt();
                }
                return nilObject;
              }

              case 35: // return current context
                returnedValue = context;
                break;

              case 36: // fast array creation
                returnedValue = new SmallObject(ArrayClass, low);
                for (int i = low - 1; i >= 0; i--)
                  returnedValue.data[i] = stack[--stackTop];
                break;

              case 41: {// open file for output
                try {
                  FileOutputStream of = new FileOutputStream(stack[--stackTop].toString());
                  PrintStream ps = new PrintStream(of);
                  returnedValue = new SmallJavaObject(stack[--stackTop], ps);
                } catch (IOException e) {
                  throw new SmallException("I/O exception " + e, context);
                }
              }
                break;

              case 42: {// open file for input
                try {
                  FileInputStream of = new FileInputStream(stack[--stackTop].toString());
                  DataInput ps = new DataInputStream(of);
                  returnedValue = new SmallJavaObject(stack[--stackTop], ps);
                } catch (IOException e) {
                  throw new SmallException("I/O exception " + e, context);
                }
              }
                break;

              case 43: {// write a string
                try {
                  PrintStream ps = (PrintStream) ((SmallJavaObject) stack[--stackTop]).value;
                  ps.print(stack[--stackTop]);
                } catch (Exception e) {
                  throw new SmallException("I/O exception " + e, context);
                }
              }
                break;

              case 44: { // read a string
                try {
                  DataInput di = (DataInput) ((SmallJavaObject) stack[--stackTop]).value;
                  String line = di.readLine();
                  if (line == null) {
                    --stackTop;
                    returnedValue = nilObject;
                  } else {
                    returnedValue = new SmallByteArray(stack[--stackTop], line);
                  }
                } catch (EOFException e) {
                  returnedValue = nilObject;
                } catch (IOException f) {
                  throw new SmallException("I/O exception " + f, context);
                }
              }
                break;

              case 50: // integer into float
                low = ((SmallInt) stack[--stackTop]).value;
                returnedValue = new SmallJavaObject(stack[--stackTop], new Double(low));
                break;

              case 51: { // addition of float
                double a = ((Double) ((SmallJavaObject) stack[--stackTop]).value).doubleValue();
                double b = ((Double) ((SmallJavaObject) stack[--stackTop]).value).doubleValue();
                returnedValue = new SmallJavaObject(stack[--stackTop], new Double(a + b));
              }
                break;

              case 52: { // subtraction of float
                double a = ((Double) ((SmallJavaObject) stack[--stackTop]).value).doubleValue();
                double b = ((Double) ((SmallJavaObject) stack[--stackTop]).value).doubleValue();
                returnedValue = new SmallJavaObject(stack[--stackTop], new Double(a - b));
              }
                break;

              case 53: { // multiplication of float
                double a = ((Double) ((SmallJavaObject) stack[--stackTop]).value).doubleValue();
                double b = ((Double) ((SmallJavaObject) stack[--stackTop]).value).doubleValue();
                returnedValue = new SmallJavaObject(stack[--stackTop], new Double(a * b));
              }
                break;

              case 54: { // division of float
                double a = ((Double) ((SmallJavaObject) stack[--stackTop]).value).doubleValue();
                double b = ((Double) ((SmallJavaObject) stack[--stackTop]).value).doubleValue();
                returnedValue = new SmallJavaObject(stack[--stackTop], new Double(a / b));
              }
                break;

              case 55: { // less than test of float
                double a = ((Double) ((SmallJavaObject) stack[--stackTop]).value).doubleValue();
                double b = ((Double) ((SmallJavaObject) stack[--stackTop]).value).doubleValue();
                returnedValue = (a < b) ? trueObject : falseObject;
              }
                break;

              case 56: { // equality test of float
                double a = ((Double) ((SmallJavaObject) stack[--stackTop]).value).doubleValue();
                double b = ((Double) ((SmallJavaObject) stack[--stackTop]).value).doubleValue();
                returnedValue = (a == b) ? trueObject : falseObject;
              }
                break;

              case 57: { // float to int
                double a = ((Double) ((SmallJavaObject) stack[--stackTop]).value).doubleValue();
                returnedValue = newInteger((int) a);
              }
                break;

              case 58: // random float
                returnedValue = new SmallJavaObject(stack[--stackTop], new Double(Math.random()));
                break;

              case 59: // print of float
                returnedValue = stack[--stackTop];
                returnedValue = new SmallByteArray(stack[--stackTop], String.valueOf(
                    ((Double) ((SmallJavaObject) returnedValue).value).doubleValue()));
                break;

              case 60: { // make window
                Window dialog = uiFactory.makeWindow();
                returnedValue = new SmallJavaObject(stack[--stackTop], dialog);
              }
                break;

              case 61: { // show/hide text window
                returnedValue = stack[--stackTop];
                SmallJavaObject jo = (SmallJavaObject) stack[--stackTop];
                if (returnedValue == trueObject) {
                  ((Window) jo.value).setVisible(true);
                } else {
                  ((Window) jo.value).setVisible(false);
                }
              }
                break;

              case 62: { // set content pane
                SmallJavaObject tc = (SmallJavaObject) stack[--stackTop];
                returnedValue = stack[--stackTop];
                SmallJavaObject jd = (SmallJavaObject) returnedValue;
                ((Window) jd.value).addChild((Widget) tc.value);
              }
                break;

              case 63: // set size
                low = ((SmallInt) stack[--stackTop]).value;
                high = ((SmallInt) stack[--stackTop]).value;
                returnedValue = stack[--stackTop];
                {
                  SmallJavaObject wo = (SmallJavaObject) returnedValue;
                  ((Window) wo.value).setSize(low, high);
                }
                break;

              case 64: { // add menu to window
                SmallJavaObject menu = (SmallJavaObject) stack[--stackTop];
                returnedValue = stack[--stackTop];
                SmallJavaObject jo = (SmallJavaObject) returnedValue;
                Window dialog = (Window) jo.value;
                dialog.addMenu((Menu) menu.value);
              }
                break;

              case 65: { // set title
                SmallObject title = stack[--stackTop];
                returnedValue = stack[--stackTop];
                SmallJavaObject jd = (SmallJavaObject) returnedValue;
                ((Window) jd.value).setTitle(title.toString());
              }
                break;

              case 66: { // repaint window
                returnedValue = stack[--stackTop];
                SmallJavaObject jd = (SmallJavaObject) returnedValue;
                ((Window) jd.value).redraw();
              }
                break;

              case 70: { // new label panel
                Label jl = uiFactory.makeLabel(stack[--stackTop].toString());
                returnedValue = new SmallJavaObject(stack[--stackTop], jl);
              }
                break;

              case 71: { // new button
                final SmallObject action = stack[--stackTop];
                Button jb = uiFactory.makeButton(stack[--stackTop].toString());
                returnedValue = new SmallJavaObject(stack[--stackTop], jb);
                jb.addButtonListener(new Button.ButtonListener() {
                  @Override
                  public void buttonClicked() {
                    new ActionThread(action, myThread).start();
                  }
                });
              }
                break;

              case 72: // new text line
                returnedValue = new SmallJavaObject(stack[--stackTop], uiFactory.makeTextField());
                break;

              case 73: // new text area
                returnedValue = new SmallJavaObject(stack[--stackTop], uiFactory.makeTextArea());
                break;

              case 74: { // new grid panel
                SmallObject data = stack[--stackTop];
                low = ((SmallInt) stack[--stackTop]).value;
                high = ((SmallInt) stack[--stackTop]).value;
                GridPanel gp = uiFactory.makeGridPanel(low, high);
                for (int i = 0; i < data.data.length; i++) {
                  gp.addChild(asWidget(data.data[i]));
                }
                returnedValue = new SmallJavaObject(stack[--stackTop], gp);
              }
                break;

              case 75: { // new list panel
                final SmallObject action = stack[--stackTop];
                SmallObject data = stack[--stackTop];
                returnedValue = stack[--stackTop];
                ListWidget jl = uiFactory.makeListWidget(data.data);
                returnedValue = new SmallJavaObject(returnedValue, jl);
                jl.addSelectionListener(new ListWidget.Listener() {
                  @Override
                  public void itemSelected(int selectedIndex) {
                    new ActionThread(action, myThread, selectedIndex).start();
                  }
                });
              }
                break;

              case 76: { // new border panel
                BorderedPanel bp = uiFactory.makeBorderedPanel();
                returnedValue = stack[--stackTop];
                if (returnedValue != nilObject) {
                  bp.addToCenter(asWidget(returnedValue));
                }
                returnedValue = stack[--stackTop];
                if (returnedValue != nilObject) {
                  bp.addToWest(asWidget(returnedValue));
                }
                returnedValue = stack[--stackTop];
                if (returnedValue != nilObject) {
                  bp.addToEast(asWidget(returnedValue));
                }
                returnedValue = stack[--stackTop];
                if (returnedValue != nilObject) {
                  bp.addToSouth(asWidget(returnedValue));
                }
                returnedValue = stack[--stackTop];
                if (returnedValue != nilObject) {
                  bp.addToNorth(asWidget(returnedValue));
                }
                returnedValue = new SmallJavaObject(stack[--stackTop], bp);
              }
                break;

              case 77: { // set image on label
                SmallJavaObject img = (SmallJavaObject) stack[--stackTop];
                SmallJavaObject lab = (SmallJavaObject) stack[--stackTop];
                Object jo = lab.value;
                if (jo instanceof Label) {
                  ((Label) jo).setPicture((Picture) img.value);
                }
              }
                break;

              case 80: { // content of text area
                SmallJavaObject jt = (SmallJavaObject) stack[--stackTop];
                returnedValue = stack[--stackTop]; // class
                Object jo = jt.value;
                if (jo instanceof HasText) {
                  HasText text = (HasText) jo;
                  returnedValue = new SmallByteArray(returnedValue, text.getText());
                } else {
                  returnedValue = new SmallByteArray(returnedValue, "");
                }
              }
                break;

              case 81: {// content of selected text area
                SmallJavaObject jt = (SmallJavaObject) stack[--stackTop];
                returnedValue = stack[--stackTop]; // class
                Object jo = jt.value;
                if (jo instanceof HasText) {
                  HasText text = (HasText) jo;
                  returnedValue = new SmallByteArray(returnedValue, text.getSelectedText());
                } else {
                  returnedValue = new SmallByteArray(returnedValue, "");
                }
              }
                break;

              case 82: { // set text area
                returnedValue = stack[--stackTop];// text
                SmallJavaObject jt = (SmallJavaObject) stack[--stackTop];
                Object jo = jt.value;
                if (jo instanceof HasText) {
                  ((HasText) jo).setText(returnedValue.toString());
                }
              }
                break;

              case 83: { // get selected index
                SmallJavaObject jo = (SmallJavaObject) stack[--stackTop];
                Object jl = jo.value;
                if (jl instanceof ListWidget) {
                  returnedValue = newInteger(((ListWidget) jl).getSelectedIndex());
                } else if (jl instanceof Slider) {
                  returnedValue = newInteger(((Slider) jl).getValue());
                } else {
                  returnedValue = newInteger(0);
                }
              }
                break;

              case 84: { // set list data
                SmallObject data = stack[--stackTop];
                returnedValue = stack[--stackTop];
                SmallJavaObject jo = (SmallJavaObject) returnedValue;
                Object jl = jo.value;
                if (jl instanceof ListWidget) {
                  ((ListWidget) jl).setData(data.data);
                }
              }
                break;

              case 85: { // new slider
                final SmallObject action = stack[--stackTop];
                int max = ((SmallInt) stack[--stackTop]).value + 10; // why?
                int min = ((SmallInt) stack[--stackTop]).value;
                SmallObject orient = stack[--stackTop];
                Slider slider = uiFactory.makeSlider(orient == trueObject, min, max);
                returnedValue = new SmallJavaObject(stack[--stackTop], slider);
                if (action != nilObject) {
                  slider.addValueAdjustedListener(new Slider.ValueAdjustedListener() {
                    @Override
                    public void valueAdjusted(int newValue) {
                      new ActionThread(action, myThread, newValue).start();
                    }
                  });
                }
              }
                break;

              case 86: { // onMouseDown b
                final SmallObject action = stack[--stackTop];
                SmallJavaObject pan = (SmallJavaObject) stack[--stackTop];
                Widget jo = (Widget) pan.value;
                jo.addMouseDownListener(new Widget.MouseListener() {
                  @Override
                  public void mouseEvent(int x, int y) {
                    new ActionThread(action, myThread, x, y).start();
                  }
                });
              }
                break;

              case 87: { // onMouseUp b
                final SmallObject action = stack[--stackTop];
                SmallJavaObject pan = (SmallJavaObject) stack[--stackTop];
                Widget jo = (Widget) pan.value;
                jo.addMouseUpListener(new Widget.MouseListener() {
                  @Override
                  public void mouseEvent(int x, int y) {
                    new ActionThread(action, myThread, x, y).start();
                  }
                });
              }
                break;

              case 88: { // onMouseMove b
                final SmallObject action = stack[--stackTop];
                SmallJavaObject pan = (SmallJavaObject) stack[--stackTop];
                Widget jo = (Widget) pan.value;
                jo.addMouseMoveListener(new Widget.MouseListener() {
                  @Override
                  public void mouseEvent(int x, int y) {
                    new ActionThread(action, myThread, x, y).start();
                  }
                });
              }
                break;

              case 90: { // new menu
                SmallObject title = stack[--stackTop]; // text
                returnedValue = stack[--stackTop]; // class
                Menu menu = uiFactory.makeMenu(title.toString());
                returnedValue = new SmallJavaObject(returnedValue, menu);
              }
                break;

              case 91: { // new menu item
                final SmallObject action = stack[--stackTop];
                final SmallObject text = stack[--stackTop];
                returnedValue = stack[--stackTop];
                SmallJavaObject mo = (SmallJavaObject) returnedValue;
                Menu menu = (Menu) mo.value;
                MenuItem mi = uiFactory.makeMenuItem(text.toString());
                mi.addItemListener(new MenuItem.MenuItemListener() {
                  @Override
                  public void itemClicked() {
                    new ActionThread(action, myThread).start();
                  }
                });
                menu.addItem(mi);
              }
                break;

              case 100: // new semaphore
                returnedValue = new SmallJavaObject(stack[--stackTop], new Sema());
                break;

              case 101: { // semaphore wait
                SmallJavaObject jo = (SmallJavaObject) stack[--stackTop];
                try {
                  returnedValue = ((Sema) jo.value).get();
                } catch (InterruptedException e) {
                  returnedValue = nilObject;
                }
              }
                break;

              case 102: { // semaphore set
                returnedValue = stack[--stackTop];
                SmallJavaObject jo = (SmallJavaObject) stack[--stackTop];
                ((Sema) jo.value).set(returnedValue);
              }
                break;

              case 110: { // new image
                low = ((SmallInt) stack[--stackTop]).value;
                high = ((SmallInt) stack[--stackTop]).value;
                Picture img = uiFactory.makePicture(low, high);
                returnedValue = new SmallJavaObject(stack[--stackTop], img);
              }
                break;

              case 111: { // new image from file
                SmallByteArray title = (SmallByteArray) stack[--stackTop];
                returnedValue = stack[--stackTop];
                Picture img = uiFactory.makePicture(title.toString());
                returnedValue = new SmallJavaObject(returnedValue, img);
              }
                break;

              case 113: { // draw image
                SmallJavaObject img2 = (SmallJavaObject) stack[--stackTop];
                low = ((SmallInt) stack[--stackTop]).value;
                high = ((SmallInt) stack[--stackTop]).value;
                SmallJavaObject img = (SmallJavaObject) stack[--stackTop];
                Picture dest = (Picture) img;
                Picture src = (Picture) img2;
                dest.drawImage(src, low, high);
              }
                break;

              case 114: { // draw text
                SmallByteArray text = (SmallByteArray) stack[--stackTop];
                low = ((SmallInt) stack[--stackTop]).value;
                high = ((SmallInt) stack[--stackTop]).value;
                int c = ((SmallInt) stack[--stackTop]).value;
                SmallJavaObject img = (SmallJavaObject) stack[--stackTop];
                Picture picture = (Picture) img.value;
                picture.setColor(c);
                picture.drawText(text.toString(), low, high);
              }
                break;

              case 115: { // draw/fill circle
                int s = ((SmallInt) stack[--stackTop]).value;
                int h = ((SmallInt) stack[--stackTop]).value;
                int w = ((SmallInt) stack[--stackTop]).value;
                low = ((SmallInt) stack[--stackTop]).value;
                high = ((SmallInt) stack[--stackTop]).value;
                int c = ((SmallInt) stack[--stackTop]).value;
                SmallJavaObject img = (SmallJavaObject) stack[--stackTop];
                Picture picture = (Picture) img.value;
                picture.setColor(c);
                switch (s) {
                  case 1:
                    picture.drawOval(low, high, h, w);
                    break;
                  case 2:
                    picture.fillOval(low, high, h, w);
                    break;
                  case 3:
                    picture.drawRect(low, high, h, w);
                    break;
                  case 4:
                    picture.fillRect(low, high, h, w);
                    break;
                  case 5:
                    picture.drawLine(low, high, h, w);
                    break;
                }
              }
                break;

              default:
                throw new SmallException("Unknown Primitive " + high, context);
            }
            stack[stackTop++] = returnedValue;
            break;

          case 15: // Do Special
            switch (low) {
              case 1: // self return
                if (arguments == null) {
                  arguments = contextData[1];
                }
                returnedValue = arguments.data[0];
                context = contextData[6]; // previous context
                break innerLoop;

              case 2: // stack return
                returnedValue = stack[--stackTop];
                context = contextData[6]; // previous context
                break innerLoop;

              case 3: // block return
                returnedValue = stack[--stackTop];
                context = contextData[8]; // creating context in block
                context = context.data[6]; // previous context
                break innerLoop;

              case 4: // duplicate
                returnedValue = stack[stackTop - 1];
                stack[stackTop++] = returnedValue;
                break;

              case 5: // pop top
                stackTop--;
                break;

              case 6: // branch
                low = code[bytePointer++] & 0x0FF;
                bytePointer = low;
                break;

              case 7: // branch if true
                low = code[bytePointer++] & 0x0FF;
                returnedValue = stack[--stackTop];
                if (returnedValue == trueObject) {
                  bytePointer = low;
                }
                break;

              case 8: // branch if false
                low = code[bytePointer++] & 0x0FF;
                returnedValue = stack[--stackTop];
                if (returnedValue == falseObject) {
                  bytePointer = low;
                }
                break;

              case 11: // send to super
                low = code[bytePointer++] & 0x0FF;
                // message selector
                // save old context
                arguments = stack[--stackTop];
                contextData[5] = newInteger(stackTop);
                contextData[4] = newInteger(bytePointer);
                // now build new context
                if (literals == null) {
                  literals = method.data[2].data;
                }
                if (method == null) {
                  method = context.data[0];
                }
                method = method.data[5]; // class in method
                method = method.data[1]; // parent in class
                method = methodLookup(method, (SmallByteArray) literals[low], context, arguments);
                context = buildContext(context, arguments, method);
                contextData = context.data;
                // load information from context
                continue outerLoop;

              default: // throw exception
                throw new SmallException("Unrecogized DoSpecial " + low, context);
            }
            break;

          default: // throw exception
            throw new SmallException("Unrecogized opCode " + low, context);
        }
      } // end of inner loop

      if ((context == null) || (context == nilObject)) {
        if (debug) {
          System.out.println("lookups " + lookup + " cached " + cached);
        }
        return returnedValue;
      }
      contextData = context.data;
      stack = contextData[3].data;
      stackTop = ((SmallInt) contextData[5]).value;
      stack[stackTop++] = returnedValue;
      contextData[5] = newInteger(stackTop);
    }
  } // end of outer loop

  private SmallObject methodLookup(SmallObject receiver, SmallByteArray messageSelector,
      SmallObject context, SmallObject arguments) throws SmallException {
    String name = messageSelector.toString();
    SmallObject cls;
    for (cls = receiver; cls != nilObject; cls = cls.data[1]) {
      SmallObject dict = cls.data[2]; // dictionary in class
      for (int i = 0; i < dict.data.length; i++) {
        SmallObject aMethod = dict.data[i];
        if (name.equals(aMethod.data[0].toString())) {
          return aMethod;
        }
      }
    }
    // try once to handle method in Smalltalk before giving up
    if (name.equals("error:")) {
      throw new SmallException("Unrecognized message selector: " + messageSelector, context);
    }
    SmallObject[] newArgs = new SmallObject[2];
    newArgs[0] = arguments.data[0]; // same receiver
    newArgs[1] =
        new SmallByteArray(messageSelector.objClass, "Unrecognized message selector: " + name);
    arguments.data = newArgs;
    return methodLookup(receiver, new SmallByteArray(messageSelector.objClass, "error:"), context,
        arguments);
  }

  // create a new small integer
  SmallInt newInteger(int val) {
    if ((val >= 0) && (val < 10)) {
      return smallInts[val];
    } else {
      return new SmallInt(IntegerClass, val);
    }
  }

  private class ActionThread extends Thread {
    private final SmallObject action;
    private final Thread myThread;

    public ActionThread(SmallObject block, Thread myT) {
      myThread = myT;
      action = new SmallObject(ContextClass, 10);
      for (int i = 0; i < 10; i++)
        action.data[i] = block.data[i];
    }

    public ActionThread(SmallObject block, Thread myT, int v1) {
      myThread = myT;
      action = new SmallObject(ContextClass, 10);
      for (int i = 0; i < 10; i++)
        action.data[i] = block.data[i];
      int argLoc = ((SmallInt) action.data[7]).value;
      action.data[2].data[argLoc] = newInteger(v1);
    }

    public ActionThread(SmallObject block, Thread myT, int v1, int v2) {
      myThread = myT;
      action = new SmallObject(ContextClass, 10);
      for (int i = 0; i < 10; i++)
        action.data[i] = block.data[i];
      int argLoc = ((SmallInt) action.data[7]).value;
      action.data[2].data[argLoc] = newInteger(v1);
      action.data[2].data[argLoc + 1] = newInteger(v2);
    }

    @Override
    public void run() {
      int stksize = action.data[3].data.length;
      action.data[3] = new SmallObject(ArrayClass, stksize); // new stack
      action.data[4] = action.data[9]; // byte pointer
      action.data[5] = newInteger(0); // stack top
      action.data[6] = nilObject;
      try {
        execute(action, this, myThread);
      } catch (Exception e) {
        System.out.println("caught exception " + e);
      }
    }
  }
}
