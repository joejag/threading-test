package suncertify.db.file.meta;

import junit.framework.TestCase;
import junit.framework.Assert;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.LinkedHashMap;
import java.util.Map;

import suncertify.db.file.meta.MetaData;
import suncertify.db.file.meta.MetaDataReader;
import suncertify.db.file.FileUtils;
import suncertify.db.file.meta.LogicalDelete;

public class TestMetaDataReader extends TestCase
{
  private MetaDataReader metaDataReader;
  private MetaData metaData;

  protected void setUp() throws Exception
  {
    FileUtils.setupFreshDatabase();

    RandomAccessFile accessFile = new RandomAccessFile(new File("Submission/res/db-1x1_orig.db"), "r");
    metaData = new MetaDataReader().setup(accessFile);
  }

  public void testHeader() throws Exception
  {
    assertEquals(257, metaData.getMagicCookie());
    Assert.assertEquals(159 + LogicalDelete.getSizeOfDeletedFlag(), metaData.getSizeOfRecord());
    assertEquals(7, metaData.getNumberOfFields());
  }

  public void testSchema()
  {
    Map<String, Short> headers = new LinkedHashMap<String, Short>();
    headers.put("name", (short) 64);
    headers.put("location", (short) 64);
    headers.put("size", (short) 4);
    headers.put("smoking", (short) 1);
    headers.put("rate", (short) 8);
    headers.put("date", (short) 10);
    headers.put("owner", (short) 8);

    assertEquals(headers, metaData.getColumns());
  }
}
