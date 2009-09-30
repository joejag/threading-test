package suncertify.db.file.reader;

import junit.framework.TestCase;
import suncertify.db.RecordNotFoundException;
import suncertify.db.file.FileUtils;
import suncertify.db.file.meta.DatabaseRow;

import java.util.List;

public class TestSimpleFileDatabaseReader extends TestCase
{
  private SimpleFileDatabaseReader fileAccessor;

  protected void setUp() throws Exception
  {
    FileUtils.setupFreshDatabase();
    fileAccessor = new SimpleFileDatabaseReader(FileUtils.DATABASE_FILE);
  }

  public void testRead() throws RecordNotFoundException
  {
    String[] result = fileAccessor.read(2);
    checkValues("Excelsior", "Smallville", "4", "Y", "$230.00", "2003/02/05", "", result);
  }

  public void testCreate() throws RecordNotFoundException
  {
    String name = "Name";
    String location = "Location";
    String beds = "4";
    String smoking = "Y";
    String price = "$200.00";
    String date = "2008/02/05";
    String customer = "bob W";
    fileAccessor.create(new String[]{name, location, beds, smoking, price, date, customer});
    fileAccessor.create(new String[]{name, location, beds, smoking, price, date, customer});

    String[] result = fileAccessor.read(31);
    checkValues(name, location, beds, smoking, price, date, customer, result);

    result = fileAccessor.read(32);
    checkValues(name, location, beds, smoking, price, date, customer, result);
  }

  public void testUpdate() throws RecordNotFoundException
  {
    String[] result = fileAccessor.read(1);

    String name = "Changed Name";
    String location = result[1];
    String beds = result[2];
    String smoking = result[3];
    String price = result[4];
    String date = result[5];
    String customer = "Jim D";
    fileAccessor.update(1, new String[]{name, location, beds, smoking, price, date, customer});

    checkValues(name, location, beds, smoking, price, date, customer, fileAccessor.read(1));
  }

  public void testFindAll()
  {
    List<DatabaseRow> all = fileAccessor.readAll();
    assertEquals(31, all.size());
  }

  private void checkValues(String name, String location, String beds, String smoking, String price, String date, String customer, String[] result)
  {
    assertEquals(7, result.length);

    assertEquals(name, result[0]);
    assertEquals(location, result[1]);
    assertEquals(beds, result[2]);
    assertEquals(smoking, result[3]);
    assertEquals(price, result[4]);
    assertEquals(date, result[5]);
    assertEquals(customer, result[6]);

//    System.out.println("" + Arrays.asList(result));
  }

//  public void testDeleted()
//  {
//    try
//    {
//      fileAccessor.delete(5);
//      fileAccessor.read(5);
//      fail("expected failure");
//    } catch (RecordNotFoundException e)
//    {
//      assertTrue(e.getMessage().contains("5"));
//    }
//  }
}
