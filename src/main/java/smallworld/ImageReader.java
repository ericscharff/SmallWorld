package smallworld;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

class ImageReader {
  private final DataInputStream in;
  private SmallObject[] objectPool;
  private int numSmallInts;

  public ImageReader(InputStream in) {
    this.in = new DataInputStream(in);
    this.objectPool = null;
  }

  private void readObjects() throws IOException {
    if (in.readInt() != 0x53575354) {
      throw new RuntimeException("Bad magic number");
    }
    if (in.readInt() != 0) {
      throw new RuntimeException("Bad version number");
    }
    int objectCount = in.readInt();
    objectPool = new SmallObject[objectCount];
    // Read headers to construct placeholder objects
    for (int i = 0; i < objectCount; i++) {
      int objType = in.readByte();
      switch (objType) {
        case 0:
          objectPool[i] = new SmallObject();
          break;
        case 1:
          objectPool[i] = new SmallInt(null, 0);
          break;
        case 2:
          objectPool[i] = new SmallByteArray(null, 0);
          break;
        default:
          throw new RuntimeException("Unknown object type " + objType);
      }
    }
    // Then fill in the objects
    for (int i = 0; i < objectCount; i++) {
      SmallObject obj = objectPool[i];
      obj.objClass = objectPool[in.readInt()];
      int dataLength = in.readInt();
      if (dataLength == -1) {
        obj.data = null;
      } else {
        obj.data = new SmallObject[dataLength];
        for (int j = 0; j < dataLength; j++) {
          obj.data[j] = objectPool[in.readInt()];
        }
      }
      // Type specific data
      if (obj instanceof SmallInt) {
        ((SmallInt) obj).value = in.readInt();
      }
      if (obj instanceof SmallByteArray) {
        SmallByteArray sba = (SmallByteArray) obj;
        int byteLength = in.readInt();
        sba.values = new byte[byteLength];
        for (int j = 0; j < byteLength; j++) {
          sba.values[j] = in.readByte();
        }
      }
    }
    numSmallInts = in.readInt();
    // Stream now points to the first root
  }

  public SmallObject readObject() throws IOException {
    if (objectPool == null) {
      readObjects();
    }
    // InputStream should now point to the index of a root
    return objectPool[in.readInt()];
  }

  public SmallInt[] readSmallInts() throws IOException {
    SmallInt[] ints = new SmallInt[numSmallInts];
    for (int i = 0; i < numSmallInts; i++) {
      ints[i] = (SmallInt) readObject();
    }
    return ints;
  }
}
