package smallworld.core;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

class ImageWriter {
  private final TreeMap<Integer, SmallObject> allObjects;
  private int numSmallInts;
  private int objectIndex;
  private final HashMap<SmallObject, Integer> objectPool;
  private final DataOutputStream out;
  private final ArrayList<Integer> roots;

  public ImageWriter(OutputStream out) {
    objectPool = new HashMap<>();
    allObjects = new TreeMap<>();
    roots = new ArrayList<>();
    this.out = new DataOutputStream(out);
    this.objectIndex = 0;
    this.numSmallInts = 0;
  }

  public void finish() throws IOException {
    // Header, SWST version 0
    out.writeInt(0x53575354); // 'SWST'
    out.writeInt(0); // version 0
    out.writeInt(objectIndex); // object count
    // First, write the object types
    // 0 = SmallObject, 1 = SmallInt, 2 = SmallByteArray
    for (Entry<Integer, SmallObject> entry : allObjects.entrySet()) {
      SmallObject obj = entry.getValue();
      if (obj instanceof SmallByteArray) {
        out.writeByte(2);
      } else if (obj instanceof SmallInt) {
        out.writeByte(1);
      } else if (obj instanceof SmallJavaObject) {
        throw new RuntimeException("JavaObject serialization not supported");
      } else {
        out.writeByte(0);
      }
    }
    // Then, write entries
    for (Entry<Integer, SmallObject> entry : allObjects.entrySet()) {
      SmallObject obj = entry.getValue();
      // Reference to class
      out.writeInt(objectPool.get(obj.objClass));
      // data (-1 if none)
      if (obj.data == null) {
        out.writeInt(-1);
      } else {
        out.writeInt(obj.data.length);
        for (SmallObject child : obj.data) {
          out.writeInt(objectPool.get(child));
        }
      }
      if (obj instanceof SmallInt) {
        out.writeInt(((SmallInt) obj).value);
      }
      if (obj instanceof SmallByteArray) {
        SmallByteArray sba = (SmallByteArray) obj;
        out.writeInt(sba.values.length);
        for (byte b : sba.values) {
          out.writeByte(b);
        }
      }
    }
    // Write the (special case) count of small integers
    out.writeInt(numSmallInts);
    // Finally, write out index of the roots, so they can be streamed back in
    for (Integer i : roots) {
      out.writeInt(i);
    }
    out.close();
  }

  public void writeObject(SmallInt[] ints) {
    if (numSmallInts > 0) {
      throw new RuntimeException("Can only write ints one time");
    }
    numSmallInts = ints.length;
    for (SmallInt child : ints) {
      writeObject(child);
    }
  }

  public void writeObject(SmallObject obj) {
    writeObjectImpl(obj);
    roots.add(objectPool.get(obj));
  }

  private void writeObjectImpl(SmallObject obj) {
    if (!objectPool.containsKey(obj)) {
      objectPool.put(obj, objectIndex);
      allObjects.put(objectIndex, obj);
      objectIndex++;
      writeObjectImpl(obj.objClass);
      if (obj.data != null) {
        for (SmallObject child : obj.data) {
          writeObjectImpl(child);
        }
      }
    }
  }
}
