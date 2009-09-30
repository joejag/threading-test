package suncertify.lang;

import java.io.IOException;
import java.io.RandomAccessFile;

public class ByteStreamUtils
{
  public static String readStringOfSize(RandomAccessFile accessFile, short lengthOfFieldName)
          throws IOException
  {
    byte[] bytes = new byte[lengthOfFieldName];
    for (int i = 0; i < lengthOfFieldName; i++)
      bytes[i] = accessFile.readByte();

    return new String(bytes);
  }
}
