package suncertify.db;

import suncertify.db.file.CachedDecorator;
import suncertify.db.file.LockingDecorator;
import suncertify.db.file.meta.DatabaseRow;
import suncertify.db.file.reader.SimpleFileDatabaseReader;
import suncertify.db.filter.RemoveDeletedFilter;
import suncertify.db.filter.SearchType;
import suncertify.db.filter.StartsWithFilter;
import suncertify.lang.CollectionToArray;
import suncertify.lang.filters.Filters;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class FileData implements DB, ExtendedDB
{
  private LockingDecorator lockingFileDatabase;
  public Filters<DatabaseRow> filtering;

  public FileData(File databaseLocation) throws FileNotFoundException
  {
    lockingFileDatabase = new LockingDecorator(new CachedDecorator(new SimpleFileDatabaseReader(databaseLocation)));
    filtering = new Filters<DatabaseRow>(new RemoveDeletedFilter(), new StartsWithFilter());
  }

  public String[] read(int recordNumber) throws RecordNotFoundException
  {
    return lockingFileDatabase.read(recordNumber);
  }

  public void update(int recordNumber, String[] data, long lockCookie) throws RecordNotFoundException, SecurityException
  {
    lockingFileDatabase.update(recordNumber, data, lockCookie);
  }

  public void delete(int recordNumber, long lockCookie) throws RecordNotFoundException, SecurityException
  {
    lockingFileDatabase.delete(recordNumber, lockCookie);
  }

  public int[] find(String[] criteria)
  {
    return find(criteria, SearchType.OR);
  }

  public int[] find(String[] criteria, SearchType type)
  {
    List<Integer> result = new ArrayList<Integer>();

    List<DatabaseRow> results = filtering.accepts(criteria, lockingFileDatabase.readAll(), type);

    for (DatabaseRow databaseRow : results)
      result.add(databaseRow.getId());

    return CollectionToArray.convert(result);
  }

  public int create(String[] data) throws DuplicateKeyException
  {
    return lockingFileDatabase.create(data);
  }

  public long lock(int recordNumber) throws RecordNotFoundException
  {
    return lockingFileDatabase.lock(recordNumber);
  }

  public void unlock(int recordNumber, long cookie) throws RecordNotFoundException, SecurityException
  {
    lockingFileDatabase.unlock(recordNumber, cookie);
  }
}
