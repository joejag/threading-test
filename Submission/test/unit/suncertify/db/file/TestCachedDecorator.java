package suncertify.db.file;

import junit.framework.TestCase;
import suncertify.db.file.reader.MockFileDatabaseReader;
import suncertify.db.RecordNotFoundException;
import suncertify.db.file.meta.DatabaseRow;

public class TestCachedDecorator extends TestCase
{
  public void testCacheWorks() throws RecordNotFoundException
  {
    MockFileDatabaseReader mockDb = new MockFileDatabaseReader();
    CachedDecorator cachedDb = new CachedDecorator(mockDb);

    assertEquals(1, mockDb.readCalls);
    cachedDb.readAll();
    assertEquals(1, mockDb.readCalls);

    cachedDb.delete(1, 0);
    assertEquals(2, mockDb.readCalls);
    cachedDb.readAll();
    cachedDb.readAll();
    assertEquals(2, mockDb.readCalls);

    cachedDb.create(new String[]{});
    assertEquals(3, mockDb.readCalls);
    cachedDb.readAll();
    cachedDb.readAll();
    assertEquals(3, mockDb.readCalls);

    cachedDb.update(1, new String[]{}, 0);
    assertEquals(4, mockDb.readCalls);
    cachedDb.readAll();
    cachedDb.readAll();
    assertEquals(4, mockDb.readCalls);
  }

  public void testHasRow()
  {
    MockFileDatabaseReader mockDb = new MockFileDatabaseReader();
    mockDb.databaseRows.add(new DatabaseRow(1, false, new String[]{}));
    mockDb.databaseRows.add(new DatabaseRow(2, true, new String[]{}));

    CachedDecorator cachedDb = new CachedDecorator(mockDb);

    assertTrue(cachedDb.hasRow(1, true));
    assertFalse(cachedDb.hasRow(3, true));
  }

  public void testRead() throws RecordNotFoundException
  {
    MockFileDatabaseReader mockDb = new MockFileDatabaseReader();
    mockDb.databaseRows.add(new DatabaseRow(1, false, new String[]{}));
    mockDb.databaseRows.add(new DatabaseRow(2, true, new String[]{}));

    CachedDecorator cachedDb = new CachedDecorator(mockDb);

    assertTrue(cachedDb.read(1) != null);

    try
    {
      cachedDb.read(3);
      fail("");
    } catch (RecordNotFoundException e)
    {

    }
  }
}
