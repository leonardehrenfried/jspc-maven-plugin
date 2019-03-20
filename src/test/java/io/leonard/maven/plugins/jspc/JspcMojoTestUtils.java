package io.leonard.maven.plugins.jspc;

import java.io.*;
import java.nio.file.Path;

public class JspcMojoTestUtils {

  public static int[] getClassVersion(Path classFilePath) throws IOException {
    DataInputStream in = new DataInputStream(new FileInputStream(classFilePath.toFile()));

    int magic = in.readInt();
    if (magic != 0xcafebabe) {
      System.out.println(classFilePath + " is not a valid class!");
    }
    int minor = in.readUnsignedShort();
    int major = in.readUnsignedShort();
    int[] byteCodeVersion = new int[2];
    byteCodeVersion[0] = major;
    byteCodeVersion[1] = minor;
    System.out.println("Version of : " + classFilePath + ": " + major + "." + minor);
    in.close();
    return byteCodeVersion;
  }
}
