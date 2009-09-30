package suncertify.db.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;

public class FileUtils
{
  public static final String LOCATION_FROM = "Submission/res/db-1x1_orig.db";
  public static final String LOCATION_TO = "Submission/res/db-1x1.db";
  
  public static final File DATABASE_FILE = new File(LOCATION_TO);

  public static void setupFreshDatabase() throws Exception
  {
    new File(LOCATION_TO).delete();
    FileUtils.copyFile(new File(LOCATION_FROM), new File(LOCATION_TO));
  }

  private static void copyFile(File in, File out) throws Exception
  {
    FileInputStream fis = new FileInputStream(in);
    FileOutputStream fos = new FileOutputStream(out);
    try
    {
      byte[] buf = new byte[1024];
      int i = 0;
      while ((i = fis.read(buf)) != -1)
      {
        fos.write(buf, 0, i);
      }
    }
    catch (Exception e)
    {
      throw e;
    }
    finally
    {
      if (fis != null) fis.close();
      if (fos != null) fos.close();
    }
  }

}
